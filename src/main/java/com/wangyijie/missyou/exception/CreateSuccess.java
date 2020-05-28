package com.wangyijie.missyou.exception;

import com.wangyijie.missyou.exception.http.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 201;
    }


}
