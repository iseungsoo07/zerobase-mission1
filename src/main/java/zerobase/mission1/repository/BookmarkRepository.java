package zerobase.mission1.repository;

import zerobase.mission1.DBConnection;
import zerobase.mission1.dto.BookmarkDTO;
import zerobase.mission1.dto.BookmarkGroupDTO;
import zerobase.mission1.entity.BookmarkGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookmarkRepository {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // Connection, PreparedStatement, ResultSet 객체 연결 해제를 위한 메소드
    public void disconnect() {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null && !pstmt.isClosed()) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 현재 시간을 SQLite DateTime형에 넣기 위한 메소드
    // 등록일자, 조회일자, 수정일자를 저장할 때 사용
    private String convertLocalDateTimeToString() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // 데이터베이스에서 가져온 DateTime형을 LocalDateTime에 넣기 위해선 형 변환이 필요
    private LocalDateTime convertStringToLocalDateTime(String datetimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(datetimeString, formatter);
    }

    // 북마크 그룹 추가
    public boolean createBookmarkGroup(String name, int sequence) {
        conn = DBConnection.DBConnect();

        String sql = "INSERT INTO BOOKMARK_GROUP (BOOKMARK_GROUP_NAME, BOOKMARK_GROUP_SEQ, REG_DATE, MODIFY_DATE) VALUES (?, ?, ?, ?)";

        String formattedDateTime = convertLocalDateTimeToString(); // 현재 시간을 String 타입으로 변환

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, sequence);
            pstmt.setString(3, formattedDateTime);

            // 처음 북마크 그룹을 추가할 때 수정일자가 빈칸이어야 하는데
            // 빈칸은 LocalDateTime 형태로 넣을 수 가 없어서 임의의 시간을 저장
            // 화면에서 보여줄 때 수정일자가 1900년이라면 빈칸을 보여주도록 함
            LocalDateTime temp = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String tempDate = temp.format(formatter);

            pstmt.setString(4, tempDate);

            int res = pstmt.executeUpdate();

            if (res > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return false;
    }

    // 북마크 그룹 리스트 가져오기
    public ArrayList<BookmarkGroup> getBookmarkGroupList() {
        conn = DBConnection.DBConnect();
        ArrayList<BookmarkGroup> list = new ArrayList<>();

        String sql = "SELECT * FROM BOOKMARK_GROUP ORDER BY BOOKMARK_GROUP_SEQ";
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookmarkGroup bookmarkGroup = new BookmarkGroup();
                bookmarkGroup.setBookmarkGroupId(rs.getInt("bookmark_group_id"));
                bookmarkGroup.setBookmarkGroupName(rs.getString("bookmark_group_name"));
                bookmarkGroup.setBookmarkGroupSeq(rs.getInt("bookmark_group_seq"));
                LocalDateTime reg_date = convertStringToLocalDateTime(rs.getString("reg_date"));
                bookmarkGroup.setRegDate(reg_date);
                LocalDateTime modify_date = convertStringToLocalDateTime(rs.getString("modify_date"));
                bookmarkGroup.setModifyDate(modify_date);

                list.add(bookmarkGroup);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return list;
    }

    // 북마크 그룹에 북마크를 추가
    public boolean createBookmark(int id, String mgrNo) {
        conn = DBConnection.DBConnect();

        String sql = "INSERT INTO BOOKMARK (BOOKMARK_GROUP_ID, X_SWIFI_MGR_NO, REG_DATE) VALUES (?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, mgrNo);
            pstmt.setString(3, convertLocalDateTimeToString());

            int res = pstmt.executeUpdate();

            if (res > 0) {
                return true;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            disconnect();
        }

        return false;
    }

    // 북마크 리스트를 가져오기
    // 북마크 엔티티를 그대로 가져오면 필요없는 정보들이 있기 때문에
    // BookmarkDTO 객체를 새로 생성해서 필요한 정보만 사용
    public ArrayList<BookmarkDTO> getBookmarkList() {
        conn = DBConnection.DBConnect();
        ArrayList<BookmarkDTO> list = new ArrayList<>();

        // wifi, bookmark, bookmark_group 테이블을 조인해서 필요한 정보를 가져옴
        String sql = "SELECT b.BOOKMARK_ID, bg.BOOKMARK_GROUP_NAME, w.X_SWIFI_MAIN_NM, b.REG_DATE" +
                " FROM BOOKMARK b JOIN BOOKMARK_GROUP bg ON b.BOOKMARK_GROUP_ID = bg.BOOKMARK_GROUP_ID" +
                " JOIN WIFI w ON b.X_SWIFI_MGR_NO = w.X_SWIFI_MGR_NO";

        try {
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                BookmarkDTO bookmark = new BookmarkDTO();
                bookmark.setBookmarkId(rs.getInt("bookmark_id"));
                bookmark.setBookmarkGroupName(rs.getString("bookmark_group_name"));
                bookmark.setWifiName(rs.getString("x_swifi_main_nm"));
                bookmark.setRegDate(convertStringToLocalDateTime(rs.getString("reg_date")));

                list.add(bookmark);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return list;
    }

    // 북마크 수정 삭제를 위한 북마크 그룹 하나의 정보 가져오기
    // 북마크 수정 삭제 페이지에는 북마크 이름과 순서만 있으면 됨
    // 이 두 정보만 가지고 있는 BookmarkGroupDTO 객체 생성
    public BookmarkGroupDTO getBookmarkGroupInfo(int id) {
        conn = DBConnection.DBConnect();
        BookmarkGroupDTO bookmarkGroup = new BookmarkGroupDTO();

        String sql = "SELECT BOOKMARK_GROUP_NAME, BOOKMARK_GROUP_SEQ FROM BOOKMARK_GROUP WHERE BOOKMARK_GROUP_ID = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                bookmarkGroup.setBookmarkGroupName(rs.getString("bookmark_group_name"));
                bookmarkGroup.setBookmarkGroupSeq(rs.getInt("bookmark_group_seq"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return bookmarkGroup;
    }

    // 북마크 그룹 정보 수정
    // 수정하면 수정일자를 변경하고 화면에 수정일자가 나오도록 함
    public boolean updateBookmarkGroup(String bookmarkGroupName, int bookmarkGroupSeq, int id) {
        conn = DBConnection.DBConnect();

        String sql = "UPDATE BOOKMARK_GROUP SET BOOKMARK_GROUP_NAME = ?, BOOKMARK_GROUP_SEQ = ?, MODIFY_DATE = ? WHERE BOOKMARK_GROUP_ID = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookmarkGroupName);
            pstmt.setInt(2, bookmarkGroupSeq);
            pstmt.setString(3, convertLocalDateTimeToString());
            pstmt.setInt(4, id);

            int res = pstmt.executeUpdate();

            if (res > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return false;
    }

    // 북마크 그룹 삭제
    public boolean deleteBookmarkGroup(int id) {
        conn = DBConnection.DBConnect();

        String sql = "DELETE FROM BOOKMARK_GROUP WHERE BOOKMARK_GROUP_ID = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int res = pstmt.executeUpdate();

            if (res > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return false;
    }
}
