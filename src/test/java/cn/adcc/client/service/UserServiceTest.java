package cn.adcc.client.service;

import cn.adcc.client.ClientApplicationTests;
import cn.adcc.client.DTO.UserDto;
import cn.adcc.client.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@Component
class UserServiceTest extends ClientApplicationTests {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        UserDto userDto = new UserDto();
        userDto.setUsername("明老大");
        userDto.setSensitiveNum(20);
        userService.save(userDto);
    }

    @Test
    void delete() {
        userRepository.deleteById(11L);
    }

    @Test
    void findByUsername() {
    }

    @Test
    void list() {
    }
}