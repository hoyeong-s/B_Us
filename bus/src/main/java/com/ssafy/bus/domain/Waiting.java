package com.ssafy.bus.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int staOrd;

    private int vehId;

    private String clientId;

    private String busNo;

    private int arsId;


    @Builder
    public Waiting(int staOrd, int vehId, String clientId, String busNo, int arsId) {
        this.staOrd = staOrd;
        this.vehId = vehId;
        this.clientId = clientId;
        this.busNo = busNo;
        this.arsId = arsId;
    }
}
