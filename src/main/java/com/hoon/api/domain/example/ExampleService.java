package com.hoon.api.domain.example;

import com.hoon.api.domain.example.dto.save.ExampleSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    public void save(ExampleSaveRequestDto request) {
    }
}
