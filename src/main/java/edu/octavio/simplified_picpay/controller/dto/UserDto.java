package edu.octavio.simplified_picpay.controller.dto;

import edu.octavio.simplified_picpay.domain.model.User;
import edu.octavio.simplified_picpay.domain.model.enums.UserType;

import java.math.BigDecimal;

public record UserDto(
        Long id,
        String name,
        String document,
        String email,
        String password,
        BigDecimal balance,
        UserType userType) {

    public UserDto(User model) {
        this(
                model.getId(),
                model.getName(),
                model.getDocument(),
                model.getEmail(),
                model.getPassword(),
                model.getBalance(),
                model.getUserType()
        );
    }

    public User toModel() {
        User model = new User();
        model.setId(this.id);
        model.setName(this.name);
        model.setDocument(this.document);
        model.setEmail(this.email);
        model.setPassword(this.password);
        model.setBalance(this.balance);
        model.setUserType(this.userType);

        return model;
    }
}
