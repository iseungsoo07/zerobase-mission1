<%@ page import="zerobase.mission1.service.BookmarkService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%if (request.getParameter("bookmark_group_id").equals("")) {%>
<script>alert('북마크 그룹을 먼저 생성해주세요.');
location.href = '/bookmark-group.jsp';</script>

<% } else {
    request.setCharacterEncoding("UTF-8");
    int bookmarkGroupId = Integer.parseInt(request.getParameter("bookmark_group_id"));
    String mgrNo = request.getParameter("mgrNo");

    BookmarkService bookmarkService = new BookmarkService();
    bookmarkService.addBookmark(bookmarkGroupId, mgrNo);
}
%>
<script>
    alert("북마크 정보를 추가하였습니다.");
    location.href = "bookmark-list.jsp";
</script>