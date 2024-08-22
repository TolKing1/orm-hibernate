package org.tolking.util;

import lombok.experimental.UtilityClass;
import org.tolking.entity.User;

@UtilityClass
public class UserUtils {
    public void prepareUserForCreation(User user, String serialUsername) {
        if (user.getPassword() == null) {
            user.setPassword(RandomString.getAlphaNumericString(10));
        }
        user.setIsActive(true);
        user.setUsername(serialUsername);
    }
    public String getUsername(User user){
        return user.getFirstName() + "." + user.getLastName();
    }

    public void toggleStatus(User user){
        user.setIsActive(!user.getIsActive());
    }
}
