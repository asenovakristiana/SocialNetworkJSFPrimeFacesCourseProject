/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.model;

/**
 *
 * @author Kristiana Asenova
 */
public class Contact {
    private int id;
    private int userId;
    private int contactId;

    public Contact(int id, int userId, int contactId) {
        this.id = id;
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getContactId() { return contactId; }
}
