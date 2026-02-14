package com.socialnetwork.util;

import java.sql.*;
import javax.faces.context.FacesContext;

/**
 *
 * @author Kristiana Asenova f.n. 25657
 */
public class DBUtil {

    private static String getDbPath() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/social.db");
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite driver not found");
        }

        return DriverManager.getConnection("jdbc:sqlite:" + getDbPath());
    }
}
