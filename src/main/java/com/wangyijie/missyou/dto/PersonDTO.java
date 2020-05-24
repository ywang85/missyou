package com.wangyijie.missyou.dto;

import com.wangyijie.missyou.dto.validators.PasswordEqual;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@PasswordEqual(min = 1, message = "密码不相同")
//@RequiredArgsConstructor
public class PersonDTO {
    @Length(min = 2, max = 10)
    private String name;
    private Integer age;
//    @Valid
//    private SchoolDTO schoolDTO;

    private String password1;
    private String password2;
}
