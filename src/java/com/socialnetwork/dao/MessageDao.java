/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.dao;

import com.socialnetwork.model.Message;
import java.sql.*;
import java.util.*;

/**
 *
 * @author Kristiana Asenova
 */
public class MessageDao {

    public List<Message> findAll(String dbPath) {
        List<Message> list = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM messages ORDER BY created_at DESC"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message m = new Message();
                m.setId(rs.getInt("id"));
                m.setUserId(rs.getInt("user_id"));
                m.setText(rs.getString("text"));
                m.setImagePath(rs.getString("image_path"));
                m.setCreatedAt(rs.getLong("created_at"));
                list.add(m);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public List<Message> findByUser(String dbPath, int userId) {
        List<Message> list = new ArrayList<>();

        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM messages WHERE user_id = ? ORDER BY created_at DESC"
            );

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message m = new Message();
                m.setId(rs.getInt("id"));
                m.setUserId(rs.getInt("user_id"));
                m.setText(rs.getString("text"));
                m.setImagePath(rs.getString("image_path"));
                m.setCreatedAt(rs.getLong("created_at"));
                list.add(m);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public void insert(String dbPath, Message m) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO messages(user_id, text, image_path, created_at) VALUES (?, ?, ?, ?)"
            );

            ps.setInt(1, m.getUserId());
            ps.setString(2, m.getText());
            ps.setString(3, m.getImagePath());
            ps.setLong(4, m.getCreatedAt());

            ps.executeUpdate();

            ps.close();
            con.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
