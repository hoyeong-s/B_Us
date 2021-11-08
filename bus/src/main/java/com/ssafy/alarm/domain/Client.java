package com.ssafy.alarm.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
public class Client {

    @Id @NotNull
    UUID uuid;

    @OneToMany
    List<Waiting> waitingList = new ArrayList<>();

}
