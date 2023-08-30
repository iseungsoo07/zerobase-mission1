<%@ page import="zerobase.mission1.ApiExplorer" %>
<%@ page import="zerobase.mission1.service.WifiService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    ApiExplorer apiExplorer = new ApiExplorer();
    WifiService wifiService = new WifiService();
    int totalCnt = 0;
    boolean isFirst = true;

    try {
        totalCnt = Integer.parseInt(String.valueOf(apiExplorer.getTotalCount()));

        if (!wifiService.saveWifiData()) {
            isFirst = false;
        }

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
            <% if (isFirst) {%>
            <h1><%=totalCnt%>개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>
            <% } else { %>
            <h1>와이파이 정보가 이미 저장되어 있습니다.</h1>
            <%
                }
            %>
            <a href="/">홈 으로 가기</a>
        </div>

    </body>
</html>
