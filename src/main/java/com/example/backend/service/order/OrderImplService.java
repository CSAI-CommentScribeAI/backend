package com.example.backend.service.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.order.OrderStatus;
import com.example.backend.entity.cart.Cart;
import com.example.backend.entity.cart.CartItem;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.order.OrderMenu;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.repository.UserAccount.UserAddressRepository;
import com.example.backend.repository.cart.CartRepository;
import com.example.backend.repository.order.OrderRepository;
import com.example.backend.repository.store.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional(readOnly = true)
public class OrderImplService implements OrderService {

    private final CartRepository cartRepository;
    private final UserAddressRepository userAddressRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    @Autowired
    public OrderImplService(CartRepository cartRepository,
                            UserAddressRepository userAddressRepository,
                            OrderRepository orderRepository,
                            StoreRepository storeRepository) {
        this.cartRepository = cartRepository;
        this.userAddressRepository = userAddressRepository;
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    @Transactional
    public OrderDTO createOrderFromCart(Authentication authentication, Long userAddressId) {
        String userId = authentication.getName();
        // 사용자의 주소 정보를 가져옴
        UserAddress userAddress = userAddressRepository.findById(userAddressId).orElse(null);
        if (userAddress == null) {
            // 사용자 주소 정보가 없으면 예외 처리 또는 적절한 방법으로 처리
            return null;
        }

        Cart cart = cartRepository.findByUserId(Long.valueOf(userId));
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있습니다.");
        }

        Store store = storeRepository.findById(cart.getStoreId())
                .orElseThrow(() -> new IllegalStateException("상점 정보를 찾을 수 없습니다."));

        // 주문 생성
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ACCEPT); // 주문 상태 설정
        order.setStore(cart.getStoreId()); // 상점 설정
        order.setTotalPrice(cart.getTotalPrice()); // 총 가격 설정
        order.setUserAccount(cart.getUser()); // 주문한 사용자 설정
        order.setUserAddress(userAddress); // 사용자의 배송 주소 설정

        // 주문 메뉴 생성
        List<OrderMenu> orderMenus = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderMenu orderMenu = new OrderMenu();
            orderMenu.setOrder(order); // 주문 정보 설정
            Menu menu = cartItem.getMenu(); // 메뉴 정보 가져오기
            orderMenu.setMenu(menu); // 메뉴 정보 설정
            orderMenu.setImageUrl(menu.getImageUrl()); // 메뉴의 이미지 URL 설정
            orderMenu.setQuantity(cartItem.getQuantity()); // 수량 설정

            orderMenus.add(orderMenu);
        }
        order.setOrderMenus(orderMenus); // 주문 메뉴 설정

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 장바구니와 장바구니 아이템 삭제
        cartRepository.delete(cart);  // 장바구니 삭제

        // 주문을 DTO로 변환하여 반환
        return OrderDTO.mapFromOrder(savedOrder);
    }

    @Override
    public void placeOrder(Long orderId) {
        // 주문 정보 가져오기
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            // 주문이 없는 경우 예외 처리 또는 적절한 방법으로 처리
            throw new IllegalArgumentException("주문을 찾을 수 없습니다.");
        }

        // 주문 상태 변경 (예: WAITING -> ACCEPT)
        order.setOrderStatus(OrderStatus.ACCEPT);

        // 주문 정보 저장
        orderRepository.save(order);
    }

}
