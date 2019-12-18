package service;

import api.test.model.User;
import api.test.service.ApiClient;
import api.test.service.ApiService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApiServiceTest {

    @InjectMocks
    private ApiService apiService;

    @Mock
    ApiClient apiClient;

    private MockMvc mockMvc;

    private List users = new ArrayList<>();

    private User londonUser;
    private User nonLondonUser;

    @Before
    public void init() {
        initMocks(this);
        londonUser = TestUtils.getLondonUser();
        nonLondonUser = TestUtils.getNonLondonUser();
        users.add(londonUser);
    }

    @Test
    public void testGetLondonUsersReturnsListOfUsers() {
        when(apiClient.callApi("https://bpdts-test-app.herokuapp.com/city/London/users")).thenReturn(users);
        List<User> returnedUsers = apiService.getLondonUsers();
        assertThat(returnedUsers.size(), is(1));
        assertThat(returnedUsers.get(0).toString(), is(londonUser.toString()));
    }

    @Test
    public void testGetNonLondonUsersReturnsNoLondonUsers() {
        users.add(nonLondonUser);
        when(apiClient.callApi("https://bpdts-test-app.herokuapp.com/users")).thenReturn(users);
        List<User> returnedUsers = apiService.getNoneLondonUsers();
        assertThat(returnedUsers.size(), is(1));
        assertThat(returnedUsers.get(0).toString(), is(nonLondonUser.toString()));
    }

}
