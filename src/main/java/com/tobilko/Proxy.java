package com.tobilko;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by Andrew Tobilko on 10/27/18.
 */
@SpringBootApplication
public class Proxy {

    public static void main(String[] args) {
        SpringApplication.run(Proxy.class, args);
    }


    @Bean
    public ApplicationRunner r(UserRepository repository, RuleRepository ruleRepository) {
        return ar -> {
            repository.save(new User("ADMIN", "andrew", "123"));
            repository.save(new User("USER", "james", "456"));
            repository.save(new User("APPRENTICE", "john", "789"));

            ruleRepository.save(new Rule("ADMIN", new String[]{"/start", "/stop", "/data"}));
            ruleRepository.save(new Rule("USER", new String[]{}));
            ruleRepository.save(new Rule("APPRENTICE", new String[]{"/start"}));
        };
    }

}
