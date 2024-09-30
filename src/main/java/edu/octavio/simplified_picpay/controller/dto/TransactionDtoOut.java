package edu.octavio.simplified_picpay.controller.dto;

import edu.octavio.simplified_picpay.domain.model.Transaction;
import edu.octavio.simplified_picpay.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDtoOut(Long id, BigDecimal transactionValue, User payer, User payee, LocalDateTime createdAt) {
    @Autowired

    public TransactionDtoOut(Transaction model) {
        this(
                model.getId(),
                model.getTransactionValue(),
                model.getPayer(),
                model.getPayee(),
                model.getCreatedAt()
        );
    }
}
