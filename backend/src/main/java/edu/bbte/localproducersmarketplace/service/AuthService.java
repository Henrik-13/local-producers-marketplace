package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.model.User;

public interface AuthService {
    boolean login(User user, String username);

    User register(User user);
}
