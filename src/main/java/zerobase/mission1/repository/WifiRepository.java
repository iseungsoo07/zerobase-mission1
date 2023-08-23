package zerobase.mission1.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zerobase.mission1.ApiExplorer;
import zerobase.mission1.DBConnection;
import zerobase.mission1.Pos;
import zerobase.mission1.entity.WifiDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WifiRepository {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    ApiExplorer apiExplorer = new ApiExplorer();

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

    // api로 가져온 wifi 정보 DB에 insert
    public void insertDB() {
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

                    String sql = "insert into wifi values (?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";

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

                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    public ArrayList<WifiDTO> getWifiList(Pos pos) {
        conn = DBConnection.DBConnect();
        ArrayList<WifiDTO> list = new ArrayList<>();

        String sql = "select round(sqrt(power(abs(w.lat - ?), 2) + power(abs(w.lnt - ?), 2)), 4) as distance, * from wifi w order by distance limit 20";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, pos.lat);
            pstmt.setDouble(2, pos.lnt);
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

}
