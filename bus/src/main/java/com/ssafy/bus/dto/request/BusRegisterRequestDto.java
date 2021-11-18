package com.ssafy.bus.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusRegisterRequestDto {

    @ApiModelProperty(value = "기계유일 UUID")
    private String clientId;

    @ApiModelProperty(value = "탑승할 버스 고유 번호", example = "208000191")
    private int vehId;

    @ApiModelProperty(value = "버스 정류장의 고유 아이디")
    private int arsId;

    @ApiModelProperty(value = "탑승할 정류장의 순서 staOrd", example = "67")
    private int staOrd;

    @ApiModelProperty(value = "탑승할 버스의 번호", example = "M5333안양")
    private String busNo;

}
