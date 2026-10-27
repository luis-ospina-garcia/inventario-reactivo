package com.luisospina.inventario.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.luisospina.inventario.domain.enums.MovementType;
import com.luisospina.inventario.domain.movement.MovementDomain;
import com.luisospina.inventario.domain.movement.exceptions.InvalidMovementException;
import com.luisospina.inventario.domain.movement.vo.Quantity;
import com.luisospina.inventario.domain.product.ProductDomain;
import com.luisospina.inventario.domain.product.exceptions.InvalidProductException;
import com.luisospina.inventario.domain.product.vo.Sku;
import org.junit.jupiter.api.Test;

class DomainValidationTest {

    @Test
    void sku_se_recorta_y_rechaza_vacio_o_demasiado_largo() {
        assertThat(new Sku("  TOR-001  ").value()).isEqualTo("TOR-001");

        assertThatThrownBy(() -> new Sku("   "))
                .isInstanceOfSatisfying(InvalidProductException.class,
                        e -> assertThat(e.getCode()).isEqualTo("PRODUCT_SKU_EMPTY"));

        assertThatThrownBy(() -> new Sku("X".repeat(Sku.MAX_LENGTH + 1)))
                .isInstanceOfSatisfying(InvalidProductException.class,
                        e -> assertThat(e.getCode()).isEqualTo("PRODUCT_SKU_TOO_LONG"));
    }

    @Test
    void quantity_debe_ser_mayor_que_cero() {
        assertThat(new Quantity(3).value()).isEqualTo(3);

        assertThatThrownBy(() -> new Quantity(0))
                .isInstanceOfSatisfying(InvalidMovementException.class,
                        e -> assertThat(e.getCode()).isEqualTo("MOVEMENT_INVALID_QUANTITY"));
    }

    @Test
    void producto_rechaza_nombre_vacio_y_stock_negativo() {
        assertThatThrownBy(() -> ProductDomain.create("SKU-1", " ", 1))
                .isInstanceOfSatisfying(InvalidProductException.class,
                        e -> assertThat(e.getCode()).isEqualTo("PRODUCT_NAME_REQUIRED"));

        assertThatThrownBy(() -> ProductDomain.create("SKU-1", "Tornillo", -1))
                .isInstanceOfSatisfying(InvalidProductException.class,
                        e -> assertThat(e.getCode()).isEqualTo("PRODUCT_NEGATIVE_STOCK"));
    }

    @Test
    void el_delta_del_movimiento_es_positivo_en_entradas_y_negativo_en_salidas() {
        MovementDomain entrada = MovementDomain.create(1L, MovementType.IN, new Quantity(4), null);
        MovementDomain salida = MovementDomain.create(1L, MovementType.OUT, new Quantity(4), null);

        assertThat(entrada.delta()).isEqualTo(4);
        assertThat(salida.delta()).isEqualTo(-4);
    }
}
