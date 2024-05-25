package com.example.backend.service;

import com.example.backend.util.GeoUtils;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    // 상점과 사용자의 위치가 특정 거리 이내인지 확인
    public boolean isWithinDeliveryRange(double storeLat, double storeLon, double userLat, double userLon, double maxDistance) {
        double distance = GeoUtils.calculateDistance(storeLat, storeLon, userLat, userLon);
        return distance <= maxDistance;
    }
}
