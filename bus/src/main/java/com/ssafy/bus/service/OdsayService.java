package com.ssafy.bus.service;

import com.ssafy.bus.api.BusApiClient;
import com.ssafy.bus.dto.BusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class OdsayService {

    private final BusApiClient busApiClient;

    @Transactional
    public BusResponseDto getBusSationInfo(int stationId) {
        return busApiClient.requestBus(stationId);
    }
}
