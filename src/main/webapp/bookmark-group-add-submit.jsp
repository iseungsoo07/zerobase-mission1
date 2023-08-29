<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="zerobase.mission1.repository.BookmarkRepository" %>
<%
    request.setCharacterEncoding("UTF-8");
    BookmarkRepository bookmarkRepository = new BookmarkRepository();
    bookmarkRepository.createBookmarkGroup(request.getParameter("bookmark_name"), Integer.parseInt(request.getParameter("bookmark_seq")));
%>
<script>
    alert('북마크 그룹 정보를 추가하였습니다.');
    location.href = 'bookmark-group.jsp';
</script>