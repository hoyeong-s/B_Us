package ssafy.third.bus;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnector extends AsyncTask<String, Void, String> {
    String sendMsg, receiveMsg;
    @Override
    // doInBackground의 매개변수 값이 여러개일 경우를 위해 배열로
    protected String doInBackground(String... strings) {
        try {
            String str = "http://k5a504.p.ssafy.io:8080/api/";
            str += "busInfo/"+strings[0];
            URL url = new URL(str);  // 어떤 서버에 요청할지(localhost 안됨.)
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");                              //데이터를 GET 방식으로 전송합니다.
            //conn.setDoOutput(true);
            conn.connect();

            // 서버에 보낼 값 포함해 요청함.
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                sendMsg = "busInfo/21028"; // GET방식으로 작성해 POST로 보냄 ex) "id=admin&pwd=1234";
//                osw.write("");                           // OutputStreamWriter에 담아 전송
//                osw.flush();

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
        }
        // 서버에서 보낸 값을 리턴합니다.
        return receiveMsg;
    }
}

