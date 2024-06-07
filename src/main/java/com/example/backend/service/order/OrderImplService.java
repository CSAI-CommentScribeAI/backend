package com.example.backend.service.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.dto.order.OrderMenuDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.order.OrderMenu;
import com.example.backend.entity.order.OrderStatus;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.cart.CartRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.repository.order.OrderRepository;
import com.example.backend.repository.store.StoreRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.repository.comment.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderImplService implements OrderService {

    private final UserAccountRepository userAccountRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public OrderDTO createOrderFromCart(Authentication authentication, OrderDTO orderDTO) {
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        Store store = storeRepository.findById(orderDTO.getStoreId())
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 가게를 찾을 수 없습니다: " + orderDTO.getStoreId()));

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

        OrderDTO orderSaveDTO = toOrderDTO(savedOrder,store);
        orderSaveDTO.setOrderId(savedOrder.getId());
        // OrderDTO 반환
        return orderSaveDTO;
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

    @Override
    public OrderDTO deliveryOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with orderId: " + orderId));

        order.setOrderStatus(OrderStatus.DELIVERED);

        Store store = storeRepository.findById(order.getStoreId())
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 가게를 찾을 수 없습니다: " + order.getStoreId()));

        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            throw new RuntimeException("Error placing order.");
        }
        return toOrderDTO(savedOrder,store);
    }

    @Override
    public List<OrderDTO> getStoreOrders(Long storeId) {
        List<Order> orders = orderRepository.findByStoreId(storeId);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 가게를 찾을 수 없습니다: " + storeId));
        return Optional.ofNullable(orders).orElse(Collections.emptyList())
                .stream()
                .map(order -> toOrderDTO(order, store))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserAccountId(userId);
        Store store = storeRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 가게를 찾을 수 없습니다: " + userId));
        return Optional.ofNullable(orders).orElse(Collections.emptyList())
                .stream()
                .map(order -> toOrderDTO(order, store))
                .collect(Collectors.toList());

    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));
    }

    @Override
    public List<Review> getRecentReviewsForUserAndStore(Long userId, Long storeId) {
        List<Review> reviews = reviewRepository.findTop10ByUserAccountIdAndStoreIdOrderByCreateAtDesc(userId, storeId);
        return Optional.ofNullable(reviews).orElse(Collections.emptyList());
    }


    @Override
    public int getOrderCountForUserAndStore(Long userId, Long storeId) {
        return orderRepository.countByUserIdAndStoreId(userId, storeId);
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

    private OrderDTO toOrderDTO(Order order, Store store) {
        return OrderDTO.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .storeId(order.getStoreId())
                .storeName(store.getName())
                .storeImageUrl(store.getStoreImageUrl())
                .totalPrice(order.getTotalPrice())
                .userId(order.getUserAccount().getId())
                .orderMenus(order.getOrderMenus().stream()
                        .map(this::toOrderMenuDTO)
                        .collect(Collectors.toList()))
                .userAddress(order.getUserAddress())
                .createdAt(order.getCreatedTime())
                .build();
    }

    private OrderMenuDTO toOrderMenuDTO(OrderMenu orderMenu) {
        return OrderMenuDTO.builder()
                .menuId(orderMenu.getMenu().getId())
                .imageUrl(orderMenu.getImageUrl())
                .menuName(orderMenu.getMenuName())
                .quantity(orderMenu.getQuantity())
                .build();
    }
}
