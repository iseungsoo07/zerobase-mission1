package zerobase.mission1.repository;

import zerobase.mission1.DBConnection;
import zerobase.mission1.entity.BookmarkDTO;
import zerobase.mission1.entity.BookmarkGroup;
import zerobase.mission1.entity.BookmarkGroupDTO;

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

    private String convertLocalDateTimeToString() {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private LocalDateTime convertStringToLocalDateTime(String datetimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(datetimeString, formatter);
    }

    // 북마크 그룹 추가
    public void createBookmarkGroup(String name, int sequence) {
        conn = DBConnection.DBConnect();

        String sql = "insert into bookmark_group (BOOKMARK_GROUP_NAME, BOOKMARK_GROUP_SEQ, REG_DATE, MODIFY_DATE) values (?, ?, ?, ?)";

        String formattedDateTime = convertLocalDateTimeToString();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, sequence);
            pstmt.setString(3, formattedDateTime);

            LocalDateTime temp = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String tempDate = temp.format(formatter);

            pstmt.setString(4, tempDate);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

    }

    // 북마크 그룹 리스트 가져오기
    public ArrayList<BookmarkGroup> getBookmarkGroupList() {
        conn = DBConnection.DBConnect();
        ArrayList<BookmarkGroup> list = new ArrayList<>();

        String sql = "select bookmark_group_id, bookmark_group_name, bookmark_group_seq, reg_date, modify_date from bookmark_group order by bookmark_group_seq";
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
    public void createBookmark(int id, String mgrNo) {
        conn = DBConnection.DBConnect();

        String sql = "insert into bookmark (BOOKMARK_GROUP_ID, X_SWIFI_MGR_NO, REG_DATE) values (?, ?, ?)";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, mgrNo);
            pstmt.setString(3, convertLocalDateTimeToString());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            disconnect();
        }
    }

    // 북마크 리스트를 가져오기
    public ArrayList<BookmarkDTO> getBookmarkList() {
        conn = DBConnection.DBConnect();
        ArrayList<BookmarkDTO> list = new ArrayList<>();

        String sql = "select b.bookmark_id, bg.bookmark_group_name, w.x_swifi_main_nm, b.reg_date" +
                " from bookmark b join bookmark_group bg on b.bookmark_group_id = bg.bookmark_group_id" +
                " join wifi w on b.X_SWIFI_MGR_NO = w.X_SWIFI_MGR_NO";

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
    public BookmarkGroupDTO getBookmarkGroupInfo(int id) {
        conn = DBConnection.DBConnect();
        BookmarkGroupDTO bookmarkGroup = new BookmarkGroupDTO();

        String sql = "select bookmark_group_name, bookmark_group_seq from bookmark_group where bookmark_group_id = ?";
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
    public void updateBookmarkGroup(String bookmarkGroupName, int bookmarkGroupSeq, int id) {
        conn = DBConnection.DBConnect();

        String sql = "update bookmark_group set BOOKMARK_GROUP_NAME = ?, BOOKMARK_GROUP_SEQ = ?, MODIFY_DATE = ? where BOOKMARK_GROUP_ID = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookmarkGroupName);
            pstmt.setInt(2, bookmarkGroupSeq);
            pstmt.setString(3, convertLocalDateTimeToString());
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

    }

    public void deleteBookmarkGroup(int id) {
        conn = DBConnection.DBConnect();

        String sql = "delete from bookmark_group where bookmark_group_id = ?";
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }
    }
}
