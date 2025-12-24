package com.algaworks.algasensors.temperature_processing.infrastructure.rabbitmq;

public enum RabbitMQExchangeEnum {

    FANNOUT_EXCHANGE_NAME("temperature-processing.temperature-received.v1.e");

    private final String exchangeName;

    RabbitMQExchangeEnum(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeName() {
        return exchangeName;
    }
}
