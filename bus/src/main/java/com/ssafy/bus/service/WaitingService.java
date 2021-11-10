package com.ssafy.bus.service;

import com.ssafy.bus.domain.Waiting;
import com.ssafy.bus.dto.request.BusRegisterRequestDto;
import com.ssafy.bus.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;


    public List<Waiting> findWaitingByVehId(int vehId) {
        List<Waiting> result = waitingRepository.findByVehId(vehId);
        return result;
    }

    public void register(BusRegisterRequestDto dto) {
        Waiting waiting = Waiting.builder()
                .clientId(dto.getClientId())
                .staOrd(dto.getStaOrd())
                .vehId(dto.getVehId())
                .busNo(dto.getBusNo())
                .arsId(dto.getArsId())
                .build();
        waitingRepository.save(waiting);
    }

    public List<Waiting> findWaitingByClientId(String clientId) {
        List<Waiting> result = waitingRepository.findByClientId(clientId);
        return result;
    }
}
