package com.wangyijie.missyou.dto;

import com.wangyijie.missyou.core.enumeration.LoginType;
import com.wangyijie.missyou.dto.validators.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不允许为空")
    private String account;
    @TokenPassword
    private String password;

    private LoginType type;
}
