package net.thumbtack.forums.utils.annotations;


import net.thumbtack.forums.utils.validations.SetNotEmptyValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SetNotEmptyValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SetNotEmpty {

    String message() default "Can't be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
