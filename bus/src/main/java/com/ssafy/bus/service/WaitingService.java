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
        // 중복처리
        Waiting exist = waitingRepository.findByClientIdAndVehId(dto.getClientId(), Integer.parseInt(String.valueOf(dto.getVehId())));
        if (exist != null) {
            return;
        }
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

    public void save(Waiting waiting1) {
        waitingRepository.save(waiting1);
    }

    public Waiting findWaitingByClientIdAndBusNo(String clientId, String busNo) {
        return waitingRepository.findByClientIdAndBusNo(clientId, busNo);

    }

    public void delete(Waiting waitingByClientIdAndBusNo) {
        waitingRepository.delete(waitingByClientIdAndBusNo);
        return;
    }
}
