package ch.mahmud.bawan.job_marketplace.services;

import ch.mahmud.bawan.job_marketplace.dtos.UserRegisterRequestDto;
import ch.mahmud.bawan.job_marketplace.dtos.UserResponseDto;
import ch.mahmud.bawan.job_marketplace.models.Role;
import ch.mahmud.bawan.job_marketplace.models.User;
import ch.mahmud.bawan.job_marketplace.repositories.UserRepository;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final Keycloak keycloak;
    private final UserRepository userRepository;

    @Value("${keycloak.realm}")
    private String realm;

    public AuthService(Keycloak keycloak, UserRepository userRepository) {
        this.keycloak = keycloak;
        this.userRepository = userRepository;
    }

    public UserResponseDto register(UserRegisterRequestDto request) {
        validateRegisterRequest(request);

        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(request.getEmail());
        keycloakUser.setEmail(request.getEmail());
        keycloakUser.setFirstName(request.getName());
        keycloakUser.setEnabled(true);
        keycloakUser.setEmailVerified(true);

        CredentialRepresentation passwordCredential = new CredentialRepresentation();
        passwordCredential.setTemporary(false);
        passwordCredential.setType(CredentialRepresentation.PASSWORD);
        passwordCredential.setValue(request.getPassword());

        keycloakUser.setCredentials(List.of(passwordCredential));

        Response response = keycloak.realm(realm)
                .users()
                .create(keycloakUser);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Could not create user in Keycloak. Status: " + response.getStatus());
        }

        String keycloakId = CreatedResponseUtil.getCreatedId(response);

        try {
            assignRealmRole(keycloakId, request.getRole());

            User localUser = new User();
            localUser.setKeycloakId(keycloakId);
            localUser.setName(request.getName());
            localUser.setEmail(request.getEmail());
            localUser.setRole(request.getRole());

            User savedUser = userRepository.save(localUser);

            return mapToUserResponseDto(savedUser);

        } catch (Exception exception) {
            keycloak.realm(realm).users().get(keycloakId).remove();
            throw exception;
        }
    }

    private void validateRegisterRequest(UserRegisterRequestDto request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Password is required");
        }

        if (request.getRole() == null) {
            throw new RuntimeException("Role is required");
        }

        if (request.getRole() == Role.ADMIN) {
            throw new RuntimeException("Users are not allowed to register as ADMIN");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("A user with this email already exists");
        }
    }

    private void assignRealmRole(String keycloakId, Role role) {
        RoleRepresentation keycloakRole = keycloak.realm(realm)
                .roles()
                .get(role.name())
                .toRepresentation();

        keycloak.realm(realm)
                .users()
                .get(keycloakId)
                .roles()
                .realmLevel()
                .add(List.of(keycloakRole));
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();

        dto.setUserId(user.getUserId());
        dto.setKeycloakId(user.getKeycloakId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
}