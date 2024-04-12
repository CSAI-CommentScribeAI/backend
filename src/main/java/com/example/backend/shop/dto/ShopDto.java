
package com.example.backend.shop.dto;

import com.example.backend.shop.entity.ShopStatus;
import com.example.backend.shop.entity.ShopSupportedOrderType;
import com.example.backend.shop.entity.ShopSupportedPayment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ShopDto {

    @NotNull(message = "User ID is mandatory")
    private String userId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Phone number is mandatory")
    private String phoneNum;

    @NotBlank(message = "Short description is mandatory")
    @Size(min = 1, max = 100, message = "Short Description length must be between 1 and 100")
    private String shortDescription;

    @NotBlank(message = "Long description is mandatory")
    @Size(min = 1, max = 1000, message = "Long Description length must be between 1 and 1000")
    private String longDescription;

    @NotNull(message = "Supported order type is mandatory")
    private ShopSupportedOrderType supportedOrderType;

    @NotNull(message = "Supported payment type is mandatory")
    private ShopSupportedPayment supportedPayment;

    @NotNull(message = "Opening time is mandatory")
    private String openTime;

    @NotNull(message = "Closing time is mandatory")
    private String closeTime;

    @Min(value = 0, message = "Delivery fee cannot be negative")
    private int deliveryFee;

    @Min(value = 0, message = "Minimum order price cannot be negative")
    private int minOrderPrice;

    @NotNull(message = "Shop status is mandatory")
    private ShopStatus shopStatus;

    @NotBlank(message = "Register number is mandatory")
    private String registerNumber;

    @NotBlank(message = "Doro address is mandatory")
    private String doroAddress;

    @Min(value = 0, message = "Doro index cannot be negative")
    private int doroIndex;

    @NotBlank(message = "Detail address is mandatory")
    private String detailAddress;

    @NotBlank(message = "Shop image is mandatory")
    private MultipartFile shopImage;

    @NotNull(message = "Category is mandatory")
    private String categoryName;

    public MultipartFile getShopImage() {
        return shopImage;
    }

    public void setShopImage(MultipartFile shopImage) {
        this.shopImage = shopImage;
    }
}