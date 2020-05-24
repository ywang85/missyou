package com.wangyijie.missyou.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
//@RequiredArgsConstructor
public class SchoolDTO {
    @Length(min = 2, max = 10)
    private String schoolName;
}
