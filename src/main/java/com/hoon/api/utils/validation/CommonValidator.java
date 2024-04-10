package com.hoon.api.utils.validation;

import static org.springframework.util.StringUtils.hasText;

public class CommonValidator {

    public boolean conditionRequired(String condition, String data, String validValue ) {
        if (data.equals(condition)) {
            return !(validValue == null || "".equals(validValue));
        }
        return true;
    }

    public boolean conditionSameBlankStatus(String ... strings) {
        boolean flag = hasText(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            if(flag != hasText(strings[i])){
                return false;
            }
        }
        return true;

    }

}
