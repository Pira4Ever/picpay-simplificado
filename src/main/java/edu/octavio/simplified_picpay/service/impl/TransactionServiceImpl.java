package edu.octavio.simplified_picpay.service.impl;

import edu.octavio.simplified_picpay.domain.model.Transaction;
import edu.octavio.simplified_picpay.domain.model.User;
import edu.octavio.simplified_picpay.domain.model.enums.UserType;
import edu.octavio.simplified_picpay.domain.repository.TransactionRepository;
import edu.octavio.simplified_picpay.service.TransactionService;
import edu.octavio.simplified_picpay.service.UserService;
import edu.octavio.simplified_picpay.service.exception.BusinessException;
import edu.octavio.simplified_picpay.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;
    private final UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Transaction findById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public Transaction create(Transaction transactionToCreate) {
        ofNullable(transactionToCreate).orElseThrow(() -> new BusinessException("Transaction to create must not be null"));
        ofNullable(transactionToCreate.getTransactionValue()).orElseThrow(() -> new BusinessException("Transaction value must not be null"));
        ofNullable(transactionToCreate.getPayee()).orElseThrow(() -> new BusinessException("Payee must not be null"));
        ofNullable(transactionToCreate.getPayer()).orElseThrow(() -> new BusinessException("Payer must not be null"));

        if (transactionToCreate.getPayer().getUserType() == UserType.MERCHANT)
            throw new BusinessException("Payer type must not be merchant");

        if (transactionToCreate.getPayer().getBalance().compareTo(transactionToCreate.getTransactionValue()) < 0)
            throw new BusinessException("Payer doesn't have enough money");

        User payer = transactionToCreate.getPayer();
        User payee = transactionToCreate.getPayee();

        payer.setBalance(payer.getBalance().subtract(transactionToCreate.getTransactionValue()));
        payee.setBalance(payee.getBalance().add(transactionToCreate.getTransactionValue()));

        userService.update(payer.getId(), payer);
        userService.update(payee.getId(), payee);

        return repository.save(transactionToCreate);
    }

    @Override
    public Transaction update(Long aLong, Transaction entity) {
        throw new BusinessException("You cannot update a transaction");
    }

    @Transactional
    @Override
    public void delete(Long aLong) {

    }
}
