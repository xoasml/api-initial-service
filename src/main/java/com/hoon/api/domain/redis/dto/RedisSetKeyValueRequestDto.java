package com.hoon.api.domain.redis.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RedisSetKeyValueRequestDto {

    @NotEmpty
    private String key;

    @NotEmpty
    private String value;


}
