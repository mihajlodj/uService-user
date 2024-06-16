package ftn.userservice.services;

import ftn.userservice.domain.dtos.UserCreateRequest;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.entities.User;
import ftn.userservice.domain.mappers.UserMapper;
import ftn.userservice.exception.exceptions.BadRequestException;
import ftn.userservice.exception.exceptions.NotFoundException;
import ftn.userservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 1 month
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User doesn't exist"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Bad password");
        }
        user.setAccessToken(generateToken(user));
        log.info(user.getUsername() + " logged in");

        return user;
    }

    public UserDto create(UserCreateRequest userCreateRequest) {
        if(!userCreateRequest.getPassword().equals(userCreateRequest.getRepeatPassword())) {
            throw new BadRequestException("Passwords are not the same");
        }

        User user = UserMapper.INSTANCE.fromCreateRequest(userCreateRequest);
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        log.info("Registering a new user: " + user.getEmail());

        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

}
