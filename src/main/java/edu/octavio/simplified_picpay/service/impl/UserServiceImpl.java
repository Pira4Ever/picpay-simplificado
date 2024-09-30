package edu.octavio.simplified_picpay.service.impl;

import edu.octavio.simplified_picpay.domain.model.User;
import edu.octavio.simplified_picpay.domain.repository.UserRepository;
import edu.octavio.simplified_picpay.service.UserService;
import edu.octavio.simplified_picpay.service.exception.BusinessException;
import edu.octavio.simplified_picpay.service.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public User create(User userToCreate) {
        ofNullable(userToCreate).orElseThrow(() -> new BusinessException("User to create must not be null"));
        ofNullable(userToCreate.getName()).orElseThrow(() -> new BusinessException("User name must not be null"));
        ofNullable(userToCreate.getDocument()).orElseThrow(() -> new BusinessException("User document must not be null"));
        ofNullable(userToCreate.getEmail()).orElseThrow(() -> new BusinessException("User email must not be null"));
        ofNullable(userToCreate.getPassword()).orElseThrow(() -> new BusinessException("User password must not be null"));
        ofNullable(userToCreate.getUserType()).orElseThrow(() -> new BusinessException("User type must not be null"));

        if (repository.existsByEmail(userToCreate.getEmail()))
            throw new BusinessException("The email already exists");
        if (repository.existsByDocument(userToCreate.getDocument()))
            throw new BusinessException("The document already exists");

        userToCreate.setDocument(userToCreate.getDocument().replaceAll("[^a-zA-Z0-9]", "").toUpperCase());

        if (!validateDocument(userToCreate.getDocument()))
            throw new BusinessException("Invalid document");

        userToCreate.setBalance(ofNullable(userToCreate.getBalance()).orElse(BigDecimal.valueOf(0)));

        return repository.save(userToCreate);
    }

    @Transactional
    @Override
    public User update(Long id, User userToUpdate) {
        User dbUser = this.findById(id);
        if (!dbUser.getId().equals(userToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same");
        }

        if (ofNullable(userToUpdate.getName()).isPresent() && !userToUpdate.getName().equals(dbUser.getName()))
            dbUser.setName(userToUpdate.getName());
        if (ofNullable(userToUpdate.getDocument()).isPresent() && !userToUpdate.getDocument().equals(dbUser.getDocument())) {
            dbUser.setDocument(userToUpdate.getDocument().replaceAll("[^a-zA-Z0-9]", "").toUpperCase());
            if (repository.existsByDocument(userToUpdate.getDocument()))
                throw new BusinessException("The document already exists");
            if(!validateDocument(dbUser.getDocument()))
                throw new BusinessException("Invalid document");
        }
        if (ofNullable(userToUpdate.getEmail()).isPresent() && !userToUpdate.getEmail().equals(dbUser.getEmail())) {
            if (repository.existsByEmail(userToUpdate.getEmail()))
                throw new BusinessException("The email already exists");
            dbUser.setEmail(userToUpdate.getEmail());
        }
        if (ofNullable(userToUpdate.getPassword()).isPresent() && !userToUpdate.getPassword().equals(dbUser.getPassword()))
            dbUser.setPassword(userToUpdate.getPassword());
        if (ofNullable(userToUpdate.getBalance()).isPresent() && userToUpdate.getBalance().compareTo(dbUser.getBalance()) != 0)
            dbUser.setBalance(userToUpdate.getBalance());
        if (ofNullable(userToUpdate.getUserType()).isPresent() && !userToUpdate.getUserType().equals(dbUser.getUserType()))
            dbUser.setUserType(userToUpdate.getUserType());

        return repository.save(dbUser);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.delete(this.findById(id));
    }

    private boolean validateDocument(String document) {
        int verifier1 = document.charAt(document.length() - 2) - 48;
        int verifier2 = document.charAt(document.length() - 1) - 48;
        if (document.length() == 14) {
            // CNPJ
            int[] multipliers1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] multipliers2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int sum = 0;

            for (int i = 0; i < 12; i++) { // nicaio
                sum += (document.charAt(i) - 48) * multipliers1[i];
            }

            if (sum % 11 >= 2) {
                if (11 - (sum % 11) != verifier1)
                    return false;
            } else {
                if (0 != verifier1)
                    return false;
            }

            sum = 0;

            for (int i = 0; i < 13; i++) {
                sum += (document.charAt(i) - 48) * multipliers2[i];
            }

            if (sum % 11 >= 2) {
                return 11 - (sum % 11) == verifier2;
            } else {
                return 0 == verifier2;
            }
        } else if (document.length() == 11) {
            // CPF
            int[] multipliers1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] multipliers2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

            int sum = 0;

            for (int i = 0; i < 9; i++) {
                sum += (document.charAt(i) - 48) * multipliers1[i];
            }

            if (sum % 11 >= 2) {
                if (11 - (sum % 11) != verifier1)
                    return false;
            } else {
                if (0 != verifier1)
                    return false;
            }

            sum = 0;

            for (int i = 0; i < 10; i++) {
                sum += (document.charAt(i) - 48) * multipliers2[i];
            }

            if (sum % 11 >= 2) {
                return 11 - (sum % 11) == verifier2;
            } else {
                return 0 == verifier2;
            }
        }
        return true;
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    @Override
    public Optional<User> findByEmailOptional(String email) {
        return repository.findByEmail(email);
    }

}
