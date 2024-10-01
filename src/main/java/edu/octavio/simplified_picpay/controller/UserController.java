package edu.octavio.simplified_picpay.controller;

import edu.octavio.simplified_picpay.controller.dto.LoginResponseDto;
import edu.octavio.simplified_picpay.controller.dto.UserDto;
import edu.octavio.simplified_picpay.domain.model.User;
import edu.octavio.simplified_picpay.infra.security.TokenService;
import edu.octavio.simplified_picpay.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Tag(name = "Users controller", description = "RESTful API for managing users.")
public record UserController(UserService service, PasswordEncoder passwordEncoder, TokenService tokenService) {
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<UserDto>> findAll() {
        var users = service.findAll();
        var usersDto = users.stream().map(UserDto::new).toList();
        return ResponseEntity.ok(usersDto);
    }

    @Operation(summary = "Get user by id", description = "Retrieve the user with specified id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found user with specified id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Register an user", description = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successful"),
            @ApiResponse(responseCode = "400", description = "Error while creating user"),
            @ApiResponse(responseCode = "422", description = "Error while creating user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
            var returnedUser = service.create(newUser);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(returnedUser.getId())
                    .toUri();
            return ResponseEntity.created(location).body(new LoginResponseDto(this.tokenService.generateToken(newUser)));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Authenticate user", description = "Authenticate the user using token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserDto userDto) {
        var user = service.findByEmail(userDto.email());
        if (passwordEncoder.matches(userDto.password(), user.getPassword())) {
            return ResponseEntity.ok(new LoginResponseDto(this.tokenService.generateToken(user)));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Update user", description = "Update the user data", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found user with specified id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        var user = service.findByEmail(tokenService.validadeToken(token));
        if (!Objects.equals(user.getId(), id) || !Objects.equals(user.getId(), userDto.id()))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new UserDto(service.update(id, userDto.toModel())));
    }

    @Operation(summary = "Update user", description = "Update the user data", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found user with specified id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
