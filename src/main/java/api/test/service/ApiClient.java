package api.test.service;

import api.test.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component
public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    public List<User> callApi(String endpoint) {
        List<User> users;

        try {
            URL url = new URL(endpoint);
            HttpURLConnection urlConnection;
            urlConnection = createConnection(url);

            if (urlConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + urlConnection.getResponseCode());
            }

            users = read(urlConnection);
            urlConnection.disconnect();

        } catch (IOException e) {
            logger.error("Error connecting to Api", e);
            throw new UncheckedIOException(e);
        }
        return users;
    }

    public HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public List<User> read(HttpURLConnection conn) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));
        return mapUsers(br);
    }

    public List<User> mapUsers(BufferedReader br) throws IOException {
        List<User> users = new ArrayList<>();
        String output;
        ObjectMapper mapper = new ObjectMapper();
        while ((output = br.readLine()) != null) {
            users = Arrays.asList(mapper.readValue(output, User[].class));
        }
        return users;
    }
}
