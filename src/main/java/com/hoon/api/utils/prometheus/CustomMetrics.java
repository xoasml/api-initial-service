package com.hoon.api.utils.prometheus;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomMetrics {

    private final MeterRegistry registry;

    @PostConstruct
    public void init() {
        registry.counter("custom_metric_name", "tag_name", "tag_value");
    }

}
