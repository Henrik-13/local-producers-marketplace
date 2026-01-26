package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.controller.mapper.UserMapper;
import edu.bbte.localproducersmarketplace.dto.out.UserOutDto;
import edu.bbte.localproducersmarketplace.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public Collection<UserOutDto> getUsers() {
        log.info("Retrieving all users");
        return userService.findAll().stream().map(userMapper::userToUserOutDto).toList();
    }

    @GetMapping("/{id}")
    public UserOutDto getUserById(@PathVariable Long id) {
        log.info("Retrieving user with id: {}", id);
        return userMapper.userToUserOutDto(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        userService.deleteById(id);
    }
}
