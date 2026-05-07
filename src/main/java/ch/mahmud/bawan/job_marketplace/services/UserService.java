package ch.mahmud.bawan.job_marketplace.services;

import ch.mahmud.bawan.job_marketplace.dtos.UserResponseDto;
import ch.mahmud.bawan.job_marketplace.models.User;
import ch.mahmud.bawan.job_marketplace.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponseDto)
                .toList();
    }

    public Optional<UserResponseDto> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::mapToUserResponseDto);
    }

    public Optional<UserResponseDto> getUserByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId)
                .map(this::mapToUserResponseDto);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Integer id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());

            return userRepository.save(existingUser);
        });
    }

    public boolean deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getKeycloakId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}