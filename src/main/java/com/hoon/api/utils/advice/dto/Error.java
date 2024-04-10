package com.hoon.api.utils.advice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Error {

    private String message;

    private String key;

    private Object value;

    private String status;

}
