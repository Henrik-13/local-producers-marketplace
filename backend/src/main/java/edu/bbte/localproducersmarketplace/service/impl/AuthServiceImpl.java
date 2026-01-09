package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.service.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public boolean login(User user, String username) {
        return false;
    }

    @Override
    public User register(User user) {
        return null;
    }
}
