package ssafy.third.bus;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class URLConnector_post extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    Map<String,Object> map = new HashMap<>();
    @Override
    // doInBackground의 매개변수 값이 여러개일 경우를 위해 배열로
    protected String doInBackground(String... strings) {
        try {
            String str = "http://k5a504.p.ssafy.io:8080/api/waiting/register";
            URL url = new URL(str);  // 어떤 서버에 요청할지(localhost 안됨.)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json;charset=utf8");
            conn.setRequestMethod("POST");                              //데이터를 GET 방식으로 전송합니다.
            conn.setDoOutput(true);

             //서버에 보낼 값 포함해 요청함.
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//            sendMsg = "arsId"+strings[0]+
//                    "&busNo"+strings[1]+
//                    "&clientId="+strings[2]+
//                    "&staOrd="+strings[3]+
//                    "&vehId="+strings[4]; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("arsId", strings[0]);
            jsonObject.accumulate("busNo",  strings[1]);
            jsonObject.accumulate("clientId",  strings[2]);
            jsonObject.accumulate("staOrd",  strings[3]);
            jsonObject.accumulate("vehId",  strings[4]);
            Log.d("string",jsonObject.toString());
            receiveMsg = jsonObject.toString();

            osw.write(receiveMsg);                           // OutputStreamWriter에 담아 전송
            osw.flush();

            // jsp와 통신이 잘 되고, 서버에서 보낸 값 받음.
            if(conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {    // 통신이 실패한 이유를 찍기위한 로그
                Log.i("통신 결과", conn.getResponseCode()+"에러");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 서버에서 보낸 값을 리턴합니다.
        return receiveMsg;
    }
}

