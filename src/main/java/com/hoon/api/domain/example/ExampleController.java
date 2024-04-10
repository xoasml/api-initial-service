package com.hoon.api.domain.example;

import com.hoon.api.domain.example.dto.save.ExampleSaveRequestDto;
import com.hoon.api.utils.aop.annotation.MethodInfoLogging;
import com.hoon.api.utils.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping("/save")
    @MethodInfoLogging(description = "예제 메소드 저장 API")
    @Operation(summary = "예제 메소드 저장 API", description = "예제 메소드 저장 API 설명")
    public CommonResponse<?> save(@Valid ExampleSaveRequestDto request){

        exampleService.save(request);

        return new CommonResponse<>("저장 성공");
    }

}
