/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.socialnetwork.bean;



import com.socialnetwork.dao.PhotoDAO;
import com.socialnetwork.util.DBUtil;
import com.socialnetwork.model.Photo;
import java.io.*;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * Kristiana Asenova 25657
 */
@ManagedBean
@SessionScoped
public class PhotoBean implements Serializable {

    private UploadedFile file;
    private UploadedFile profilFile;
    private List<String> photos = new ArrayList<>();
    private List<String> profilPhotos = new ArrayList<>();
    private boolean profileUploaded = false;

    @ManagedProperty("#{userBean}")
    private UserBean userBean;

    public UserBean getUserBean() { return userBean; }
    public void setUserBean(UserBean userBean) { this.userBean = userBean; }

    private int userId() {
        return userBean.getLoggedUser().getId();
    }

    private String getUploadsPath() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/uploads");
    }
    
    private String getProfilUploadsPath() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/images/uploads");
    }
    
    public void upload() {
        try {
            if (file != null && file.getFileName() != null && !file.getFileName().isEmpty()) {

                File uploadsDir = new File(getUploadsPath());
                if (!uploadsDir.exists()) uploadsDir.mkdirs();

                String username = userBean.getLoggedUser().getUsername();

                String extension = "";
                String original = file.getFileName();
                int dot = original.lastIndexOf(".");
                if (dot != -1) extension = original.substring(dot);

                String fileName = username + "_" + System.currentTimeMillis() + extension;

                File target = new File(uploadsDir, fileName);

                try (InputStream in = file.getInputstream();
                     OutputStream out = new FileOutputStream(target)) {

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                }

                String dbPath = "/resources/uploads/" + fileName;

                new PhotoDAO().insert(userId(), dbPath);

                loadPhotos(username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProfileImage(String path) throws SQLException {
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE users SET profile_image=? WHERE id=?"
        );
        ps.setString(1, path);
        ps.setInt(2, userId());
        ps.executeUpdate();
        conn.close();
    }

    public List<String> loadPhotos(String username) {
        photos.clear();

        String folderPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/resources/uploads");

        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {

                if (file.isFile() && file.getName().startsWith(username + "_")) {
                    photos.add("/resources/uploads/" + file.getName());
                }
            }
        }
        return photos;
    }
    
    public List<String> loadProfilPhotos(String username) {
        profilPhotos.clear();

        String folderPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/images/uploads");

        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {

                if (file.isFile() && file.getName().startsWith(username + "_")) {
                    profilPhotos.add("/resources/uploads/" + file.getName());
                }
            }
        }
        return profilPhotos;
    }
    
    public void uploadProfilImage() {
        try {
            if (profilFile != null && profilFile.getFileName() != null && !profilFile.getFileName().isEmpty()) {

                File uploadsDir = new File(getProfilUploadsPath());
                if (!uploadsDir.exists()) uploadsDir.mkdirs();

                String username = userBean.getLoggedUser().getUsername();

                String extension = "";
                String original = profilFile.getFileName();
                int dot = original.lastIndexOf(".");
                if (dot != -1) extension = original.substring(dot);

                String fileName = username + "_" + System.currentTimeMillis() + extension;

                File target = new File(uploadsDir, fileName);

                try (InputStream in = profilFile.getInputstream();
                     OutputStream out = new FileOutputStream(target)) {

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                }

                String dbPath = "/resources/images/" + fileName;
                loadProfilPhotos(username);
                updateProfileImage(dbPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UploadedFile getFile() { return file; }
    public void setFile(UploadedFile file) { this.file = file; }
    public UploadedFile getProfilFile() { return profilFile; }
    public void setProfilFile(UploadedFile profilFile) { 
        this.profilFile = profilFile; 
        profileUploaded = true;

    }

    public String getCurrentPhoto() {
        if (profilPhotos.isEmpty()) return null;
        if (profileUploaded = true) {
        return profilPhotos.get(profilPhotos.size() - 1);
        }
        return null;
    }
    public List<String> getPhotos() { return photos; }
}
