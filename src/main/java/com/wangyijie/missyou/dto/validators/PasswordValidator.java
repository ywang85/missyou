package com.wangyijie.missyou.dto.validators;

import com.wangyijie.missyou.dto.PersonDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordEqual, PersonDTO> {
    // 第二个：自定义注解修饰的目标的类型
    private int min;
    private int max;

    @Override
    public boolean isValid(PersonDTO personDTO, ConstraintValidatorContext constraintValidatorContext) {
        String password1 = personDTO.getPassword1();
        String password2 = personDTO.getPassword2();
        boolean match = password1.equals(password2);
        return password1.length() > min && password1.length() < max
                && password2.length() > min && password2.length() < max
                && match;
    }

    @Override
    public void initialize(PasswordEqual constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }
}
