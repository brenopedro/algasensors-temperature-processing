package com.algaworks.algasensors.temperature_processing.infrastructure.rabbitmq;

import lombok.Getter;

@Getter
public enum RabbitMQExchangeEnum {

    FANNOUT_EXCHANGE_NAME("temperature-processing.temperature-received.v1.e");

    private final String exchangeName;

    RabbitMQExchangeEnum(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
