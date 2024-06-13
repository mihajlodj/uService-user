package ftn.userservice.services;

import ftn.userservice.domain.dtos.ChangePasswordRequest;
import ftn.userservice.domain.dtos.ReservationDto;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import ftn.userservice.domain.entities.Role;
import ftn.userservice.domain.entities.User;
import ftn.userservice.domain.mappers.UserMapper;
import ftn.userservice.exception.exceptions.BadRequestException;
import ftn.userservice.exception.exceptions.ForbiddenException;
import ftn.userservice.exception.exceptions.NotFoundException;
import ftn.userservice.repositories.UserRepository;
import ftn.userservice.utils.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestService restService;

    public UserDto getById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist"));
        return UserMapper.INSTANCE.toDto(user);
    }

    public UserDto me() {
        UUID id = AuthUtils.getLoggedUserId();
        return getById(id);
    }

    public UserDto update(UserUpdateRequest userUpdateRequest) {
        UUID id = AuthUtils.getLoggedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist"));
        UserMapper.INSTANCE.update(user, userUpdateRequest);

        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        UUID id = AuthUtils.getLoggedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist"));

        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect old password");
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getRepeatNewPassword())) {
            throw new BadRequestException("Passwords are not the same");
        }

        String newPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(newPassword);

        userRepository.save(user);
    }

    public UserDto updateNotifications() {
        UUID id = AuthUtils.getLoggedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User doesn't exist"));
        user.setNotificationsAllowed(!user.isNotificationsAllowed());
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    public void delete() {
        UUID id = AuthUtils.getLoggedUserId();
        User user = userRepository.getReferenceById(id);
        List<ReservationDto> reservations = new ArrayList<>();
        boolean found = false;

        if (user.getRole() == Role.GUEST) {
            reservations = restService.getGuestReservations();
        } else if (user.getRole() == Role.HOST) {
            reservations = restService.getHostReservations();
        }

        for (ReservationDto reservation: reservations) {
            if (reservation.getStatus() == ReservationDto.ReservationStatus.ACTIVE) {
                if (reservation.getDateFrom().isAfter(LocalDateTime.now().minusDays(1))) {
                    found = true;
                    break;
                }
            }
        }

        if (found) {
            throw new ForbiddenException("Can't delete account with reservations");
        }

        if (user.getRole() == Role.HOST) {
            restService.deleteHostLodges(id);
        }
        userRepository.deleteById(id);
    }

}
