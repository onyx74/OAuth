package com.vanya.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class PhotoService {
    @Autowired
    private UserService userService;


    public void storeNewPhoto(long userId, MultipartFile photo) throws IOException {
        String pathToPhoto = "user-" + userId;
        String filepath = Paths.get("/home/ivan/java/OAuth/user/src/main/resources/static/api/user/photo", pathToPhoto).toString();
        String filepath2 = Paths.get("//home/ivan/java/OAuth/user/target/classes/static/api/user/photo", pathToPhoto).toString();


        saveTheFileLocally(photo, filepath);
        saveTheFileLocally(photo, filepath2);
        userService.setNewPhoto(userId, pathToPhoto);
    }

    private void saveTheFileLocally(MultipartFile photo, String filepath) throws IOException {
        File file = new File(filepath);
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(file));
        stream.write(photo.getBytes());
        stream.close();
    }
}
