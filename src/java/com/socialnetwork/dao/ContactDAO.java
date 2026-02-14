/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.dao;

import com.socialnetwork.model.User;
import com.socialnetwork.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kristiana Asenova
 */
public class ContactDAO {

    public Integer getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void addContact(int userId, int contactId) {
        try (Connection conn = DBUtil.getConnection()) {

            conn.setAutoCommit(true);

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contacts(user_id, contact_id) VALUES (?, ?)"
            );

            ps.setInt(1, userId);
            ps.setInt(2, contactId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<User> getContacts(int userId) {
        List<User> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT u.* FROM users u JOIN contacts c ON u.id = c.contact_id WHERE c.user_id = ?"
            );
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                User u = new User();
                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setGender(rs.getString("gender"));
                u.setAge(rs.getInt("age"));
                u.setLocation(rs.getString("location"));
                u.setImagePath(rs.getString("profile_image"));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
