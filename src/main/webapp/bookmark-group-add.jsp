<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>와이파이 정보 구하기</title>
    </head>
    <link rel="stylesheet" href="./resources/styles.css">
    <body>
        <h1>북마크 그룹 추가</h1>
        <div class="buttons">
            <a href="/">홈</a> |
            <a href="history.jsp">위치 히스토리 목록</a> |
            <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a> |
            <a href="bookmark-list.jsp">북마크 보기</a> |
            <a href="bookmark-group.jsp">북마크 그룹 관리</a>
        </div>

        <form action="bookmark-group-add-submit.jsp">
            <table>
                <tr>
                    <th>북마크 이름</th>
                    <td><input type="text" name="bookmark_name" id="bookmark_name"></td>
                </tr>
                <tr>
                    <th>순서</th>
                    <td><input type="text" name="bookmark_seq" id="bookmark_seq"></td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center">
                        <input type="submit" value="추가">
                    </td>
                </tr>
            </table>
        </form>


    </body>
</html>