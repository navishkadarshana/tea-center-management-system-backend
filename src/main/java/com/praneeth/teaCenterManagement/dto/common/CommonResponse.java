package com.praneeth.teaCenterManagement.dto.common;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private int code;
    private boolean success;
    private String message;
    private T body;

    public CommonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CommonResponse(boolean success, String message, T body) {
        this.success = success;
        this.message = message;
        this.body = body;
    }

    public CommonResponse(boolean success, T body) {
        this.success = success;
        this.body = body;
    }
}
