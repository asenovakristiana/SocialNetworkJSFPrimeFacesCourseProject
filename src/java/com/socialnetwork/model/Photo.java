/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.model;

import java.io.Serializable;

/**
 *
 * @author Kristiana
 */

public class Photo implements Serializable {

    private int id;
    private int userId;
    private String path;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
