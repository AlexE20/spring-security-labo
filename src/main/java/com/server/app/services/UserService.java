package com.server.app.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.app.dto.auth.SignupDto;
import com.server.app.dto.auth.UpdatePasswordDto;
import com.server.app.dto.auth.UpdateProfileDto;
import com.server.app.dto.user.UserCreateDto;
import com.server.app.dto.user.UserUpdateDto;
import com.server.app.entities.Role;
import com.server.app.entities.User;
import com.server.app.exceptions.BadRequestException;
import com.server.app.exceptions.ConfictException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.exceptions.UnauthorizedException;
import com.server.app.repositories.RoleRepository;
import com.server.app.repositories.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Transactional
  public User create(UserCreateDto dto) {
    uniqueUsername(dto.getUsername(), null);
    uniqueEmail(dto.getEmail(), null);
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setName(dto.getName());
    user.setSurname(dto.getSurname());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));

    if (dto.getRole() != null) {
      Role role = roleRepository.findById(dto.getRole())
          .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
      user.setRole(role);
    }

    return userRepository.save(user);
  }

  public Page<User> findAll(int page, int size, String search) {
    return userRepository.findAll(PageRequest.of(page, size), search);
  }

  public User findById(int id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
  }

  @Transactional
  public User updateUser(int userId, UserUpdateDto dto) {
    User user = findById(userId);

    if (user.isBlocked()) {
      throw new ConfictException("The user: " + user.getUsername() + " is locked");
    }

    if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
      uniqueUsername(dto.getUsername(), userId);
      user.setUsername(dto.getUsername());
    }

    if (dto.getName() != null && !dto.getName().isBlank()) {
      user.setName(dto.getName());
    }

    if (dto.getSurname() != null && !dto.getSurname().isBlank()) {
      user.setSurname(dto.getSurname());
    }

    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      uniqueEmail(dto.getEmail(), userId);
      user.setEmail(dto.getEmail());
    }

    if (dto.getBlocked() != null) {
      user.setBlocked(dto.getBlocked());
    }

    if (dto.getRole() != null) {
      Role role = roleRepository.findById(dto.getRole())
          .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
      user.setRole(role);
    }

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
      user.setPassword(dto.getPassword());
    }

    return userRepository.save(user);
  }

  public User login(String username, String rawPassword) {
    User user = userRepository.findUserByUsername(username)
        .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

    if (user.getRole() == null || user.getRole().getActive() == null || !user.getRole().getActive()) {
      throw new UnauthorizedException("Your account role is not active");
    }

    if (user.isBlocked()) {
      throw new UnauthorizedException("Your account has been blocked");
    }

    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new UnauthorizedException("Credenciales inválidas");
    }

    return user;
  }

  @Transactional
  public User signup(SignupDto dto) {
    uniqueUsername(dto.getUsername(), null);
    uniqueEmail(dto.getEmail(), null);

    Role role = roleRepository.findByName("ADMIN")
        .orElseThrow(() -> new NotFoundException("Rol ADMIN no encontrado"));

    if (role.getActive() == null || !role.getActive()) {
      throw new UnauthorizedException("Your account role is not active");
    }

    User user = new User();
    user.setUsername(dto.getUsername());
    user.setName(dto.getName());
    user.setSurname(dto.getSurname());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(role);

    return userRepository.save(user);
  }

  @Transactional
  public User updateAuthProfile(int userId, UpdateProfileDto dto) {
    User user = findById(userId);

    if (dto.getUsername() != null && !dto.getUsername().isBlank()) {
      uniqueUsername(dto.getUsername(), userId);
      user.setUsername(dto.getUsername());
    }

    if (dto.getName() != null && !dto.getName().isBlank()) {
      user.setName(dto.getName());
    }

    if (dto.getSurname() != null && !dto.getSurname().isBlank()) {
      user.setSurname(dto.getSurname());
    }

    if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
      uniqueEmail(dto.getEmail(), userId);
      user.setEmail(dto.getEmail());
    }

    return userRepository.save(user);
  }

  @Transactional
  public User updatePassword(int userId, UpdatePasswordDto dto) {
    User user = findById(userId);

    if (!passwordEncoder.matches(dto.getOldpassword(), user.getPassword())) {
      throw new BadRequestException("La contraseña anterior es incorrecta");
    }

    if (!dto.getNewpassword().equals(dto.getConfirmpassword())) {
      throw new BadRequestException("La nueva contraseña y su confirmación no coinciden");
    }

    user.setPassword(dto.getNewpassword());
    return userRepository.save(user);
  }

  private void uniqueUsername(String username, Integer id) {
    userRepository.findUserByUsername(username).ifPresent(existing -> {
      if (id == null || existing.getId() != id) {
        throw new ConfictException("El nombre de usuario ya está en uso");
      }
    });
  }

  private void uniqueEmail(String email, Integer id) {
    userRepository.findUserByEmail(email).ifPresent(existing -> {
      if (id == null || existing.getId() != id) {
        throw new ConfictException("El correo electrónico ya está en uso");
      }
    });
  }
}
