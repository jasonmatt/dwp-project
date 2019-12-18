package api.test.controller;

import api.test.model.User;
import api.test.service.ApiService;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ApiController {

    private ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/users")
    public ResponseEntity<AddDefaultCharsetFilter.ResponseWrapper> getUsers() {
        List<User> londonUsers = apiService.getLondonUsers();
        List<User> nonLondonUsers = apiService.getNoneLondonUsers();
        Set<User> users = new HashSet<>();
        users.addAll(londonUsers);
        users.addAll(nonLondonUsers);
        return new ResponseEntity(users, HttpStatus.OK);
    }
}




