package com.example.backend.UserAccount.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserAddressDto {

    @NotBlank(message = "Full address is mandatory")
    @Size(min = 1, max = 200, message = "Full address length must be between 1 and 200")
    private String fullAddress; // 전체 주소

    @NotBlank(message = "Road address is mandatory")
    @Size(min = 1, max = 200, message = "Road address length must be between 1 and 200")
    private String roadAddress; // 도로명 주소

    @Size(max = 200, message = "Jibun address length must be up to 200")
    private String jibunAddress; // 지번 주소

    @NotBlank(message = "Postal code is mandatory")
    @Size(min = 1, max = 20, message = "Postal code length must be between 1 and 20")
    private String postalCode; // 우편번호

    @NotNull(message = "Latitude is mandatory")
    private Double latitude; // 위도

    @NotNull(message = "Longitude is mandatory")
    private Double longitude; // 경도
}
