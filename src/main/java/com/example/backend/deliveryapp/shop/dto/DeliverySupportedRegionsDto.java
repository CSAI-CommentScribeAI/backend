package com.example.backend.deliveryapp.shop.dto;

import com.example.backend.deliveryapp.shop.entity.DeliverySupportedRegions;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeliverySupportedRegionsDto {

    @NotNull(message = "LocationCode is mandatory")
    private String locationCode;

    public DeliverySupportedRegionsDto(DeliverySupportedRegions deliverySupportedRegions) {
        this.locationCode = deliverySupportedRegions.getLocationCode();
    }
}
