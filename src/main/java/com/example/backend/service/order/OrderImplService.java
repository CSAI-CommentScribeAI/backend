package com.example.backend.service.order;

import com.example.backend.dto.order.OrderDTO;
import com.example.backend.dto.order.OrderMenuDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.order.UserOrder;
import com.example.backend.entity.order.OrderMenu;
import com.example.backend.entity.order.OrderStatus;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.exception.store.AlreadyStoreBossAssignException;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.cart.CartRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.repository.order.OrderRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.backend.repository.store.StoreRepository;
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
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderStatus(OrderStatus.REQUEST);
        userOrder.setStoreId(orderDTO.getStoreId());
        userOrder.setTotalPrice(orderDTO.getTotalPrice());
        userOrder.setUserAccount(userAccount);
        userOrder.setUserAddress(orderDTO.getUserAddress());

        // OrderMenu 엔티티 리스트 생성 및 설정
        List<OrderMenu> orderMenus = orderDTO.getOrderMenus().stream()
                .map(orderMenuDTO -> toOrderMenuEntity(orderMenuDTO, userOrder))
                .collect(Collectors.toList());

        // Order와 OrderMenu 간의 연관 설정
        userOrder.setOrderMenus(orderMenus);

        // Order 저장 (OrderMenus는 CascadeType.ALL로 설정되어 있어 함께 저장됨)
        UserOrder savedUserOrder = orderRepository.save(userOrder);

        cartRepository.deleteByUserId(userAccount.getId());

        OrderDTO orderSaveDTO = toOrderDTO(savedUserOrder,store);
        orderSaveDTO.setOrderId(savedUserOrder.getId());
        // OrderDTO 반환
        return orderSaveDTO;
    }

    @Override
    public void placeOrder(Long orderId, boolean approve) {
        UserOrder userOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with orderId: " + orderId));

        if(approve) {
            userOrder.setOrderStatus(OrderStatus.ACCEPT);
        } else {
            userOrder.setOrderStatus(OrderStatus.CANCEL);
        }

        UserOrder savedUserOrder = orderRepository.save(userOrder);
        if (savedUserOrder == null) {
            throw new RuntimeException("Error placing order.");
        }
    }

    @Override
    public OrderDTO deliveryOrder(Long orderId) {
        UserOrder userOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with orderId: " + orderId));

        userOrder.setOrderStatus(OrderStatus.DELIVERED);

        Store store = storeRepository.findById(userOrder.getStoreId())
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 가게를 찾을 수 없습니다: " + userOrder.getStoreId()));

        UserOrder savedUserOrder = orderRepository.save(userOrder);
        if (savedUserOrder == null) {
            throw new RuntimeException("Error placing order.");
        }
        return toOrderDTO(savedUserOrder,store);
    }

    @Override
    public List<OrderDTO> getStoreOrders(Authentication authentication,Long storeId) {
        String userId = authentication.getName();

        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new AlreadyStoreBossAssignException();
        }
        List<UserOrder> userOrders = orderRepository.findByStoreId(storeId);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 가게를 찾을 수 없습니다: " + storeId));
        return Optional.ofNullable(userOrders).orElse(Collections.emptyList())
                .stream()
                .map(order -> toOrderDTO(order, store))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getUserOrders(Authentication authentication) {
        String userId = authentication.getName();

        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        if (userAccount.getUserRole() != UserRole.ROLE_USER) {
            throw new AlreadyStoreBossAssignException();
        }
        List<UserOrder> userOrders = orderRepository.findByUserAccountId(Long.valueOf(userId));

        // storeIds 추출
        List<Long> storeIds = userOrders.stream()
                .map(UserOrder::getStoreId)
                .distinct()
                .collect(Collectors.toList());

        // storeIds를 사용하여 stores 조회
        List<Store> stores = storeRepository.findAllById(storeIds);

        // Store 목록을 Map으로 변환하여 빠른 조회 가능하도록 설정
        Map<Long, Store> storeMap = stores.stream()
                .collect(Collectors.toMap(Store::getId, store -> store));

        // Order를 OrderDTO로 변환
        return Optional.ofNullable(userOrders).orElse(Collections.emptyList())
                .stream()
                .map(order -> toOrderDTO(order, storeMap.get(order.getStoreId())))
                .collect(Collectors.toList());
    }

    @Override
    public UserOrder getOrderById(Long orderId) {
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


    private OrderMenu toOrderMenuEntity(OrderMenuDTO orderMenuDTO, UserOrder userOrder) {
        Menu menu = menuRepository.findById(orderMenuDTO.getMenuId())
                .orElseThrow(() -> new RuntimeException("Menu not found with menuId: " + orderMenuDTO.getMenuId()));

        return OrderMenu.builder()
                .userOrder(userOrder)
                .menu(menu)
                .menuName(menu.getName())
                .imageUrl(orderMenuDTO.getImageUrl())
                .quantity(orderMenuDTO.getQuantity())
                .build();
    }

    private OrderDTO toOrderDTO(UserOrder userOrder, Store store) {
        return OrderDTO.builder()
                .orderId(userOrder.getId())
                .orderStatus(userOrder.getOrderStatus())
                .storeId(userOrder.getStoreId())
                .storeName(store.getName())
                .storeImageUrl(store.getStoreImageUrl())
                .totalPrice(userOrder.getTotalPrice())
                .userId(userOrder.getUserAccount().getId())
                .orderMenus(userOrder.getOrderMenus().stream()
                        .map(this::toOrderMenuDTO)
                        .collect(Collectors.toList()))
                .userAddress(userOrder.getUserAddress())
                .createdAt(userOrder.getCreatedTime())
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
