<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="zerobase.mission1.service.BookmarkService" %>
<%
    int id = Integer.parseInt(request.getParameter("id"));

    BookmarkService bookmarkService = new BookmarkService();
    bookmarkService.removeBookmarkGroup(id);
%>

<script>
    alert('북마크 그룹 정보를 삭제하였습니다.');
    location.href = "bookmark-group.jsp";
</script>