package zerobase.mission1.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zerobase.mission1.ApiExplorer;
import zerobase.mission1.DBConnection;
import zerobase.mission1.Pos;
import zerobase.mission1.entity.Wifi;

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

                    String sql = "insert into wifi values (?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";

                    pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, String.valueOf(temp.get("X_SWIFI_MGR_NO")));
                    pstmt.setString(2, String.valueOf(temp.get("X_SWIFI_WRDOFC")));
                    pstmt.setString(3, String.valueOf(temp.get("X_SWIFI_MAIN_NM")));
                    pstmt.setString(4, String.valueOf(temp.get("X_SWIFI_ADRES1")));
                    pstmt.setString(5, String.valueOf(temp.get("X_SWIFI_ADRES2")));
                    pstmt.setString(6, String.valueOf(temp.get("X_SWIFI_INSTL_FLOOR")));
                    pstmt.setString(7, String.valueOf(temp.get("X_SWIFI_INSTL_TY")));
                    pstmt.setString(8, String.valueOf(temp.get("X_SWIFI_INSTL_MBY")));
                    pstmt.setString(9, String.valueOf(temp.get("X_SWIFI_SVC_SE")));
                    pstmt.setString(10, String.valueOf(temp.get("X_SWIFI_CMCWR")));
                    pstmt.setString(11, String.valueOf(temp.get("X_SWIFI_CNSTC_YEAR")));
                    pstmt.setString(12, String.valueOf(temp.get("X_SWIFI_INOUT_DOOR")));
                    pstmt.setString(13, String.valueOf(temp.get("X_SWIFI_REMARS3")));
                    pstmt.setString(14, String.valueOf(temp.get("LAT")));
                    pstmt.setString(15, String.valueOf(temp.get("LNT")));
                    pstmt.setString(16, String.valueOf(temp.get("WORK_DTTM")));

                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

    }

    public ArrayList<Wifi> getWifiList(Pos pos) {
        ArrayList<Wifi> list = new ArrayList<>();


        return list;
    }

    // public ArrayList<Wifi> getWifiList(Pos pos) {
    //     conn = DBConnection.DBConnect();
    //
    //     ArrayList<Wifi> list = new ArrayList<>();
    //     ArrayList<Pos> posList = new ArrayList<>();
    //
    //     String sql = "select LAT, LNT from wifi";
    //
    //     try {
    //         pstmt = conn.prepareStatement(sql);
    //         rs = pstmt.executeQuery();
    //         while (rs.next()) {
    //             Pos wifiPos = new Pos(Double.parseDouble(rs.getString("LAT")), Double.parseDouble(rs.getString("LNT")));
    //             posList.add(wifiPos);
    //         }
    //
    //         sql = "select round(sqrt(power(abs(pos.lnt - ?), 2) + power(abs(pos.lat - ?), 2)), 4) as distance,* from wifi order by distance limit 20";
    //
    //         pstmt = conn.prepareStatement(sql);
    //         pstmt.setString(1, String.valueOf(posList.get(0).getLat()));
    //         pstmt.setString(2, String.valueOf(posList.get(0).getLnt()));
    //
    //         rs = pstmt.executeQuery();
    //
    //         while (rs.next()) {
    //             Wifi wifi = new Wifi();
    //             wifi.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
    //             wifi.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
    //             wifi.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
    //             wifi.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
    //             wifi.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
    //             wifi.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
    //             wifi.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
    //             wifi.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
    //             wifi.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
    //             wifi.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
    //             wifi.setX_SWIFI_CNSTC_YEAR(rs.getString("X_SWIFI_CNSTC_YEAR"));
    //             wifi.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
    //             wifi.setX_SWIFI_REMARS3(rs.getString("X_SWIFI_REMARS3"));
    //             wifi.setLAT(rs.getString("LAT"));
    //             wifi.setLNT(rs.getString("LNT"));
    //             wifi.setWORK_DTTM(rs.getString("WORK_DTTM"));
    //
    //             list.add(wifi);
    //         }
    //
    //         // while (rs.next()) {
    //         //     Wifi wifi = new Wifi();
    //         //     wifi.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
    //         //     wifi.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
    //         //     wifi.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
    //         //     wifi.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
    //         //     wifi.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
    //         //     wifi.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
    //         //     wifi.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
    //         //     wifi.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
    //         //     wifi.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
    //         //     wifi.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
    //         //     wifi.setX_SWIFI_CNSTC_YEAR(rs.getString("X_SWIFI_CNSTC_YEAR"));
    //         //     wifi.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
    //         //     wifi.setX_SWIFI_REMARS3(rs.getString("X_SWIFI_REMARS3"));
    //         //     wifi.setLAT(rs.getString("LAT"));
    //         //     wifi.setLNT(rs.getString("LNT"));
    //         //     wifi.setWORK_DTTM(rs.getString("WORK_DTTM"));
    //         //
    //         //     list.add(wifi);
    //         // }
    //
    //     } catch (Exception e) {
    //         throw new RuntimeException(e);
    //     } finally {
    //         disconnect();
    //     }
    //
    //     return list;
    // }


}
