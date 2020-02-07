package net.thumbtack.forums.utils.validations;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.utils.annotations.MaxLength;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {

    private Config config;

    @Autowired
    public MaxLengthValidator(Config config) {
        this.config = config;
    }

    @Override
    public void initialize(MaxLength maxLength) {
    }

    @Override
    public boolean isValid(String field,
                           ConstraintValidatorContext cxt) {
        cxt.disableDefaultConstraintViolation();
        cxt.buildConstraintViolationWithTemplate("Length must be less or equals then " + config.getMaxNameLength()).addConstraintViolation();
        return field.length() <= config.getMaxNameLength();

    }

}
