<%@ page import="zerobase.mission1.repository.WifiRepository" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zerobase.mission1.Pos" %>
<%@ page import="zerobase.mission1.dto.WifiDTO" %>
<%@ page import="zerobase.mission1.entity.PositionHisotry" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    WifiRepository wifiRepository = new WifiRepository();

    ArrayList<PositionHisotry> histories = wifiRepository.getHistory();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>와이파이 정보 구하기</title>
    </head>
    <link rel="stylesheet" href="./resources/styles.css">
    <body>
        <h1>위치 히스토리 목록</h1>
        <div class="buttons">
            <a href="/">홈</a> |
            <a href="history.jsp">위치 히스토리 목록</a> |
            <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> |
            <a href="bookmark-list.jsp">북마크 보기</a> |
            <a href="bookmark-group.jsp">북마크 그룹 관리</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>X 좌표</th>
                    <th>Y 좌표</th>
                    <th>조회 일자</th>
                    <th>비고</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (int i = 0; i < histories.size(); i++) {
                %>
                <tr>
                    <td><%=histories.get(i).getHistoryId()%>
                    </td>
                    <td><%=histories.get(i).getLnt()%>
                    </td>
                    <td><%=histories.get(i).getLat()%>
                    </td>
                    <td><%=histories.get(i).getSearchDate()%>
                    </td>
                    <td style="text-align: center">
                        <a href="delete-history.jsp?id=<%=histories.get(i).getHistoryId()%>">
                            <input type="button" value="삭제">
                        </a>
                    </td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>


    </body>
</html>