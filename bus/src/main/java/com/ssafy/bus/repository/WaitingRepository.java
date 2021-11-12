package com.ssafy.bus.repository;

import com.ssafy.bus.domain.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByVehId(int vehId);

    List<Waiting> findByClientId(String clientId);

    Waiting findByClientIdAndBusNo(String clientId, String busNo);
}
