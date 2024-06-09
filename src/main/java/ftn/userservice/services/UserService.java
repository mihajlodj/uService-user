package ftn.userservice.services;

import ftn.userservice.domain.dtos.ChangePasswordRequest;
import ftn.userservice.domain.dtos.UserDto;
import ftn.userservice.domain.dtos.UserUpdateRequest;
import ftn.userservice.domain.entities.User;
import ftn.userservice.domain.mappers.UserMapper;
import ftn.userservice.exception.exceptions.BadRequestException;
import ftn.userservice.exception.exceptions.NotFoundException;
import ftn.userservice.repositories.UserRepository;
import ftn.userservice.utils.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        //TODO check reservations when other services are done
        userRepository.deleteById(id);
    }

}
