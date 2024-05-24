//package com.example.backend.service.order;
//
//import com.example.backend.dto.order.OrderDTO;
//import com.example.backend.entity.menu.Menu;
//import com.example.backend.entity.order.OrderStatus;
//import com.example.backend.entity.cart.Cart;
//import com.example.backend.entity.cart.CartItem;
//import com.example.backend.entity.order.Order;
//import com.example.backend.entity.order.OrderMenu;
//import com.example.backend.entity.store.Store;
//import com.example.backend.entity.userAccount.UserAddress;
//import com.example.backend.repository.UserAccount.UserAddressRepository;
//import com.example.backend.repository.cart.CartRepository;
//import com.example.backend.repository.order.OrderRepository;
//import com.example.backend.repository.store.StoreRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//@Service
//@Transactional(readOnly = true)
//@RequiredArgsConstructor
//public class OrderImplService implements OrderService {
//
//    // 기타 메소드 생략
//
//    @Override
//    @Transactional
//    public OrderDTO createOrderFromCart(Authentication authentication, Long userAddressId) {
//        String userId = authentication.getName();
//        UserAddress userAddress = userAddressRepository.findById(userAddressId).orElse(null);
//        if (userAddress == null) {
//            return null;
//        }
//
//        Cart cart = cartRepository.findByUserId(Long.valueOf(userId));
//        if (cart == null || cart.getCartItems().isEmpty()) {
//            throw new IllegalStateException("장바구니가 비어 있습니다.");
//        }
//
//        Store store = storeRepository.findById(cart.getStoreId())
//                .orElseThrow(() -> new IllegalStateException("상점 정보를 찾을 수 없습니다."));
//
//        Order order = new Order();
//        order.setOrderStatus(OrderStatus.ACCEPT);
//        order.setStore(cart.getStoreId());
//        order.setTotalPrice(cart.getTotalPrice());
//        order.setUserAccount(cart.getUser());
//        order.setUserAddress(userAddress);
//
//        List<OrderMenu> orderMenus = new ArrayList<>();
//        for (CartItem cartItem : cart.getCartItems()) {
//            OrderMenu orderMenu = new OrderMenu();
//            orderMenu.setOrder(order);
//            Menu menu = cartItem.getMenu();
//            orderMenu.setMenu(menu);
//            orderMenu.setImageUrl(menu.getImageUrl());
//            orderMenu.setQuantity(cartItem.getQuantity());
//            orderMenus.add(orderMenu);
//        }
//        order.setOrderMenus(orderMenus);
//
//        Order savedOrder = orderRepository.save(order);
//        cartRepository.delete(cart);
//
//        return OrderDTO.mapFromOrder(savedOrder);
//    }
//
//    @Override
//    @Transactional
//    public void placeOrder(Long orderId) {
//        Order order = orderRepository.findById(orderId).orElse(null);
//        if (order == null) {
//            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
//        }
//
//        order.setOrderStatus(OrderStatus.ACCEPT);
//        orderRepository.save(order);
//    }
//
//    @Transactional
//    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));
//        order.setOrderStatus(newStatus);
//        Order updatedOrder = orderRepository.save(order);
//        return OrderDTO.mapFromOrder(updatedOrder);
//    }
//}
