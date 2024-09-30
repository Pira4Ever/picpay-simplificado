package edu.octavio.simplified_picpay.controller;

import edu.octavio.simplified_picpay.controller.dto.LoginResponseDto;
import edu.octavio.simplified_picpay.controller.dto.UserDto;
import edu.octavio.simplified_picpay.domain.model.User;
import edu.octavio.simplified_picpay.infra.security.TokenService;
import edu.octavio.simplified_picpay.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public record UserController(UserService service, PasswordEncoder passwordEncoder, TokenService tokenService) {
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        var users = service.findAll();
        var usersDto = users.stream().map(UserDto::new).toList();
        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        var user = service.findById(id);
        String email = tokenService.validadeToken(authorizationHeader.replace("Bearer ", ""));
        if (email != null) {
            String emailToVerify = service.findByEmail(email).getEmail();
            if (user.getEmail().equals(emailToVerify))
                return ResponseEntity.ok(new UserDto(user));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserDto userDto) {
        var user = service.findByEmailOptional(userDto.email());

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setUserType(userDto.userType());
            newUser.setDocument(userDto.document());
            newUser.setName(userDto.name());
            newUser.setBalance(userDto.balance());
            newUser.setEmail(userDto.email());
            newUser.setPassword(passwordEncoder().encode(userDto.password()));
            service.create(newUser);
            return ResponseEntity.ok(new LoginResponseDto(this.tokenService.generateToken(newUser)));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto userDto) {
        var user = service.findByEmail(userDto.email());
        if (passwordEncoder.matches(userDto.password(), user.getPassword())) {
            return ResponseEntity.ok(new LoginResponseDto(this.tokenService.generateToken(user)));
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        var user = service.findByEmail(tokenService.validadeToken(token));
        if (!Objects.equals(user.getId(), id) || !Objects.equals(user.getId(), userDto.id()))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new UserDto(service.update(id, userDto.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        var user = service.findByEmail(tokenService.validadeToken(authorizationHeader.replace("Bearer ", "")));
        if (user.getId().equals(id)) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
