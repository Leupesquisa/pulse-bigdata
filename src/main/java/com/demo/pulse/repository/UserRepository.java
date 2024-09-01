package com.demo.pulse.repository;
// @author Leu A. Manuel
// @see https://github.com/Leupesquisa

import com.demo.pulse.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
