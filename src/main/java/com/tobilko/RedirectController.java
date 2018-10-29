package com.tobilko;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

/**
 * Created by Andrew Tobilko on 10/27/18.
 */
@RestController
public class RedirectController {

    @Autowired
    private SecurityService service;

    private RestTemplate template = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RuleRepository ruleRepository;


    @RequestMapping("/*")
    public String redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String headerValue = request.getHeader("Authorization");

        if (headerValue == null) {
            throw new IllegalArgumentException("The header 'Authorization' wasn't provided");
        }

        User incomingUser = parseHeaderValue(headerValue);
        Optional<User> optionalUser = userRepository.getUserByName(incomingUser.getName());

        User userFromDB = optionalUser.orElseThrow(() -> new IllegalArgumentException("User '" + incomingUser.getName() + "' wasn't found in the configuration."));

        if (!userFromDB.getPassword().equals(incomingUser.getPassword())) {
            throw new IllegalArgumentException("Illegal credentials for user '" + incomingUser.getName() + "'.");
        }

        validate(request, userFromDB);

        final String body = getBody(request);
        try {
            String url = service.getBaseServerURL() + request.getRequestURI();
            final HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type", "text/plain");

            final ResponseEntity<String> exchange = template.exchange(url, httpMethod, new HttpEntity<>(body, headers), String.class);

            return service.isEncryptResponses() ? encrypt(exchange.getBody()) : exchange.getBody();
        } catch (HttpClientErrorException e) {
            return e.getMessage();
        }
    }

    private String encrypt(String body) {
        return service.getEncryptor().encrypt(body);
    }

    private void validate(HttpServletRequest request, User userFromDB) {
        final String uri = request.getRequestURI();

        final String role = userFromDB.getRole();

        final Optional<Rule> ruleByRole = ruleRepository.findRuleByRole(role);

        final Rule rule = ruleByRole.orElseThrow(() -> new IllegalArgumentException("There is no rules for role '" + role + "'."));

        if (!new HashSet<>(Arrays.asList(rule.getAllowedURIs())).contains(uri)) {
            throw new IllegalArgumentException("User '" + userFromDB.getName() + "' isn't allowed to perform this operation.");
        }

    }

    private String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ex) {
//                    throw ex;
//                }
//            }
        }

        body = stringBuilder.toString();
        return body;
    }

    private User parseHeaderValue(String headerValue) throws IOException {
        final String credentials = headerValue.split(" ")[1];

        final String decodedCredentials = new String(new BASE64Decoder().decodeBuffer(credentials));

        final String[] values = decodedCredentials.split(":");
        String name = values[0];
        String password = values[1];

        return new User(name, password);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIAE(IllegalArgumentException e) {
        return e.getMessage();
    }

}
