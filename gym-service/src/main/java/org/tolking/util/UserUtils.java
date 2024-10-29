package org.tolking.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tolking.entity.User;
import org.tolking.enums.RoleType;

@UtilityClass
public class UserUtils {
    public String prepareUserForCreation(User user, PasswordEncoder passwordEncoder, String serialUsername, RoleType roleType) {
        String alphaNumericString = null;

        if (user.getPassword() == null) {
            alphaNumericString = RandomString.getAlphaNumericString(10);
            user.setPassword(passwordEncoder.encode(alphaNumericString));
        }

        user.setRole(roleType);
        user.setIsActive(true);
        user.setUsername(serialUsername);

        return alphaNumericString;
    }

    public String getUsername(User user) {
        return user.getFirstName() + "." + user.getLastName();
    }

    public void toggleStatus(User user) {
        user.setIsActive(!user.getIsActive());
    }
}
