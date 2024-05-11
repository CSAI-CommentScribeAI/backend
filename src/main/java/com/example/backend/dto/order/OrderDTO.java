package com.example.backend.dto.order;

import com.example.backend.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Data
@Builder
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderStatus;
    private Long storeId;
    private int totalPrice;
    private Long userAccountId;
    private List<OrderMenuDTO> orderMenus;

    public OrderDTO() {

    }
    public static OrderDTO mapFromOrder(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderStatus(order.getOrderStatus().name());
        orderDTO.setStoreId(order.getStoreId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setUserAccountId(order.getUserAccount().getId());

        // Set the list of OrderMenuDTO from the Order entity's orderMenus
        orderDTO.setOrderMenus(order.getOrderMenus().stream()
                .map(orderMenu -> OrderMenuDTO.mapFromOrderMenu(orderMenu))
                .collect(Collectors.toList()));

        return orderDTO;
    }
}