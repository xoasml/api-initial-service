package com.hoon.api.domain.redis;

import com.hoon.api.domain.redis.dto.RedisSetKeyValueRequestDto;
import com.hoon.api.utils.redis.RedisRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisRepository redisRepository;

    @PostMapping("/set")
    public void setKeyValue(@Valid @RequestBody RedisSetKeyValueRequestDto request) {
        redisRepository.setValue(request.getKey(), request.getValue());
    }

    @GetMapping("/get/{key}")
    public String getKeyValue(@PathVariable String key) {
        return redisRepository.getValue(key);
    }

}
