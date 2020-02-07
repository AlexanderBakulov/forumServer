package net.thumbtack.forums.utils.validations;

import net.thumbtack.forums.utils.annotations.SetNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class SetNotEmptyValidator implements ConstraintValidator<SetNotEmpty, Set<?>> {

    @Override
    public void initialize(SetNotEmpty minLength) {
    }


    @Override
    public boolean isValid(Set<?> field,
                           ConstraintValidatorContext cxt) {
        cxt.disableDefaultConstraintViolation();
        cxt.buildConstraintViolationWithTemplate("Set can't be empty");
        if(field != null) {
            return (field.size() != 0 );
        }
        return true;
    }

}
