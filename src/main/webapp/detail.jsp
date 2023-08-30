<%@ page import="zerobase.mission1.dto.WifiDTO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zerobase.mission1.entity.BookmarkGroup" %>
<%@ page import="zerobase.mission1.service.BookmarkService" %>
<%@ page import="zerobase.mission1.service.WifiService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    WifiService wifiService = new WifiService();
    BookmarkService bookmarkService = new BookmarkService();

    WifiDTO wifiDTO = wifiService.getWifi(request.getParameter("mgrNo"));
    double distance = Double.parseDouble(request.getParameter("distance"));

    ArrayList<BookmarkGroup> bookmarkGroupList = bookmarkService.getBookmarkGroups();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>와이파이 정보 구하기</title>
    </head>
    <link rel="stylesheet" href="./resources/styles.css">
    <body>
        <h1>와이파이 정보 구하기</h1>
        <div class="buttons">
            <a href="/">홈</a> |
            <a href="history.jsp">위치 히스토리 목록</a> |
            <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> |
            <a href="bookmark-list.jsp">북마크 보기</a> |
            <a href="bookmark-group.jsp">북마크 그룹 관리</a>
        </div>

        <form action="bookmark-add-submit.jsp" method="post">
            <input type="hidden" name="mgrNo" value="<%=wifiDTO.getX_SWIFI_MGR_NO()%>">
            <select name="bookmark_group_id" id="bookmark_group">
                <option value="">북마크 그룹 이름 선택</option>
                <%
                    for (int i = 0; i < bookmarkGroupList.size(); i++) {
                %>
                <option value="<%=bookmarkGroupList.get(i).getBookmarkGroupId()%>">
                    <%=bookmarkGroupList.get(i).getBookmarkGroupName()%>
                </option>
                <%
                    }
                %>
            </select>
            <input type="submit" value="북마크 추가하기">
        </form>

        <table>
            <thead>
                <tr>
                    <th>거리(Km)</th>
                    <td>
                        <%=distance%>
                    </td>
                </tr>
                <tr>
                    <th>관리번호</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_MGR_NO()%>
                    </td>
                </tr>
                <tr>
                    <th>자치구</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_WRDOFC()%>
                    </td>
                </tr>
                <tr>
                    <th>와이파이명</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_MAIN_NM()%>
                    </td>
                </tr>
                <tr>
                    <th>도로명주소</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_ADRES1()%>
                    </td>
                </tr>
                <tr>
                    <th>상세주소</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_ADRES2()%>
                    </td>
                </tr>
                <tr>
                    <th>설치위치(층)</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_INSTL_FLOOR()%>
                    </td>
                </tr>
                <tr>
                    <th>설치유형</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_INSTL_TY()%>
                    </td>
                </tr>
                <tr>
                    <th>설치기관</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_INSTL_MBY()%>
                    </td>
                </tr>
                <tr>
                    <th>서비스구분</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_SVC_SE()%>
                    </td>
                </tr>
                <tr>
                    <th>망 종류</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_CMCWR()%>
                    </td>
                </tr>
                <tr>
                    <th>설치년도</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_CNSTC_YEAR()%>
                    </td>
                </tr>
                <tr>
                    <th>실내외구분</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_INOUT_DOOR()%>
                    </td>
                </tr>
                <tr>
                    <th>WIFI 접속환경</th>
                    <td>
                        <%=wifiDTO.getX_SWIFI_REMARS3()%>
                    </td>
                </tr>
                <tr>
                    <th>X 좌표</th>
                    <td>
                        <%=wifiDTO.getLNT()%>
                    </td>
                </tr>
                <tr>
                    <th>Y 좌표</th>
                    <td>
                        <%=wifiDTO.getLAT()%>
                    </td>
                </tr>
                <tr>
                    <th>작업 일자</th>
                    <td>
                        <%=wifiDTO.getWORK_DTTM()%>
                    </td>
                </tr>

            </thead>

        </table>


    </body>
</html>