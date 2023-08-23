<%@ page import="zerobase.mission1.ApiExplorer" %>
<%@ page import="zerobase.mission1.repository.WifiRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ApiExplorer apiExplorer = new ApiExplorer();
    WifiRepository wifiRepository = new WifiRepository();
    int totalCnt = 0;

    try {
        totalCnt = Integer.parseInt(String.valueOf(apiExplorer.getTotalCount()));
        wifiRepository.insertDB();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>와이파이 정보 구하기</title>
    </head>
    <body>
        <div style="text-align: center">
            <h1><%=totalCnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
            <a href="/">홈 으로 가기</a>
        </div>

    </body>
</html>
