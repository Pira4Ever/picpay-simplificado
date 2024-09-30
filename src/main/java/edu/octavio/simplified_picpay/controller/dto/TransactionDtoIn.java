package edu.octavio.simplified_picpay.controller.dto;

import edu.octavio.simplified_picpay.domain.model.Transaction;
import edu.octavio.simplified_picpay.domain.model.User;
import edu.octavio.simplified_picpay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDtoIn(Long id, BigDecimal transactionValue, Long payerId, Long payeeId, LocalDateTime createdAt) {

    public Transaction toModel(UserService userService) {
        Transaction model = new Transaction();
        model.setId(this.id);
        model.setPayer(userService.findById(this.payerId));
        model.setPayee(userService.findById(this.payeeId));
        model.setTransactionValue(this.transactionValue);
        model.setCreatedAt(this.createdAt);

        return model;
    }
}
