package com.ssafy.bus.controller;

import com.ssafy.bus.domain.Waiting;
import com.ssafy.bus.dto.request.BusRegisterRequestDto;
import com.ssafy.bus.dto.response.BusResponseDto;
import com.ssafy.bus.service.WaitingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
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
    public ResponseEntity getBusListFromClient(@PathVariable String clientId) throws IOException {
        List<Waiting> waitingList = waitingService.findWaitingByClientId(clientId);

        if (waitingList.size() == 0) {
            return (ResponseEntity) ResponseEntity.ok();
        }

        Waiting waiting = waitingList.get(0);
        int arsId = waiting.getArsId();

        String result = getFullUri("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid", "arsId", arsId);
        List<Map> itemList = getItemListFromXML(result);

        waitingList.stream().forEach(waiting1 -> System.out.println("waiting1 = " + waiting1));

        List<BusResponseDto> busResponseDtos = new ArrayList<>();
        for (Map map : itemList) {
            for (Waiting waiting1 : waitingList) {
                if (map.get("rtNm").equals(waiting1.getBusNo())) {
                    BusResponseDto dto = BusResponseDto.builder()
                            .vehId1(map.get("vehId1").toString())
                            .staOrd(map.get("staOrd").toString())
                            .busNo(map.get("rtNm").toString())
                            .remainingTime(changeRemainingTime(map.get("arrmsgSec1").toString()))
                            .busRouteId(map.get("busRouteId").toString())
                            .build();
                    busResponseDtos.add(dto);

                    if (waiting1.getVehId() != Integer.parseInt(map.get("vehId1").toString())) {
                        waiting1.setVehId(Integer.parseInt(map.get("vehId1").toString()));
                        waitingService.save(waiting1);
                    }
                }
            }
        }

        return ResponseEntity.ok(busResponseDtos);
    }





    @ApiOperation(value = "등록한 차량 번호 삭제, 인자는 승객아이디, 버스번호")
    @RequestMapping(value = "/{clientId}/{busNo}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String clientId, @PathVariable String busNo) {
        Waiting waitingByClientIdAndBusNo = waitingService.findWaitingByClientIdAndBusNo(clientId, busNo);
        if (waitingByClientIdAndBusNo == null) {
            return;
        }

        waitingService.delete(waitingByClientIdAndBusNo);
    }

//    @ApiOperation(value = "차량 고유ID(vehId)를 이용해서 현재 대기중인 승객 모두 가져오기")
//    @RequestMapping(value = "/vehicle/{vehId}", method = RequestMethod.GET)
//    public void findWaitingByVehId(@PathVariable int vehId) {
//        List<Waiting> waitingByVehId = waitingService.findWaitingByVehId(vehId);
//        System.out.println("waitingByVehId = " + waitingByVehId);
//    }

}
