package zerobase.mission1;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiExplorer {
    StringBuilder urlBuilder;

    public void getURL(int startIdx, int endIdx) throws Exception {
        urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /*URL*/
        urlBuilder.append("/" + URLEncoder.encode("774e71417969736536364e4c764652", "UTF-8")); /*인증키 (sample사용시에는 호출시 제한됩니다.)*/
        urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /*요청파일타입 (xml,xmlf,xls,json) */
        urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /*서비스명 (대소문자 구분 필수입니다.)*/
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(startIdx), "UTF-8")); /*요청시작위치 (sample인증키 사용시 5이내 숫자)*/
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(endIdx), "UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안 됨)*/
    }

    public StringBuilder getJson() throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml");
        System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인이 필요하므로 추가합니다.*/
        BufferedReader rd;

        // 서비스코드가 정상이면 200~300사이의 숫자가 나옵니다.
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb;
    }

    public int getTotalCount() throws Exception {
        getURL(1, 1);
        StringBuilder sb = getJson();

        JsonObject jsonObject = (JsonObject) JsonParser.parseString(sb.toString());
        JsonObject data = (JsonObject) jsonObject.get("TbPublicWifiInfo");
        JsonElement totalCnt = data.get("list_total_count");

        return Integer.parseInt(String.valueOf(totalCnt));
    }

}