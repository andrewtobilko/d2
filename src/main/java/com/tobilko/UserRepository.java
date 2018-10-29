package com.tobilko;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by Andrew Tobilko on 10/28/18.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> getUserByName(String name);

}
