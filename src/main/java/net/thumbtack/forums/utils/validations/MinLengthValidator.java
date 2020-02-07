package net.thumbtack.forums.utils.validations;

import net.thumbtack.forums.Config;
import net.thumbtack.forums.utils.annotations.MinLength;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinLengthValidator implements ConstraintValidator<MinLength, String> {

    private Config config;

    @Autowired
    public MinLengthValidator(Config config) {
        this.config = config;
    }

    @Override
    public void initialize(MinLength minLength) {
    }

    @Override
    public boolean isValid(String field,
                           ConstraintValidatorContext cxt) {
        cxt.disableDefaultConstraintViolation();
        cxt.buildConstraintViolationWithTemplate("Length must be lager or equals then " + config.getMinPasswordLength()).addConstraintViolation();
        return field.length() >= config.getMinPasswordLength();

    }

}
