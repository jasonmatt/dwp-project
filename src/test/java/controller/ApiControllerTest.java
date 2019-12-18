package controller;

import api.test.controller.ApiController;
import api.test.model.User;
import api.test.service.ApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.TestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiControllerTest {

    @InjectMocks
    private ApiController apiController;

    @Mock
    ApiService apiService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(apiController)
                .build();
    }

    @Test
    public void testGetUsersReturnsListOfUsers() throws Exception {
        List users = new ArrayList<>();
        User londonUser = TestUtils.getLondonUser();
        User nonLondonUser = TestUtils.getNonLondonUser();

        users.add(londonUser);
        users.add(nonLondonUser);
        when(apiService.getLondonUsers()).thenReturn(users);

        MvcResult result = mockMvc.perform(get("https://bpdts-test-app.herokuapp.com/users"))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<User> returnedUsers = Arrays.asList(mapper.readValue(content, User[].class));

        assertThat(returnedUsers.size(), is(2));
        assertThat(returnedUsers.get(0), is(instanceOf(User.class)));
        assertThat(returnedUsers.get(1), is(instanceOf(User.class)));
    }

}
