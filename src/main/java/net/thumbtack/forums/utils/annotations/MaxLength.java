package net.thumbtack.forums.utils.annotations;


import net.thumbtack.forums.utils.validations.MaxLengthValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaxLengthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxLength {
    String message() default "Name is too long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}