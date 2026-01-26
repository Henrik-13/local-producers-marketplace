package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.repository.UserRepository;
import edu.bbte.localproducersmarketplace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }
}
