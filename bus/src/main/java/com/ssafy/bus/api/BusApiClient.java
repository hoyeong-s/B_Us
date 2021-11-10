package com.ssafy.bus.api;


import com.ssafy.bus.dto.response.BusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class BusApiClient {

    private final RestTemplate restTemplate;

    private final String SERVICE_KEY = "Cd1Kw21w%2FOpUmlWaO%2FwpyF47QEYq76243PN57pJNwTFQ%2BKkOsSLxzta%2FjG8oLag%2FAVdYt6DS9YTFwVi85KJabg%3D%3D";

    private final String OpenAPIUrl_getInfomation =
            "http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=" + SERVICE_KEY + "&arsId={stationId}";


//    public BusResponseDto requestBus(int stationId) {
    public BusResponseDto requestBus(int stationId) {
        final HttpHeaders headers = new HttpHeaders();

        final HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(OpenAPIUrl_getInfomation, HttpMethod.GET, entity, BusResponseDto.class, stationId).getBody();
    }
}
