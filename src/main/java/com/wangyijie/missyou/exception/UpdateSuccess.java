package com.wangyijie.missyou.exception;

import com.wangyijie.missyou.exception.http.HttpException;

public class UpdateSuccess extends HttpException {
    public UpdateSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 200;
    }


}
