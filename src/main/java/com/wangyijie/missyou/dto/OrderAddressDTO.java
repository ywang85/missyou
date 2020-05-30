package com.wangyijie.missyou.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderAddressDTO {
    private String userName;
    private String province;
    private String city;
    private String country;
    private String mobile;
    private String nationalCode;
    private String postalCode;
    private String detail;
}
