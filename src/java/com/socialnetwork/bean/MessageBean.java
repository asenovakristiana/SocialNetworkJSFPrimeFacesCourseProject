/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.bean;

import com.socialnetwork.dao.MessageDao;
import com.socialnetwork.model.Message;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Kristiana Asenova
 */
@ManagedBean
@RequestScoped
public class MessageBean {

    private String text;
    private UploadedFile uploadedFile;

    @ManagedProperty("#{userBean}")
    private UserBean userBean;

    @ManagedProperty("#{photoBean}")
    private PhotoBean photoBean;

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public void setPhotoBean(PhotoBean photoBean) {
        this.photoBean = photoBean;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public UploadedFile getUploadedFile() { return uploadedFile; }
    public void setUploadedFile(UploadedFile uploadedFile) { this.uploadedFile = uploadedFile; }

    private String getDbPath() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/social.db");
    }

    private String getUploadsPath() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/uploads");
    }

    public void publish() {

        String imagePath = null;

        if (uploadedFile != null && uploadedFile.getFileName() != null && !uploadedFile.getFileName().isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getFileName();

                File uploadsDir = new File(getUploadsPath());
                if (!uploadsDir.exists()) {
                    uploadsDir.mkdirs();
                }

                File target = new File(uploadsDir, fileName);

                InputStream in = uploadedFile.getInputstream();
                OutputStream out = new FileOutputStream(target);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                in.close();
                out.close();

                imagePath = "/resources/uploads/" + fileName;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int userId = userBean.getLoggedUser().getId();

        Message m = new Message();
        m.setUserId(userId);
        m.setText(text);
        m.setImagePath(imagePath);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        long numericDate = Long.parseLong(now.format(formatter));
        m.setCreatedAt(numericDate);


        new MessageDao().insert(getDbPath(), m);

        text = "";
        uploadedFile = null;
    }

    public List<Message> getMessages() {
        return new MessageDao().findByUser(getDbPath(), userBean.getLoggedUser().getId());
    }

    public List<Message> getTextPosts() {
        List<Message> all = getMessages();
        List<Message> onlyText = new ArrayList<>();

        for (Message m : all) {
            if (m.getImagePath() == null || m.getImagePath().isEmpty()) {
                onlyText.add(m);
            }
        }
        return onlyText;
    }

    public List<String> getAllUploadedImages() { 
        String username = userBean.getLoggedUser().getUsername(); 
        return photoBean.loadPhotos(username); }
}

