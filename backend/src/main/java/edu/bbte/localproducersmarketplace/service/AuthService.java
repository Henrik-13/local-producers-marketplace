package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.model.User;

public interface AuthService {
    String login(String email, String password);

    User register(User user);
}
