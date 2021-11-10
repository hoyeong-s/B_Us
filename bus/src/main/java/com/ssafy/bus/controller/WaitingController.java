package com.ssafy.bus.controller;

import com.ssafy.bus.domain.Waiting;
import com.ssafy.bus.dto.request.BusRegisterRequestDto;
import com.ssafy.bus.service.WaitingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ssafy.bus.common.ManageXML.*;

@RestController
@RequestMapping("/api/waiting")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    @ApiOperation(value = "타고자하는 버스를 등록하는 API입니다. 인자는 총 3개, 승객의 고유번호 clientId, 타고자하는 버스의 고유번호 vehId, 타는 정류장의 순서 staOrd ")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@RequestBody BusRegisterRequestDto dto) {
        waitingService.register(dto);
    }

    @ApiOperation(value = "승객이 등록한 버스의 목록 전부 가져오는 API입니다. 인자는 승객의 고유아이디")
    @RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET)
    public void getBusListFromClient(@PathVariable String clientId) throws IOException {
        List<Waiting> waitingList = waitingService.findWaitingByClientId(clientId);

        if (waitingList.size() == 0) {
            return;
        }

        Waiting waiting = waitingList.get(0);
        int arsId = waiting.getArsId();

        String result = getFullUri("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid", "arsId",arsId);
        List<Map> itemList = getItemListFromXML(result);

        for (Waiting waiting1 : waitingList) {
            for (Map map : itemList) {
                if (map.get(""))
            }
        }



    }


    @ApiOperation(value = "차량 고유ID(vehId1)를 이용해서 현재 정류장의 번호 가져오기")
    @RequestMapping(value = "/{vehId}", method = RequestMethod.GET)
    public String checkBusLocation(@PathVariable int vehId) throws IOException {

        System.out.println("vehId = " + vehId);
        String result = getFullUri("http://ws.bus.go.kr/api/rest/buspos/getBusPosByVehId", "vehId", vehId);

        HashMap<String, Object> itemList = getItemFromXML(result);
        String nowStOrd = itemList.get("stOrd").toString();
        return nowStOrd;
    }

    @ApiOperation(value = "차량 고유ID(vehId)를 이용해서 현재 대기중인 승객 모두 가져오기")
    @RequestMapping(value = "/vehicle/{vehId}", method = RequestMethod.GET)
    public void findWaitingByVehId(@PathVariable int vehId) {
        List<Waiting> waitingByVehId = waitingService.findWaitingByVehId(vehId);
        System.out.println("waitingByVehId = " + waitingByVehId);
    }

}
