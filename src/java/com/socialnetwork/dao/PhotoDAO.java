/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.dao;


import com.socialnetwork.model.Photo;
import com.socialnetwork.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kristiana
 */
public class PhotoDAO {

    public int getNextPhotoNumber(int userId) {
        int number = 1;

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) AS total FROM photos WHERE user_id=?"
            );

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                number = rs.getInt("total") + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return number;
    }

    public void insert(int userId, String path) {
        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO photos(user_id, path) VALUES (?, ?)"
            );

            ps.setInt(1, userId);
            ps.setString(2, path);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Photo> findByUser(int userId) {
        List<Photo> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM photos WHERE user_id=?"
            );

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Photo p = new Photo();
                p.setId(rs.getInt("id"));
                p.setUserId(rs.getInt("user_id"));
                p.setPath(rs.getString("path"));
                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}

