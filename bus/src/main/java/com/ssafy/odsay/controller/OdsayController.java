package com.ssafy.odsay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("api/busStop")
public class OdsayController {

    @GetMapping("/{busStopId}")
    public String getBusList(@PathVariable Long busStopId) {
        log.info("log", "API요청하는 메소드");

        HashMap<String, Object> result = new HashMap<>();

        String jsonInString = "";

        try {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectionRequestTimeout(5000);
            factory.setReadTimeout(5000);
            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            String url = "https://api.odsay.com/v1/api/busStationInfo";

            UriComponentsBuilder uri = UriComponentsBuilder.fromHttpUrl(url + "?" + "apiKey=XO87l+q4e5Ceh9BywlfbEMnHrXEY++7Adzozyd9MgtQ");
            UriComponentsBuilder uri1 = UriComponentsBuilder.fromHttpUrl(uri + "&" + "lang=0");
            UriComponentsBuilder uri2 = UriComponentsBuilder.fromHttpUrl(uri1 + "&" + "stationID=" + busStopId);

            ResponseEntity<Map> resultMap = restTemplate.exchange(uri2.toString(), HttpMethod.GET, entity, Map.class);
            result.put("statusCode", resultMap.getStatusCodeValue());
            result.put("header", resultMap.getHeaders());
            result.put("body", resultMap.getBody());

            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writeValueAsString(resultMap.getBody());

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            result.put("statusCode", e.getRawStatusCode());
            result.put("body", e.getStatusText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

    @GetMapping("busStop")
    public String init() {
        return "hi";
    }
}
