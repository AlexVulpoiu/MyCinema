package com.unibuc.fmi.mycinema.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {OnlyLettersAndDigitsValidation.class})
public @interface OnlyLettersAndDigits {

    String message() default "Only letters and digits allowed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
