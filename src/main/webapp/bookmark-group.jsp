<%@ page import="java.util.ArrayList" %>
<%@ page import="zerobase.mission1.entity.BookmarkGroup" %>
<%@ page import="zerobase.mission1.service.BookmarkService" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    BookmarkService bookmarkService = new BookmarkService();
    ArrayList<BookmarkGroup> bookmarkGroupList = bookmarkService.getBookmarkGroups();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>와이파이 정보 구하기</title>
    </head>
    <link rel="stylesheet" href="./resources/styles.css">
    <body>
        <h1>북마크 그룹 관리</h1>
        <div class="buttons">
            <a href="/">홈</a> |
            <a href="history.jsp">위치 히스토리 목록</a> |
            <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> |
            <a href="bookmark-list.jsp">북마크 보기</a> |
            <a href="bookmark-group.jsp">북마크 그룹 관리</a>
        </div>

        <a href="bookmark-group-add.jsp">
            <button>북마크 그룹 이름 추가</button>
        </a>
        <table>
            <tr>
                <th>ID</th>
                <th>북마크 이름</th>
                <th>순서</th>
                <th>등록일자</th>
                <th>수정일자</th>
                <th>비고</th>
            </tr>
            <% if (bookmarkGroupList.size() == 0) { %>
            <tr>
                <td colspan="6" class="table_blank"> 정보가 존재하지 않습니다.</td>
            </tr>

            <% } else {
                for (int i = 0; i < bookmarkGroupList.size(); i++) {
            %>
            <tr>
                <td>
                    <%=bookmarkGroupList.get(i).getBookmarkGroupId()%>
                </td>
                <td>
                    <%=bookmarkGroupList.get(i).getBookmarkGroupName()%>
                </td>
                <td>
                    <%=bookmarkGroupList.get(i).getBookmarkGroupSeq()%>
                </td>
                <td>
                    <%=bookmarkGroupList.get(i).getRegDate()%>
                </td>

                <% if (bookmarkGroupList.get(i).getModifyDate().getYear() == 1900) { %>
                <td></td>
                <% } else { %>
                <td>
                    <%=bookmarkGroupList.get(i).getModifyDate()%>
                </td>
                <% } %>
                <td style="text-align: center">
                    <a href="bookmark-group-edit.jsp?id=<%=bookmarkGroupList.get(i).getBookmarkGroupId()%>">수정</a>
                    <a href="bookmark-group-delete.jsp?id=<%=bookmarkGroupList.get(i).getBookmarkGroupId()%>">삭제</a>
                </td>
            </tr>

            <%
                    }
                }
            %>
        </table>


    </body>
</html>