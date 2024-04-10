package com.hoon.api.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time = LocalDateTime.now();
    private String resultMessage = "SUCCESS";
    private String statusCode = HttpStatus.OK.toString();
    private String message = "정상 처리입니다.";

    private long contentCnt;
    private Map<String, Object> paging = new HashMap<>();
    private List<T> content = new ArrayList<>();

    public CommonResponse(String message) {
        this.message = message;
    }

    public CommonResponse(List<T> resultSet) {
        this.content = resultSet;
        this.contentCnt = resultSet.size();
    }

    public CommonResponse(T result) {

        if (result != null) {
            this.contentCnt = 1;
            this.content.add(result);
        } else {
            this.contentCnt = 0;
            this.message = "데이터가 없습니다.";
        }
    }

    public CommonResponse(Page<T> resultSet) {

        this.content = resultSet.getContent();
        this.contentCnt = resultSet.getTotalElements();
        this.paging.put("pageCount", resultSet.getTotalPages());
        this.paging.put("pageNumber", resultSet.getNumber());
        this.paging.put("isLastPage", resultSet.isLast());
        this.paging.put("isFirstPage", resultSet.isFirst());

    }

    public void setResultMsg(String msg) {
        this.message = msg;
    }

}
