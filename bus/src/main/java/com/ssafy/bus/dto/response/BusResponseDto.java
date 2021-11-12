package com.ssafy.bus.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusResponseDto {

    private String busNo;
    private String remainingTime;
    private String busRouteId;
    private String staOrd;
    private String vehId1;

    @Builder
    public BusResponseDto(String busNo, String remainingTime, String busRouteId, String staOrd, String vehId1) {
        this.busNo = busNo;
        this.remainingTime = remainingTime;
        this.busRouteId = busRouteId;
        this.staOrd = staOrd;
        this.vehId1 = vehId1;
    }

//    public static List<BusResponseDto> of(HashMap<String, Object> hashMap) {

//        BusResponseDto dto = BusResponseDto.builder()
//    }


}
