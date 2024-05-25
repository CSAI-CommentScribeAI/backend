package com.example.backend.util;

public class GeoUtils {

    // 지구의 반지름, 킬로미터 단위
    private static final double EARTH_RADIUS_KM = 6371.0;

    // Haversine 공식을 이용해 두 지점 간의 거리를 계산
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // 결과는 킬로미터 단위
    }
}
