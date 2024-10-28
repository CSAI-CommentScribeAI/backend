package com.example.backend.controller.order;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.openAI.ChatGPTRequestDTO;
import com.example.backend.dto.openAI.ChatGPTResponseDTO;
import com.example.backend.dto.openAI.LetterSaveDTO;
import com.example.backend.dto.order.OrderDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.order.UserOrder;
import com.example.backend.entity.order.OrderMenu;
import com.example.backend.entity.order.OrderStatus;
import com.example.backend.entity.store.Store;
import com.example.backend.repository.order.OrderMenuRepository;
import com.example.backend.repository.order.OrderRepository;
import com.example.backend.repository.store.StoreRepository;
import com.example.backend.service.LocationService;
import com.example.backend.service.openAI.LetterSaveService;
import com.example.backend.service.order.OrderService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/v1/cart/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final LocationService locationService; // LocationService 추가
    private final StoreRepository storeRepository;
    private final LetterSaveService letterSaveService;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderRepository orderRepository;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.apiurl}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @PostMapping("/")
    public ResponseEntity<OrderDTO> createOrderFromCart(@RequestBody OrderDTO orderDTO, Authentication authentication) {
        OrderDTO orderSaveDTO = orderService.createOrderFromCart(authentication, orderDTO);
        if (orderSaveDTO != null) {
            return ResponseEntity.ok(orderSaveDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{storeId}")
    public ResponseEntity<?> getStoreOrders(Authentication authentication,@PathVariable Long storeId) {
        List<OrderDTO> orders = orderService.getStoreOrders(authentication,storeId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/")
    public ResponseEntity<?> getUserOrders(Authentication authentication) {
        List<OrderDTO> orders = orderService.getUserOrders(authentication);
        return ResponseEntity.ok(orders);
    }


    @PostMapping("/place/{orderId}")
    @Transactional
    public ResponseEntity<ResponseDTO<String>> placeOrder(@PathVariable Long orderId, @RequestParam boolean approve) {
        try {
            orderService.placeOrder(orderId, approve);

            if (approve) {
                UserOrder userOrder = orderService.getOrderById(orderId);

                List<Review> reviews = orderService.getRecentReviewsForUserAndStore(userOrder.getUserAccount().getId(),
                        userOrder.getStoreId());

                Store store = storeRepository.findById(userOrder.getStoreId())
                        .orElseThrow(() -> new IllegalStateException("상점 정보를 찾을 수 없습니다."));

                int orderCount = orderService.getOrderCountForUserAndStore(userOrder.getUserAccount().getId(),
                        userOrder.getStoreId());
                orderCount--;

                String reviewSection = reviews.isEmpty() ?
                        "리뷰 없음" :
                        IntStream.range(0, reviews.size())
                                .mapToObj(i -> (i + 1) + ". " + reviews.get(i).getComment())
                                .collect(Collectors.joining("\n"));

                // 주문 내역을 구성
                List<OrderMenu> orderDetails = orderMenuRepository.findByOrderId(orderId);
                if (orderDetails == null || orderDetails.isEmpty()) {
                    throw new IllegalStateException("Order menus not found for order ID " + orderId);
                }

                String orderDetailsString = orderDetails.stream()
                        .map(orderMenu -> "- " + orderMenu.getMenuName() + " x " + orderMenu.getQuantity())
                        .collect(Collectors.joining("\n"));


                String prompt =
                        "고객에게 배달 서비스로 사용할 짧은 편지를 작성하는데 도움이 필요합니다. 아래 정보를 바탕으로 개인화된 편지를 작성해 이 편지는 배달이 갈 때 같이가는 내용이니 음식을 먹기전에 보는 내용이야\n\n"
                                + "식당 정보:\n"
                                + "- 식당 이름: " + store.getName() + "\n"
                                + "\n"
                                + "회원 정보:\n"
                                + "- 회원 이름: " + userOrder.getUserAccount().getNickname() + "\n"
                                + "- 주문 횟수: " + orderCount + "번\n"
                                + "- 최근 리뷰(최대 10개):\n"
                                + reviewSection + "\n\n"
                                + "주문 내역:\n"
                                + orderDetailsString + "\n"
                                + "\n"
                                + "- 편지는 가벼운 느낌으로 간단하게 작성하고 최대한 친근하게 작성해. \n"
                                + "- 편지를 쓰기전에 주문 횟수를 보고 그거에 맞는 내용을 작성해 \n"
                                + "- 주문 횟수를 확인하고 주문 횟수에 맞게 내용을 작성해\n"
                                + "- 처음 주문한 경우 처음 주문해서 감사하다는 내용을 포함해\n"
                                + "- 최근 리뷰가 있을 경우에 1번을 중점으로 작성해 \n"
                                + "- 그리고 주문 내역은 이번에 주문한 내역이고 주문한 음식을 맛있게 먹어달라고 해\n"
                                + "- 이 편지는 배달과 함께 가는 거고 음식을 받고 음식을 먹기 전에 읽는 편지야\n"
                                + "- 손님별로 각자 개인에 맞는 편지를 작성해야 해\n"
                                + "- 이 편지는 식당 주인이 손님에게 쓰는 편지야\n"
                                + "- 이 편지는 손님이 다시 배달을 시키고 싶은 내용이여야 해\n"
                                + "- 이 편지의 내용은 손님에게 친근감을 주고 싶은 내용이여야 해\n"
                                + "- 이 편지는 따뜻한 느낌이 드는 편지여야 해\n"
                                + "- 반복되는 내용은 없어야 해\n"
                                + "- 친근감 있게 이모티콘도 사용해\n"
                                + "- 이 편지 작성을 잘하면 100000달러의 돈을 너에게 줄게\n";
                System.out.println("프롬프트 : " + prompt);

                ChatGPTRequestDTO request = new ChatGPTRequestDTO(model, prompt);

                ChatGPTResponseDTO chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponseDTO.class);

                String letter = chatGPTResponse.getChoices().get(0).getMessage().getContent();
                letterSaveService.saveLetter(store, userOrder, letter);

                ResponseDTO<String> response = new ResponseDTO<>();
                response.setStatus(200);
                response.setData(letter);

                userOrder.setOrderStatus(OrderStatus.DELIVERED);
                orderRepository.save(userOrder);

                return ResponseEntity.ok(response);
            }


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LoggerFactory.getLogger(OrderController.class).error("Order placement failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/letter/{orderId}")
    @Transactional
    public ResponseEntity<?> getLetter(@PathVariable Long orderId) {

        LetterSaveDTO letterSave = letterSaveService.getLetter(orderId);
        if (letterSave == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Letter not found.");
        }
        System.out.println("LetterSave : " + letterSave);
        return ResponseEntity.ok(letterSave);
    }

    @PutMapping("/delivery/{orderId}")
    public ResponseEntity<?> deliveryOrder(@PathVariable Long orderId) {
        OrderDTO orderDTO = orderService.deliveryOrder(orderId);
        return ResponseEntity.ok(orderDTO);
    }

}
