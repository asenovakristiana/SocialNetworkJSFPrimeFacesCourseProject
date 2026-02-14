/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.bean;

import com.socialnetwork.dao.UserDAO;
import com.socialnetwork.model.User;
import java.io.Serializable;
import java.security.MessageDigest;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Kristiana Asenova f.n. 25657
 * 
 */
@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

    private User user = new User();
    private String password;
    private User loggedUser;

    private UserDAO userDAO = new UserDAO();

    public User getUser() { return user; }
    public User getLoggedUser() { return loggedUser; }

    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String register() {

        user.setPasswordHash(sha256(password));
        user.setImagePath("/resources/images/default-avatar.png");

        userDAO.registerUser(user);

        user = new User();
        password = "";

        return "login.xhtml?faces-redirect=true";
    }

    public String login() {

        User dbUser = userDAO.getUserByUsername(user.getUsername());

        if (dbUser != null && dbUser.getPasswordHash().equals(sha256(password))) {
            loggedUser = dbUser;

            user = new User();
            password = "";

            return "profile.xhtml?faces-redirect=true";
        }

        return "login.xhtml";
    }

    public String logout() {
        loggedUser = null;
        user = new User();
        password = "";
        return "login.xhtml?faces-redirect=true";
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
