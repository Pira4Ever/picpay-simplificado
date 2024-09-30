package edu.octavio.simplified_picpay.controller;

import edu.octavio.simplified_picpay.controller.dto.TransactionDtoIn;
import edu.octavio.simplified_picpay.controller.dto.TransactionDtoOut;
import edu.octavio.simplified_picpay.infra.security.TokenService;
import edu.octavio.simplified_picpay.service.TransactionService;
import edu.octavio.simplified_picpay.service.UserService;
import edu.octavio.simplified_picpay.service.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/transaction")
public record TransactionController(TransactionService service, UserService userService, PasswordEncoder passwordEncoder, TokenService tokenService) {
    @GetMapping
    public ResponseEntity<List<TransactionDtoOut>> findAll() {
        var transactions = service.findAll();
        var transactionsDto = transactions.stream().map(TransactionDtoOut::new).toList();
        return ResponseEntity.ok(transactionsDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDtoOut> findById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        var user = userService.findByEmail(tokenService.validadeToken(token));
        var transaction = service.findById(id);
        if (Objects.equals(user.getId(), transaction.getPayer().getId())) {
            TransactionDtoOut dtoOut = new TransactionDtoOut(transaction);
            dtoOut.payee().setPassword(null);
            dtoOut.payee().setBalance(null);
            return ResponseEntity.ok(dtoOut);
        } else if (Objects.equals(user.getId(), transaction.getPayee().getId())) {
            TransactionDtoOut dtoOut = new TransactionDtoOut(transaction);
            dtoOut.payer().setPassword(null);
            dtoOut.payer().setBalance(null);
            return ResponseEntity.ok(dtoOut);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<TransactionDtoOut> create(@RequestBody TransactionDtoIn transactionDtoIn, @RequestHeader("Authorization") String authorizationHeader) {
        if (Objects.equals(transactionDtoIn.payeeId(), transactionDtoIn.payerId())) throw new BusinessException("payer id and payee id must be different");

        String token = authorizationHeader.replace("Bearer ", "");
        var user = userService.findByEmail(tokenService.validadeToken(token));
        if (!Objects.equals(user.getId(), transactionDtoIn.payerId())) throw new BusinessException("the authenticated user must be the payer");

        var transaction = service.create(transactionDtoIn.toModel(userService));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transaction.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TransactionDtoOut(transaction));
    }
}
