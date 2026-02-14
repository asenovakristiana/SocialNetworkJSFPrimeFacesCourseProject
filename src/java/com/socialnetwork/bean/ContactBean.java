/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.bean;

import com.socialnetwork.dao.ContactDAO;
import com.socialnetwork.dao.UserDAO;
import com.socialnetwork.model.User;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Kristiana Asenova
 */

@ManagedBean
@SessionScoped
public class ContactBean implements Serializable {

    private String searchQuery = "";
    private List<User> searchResults;
    private List<User> myContacts;

    private UserDAO userDAO = new UserDAO();
    private ContactDAO contactDAO = new ContactDAO();

    @ManagedProperty("#{userBean}")
    private UserBean userBean;

    public void search() {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            searchResults = userDAO.searchUsers(""); 
        } else {
            searchResults = userDAO.searchUsers(searchQuery);
        }
    }

    public void addContact(int contactId) {
        int userId = userBean.getLoggedUser().getId();

        if (userId == contactId) return; 
        contactDAO.addContact(userId, contactId);

        loadContacts();
        search();       
    }

    public void loadContacts() {
        myContacts = contactDAO.getContacts(userBean.getLoggedUser().getId());
    }

    public List<User> getSearchResults() { return searchResults; }
    public List<User> getMyContacts() { return myContacts; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
        loadContacts();
        search();
    }
}
