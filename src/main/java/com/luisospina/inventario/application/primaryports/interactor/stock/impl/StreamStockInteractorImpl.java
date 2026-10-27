package com.luisospina.inventario.application.primaryports.interactor.stock.impl;

import com.luisospina.inventario.application.primaryports.dto.stock.response.StockUpdatedResponse;
import com.luisospina.inventario.application.primaryports.interactor.stock.StreamStockInteractor;
import com.luisospina.inventario.application.primaryports.mapper.stock.StockDTOMapper;
import com.luisospina.inventario.application.usecase.stock.StreamStockUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class StreamStockInteractorImpl implements StreamStockInteractor {

    private final StreamStockUseCase streamStockUseCase;
    private final StockDTOMapper stockDTOMapper;

    public StreamStockInteractorImpl(StreamStockUseCase streamStockUseCase, StockDTOMapper stockDTOMapper) {
        this.streamStockUseCase = streamStockUseCase;
        this.stockDTOMapper = stockDTOMapper;
    }

    @Override
    public Flux<StockUpdatedResponse> execute(Long productId) {
        return streamStockUseCase.execute(productId).map(stockDTOMapper::toStockUpdatedResponse);
    }
}
