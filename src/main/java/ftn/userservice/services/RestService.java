package ftn.userservice.services;

import ftn.userservice.domain.dtos.ReservationDto;
import ftn.userservice.utils.AuthUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestService {

    private final RestTemplate restTemplate;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${reservation.service}")
    private String reservationServiceUrl;

    static final long EXPIRATION_TIME = 30L * 24 * 60 * 60 * 1000; // 1 month

    public List<ReservationDto> getGuestReservations() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + createUserToken(AuthUtils.getLoggedUsername(), AuthUtils.getLoggedUserId().toString(), "GUEST"));
            HttpEntity<String> httpRequest = new HttpEntity<>(headers);

            String url = reservationServiceUrl + "/api/reservation/all/guest";
            ResponseEntity<List<ReservationDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpRequest,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get reservations");
            }
        } catch (Exception e) {
            log.error("Error while getting reservations: ", e);
            throw new RuntimeException("Unexpected error while getting reservations");
        }
    }

    public List<ReservationDto> getHostReservations() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + createUserToken(AuthUtils.getLoggedUsername(), AuthUtils.getLoggedUserId().toString(), "HOST"));
            HttpEntity<String> httpRequest = new HttpEntity<>(headers);

            String url = reservationServiceUrl + "/api/reservation/all/host";
            ResponseEntity<List<ReservationDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpRequest,
                    new ParameterizedTypeReference<>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to get reservations");
            }
        } catch (Exception e) {
            log.error("Error while getting reservations: ", e);
            throw new RuntimeException("Unexpected error while getting reservations");
        }
    }

    public void deleteHostLodges(UUID hostId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + createAdminToken());
            HttpEntity<String> httpRequest = new HttpEntity<>(headers);

            String url = reservationServiceUrl + "/api/reservation/delete/host/" + hostId;
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error while deleting lodges: ", e);
            throw new RuntimeException("Unexpected error while deleting lodges");
        }
    }

    private String createAdminToken() {
        return Jwts.builder()
                .setSubject("admin@ftn.com")
                .claim("role", "ADMIN")
                .claim("userId", "e40fcab5-d45b-4567-9d91-14e58178fea6")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String createUserToken(String email, String userId, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

}
