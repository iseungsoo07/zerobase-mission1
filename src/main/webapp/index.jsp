<%@ page import="zerobase.mission1.repository.WifiRepository" %>
<%@ page import="zerobase.mission1.entity.Wifi" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zerobase.mission1.Pos" %>
<%@ page import="zerobase.mission1.entity.WifiDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    WifiRepository wifiRepository = new WifiRepository();

    double lat = request.getParameter("lat") == null ? 0.0 : Double.parseDouble(request.getParameter("lat"));
    double lnt = request.getParameter("lnt") == null ? 0.0 : Double.parseDouble(request.getParameter("lnt"));

    Pos pos = new Pos(lat, lnt);
    ArrayList<WifiDTO> list = wifiRepository.getWifiList(pos);

%>
<!DOCTYPE html>
<html>
    <head>
        <title>와이파이 정보 구하기</title>
    </head>
    <link rel="stylesheet" href="./resources/styles.css">
    <script>
        function getPosition() {
            const lat = document.getElementById("lat");
            const lnt = document.getElementById("lnt");

            navigator.geolocation.getCurrentPosition((position) => {
                lat.value = String(position.coords.latitude);
                lnt.value = String(position.coords.longitude);
            });
        }
    </script>
    <body>
        <h1>와이파이 정보 구하기</h1>
        <div class="buttons">
            <a href="/">홈</a>
            <a href="#">위치 히스토리 목록</a>
            <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
        </div>

        <form action="/" method="get">
            <label for="lat">
                LAT : <input type="text" id="lat" name="lat" value="<%= lat %>"/>,
            </label>
            <label for="lnt">
                LNT : <input type="text" id="lnt" name="lnt" value="<%= lnt %>"/>
            </label>
            <input type="button" id="pos_btn" value="내 위치 가져오기" onclick="getPosition()">
            <input type="submit" value="근처 WIPI 정보 보기">
        </form>

        <table>
            <thead>
                <tr>
                    <th>거리(Km)</th>
                    <th>관리번호</th>
                    <th>자치구</th>
                    <th>와이파이명</th>
                    <th>도로명주소</th>
                    <th>상세주소</th>
                    <th>설치위치(층)</th>
                    <th>설치유형</th>
                    <th>설치기관</th>
                    <th>서비스구분</th>
                    <th>망 종류</th>
                    <th>설치년도</th>
                    <th>실내외구분</th>
                    <th>WIFI 접속환경</th>
                    <th>X 좌표</th>
                    <th>Y 좌표</th>
                    <th>작업 일자</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (request.getParameter("lat") == null && request.getParameter("lnt") == null) {
                %>
                <tr>
                    <td colspan="17">위치 정보를 입력한 후에 조회해 주세요.</td>
                </tr>
                <%
                } else {
                    for (int i = 0; i < list.size(); i++) {
                %>
                <tr>
                    <td>
                        <%=list.get(i).getDistance()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_MGR_NO()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_WRDOFC()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_MAIN_NM()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_ADRES1()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_ADRES2()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_INSTL_FLOOR()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_INSTL_TY()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_INSTL_MBY()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_SVC_SE()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_CMCWR()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_CNSTC_YEAR()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_INOUT_DOOR()%>
                    </td>
                    <td>
                        <%=list.get(i).getX_SWIFI_REMARS3()%>
                    </td>
                    <td>
                        <%=list.get(i).getLNT()%>
                    </td>
                    <td>
                        <%=list.get(i).getLAT()%>
                    </td>
                    <td>
                        <%=list.get(i).getWORK_DTTM()%>
                    </td>

                </tr>
                <%
                        }
                    }
                %>

            </tbody>
        </table>


    </body>
</html>