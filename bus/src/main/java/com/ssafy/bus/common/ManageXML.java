package com.ssafy.bus.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ManageXML {

    /**
     * 밥환되는 결과가 여러개인 메소드
     * @param str
     * @return
     * @throws IOException
     */
    public static List getItemListFromXML(String str) throws IOException {
        String xml = str;
        JSONObject jObject = XML.toJSONObject(xml);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object json = mapper.readValue(jObject.toString(), Object.class);
        String output = mapper.writeValueAsString(json);
        System.out.println("output = " + output);

        HashMap<String, Object> jsonMap = new ObjectMapper().readValue(output, HashMap.class);
        HashMap<String, Object> serviceResult = (HashMap<String, Object>) jsonMap.get("ServiceResult");
        HashMap<String, Object> msgBody = (HashMap<String, Object>) serviceResult.get("msgBody");
        List itemList = (List) msgBody.get("itemList");

        return itemList;
    }

    /**
     * 반환되는 결과가 하나인 메소드
     * @param str
     * @return
     * @throws IOException
     */
    public static HashMap<String, Object> getItemFromXML(String str) throws IOException {
        String xml = str;
        JSONObject jObject = XML.toJSONObject(xml);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        Object json = mapper.readValue(jObject.toString(), Object.class);
        String output = mapper.writeValueAsString(json);
        System.out.println("output = " + output);

        HashMap<String, Object> jsonMap = new ObjectMapper().readValue(output, HashMap.class);
        HashMap<String, Object> serviceResult = (HashMap<String, Object>) jsonMap.get("ServiceResult");
        HashMap<String, Object> msgBody = (HashMap<String, Object>) serviceResult.get("msgBody");
        HashMap<String, Object> itemList = (HashMap<String, Object>) msgBody.get("itemList");
        return itemList;
    }

    public static String getFullUri(String basicUrl, String key, Object value) throws IOException {
        String urlStr = basicUrl
                + "?serviceKey=Cd1Kw21w%2FOpUmlWaO%2FwpyF47QEYq76243PN57pJNwTFQ%2BKkOsSLxzta%2FjG8oLag%2FAVdYt6DS9YTFwVi85KJabg%3D%3D"
                + "&" + key + "=" + value;
        URL url = new URL(urlStr);

        System.out.println("url.toString() = " + url.toString());
        BufferedReader bf;
        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

        String result = bf.readLine();
        return result;
    }
}
