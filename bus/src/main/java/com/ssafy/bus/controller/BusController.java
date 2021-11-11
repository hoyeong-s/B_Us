package com.ssafy.bus.controller;

import com.ssafy.bus.domain.Waiting;
import com.ssafy.bus.service.WaitingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssafy.bus.common.ManageXML.getFullUri;
import static com.ssafy.bus.common.ManageXML.getItemFromXML;

@RestController
@RequestMapping(value = "/api/bus")
@RequiredArgsConstructor
public class BusController {

    private final WaitingService waitingService;


    @ApiOperation(value = "차량 고유ID(vehId1)를 이용해서 다음정류장에 승객이 있는지 True, False // 앞 뒤 현재 정류장 이름이 무엇인지")
    @RequestMapping(value = "/{vehId}", method = RequestMethod.GET)
    public ResponseEntity checkBusLocation(@PathVariable int vehId) throws IOException {

        Map<String, Object> result = new HashMap<>();

        // 현재 버스의 위치 파악하기
        System.out.println("vehId = " + vehId);
        String result = getFullUri("http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId", "vehId", vehId);

        HashMap<String, Object> itemList = getItemFromXML(result);
        int nowStOrd = Integer.parseInt(itemList.get("stOrd").toString());


        // 이 버스에 기다리는 승객 가져오기
        List<Waiting> waitingByVehId = waitingService.findWaitingByVehId(vehId);

        for (Waiting waiting : waitingByVehId) {
            if (waiting.getStaOrd() == nowStOrd - 1) {

            }
        }
        return false;
    }
}
