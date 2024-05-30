package com.example.backend.service.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.dto.order.OrderMenuDTO;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.order.OrderMenu;
import com.example.backend.entity.order.OrderStatus;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.cart.CartRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.repository.order.OrderRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderImplService implements OrderService {

    private final UserAccountRepository userAccountRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;

    @Transactional
    public OrderDTO createOrderFromCart(Authentication authentication, OrderDTO orderDTO) {
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));


        // Order 엔티티 생성 및 설정
        Order order = new Order();
        order.setOrderStatus(OrderStatus.REQUEST);
        order.setStoreId(orderDTO.getStoreId());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setUserAccount(userAccount);
        order.setUserAddress(orderDTO.getUserAddress());

        // OrderMenu 엔티티 리스트 생성 및 설정
        List<OrderMenu> orderMenus = orderDTO.getOrderMenus().stream()
                .map(orderMenuDTO -> toOrderMenuEntity(orderMenuDTO, order))
                .collect(Collectors.toList());

        // Order와 OrderMenu 간의 연관 설정
        order.setOrderMenus(orderMenus);

        // Order 저장 (OrderMenus는 CascadeType.ALL로 설정되어 있어 함께 저장됨)
        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteByUserId(userAccount.getId());

        // OrderDTO 반환
        return toOrderDTO(savedOrder);
    }

    @Override
    public void placeOrder(Long orderId, boolean approve) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with orderId: " + orderId));

        if(approve) {
            order.setOrderStatus(OrderStatus.ACCEPT);
        } else {
            order.setOrderStatus(OrderStatus.CANCEL);
        }

        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            throw new RuntimeException("Error placing order.");
        }
    }

    private OrderMenu toOrderMenuEntity(OrderMenuDTO orderMenuDTO, Order order) {
        Menu menu = menuRepository.findById(orderMenuDTO.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found with menuId: " + orderMenuDTO.getMenuId()));

        return OrderMenu.builder()
                .order(order)
                .menu(menu)
                .menuName(menu.getName())
                .imageUrl(orderMenuDTO.getImageUrl())
                .quantity(orderMenuDTO.getQuantity())
                .build();
    }

    private OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .storeId(order.getStoreId())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUserAccount().getId())
                .orderMenus(order.getOrderMenus().stream()
                        .map(this::toOrderMenuDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderMenuDTO toOrderMenuDTO(OrderMenu orderMenu) {
        return OrderMenuDTO.builder()
                .menuId(orderMenu.getMenu().getId())
                .imageUrl(orderMenu.getImageUrl())
                .quantity(orderMenu.getQuantity())
                .build();
    }
}
