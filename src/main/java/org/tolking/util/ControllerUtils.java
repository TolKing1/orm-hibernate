package org.tolking.util;

import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;
import org.tolking.exception.InvalidDataException;

@UtilityClass
public class ControllerUtils {
    public static final String ERROR_IN_VALIDATION = "Provided JSON is invalid and has some error in validation";
    public static final String CREDENTIALS_IS_INCORRECT = "Login credentials is incorrect";
    public static final String PASSWORD_HAS_BEEN_CHANGED = "Password has been changed";
    public static final String PROFILE_HAS_BEEN_CHANGED = "Profile has been changed";
    public static final String STATUS_HAS_BEEN_TOGGLED = "Status has been toggled";
    public static final String TRAINING_RETRIEVED = "Training list has been retrieved";

    public static void throwExceptionIfHasError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidDataException(bindingResult);
        }
    }
}
