package com.wangyijie.missyou.exception;

import com.wangyijie.missyou.exception.http.HttpException;

public class DeleteSuccess extends HttpException {
    public DeleteSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 200;
    }


}
