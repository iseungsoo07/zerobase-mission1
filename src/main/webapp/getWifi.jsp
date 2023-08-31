<%@ page import="zerobase.mission1.service.WifiService" %>
<%@ page import="zerobase.mission1.dto.WifiDTO" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="zerobase.mission1.Pos" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- Post -> Redirect -> Get 방식을 위한 페이지 --%>
<%
    WifiService wifiService = new WifiService();

    double lat = Double.parseDouble(request.getParameter("lat"));
    double lnt = Double.parseDouble(request.getParameter("lnt"));

    ArrayList<WifiDTO> list = new ArrayList<>();

    if (request.getParameter("lat") != null && request.getParameter("lnt") != null) {
        Pos pos = new Pos(lat, lnt);
        list = wifiService.getWifiList(pos);
    }

    session.setAttribute("list", list);
    response.sendRedirect("/?lat=" + lat + "&lnt=" + lnt);
%>