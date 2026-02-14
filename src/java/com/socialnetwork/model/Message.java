/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.model;

import java.io.Serializable;

/**
 *
 * @author Kristiana Asenova
 */
public class Message implements Serializable {

    private int id;
    private int userId;
    private String text;
    private String imagePath;
    private long createdAt;

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public String getText() { 
        return text; 
    }
    public void setText(String text) { 
        this.text = text; 
    }

    public String getImagePath() { 
        return imagePath; 
    }
    public void setImagePath(String imagePath) { 
        this.imagePath = imagePath; 
    }

    public long getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(long createdAt) { 
        this.createdAt = createdAt; 
    }
}
