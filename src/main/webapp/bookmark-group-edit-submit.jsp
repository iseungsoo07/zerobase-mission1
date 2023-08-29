<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="zerobase.mission1.repository.BookmarkRepository" %>
<%
    request.setCharacterEncoding("UTF-8");

    int id = Integer.parseInt(request.getParameter("id"));
    String bookmarkGroupName = request.getParameter("bookmark_group_name");
    int bookmarkGroupSeq = Integer.parseInt(request.getParameter("bookmark_group_seq"));

    BookmarkRepository bookmarkRepository = new BookmarkRepository();
    bookmarkRepository.updateBookmarkGroup(bookmarkGroupName, bookmarkGroupSeq, id);
%>

<script>
    alert('북마크 그룹 정보를 수정하였습니다.');
    location.href = "bookmark-group.jsp";
</script>