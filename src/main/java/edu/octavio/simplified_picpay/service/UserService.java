package edu.octavio.simplified_picpay.service;

import edu.octavio.simplified_picpay.domain.model.User;

import java.util.Optional;

public interface UserService extends CrudService<Long, User> {
    User findByEmail(String email);
    Optional<User> findByEmailOptional(String email);
}
