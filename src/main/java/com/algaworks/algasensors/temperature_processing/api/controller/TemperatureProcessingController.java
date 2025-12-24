package com.algaworks.algasensors.temperature_processing.api.controller;

import com.algaworks.algasensors.temperature_processing.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature_processing.common.IdGenerator;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

import static com.algaworks.algasensors.temperature_processing.infrastructure.rabbitmq.RabbitMQExchangeConstants.FANNOUT_EXCHANGE_NAME;

@Slf4j
@RestController
@RequestMapping("api/sensors/{sensorId}/temperatures/data")
@RequiredArgsConstructor
public class TemperatureProcessingController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable TSID sensorId, @RequestBody String input) {
        if (input == null || input.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Double temperature;

        try {
            temperature = Double.valueOf(input);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        TemperatureLogOutput logOutput = TemperatureLogOutput.builder()
                .id(IdGenerator.generate())
                .value(temperature)
                .registeredAt(OffsetDateTime.now())
                .sensorId(sensorId)
                .build();

        log.info("New temperature log created: {}", logOutput);

        MessagePostProcessor messagePostProcessor = (MessagePostProcessor) message -> {
            message.getMessageProperties().setHeader("sensorId", logOutput.getSensorId().toString());
            return message;
        };
        rabbitTemplate.convertAndSend(FANNOUT_EXCHANGE_NAME,
                "", logOutput, messagePostProcessor);

    }
}
