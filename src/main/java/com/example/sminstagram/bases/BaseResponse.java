package com.example.sminstagram.bases;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {
    private String status;
    private String message;
    private int code;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .status("success")
                .message(null)
                .code(HttpStatus.OK.value())
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> created(T data) {
        return BaseResponse.<T>builder()
                .status("success")
                .message(null)
                .code(HttpStatus.CREATED.value())
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> fail(int code, String message) {
        return BaseResponse.<T>builder()
                .status("fail")
                .message(message)
                .code(code)
                .data(null)
                .build();
    }
}