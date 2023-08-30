package zerobase.mission1.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zerobase.mission1.ApiExplorer;
import zerobase.mission1.DBConnection;
import zerobase.mission1.Pos;
import zerobase.mission1.dto.WifiDTO;
import zerobase.mission1.entity.PositionHisotry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WifiRepository {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ApiExplorer apiExplorer = new ApiExplorer();

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

    // Open API wifi 정보를 DB에 추가
    public boolean saveWifiData() {
        conn = DBConnection.DBConnect();

        int start = 0;
        int end = 0;

        try {
            int total = apiExplorer.getTotalCount();

            int lastPage = total / 1000;
            int lastPageRemain = total % 1000;

            for (int i = 0; i <= lastPage; i++) {
                start = 1000 * i + 1;

                if (i == lastPage) {
                    end = start + lastPageRemain - 1;
                } else {
                    end = 1000 * (i + 1);
                }

                apiExplorer.getURL(start, end);
                StringBuilder sb = apiExplorer.getJson();

                JsonObject jsonObject = (JsonObject) JsonParser.parseString(sb.toString());
                JsonObject data = (JsonObject) jsonObject.get("TbPublicWifiInfo");
                JsonArray jsonArray = data.getAsJsonArray("row");

                for (int j = 0; j < jsonArray.size(); j++) {
                    JsonObject temp = (JsonObject) jsonArray.get(j);

                    String sql = "INSERT INTO WIFI VALUES (?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";

                    pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, (temp.get("X_SWIFI_MGR_NO").getAsString()));
                    pstmt.setString(2, (temp.get("X_SWIFI_WRDOFC")).getAsString());
                    pstmt.setString(3, (temp.get("X_SWIFI_MAIN_NM")).getAsString());
                    pstmt.setString(4, (temp.get("X_SWIFI_ADRES1")).getAsString());
                    pstmt.setString(5, (temp.get("X_SWIFI_ADRES2")).getAsString());
                    pstmt.setString(6, (temp.get("X_SWIFI_INSTL_FLOOR")).getAsString());
                    pstmt.setString(7, (temp.get("X_SWIFI_INSTL_TY")).getAsString());
                    pstmt.setString(8, (temp.get("X_SWIFI_INSTL_MBY")).getAsString());
                    pstmt.setString(9, (temp.get("X_SWIFI_SVC_SE")).getAsString());
                    pstmt.setString(10, (temp.get("X_SWIFI_CMCWR")).getAsString());
                    pstmt.setString(11, (temp.get("X_SWIFI_CNSTC_YEAR")).getAsString());
                    pstmt.setString(12, (temp.get("X_SWIFI_INOUT_DOOR")).getAsString());
                    pstmt.setString(13, (temp.get("X_SWIFI_REMARS3")).getAsString());
                    pstmt.setDouble(14, (temp.get("LAT").getAsDouble()));
                    pstmt.setDouble(15, (temp.get("LNT").getAsDouble()));
                    pstmt.setString(16, (temp.get("WORK_DTTM")).getAsString());

                    int res = pstmt.executeUpdate();

                    if (res > 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            disconnect();
        }
        return false;
    }

    // 현재 내 위치로부터 가장 가까운 와이파이 목록을 20개만 보여주는 메소드
    // 두 위도와 경도 사이의 거리는 harversine(하버사인) 공식을 사용
    public ArrayList<WifiDTO> getWifiList(Pos pos) {
        conn = DBConnection.DBConnect();
        ArrayList<WifiDTO> list = new ArrayList<>();

        saveHistory(pos); // 와이파이 목록을 조회할 때 위치 정보를 히스토리에 저장

        String sql = "SELECT ROUND(6371 * 2 * " +
                "ASIN(SQRT(POWER(SIN(((LAT - ?) * PI() / 180) / 2), 2) + COS(? * PI() / 180) * COS((LAT * PI() / 180)) " +
                "* POWER(SIN(((LNT - ?) * PI() / 180) / 2), 2))), 4) AS distance, * FROM WIFI ORDER BY distance LIMIT 20;";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, pos.lat);
            pstmt.setDouble(2, pos.lat);
            pstmt.setDouble(3, pos.lnt);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                WifiDTO wifi = new WifiDTO();
                wifi.setDistance(rs.getDouble("distance"));
                wifi.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
                wifi.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
                wifi.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
                wifi.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
                wifi.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
                wifi.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
                wifi.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
                wifi.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
                wifi.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
                wifi.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
                wifi.setX_SWIFI_CNSTC_YEAR(rs.getString("X_SWIFI_CNSTC_YEAR"));
                wifi.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
                wifi.setX_SWIFI_REMARS3(rs.getString("X_SWIFI_REMARS3"));
                wifi.setLAT(rs.getDouble("LAT"));
                wifi.setLNT(rs.getDouble("LNT"));
                wifi.setWORK_DTTM(rs.getString("WORK_DTTM"));

                list.add(wifi);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return list;
    }

    // 와이파이 상세 정보를 가져오기 위한 메소드
    // 와이파이 테이블의 PK인 X_SWIFI_MGR_NO를 이용해서 조회
    public WifiDTO getWifi(String id) {
        conn = DBConnection.DBConnect();
        WifiDTO wifi = null;

        String sql = "SELECT * FROM WIFI WHERE X_SWIFI_MGR_NO = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                wifi = new WifiDTO();
                wifi.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
                wifi.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
                wifi.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
                wifi.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
                wifi.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
                wifi.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
                wifi.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
                wifi.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
                wifi.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
                wifi.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
                wifi.setX_SWIFI_CNSTC_YEAR(rs.getString("X_SWIFI_CNSTC_YEAR"));
                wifi.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
                wifi.setX_SWIFI_REMARS3(rs.getString("X_SWIFI_REMARS3"));
                wifi.setLAT(rs.getDouble("LAT"));
                wifi.setLNT(rs.getDouble("LNT"));
                wifi.setWORK_DTTM(rs.getString("WORK_DTTM"));

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

        return wifi;
    }

    // 히스토리 저장을 위한 메소드
    // 현재 위치를 파라미터로 받아서 저장한다.
    public boolean saveHistory(Pos pos) {
        conn = DBConnection.DBConnect();

        String sql = "INSERT INTO POSITION_HISTORY (LNT, LAT, SEARCH_DATE) VALUES (?, ?, ?)";

        String formattedDateTime = convertLocalDateTimeToString(); // 현재 시간을 String 타입으로 변경해서 조회일자로 사용

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, pos.lnt);
            pstmt.setDouble(2, pos.lat);
            pstmt.setString(3, formattedDateTime);

            int res = pstmt.executeUpdate();

            if (res > 0) {
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    // 히스토리 목록을 보여주기 위한 메소드
    public ArrayList<PositionHisotry> getHistory() {
        conn = DBConnection.DBConnect();
        ArrayList<PositionHisotry> list = new ArrayList<>();

        String sql = "SELECT * FROM POSITION_HISTORY ORDER BY HISTORY_ID DESC";

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PositionHisotry history = new PositionHisotry();
                history.setHistoryId(rs.getInt("history_id"));
                history.setLnt(rs.getDouble("lnt"));
                history.setLat(rs.getDouble("lat"));
                history.setSearchDate(convertStringToLocalDateTime(rs.getString("search_date")));

                list.add(history);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    // 히스토리 내역을 삭제하는 메소드
    // 히스토리 번호를 파라미터로 받아서 사용
    public boolean deleteHistory(int id) {
        conn = DBConnection.DBConnect();

        String sql = "DELETE FROM POSITION_HISTORY WHERE HISTORY_ID = ?";

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
