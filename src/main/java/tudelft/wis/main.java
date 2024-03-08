package tudelft.wis;

import tudelft.wis.idm_tasks.basicJDBC.interfaces.JDBCManager;

import java.sql.Connection;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        JDBCManager jdbcManager = new JDBCManager() {
            @Override
            public Connection getConnection() throws SQLException, ClassNotFoundException {
                return null;
            }
        };
    }
}
