package vuetinaut.com.mick.vuetinaut.user;

import com.mick.vuetinaut.exceptions.NotFoundException;
import com.mick.vuetinaut.jooq.model.tables.pojos.User;
import com.mick.vuetinaut.security.PasswordService;
import com.mick.vuetinaut.user.UserRepository;
import com.mick.vuetinaut.user.UserService;
import com.mick.vuetinaut.user.UsernameAlreadyExistsException;
import io.reactivex.Single;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(new PasswordService("test"), userRepository);
    }

    @Test
    public void testCreateNewUser() {
        User user = createUser();
        when(userRepository.fetchByUsername(user.getUsername())).thenReturn(Single.error(new NotFoundException()));
        when(userRepository.insert(user)).thenReturn(Single.just(user));

        userService.createUser(user)
                .test()
                .assertComplete()
                .assertNoErrors();

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).insert(argumentCaptor.capture());
        User createdUser = argumentCaptor.getValue();

        assertThat(createdUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testUsernameAlreadyExisting() {
        User user = createUser();
        when(userRepository.fetchByUsername(user.getUsername())).thenReturn(Single.just(new User()));
        when(userRepository.insert(user)).thenReturn(Single.just(user));

        userService.createUser(user)
                .test()
                .assertError(UsernameAlreadyExistsException.class);
    }

    public User createUser() {
        User user = new User();
        user.setUsername("A new user");
        user.setPassword("password");

        return user;
    }
}
