package com.hoon.api.utils.swagger;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Springdoc 테스트")
                .description(
                             "<pre>" +
                             "Springdoc을 사용한 Swagger UI 테스트<br>" +
                             "<br>" +
                             "Paging 설명 : pageable object가 있는 요청은 페이징 처리가 가능<br><br>" +
                             "{<br>" +
                             "  \"page\": 0, (선택 페이지, 0부터 시작) <br>" +
                             "  \"size\": 10, (페이지당 표시 로우 갯수 설정)<br>" +
                             "  \"sort\": [ (내용 정렬 기준 설정)<br>" +
                             "    \"id,desc\" (#{컬럼명},#{기준})<br>" +
                             "  ]<br>" +
                             "}<br><br>" +
                             "위와 같을 경우 아래와 같이 설정 됨<br>" +
                             "1. 0번째 페이지 보여줌<br>" +
                             "2. 한 페이지당 10개의 로우를 보여줌<br>" +
                             "3. id 컬럼 내림차순으로 보여줌<br>" +
                             "<pre>"
                )
                .version("1.0.0");
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> openApi.setPaths(getSortedPaths(openApi));
    }

    private Paths getSortedPaths(OpenAPI openApi) {
        Map<String, PathItem> sortedPaths = new LinkedHashMap<>();
        openApi.getPaths().entrySet().stream()
                .sorted((a, b) -> getIndexFromPathItem(a) - getIndexFromPathItem(b))
                .forEachOrdered(x -> sortedPaths.put(x.getKey(), x.getValue()));
        Paths paths = new Paths();
        paths.putAll(sortedPaths);
        return paths;
    }

    private int getIndexFromPathItem(Map.Entry pathItem) {

        List<Operation> ls = new ArrayList<>();
        PathItem value = (PathItem) pathItem.getValue();

        ls.add(value.getGet());
        ls.add(value.getPut());
        ls.add(value.getPost());
        ls.add(value.getDelete());
        ls.add(value.getPatch());
        ls = ls.stream().filter(v -> v != null).toList();

        if (ls.get(0).getSummary() == null) {
            return 999;
        }
        try {
            return Integer.parseInt(ls.get(0).getSummary().split("\\.")[0]);
        } catch (NumberFormatException e) {
            return 999;
        }
    }
}
