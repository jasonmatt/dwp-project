package utils;

import api.test.model.User;

public class TestUtils {

    public static User getLondonUser() {
        User user  = new User();
        user.setFirstName("David");
        user.setLastName("FromLondon");
        user.setLatitude("130.00");
        user.setLongitude("-20.00");
        user.setEmail("www.fake-email.com");
        user.setIpAddress("00.0.00.000");
        return user;
    }

    public static User getNonLondonUser() {
        User user  = new User();
        user.setFirstName("David");
        user.setLastName("NotFromLondon");
        user.setLatitude("20.00");
        user.setLongitude("-30.00");
        user.setEmail("www.fake-email.com");
        user.setIpAddress("00.0.00.000");
        return user;
    }

}
