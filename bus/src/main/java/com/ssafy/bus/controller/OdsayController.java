package com.ssafy.bus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ssafy.bus.dto.BusResponseDto;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OdsayController {

    @RequestMapping(value = "/stationInfo/{stationName}", method = RequestMethod.GET)
    public void getStationInfo(@PathVariable Object stationName) throws IOException {
        String urlStr =
                "http://ws.bus.go.kr/api/rest/stationinfo/getStationByName"
                        + "?serviceKey=Cd1Kw21w%2FOpUmlWaO%2FwpyF47QEYq76243PN57pJNwTFQ%2BKkOsSLxzta%2FjG8oLag%2FAVdYt6DS9YTFwVi85KJabg%3D%3D"
                        + "&stSrch=" + stationName;
        URL url = new URL(urlStr);
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        String result = bf.readLine();
        List list = xmlToList(result);

        list.stream().forEach(li -> System.out.println("li = " + li));
    }

    @ApiOperation(value = "정류장에 오는 모든 버스를 조회하는 함수입니다. 추가로 시간을 반환해줍니다. 인자는 stationId 입니다")
    @RequestMapping(value = "/busInfo/{stationId}", method = RequestMethod.GET)
    public ResponseEntity<List<BusResponseDto>> getBusInfo(@PathVariable int stationId) throws IOException {
        String urlStr =
                "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid"
                        + "?serviceKey=Cd1Kw21w%2FOpUmlWaO%2FwpyF47QEYq76243PN57pJNwTFQ%2BKkOsSLxzta%2FjG8oLag%2FAVdYt6DS9YTFwVi85KJabg%3D%3D"
                        + "&arsId=" + stationId;
        URL url = new URL(urlStr);

        System.out.println("url.toString() = " + url.toString());
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        String result = bf.readLine();
        List<Map> itemList = xmlToList(result);
        System.out.println("itemList = " + itemList);

        List<BusResponseDto> busResponseDtos = new ArrayList<>();
        itemList.stream().forEach(
                item -> {
                    BusResponseDto dto = BusResponseDto.builder()
                            .busNo(item.get("rtNm").toString())
                            .remainingTime(changeRemainingTime(item.get("arrmsgSec1").toString()))
                            .busRouteId(item.get("busRouteId").toString())
                            .build();
                    busResponseDtos.add(dto);
                }
        );

        return ResponseEntity.ok(busResponseDtos);
    }

    public List xmlToList(String str) throws IOException {
        String xml = str;
        JSONObject jObject = XML.toJSONObject(xml);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object json = mapper.readValue(jObject.toString(), Object.class);
        String output = mapper.writeValueAsString(json);
        System.out.println("output = " + output);
//        System.out.println("output.getClass().getName() = " + output.getClass().getName());

        HashMap<String, Object> jsonMap = new ObjectMapper().readValue(output, HashMap.class);
        HashMap<String, Object> serviceResult = (HashMap<String, Object>) jsonMap.get("ServiceResult");
        HashMap<String, Object> msgBody = (HashMap<String, Object>) serviceResult.get("msgBody");
        List itemList = (List) msgBody.get("itemList");
//        System.out.println("itemList = " + itemList);

        return itemList;
    }

    private String changeRemainingTime(String time) {

        if (time.equals("곧 도착")) {
            return time;
        }

        int index = time.indexOf("분");

        if (index == -1) {
            return "도착 정보 없음";
        }

        String changedTime = time.substring(0, index);
        return changedTime+"분 뒤 도착";
    }

}
