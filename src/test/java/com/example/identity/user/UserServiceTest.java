package com.example.identity.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.identity.auth.InvalidCredentialsException;
import com.example.identity.auth.UserAlreadyExistsException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_throwsWhenEmailExists() {
        when(userRepository.existsByEmailIgnoreCase("a@B.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.register("a@B.com", "password123"))
            .isInstanceOf(UserAlreadyExistsException.class)
            .hasMessage("User already exists with this email");

        verify(userRepository, never()).save(any());
    }

    @Test
    void register_savesUserWithLowercasedEmailAndEncodedPassword() {
        when(userRepository.existsByEmailIgnoreCase("A@B.com")).thenReturn(false);
        when(passwordEncoder.encode("secretPass99")).thenReturn("hash-abc");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.register("A@B.com", "secretPass99");

        assertThat(result.getEmail()).isEqualTo("a@b.com");
        assertThat(result.isEnabled()).isTrue();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("a@b.com");
        assertThat(captor.getValue().getPasswordHash()).isEqualTo("hash-abc");
        verify(passwordEncoder).encode("secretPass99");
    }

    @Test
    void authenticate_throwsWhenUserNotFound() {
        when(userRepository.findByEmailIgnoreCase("nope@x.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.authenticate("nope@x.com", "any"))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessage("Invalid credentials");
    }

    @Test
    void authenticate_throwsWhenPasswordDoesNotMatch() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setEnabled(true);
        user.setPasswordHash("stored-hash");
        when(userRepository.findByEmailIgnoreCase("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "stored-hash")).thenReturn(false);

        assertThatThrownBy(() -> userService.authenticate("a@b.com", "wrong"))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessage("Invalid credentials");
    }

    @Test
    void authenticate_throwsWhenUserDisabled() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setEnabled(false);
        user.setPasswordHash("stored-hash");
        when(userRepository.findByEmailIgnoreCase("a@b.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.authenticate("a@b.com", "ok"))
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessage("Invalid credentials");
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void authenticate_returnsUserWhenValid() {
        User user = new User();
        user.setEmail("a@b.com");
        user.setEnabled(true);
        user.setPasswordHash("stored-hash");
        when(userRepository.findByEmailIgnoreCase("a@b.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correct", "stored-hash")).thenReturn(true);

        User result = userService.authenticate("a@b.com", "correct");

        assertThat(result).isSameAs(user);
        verify(passwordEncoder).matches(eq("correct"), eq("stored-hash"));
    }
}
