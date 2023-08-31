package zerobase.mission1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    static Connection conn = null;

    // Connection 객체를 얻어오기 위한 DBConnect()
    public static Connection DBConnect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:/Users/iseungsoo07/zerobase/mission1.db");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return conn;
    }
}
