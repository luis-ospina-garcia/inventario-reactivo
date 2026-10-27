# Inventario Reactivo

API REST reactiva para controlar el inventario de una tienda: productos, entradas y salidas de stock, e historial de movimientos, con un stream en tiempo real (SSE) de los cambios de stock.

Proyecto personal para practicar **programación reactiva con Spring WebFlux y Project Reactor** aplicando **Clean Architecture / arquitectura hexagonal**.

> La organización por capas y carpetas (Controller → Interactor → UseCase → RulesValidator → Repository) se tomó como referencia estructural del repositorio [BancoPago](https://github.com/YoSuarezz/BancoPago). El código, el dominio y las decisiones de este proyecto son propios.

## Stack

Java 25 · Spring Boot 3.5 · WebFlux · Project Reactor · Spring Data R2DBC · PostgreSQL · Flyway · Docker Compose · JUnit 5 · Mockito · StepVerifier · WebTestClient

## Flujo de una petición

```
Controller → Interactor → UseCase → RulesValidator (+ Rule) → Repository
 (HTTP)     (DTO↔dominio,   (lógica     (reglas de negocio      (puerto de salida,
             orquestación)   de negocio)  con estado)             adaptador R2DBC)
```

- **Controller**: solo HTTP; recibe el DTO, delega en el interactor y arma la respuesta.
- **Interactor**: traduce DTO ↔ dominio con mappers y orquesta (p. ej. publica el evento de stock *después* de confirmar la transacción).
- **UseCase**: la lógica de negocio; es donde viven las transacciones (`@Transactional`).
- **RulesValidator + Rule**: reglas que necesitan consultar datos (SKU único, producto existente). Las invariantes puras viven en el dominio (value objects `Sku` y `Quantity`).
- **Repository**: interfaz en `secondaryports`; la implementa un adaptador R2DBC en `infrastructure`.

## Estructura

```
src/main/java/com/luisospina/inventario
├── crosscutting/          excepciones base (DomainException, ErrorCode, Layer) y helpers
├── domain/                modelo puro: ProductDomain, MovementDomain, value objects, eventos, excepciones
├── application/
│   ├── primaryports/      interactor/, dto/ (request, response) y mapper/
│   ├── secondaryports/    repository/ y service/ (interfaces que implementa la infraestructura)
│   └── usecase/           casos de uso, rulesvalidator/ y rules/ por módulo (product, movement, stock)
└── infrastructure/
    ├── primaryadapters/   controller/ y adapter/response (ApiResponse, SseEvents...)
    ├── secondaryadapters/ r2dbc/ (entity, mapper, repositorios) y messaging/ (Sinks)
    └── GlobalExceptionHandler, ErrorResponse
```

## Dónde se valida cada cosa

| Qué | Dónde | Ejemplo | Resultado |
|---|---|---|---|
| Invariantes puras | **Dominio** (value objects y constructores) | `Sku` no vacío y ≤ 50, `Quantity` > 0, stock ≥ 0 | 400 con el código del módulo |
| Reglas que consultan datos | **`Rule`** en `application/usecase/**/rules` | SKU único, producto existente | 409 / 404 |
| Concurrencia | **Base de datos** | `UPDATE ... WHERE stock + :delta >= 0`, `CHECK`, `UNIQUE` | 409 por stock insuficiente |
| Forma de la petición | **DTO + `jakarta.validation`** | `@NotBlank`, `@Positive` | 400 con detalle por campo |

Las anotaciones del DTO solo existen para dar un 400 útil por campo; no sustituyen al dominio, que vuelve a comprobar todo.

### Añadir una regla

Los `RulesValidatorImpl` reciben `List<Rule<T>>` por constructor y las ejecutan en orden, deteniéndose en la primera que falle (`OrderedRulesValidator`). Para añadir una regla basta con crear un `@Component` que implemente `Rule<T>` del tipo que valida el caso de uso; si el orden importa, se marca con `@Order`. No hay que tocar el validador. Las reglas que usan varios módulos viven en `application/usecase/shared/rules`.

## Decisiones técnicas

- **Stock atómico, sin carreras.** Las salidas usan un único `UPDATE product SET stock = stock + :delta WHERE id = :id AND stock + :delta >= 0`. Si no afecta ninguna fila, no alcanzaba el stock. Por eso "stock suficiente" **no** es una `Rule`: validar y luego escribir dejaría una ventana de concurrencia. Hay un test con 20 salidas en paralelo sobre un stock de 10.
- **Evento después del commit.** El `StockUpdated` se publica desde el interactor cuando el caso de uso (y su transacción) ya terminó, así los suscriptores nunca ven un cambio que luego se revierte.
- **SSE con `Sinks`.** `Sinks.many().multicast().directBestEffort()` reparte los eventos a los clientes conectados; cada cliente filtra por su producto.
- **Errores centralizados.** Cada módulo tiene su enum de errores (`ProductError`, `MovementError`) con código y mensaje; `GlobalExceptionHandler` los traduce a 404 / 409 / 400 con un cuerpo uniforme.
- **Migraciones con Flyway** (por JDBC, mientras la app usa R2DBC) y restricciones en la base (`stock >= 0`, `quantity > 0`, SKU único) como última línea de defensa.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/products` | Crea un producto (con stock inicial opcional) |
| GET | `/api/v1/products` | Lista productos |
| GET | `/api/v1/products/{id}` | Consulta un producto |
| GET | `/api/v1/products/{id}/stock/stream` | Stream SSE (evento `stock`) con los cambios de stock |
| POST | `/api/v1/movements` | Registra una entrada (`IN`) o salida (`OUT`) |
| GET | `/api/v1/movements?productId={id}` | Historial de movimientos de un producto |

Respuestas exitosas: `{"data": [...], "messages": [...]}`. Errores: `{"code": "...", "message": "...", "messages": [...]}`.

## Ejecutar

Con Docker Compose (app + PostgreSQL):

```bash
docker compose up --build
```

Solo la base de datos y la app desde Maven:

```bash
docker compose up -d postgres
mvn spring-boot:run
```

Ejemplo:

```bash
curl -X POST localhost:8080/api/v1/products -H 'Content-Type: application/json' \
  -d '{"sku":"TOR-001","name":"Tornillo 1/4","initialStock":10}'

curl -N localhost:8080/api/v1/products/1/stock/stream   # en otra terminal

curl -X POST localhost:8080/api/v1/movements -H 'Content-Type: application/json' \
  -d '{"productId":1,"type":"OUT","quantity":3,"reason":"Venta"}'
```

## Pruebas

```bash
mvn verify
```

Usan H2 en memoria (modo PostgreSQL), no necesitan Docker.

- **Dominio**: value objects y validaciones.
- **Casos de uso** (Mockito + StepVerifier): stock insuficiente, SKU duplicado, stock inicial, producto inexistente.
- **Adaptador de eventos**: publicación y suscripción con StepVerifier.
- **Integración** (`@SpringBootTest` + WebTestClient): flujo completo, 409/400/404, concurrencia y stream de stock.
