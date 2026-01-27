package edu.bbte.localproducersmarketplace.service.impl;

import edu.bbte.localproducersmarketplace.dto.in.CartCreateDTO;
import edu.bbte.localproducersmarketplace.dto.in.CartItemCreateDTO;
import edu.bbte.localproducersmarketplace.dto.out.CartResponseDTO;
import edu.bbte.localproducersmarketplace.exception.CartNotFoundException;
import edu.bbte.localproducersmarketplace.exception.ProductNotFoundException;
import edu.bbte.localproducersmarketplace.exception.UserNotFoundException;
import edu.bbte.localproducersmarketplace.mapper.CartMapper;
import edu.bbte.localproducersmarketplace.model.Cart;
import edu.bbte.localproducersmarketplace.model.CartItem;
import edu.bbte.localproducersmarketplace.model.Product;
import edu.bbte.localproducersmarketplace.model.User;
import edu.bbte.localproducersmarketplace.repository.CartRepository;
import edu.bbte.localproducersmarketplace.repository.ProductRepository;
import edu.bbte.localproducersmarketplace.repository.UserRepository;
import edu.bbte.localproducersmarketplace.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartResponseDTO createCart(User user, CartCreateDTO dto) {
        // Check if user already has a cart
        Optional<Cart> existingCart = cartRepository.findByUserId(user.getId());
        if (existingCart.isPresent()) {
            throw new CartNotFoundException("User already has a cart. Use update or add item endpoints instead.");
        }

        Cart cart = new Cart();
        cart.setUser(user);

        List<CartItem> items = new ArrayList<>();

        for (CartItemCreateDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));

            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setCart(cart);

            items.add(item);
        }

        cart.setItems(items);

        Cart saved = cartRepository.save(cart);
        return cartMapper.toResponse(saved);
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItemToCart(Long userId, CartCreateDTO dto) {
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        for (CartItemCreateDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));

            // Check if item already exists in cart
            Optional<CartItem> existingItem = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId() == product.getId())
                    .findFirst();

            if (existingItem.isPresent()) {
                // Update quantity if item exists
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + itemDTO.getQuantity());
            } else {
                // Add new item
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitPrice(itemDTO.getUnitPrice());
                item.setCart(cart);
                cart.getItems().add(item);
            }
        }

        Cart saved = cartRepository.save(cart);
        return cartMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CartResponseDTO updateCart(Long userId, CartCreateDTO dto) {
        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        cart.getItems().clear();

        List<CartItem> items = new ArrayList<>();

        for (CartItemCreateDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + itemDTO.getProductId()));

            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setCart(cart);

            items.add(item);
        }

        cart.setItems(items);

        Cart saved = cartRepository.save(cart);
        return cartMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CartResponseDTO removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        cart.getItems().removeIf(item -> item.getProduct().getId() == productId);

        Cart saved = cartRepository.save(cart);
        return cartMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));

        cartRepository.delete(cart);
    }
}

