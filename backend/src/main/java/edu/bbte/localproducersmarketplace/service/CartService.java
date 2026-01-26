package edu.bbte.localproducersmarketplace.service;

import edu.bbte.localproducersmarketplace.dto.in.CartCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.CartResponseDTO;
import edu.bbte.localproducersmarketplace.model.User;

import java.util.List;

public interface CartService {

    CartResponseDTO createCart(User user, CartCreateDTO dto);

    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO addItemToCart(Long userId, CartCreateDTO dto);

    CartResponseDTO updateCart(Long userId, CartCreateDTO dto);

    CartResponseDTO removeItemFromCart(Long userId, Long productId);

    void clearCart(Long userId);

    void deleteCart(Long userId);
}

