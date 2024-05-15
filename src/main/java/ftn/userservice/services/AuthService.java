package ftn.userservice.services;

import ftn.userservice.domain.entities.User;
import ftn.userservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 1 month
    @Value("${jwt.secret}")
    private String jwtSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String username, String password){
        User user = userRepository.findByUsername(username).orElseThrow();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            //TODO
        }

        user.setAccessToken(generateToken(user));
        return user;
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

}
