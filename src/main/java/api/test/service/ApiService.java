package api.test.service;

import api.test.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {

    private ApiClient apiClient;

    public ApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<User> getNoneLondonUsers() {
        List<User> userList = apiClient.callApi("https://bpdts-test-app.herokuapp.com/users");
        List<User> nonLondonUsers = new ArrayList<>();
        for (User user : userList) {
            checkCoordinates(nonLondonUsers, user);
        }
        return nonLondonUsers;
    }

    public List<User> getLondonUsers() {
        return apiClient.callApi("https://bpdts-test-app.herokuapp.com/city/London/users");
    }

    private void checkCoordinates(List<User> nonLondonUsers, User user) {
        if (!(Double.parseDouble(user.getLatitude()) >= 50) && !(Double.parseDouble(user.getLatitude()) <= -50) &&
                !(Double.parseDouble(user.getLongitude()) >= 50) && !(Double.parseDouble(user.getLongitude()) <= -50)) {
            nonLondonUsers.add(user);
        }
    }
}
