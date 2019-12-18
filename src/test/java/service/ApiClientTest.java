package service;

import api.test.model.User;
import api.test.service.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Spy;
import utils.TestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApiClientTest {

    @Spy
    ApiClient apiClient;

    @Mock
    HttpURLConnection connection;

    @Mock
    ObjectMapper mapper;

    @Mock
    BufferedReader bufferedReader;

    String url = "https://bpdts-test-app.herokuapp.com/city/London/users";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User[] users = new User[1];
    private User londonUser;

    @Before
    public void init() {
        initMocks(this);
        londonUser = TestUtils.getLondonUser();
        users[0] = londonUser;
    }

    @Test
    public void testCallApiReturnsLondonUsersWhenConnectionIsSuccesful() throws IOException {

        doReturn(connection).when(apiClient).createConnection(any());
        doReturn(200).when(connection).getResponseCode();
        doReturn(Arrays.asList(users)).when(apiClient).read(any());

        List<User> returnedUsers = apiClient.callApi(url);

        Assert.assertThat(returnedUsers.size(), is(1));
        Assert.assertThat(Arrays.asList(users), is(returnedUsers));
    }

    @Test
    public void testMapUsersReturnsEmptyListWhenNoResultsAreReturned() throws IOException {
        when(mapper.readValue(anyString(), ArgumentMatchers.<Class<User[]>>any())).thenReturn(users);
        when(bufferedReader.readLine()).thenReturn(null);
        List<User> returnedUsers = apiClient.mapUsers(bufferedReader);
        assertThat(returnedUsers.size(), is(0));
    }

    @Test
    public void testRuntimeExceptionWhenResponseIsNotOk() throws IOException {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Failed : HTTP error code : 404");
        doReturn(connection).when(apiClient).createConnection(any());
        doReturn(404).when(connection).getResponseCode();
        apiClient.callApi(url);
    }

    @Test
    public void testIOExceptionIsHandled() throws IOException {
        doThrow(new IOException("HEY")).when(apiClient).createConnection(any());
        thrown.expect(UncheckedIOException.class);
        thrown.expectMessage("HEY");
        apiClient.callApi(url);
    }
}
