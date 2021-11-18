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
import java.util.*;

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
                System.out.println("map = " + map.toString());
                if (map.get("rtNm").toString().equals(waiting1.getBusNo())) {
                    BusResponseDto dto = BusResponseDto.builder()
                            .vehId1(map.get("vehId1").toString())
                            .staOrd(map.get("staOrd").toString())
                            .busNo(map.get("rtNm").toString())
                            .remainingTime(changeRemainingTime(map.get("arrmsgSec1").toString()))
                            .order(setOrder(changeRemainingTime(map.get("arrmsgSec1").toString())))
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

        busResponseDtos.sort(new Comparator<BusResponseDto>() {
            @Override
            public int compare(BusResponseDto o1, BusResponseDto o2) {

                int busOrder1 = o1.getOrder();
                int busOrder2 = o2.getOrder();

                if (busOrder1 == busOrder2) return 0;
                else if (busOrder1 > busOrder2) return 1;
                else return -1;
            }
        });

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


    private int setOrder(String arriveTime) {
        System.out.println("arriveTime = " + arriveTime);
        if (arriveTime.equals("곧 도착")) {
            return 0;
        } else if (arriveTime == "도착 정보 없음") {
            return 100;
        } else {
            int minute = arriveTime.indexOf("분");
            return Integer.parseInt(arriveTime.substring(0, minute));
        }
    }
}
