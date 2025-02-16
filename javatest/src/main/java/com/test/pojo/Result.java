package com.test.pojo;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;  // 状态码
    private String message; // 提示信息
    private T data;         // 返回数据

    // 构造函数
    public Result() {}

    // 自定义构造函数
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功时的响应
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    // 自定义成功响应，带有消息
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    // 错误时的响应
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // 自定义错误响应
    public static <T> Result<T> error(String message) {
        return new Result<>(400, message, null);
    }
}
