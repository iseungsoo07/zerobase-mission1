<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="zerobase.mission1.service.WifiService" %>
<%
    request.setCharacterEncoding("UTF-8");
%>
<script>
    let answer = confirm('삭제하시겠습니까?');

    if (answer) {
        <%
            WifiService wifiService = new WifiService();
            if(wifiService.removeHistory(Integer.parseInt(request.getParameter("id")))) {
        %>
                alert("삭제 완료되었습니다.");
                location.href = "history.jsp";
        <%
            }
        %>
    } else {
        history.go(-1);
    }
</script>


