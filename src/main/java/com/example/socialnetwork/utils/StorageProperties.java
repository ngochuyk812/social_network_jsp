package com.example.socialnetwork.utils;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Configuration

public class StorageProperties {

    private Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public StorageProperties() throws IOException {
        CURRENT_FOLDER = CURRENT_FOLDER.resolve(createPath("static"));
    }

    public Path createPath(String path) throws IOException {
        Path staticPath = Paths.get(path);
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath));
        }
        return CURRENT_FOLDER.resolve(staticPath);
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(CURRENT_FOLDER.toFile());
    }

    public Resource load(String path) {
        Path file = CURRENT_FOLDER.resolve(path);
        try {
            Resource resource = (Resource) new UrlResource(file.toUri());
            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public String uploadImage(String base64, String path) throws IOException {
        byte[] data = Base64.decodeBase64(base64);
        long timeDate = new Date().getTime();
        if(!path.equals("")){
            createPath(path);
        }
        Path file = CURRENT_FOLDER.resolve(path).resolve(timeDate+".jpg");
        try (OutputStream stream = new FileOutputStream(file.toFile())) {
            stream.write(data);
            System.out.println(file.toString() + "-------------------------FILE");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return file.toString().replace(CURRENT_FOLDER.toString() + "\\", "");
    }

    public String save(MultipartFile file, String path) throws IOException {
        long timeDate = new Date().getTime();
        if(!path.equals("")){
            createPath(path);
        }
        System.out.println(file.getContentType() +  "sdsdsdsdsd");
        Path fileSave = null;
        if(file.getContentType().contains("image"))
         fileSave = CURRENT_FOLDER.resolve(path).resolve("image_"+timeDate+".jpg");
        if(file.getContentType().contains("video"))
         fileSave = CURRENT_FOLDER.resolve(path).resolve("video_"+timeDate+".mp4");
        try {

            Files.copy(file.getInputStream(), fileSave);
            return fileSave.toString().replace(CURRENT_FOLDER.toString() + "\\", "");
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

}
