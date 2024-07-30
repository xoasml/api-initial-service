package com.hoon.api.domain.example;

import com.hoon.api.domain.example.dto.save.ExampleSaveRequestDto;
import com.hoon.api.utils.aop.annotation.MethodInfoLogging;
import com.hoon.api.utils.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    @GetMapping("")
    @MethodInfoLogging(description = "예제 메소드 저장 API")
    @Operation(summary = "예제 메소드 저장 API", description = "예제 메소드 저장 API 설명")
    public CommonResponse<?> get(@Valid ExampleSaveRequestDto request){

        return new CommonResponse<>("리스트 조회 성공");
    }

    @PostMapping("")
    @MethodInfoLogging(description = "예제 메소드 저장 API")
    @Operation(summary = "예제 메소드 저장 API", description = "예제 메소드 저장 API 설명")
    public CommonResponse<?> post(@Valid ExampleSaveRequestDto request){

        return new CommonResponse<>("저장 성공");
    }

    @PutMapping("/{pk}")
    @MethodInfoLogging(description = "예제 수정 API")
    @Operation(summary = "예제 메소드 저장 API", description = "예제 메소드 저장 API 설명")
    public CommonResponse<?> put(@Valid ExampleSaveRequestDto request, @RequestParam Integer pk){

        return new CommonResponse<>("수정 성공");
    }

    @DeleteMapping("/{pk}")
    @MethodInfoLogging(description = "예제 메소드 삭제 (DELETE) API")
    @Operation(summary = "예제 메소드 저장 API", description = "예제 메소드 저장 API 설명")
    public CommonResponse<?> delete(@Valid ExampleSaveRequestDto request, @RequestParam Integer pk){

        return new CommonResponse<>("삭제 성공");
    }


}
