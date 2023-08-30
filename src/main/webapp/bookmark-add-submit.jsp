<%@ page import="zerobase.mission1.service.BookmarkService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    int bookmarkGroupId = Integer.parseInt(request.getParameter("bookmark_group_id"));
    String mgrNo = request.getParameter("mgrNo");

    BookmarkService bookmarkService = new BookmarkService();
    bookmarkService.addBookmark(bookmarkGroupId, mgrNo);
%>
<script>
    alert("북마크 정보를 추가하였습니다.");
    location.href = "/bookmark-list.jsp";
</script>