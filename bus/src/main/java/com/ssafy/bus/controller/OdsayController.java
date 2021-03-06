package com.ssafy.bus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ssafy.bus.dto.response.BusResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

import static com.ssafy.bus.common.ManageXML.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OdsayController {

    @RequestMapping(value = "/stationInfo/{stationName}", method = RequestMethod.GET)
    public void getStationInfo(@PathVariable String stationName) throws IOException {
        String result = getFullUri("http://ws.bus.go.kr/api/rest/stationinfo/getStationByName", "stSrch", stationName);
        List itemList = getItemListFromXML(result);

        itemList.stream().forEach(li -> System.out.println("li = " + li));
    }

    @ApiOperation(value = "정류장의 이름과, 오는 모든 버스를 조회하는 함수입니다. 추가로 시간을 반환해줍니다. 인자는 stationId 입니다")
    @RequestMapping(value = "/busInfo/{arsId}", method = RequestMethod.GET)
    public ResponseEntity getBusInfo(@PathVariable int arsId) throws IOException {
        String result = getFullUri("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid", "arsId",arsId);
        List<Map> itemList = getItemListFromXML(result);
        System.out.println("itemList = " + itemList);

        Map<String, Object> returnMap = new HashMap<>();

        String stNm = (String) itemList.get(0).get("stNm");
        returnMap.put("stationName", stNm);

        List<BusResponseDto> busResponseDtos = new ArrayList<>();
        itemList.stream().forEach(
                item -> {
                    BusResponseDto dto = BusResponseDto.builder()
                            .busNo(item.get("rtNm").toString())
                            .remainingTime(changeRemainingTime(item.get("arrmsgSec1").toString()))
                            .busRouteId(item.get("busRouteId").toString())
                            .staOrd(item.get("staOrd").toString())
                            .vehId1(item.get("vehId1").toString())
                            .build();
                    busResponseDtos.add(dto);
                }
        );

        busResponseDtos.sort(new Comparator<BusResponseDto>() {
            @Override
            public int compare(BusResponseDto o1, BusResponseDto o2) {

                String busNo1 = o1.getBusNo();
                String busNo2 = o2.getBusNo();
                int i = busNo1.compareTo(busNo2);

                if (i == 0) return 0;
                else if (i > 0) return 1;
                else return -1;
            }
        });
        returnMap.put("itemList", busResponseDtos);

        return ResponseEntity.ok(returnMap);
    }


}
