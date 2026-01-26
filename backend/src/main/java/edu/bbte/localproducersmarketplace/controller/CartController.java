package edu.bbte.localproducersmarketplace.controller;

import edu.bbte.localproducersmarketplace.dto.in.CartCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.CartResponseDTO;
import edu.bbte.localproducersmarketplace.exception.UserNotFoundException;
import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.repository.UserRepository;
import edu.bbte.localproducersmarketplace.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    @PostMapping("/{userId}")
    public CartResponseDTO create(@PathVariable Long userId,
                                  @RequestBody @Valid CartCreateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return cartService.createCart(user, dto);
    }

    @GetMapping("/user/{userId}")
    public CartResponseDTO getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/{userId}/items")
    public CartResponseDTO addItems(@PathVariable Long userId,
                                    @RequestBody @Valid CartCreateDTO dto) {
        return cartService.addItemToCart(userId, dto);
    }

    @PutMapping("/{userId}")
    public CartResponseDTO updateCart(@PathVariable Long userId,
                                      @RequestBody @Valid CartCreateDTO dto) {
        return cartService.updateCart(userId, dto);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public CartResponseDTO removeItem(@PathVariable Long userId,
                                      @PathVariable Long productId) {
        return cartService.removeItemFromCart(userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(@PathVariable Long userId) {
        cartService.deleteCart(userId);
    }
}

