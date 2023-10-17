package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DbUserRepositoryTest {
    private final UserRepository userRepository;

    @Test
    void saveUser() {
        userRepository.saveUser(new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9)));
        Optional<User> userOptional = userRepository.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "email"));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login"));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "login"));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(2000, 2, 9)));
    }

    @Test
    void updateUser() {
        userRepository.saveUser(new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9)));
        userRepository.updateUser(new User(1L, "mail", "name", "login",
                LocalDate.of(2000, 2, 9)));
        Optional<User> newUserOptional = userRepository.getUser(1);

        assertThat(newUserOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
        assertThat(newUserOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "mail"));
        assertThat(newUserOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "name"));
        assertThat(newUserOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "login"));
        assertThat(newUserOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(2000, 2, 9)));
    }

    @Test
    void getAllUsers() {
        User firstUser = new User(1L, "email@ru.ru", "login", "nme",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(firstUser);
        User secondUser = new User(2L, "two@ru.ru", "loginTwo", "",
                LocalDate.of(2000, 2, 21));
        userRepository.saveUser(secondUser);
        List<User> userList = userRepository.getAllUsers();

        assertEquals(2, userList.size());
        assertEquals(firstUser, userList.get(0));
        assertEquals(secondUser, userList.get(1));
    }

    @Test
    void getUser() {
        userRepository.saveUser(new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9)));
        Optional<User> userOptional = userRepository.getUser(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "email"));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", "login"));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "login"));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                LocalDate.of(2000, 2, 9)));
    }

    @Test
    void addFriend() {
        User user = new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);
        User friend = new User(2L, "emailTwo", "loginTwo", "Two",
                LocalDate.of(2000, 2, 11));
        userRepository.saveUser(friend);
        userRepository.addFriend(user, friend);
        List<User> userList = userRepository.getFriends(user);

        assertEquals(1, userList.size());
        assertEquals(friend, userList.get(0));
    }

    @Test
    void deleteFriend() {
        User user = new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);
        User friend = new User(2L, "emailTwo", "loginTwo", "Two",
                LocalDate.of(2000, 2, 29));
        userRepository.saveUser(friend);
        userRepository.addFriend(user, friend);
        userRepository.deleteFriend(user, friend);
        List<User> userList = userRepository.getFriends(user);

        assertEquals(0, userList.size());
    }

    @Test
    void getCommonFriends() {
        User user = new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);
        User commonFriend = new User(1L, "commonEmail", "commonLogin", "common",
                LocalDate.of(2000, 2, 19));
        userRepository.saveUser(commonFriend);
        User secondUser = new User(2L, "emailTwo", "loginTwo", "Two",
                LocalDate.of(2000, 2, 29));
        userRepository.saveUser(secondUser);
        userRepository.addFriend(user, commonFriend);
        userRepository.addFriend(secondUser, commonFriend);
        List<User> userList = userRepository.getCommonFriends(user, secondUser);

        assertEquals(1, userList.size());
        assertEquals(commonFriend, userList.get(0));
    }

    @Test
    void getFriends() {
        User user = new User(1L, "email", "login", "",
                LocalDate.of(2000, 2, 9));
        userRepository.saveUser(user);
        User firstFriend = new User(1L, "secondEmail", "secondLogin", "second",
                LocalDate.of(2000, 2, 19));
        userRepository.saveUser(firstFriend);
        User secondFriend = new User(2L, "emailTwo", "loginTwo", "Two",
                LocalDate.of(2000, 2, 29));
        userRepository.saveUser(secondFriend);
        userRepository.addFriend(user, firstFriend);
        userRepository.addFriend(user, secondFriend);
        List<User> userList = userRepository.getFriends(user);

        assertEquals(2, userList.size());
        assertEquals(firstFriend, userList.get(0));
        assertEquals(secondFriend, userList.get(1));
    }
}