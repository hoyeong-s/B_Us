package com.ssafy.bus.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusResponseDto {

    private String busNo;
    private String remainingTime;
    private String busRouteId;

    @Builder
    public BusResponseDto(String busNo, String remainingTime, String busRouteId) {
        this.busNo = busNo;
        this.remainingTime = remainingTime;
        this.busRouteId = busRouteId;
    }

//    public static List<BusResponseDto> of(HashMap<String, Object> hashMap) {

//        BusResponseDto dto = BusResponseDto.builder()
//    }


}
