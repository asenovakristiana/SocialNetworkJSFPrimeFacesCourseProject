/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.dao;

import com.socialnetwork.model.User;
import com.socialnetwork.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kristiana Asenova
 */
public class UserDAO {

    public User getUserById(int id) {
        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE id = ?"
            );
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractUser(rs);

        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public User getUserByUsername(String username) {
        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username = ?"
            );
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extractUser(rs);

        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void registerUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO users(email, username, password_hash, gender, age, location, profile_image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getGender());
            ps.setInt(5, user.getAge());
            ps.setString(6, user.getLocation());
            ps.setString(7, user.getImagePath());

            ps.executeUpdate();

        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<User> searchUsers(String query) {
        List<User> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username LIKE ?"
            );
            ps.setString(1, "%" + query + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extractUser(rs));

        } catch (Exception e) { e.printStackTrace(); }

        return list;
    }

    private User extractUser(ResultSet rs) throws Exception {
        User u = new User();

        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setGender(rs.getString("gender"));
        u.setAge(rs.getInt("age"));
        u.setLocation(rs.getString("location"));
        u.setImagePath(rs.getString("profile_image"));

        return u;
    }
}
