package com.app.Echohub.utility;

import com.app.Echohub.utility.enums.FileType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


public class FileStorage {

    private static final Path baseDirectory;

    static {
        try {
            Resource resource = new ClassPathResource("");
            Path dirToStatic = Paths.get(resource.getURI()).resolve("static");
            if (!Files.exists(dirToStatic)) {
                Files.createDirectories(dirToStatic);
            }
            Path dirToImage = dirToStatic.resolve("resource");
            if (!Files.exists(dirToImage)) {
                Files.createDirectories(dirToImage);
            }
            baseDirectory=dirToImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String saveFile(MultipartFile file, FileType fileType) throws IOException {
        Path directory = baseDirectory.resolve(fileType.getDirectory());
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.lastIndexOf('.') != -1) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }else {
            throw new IOException("file can not be uploaded");
        }
        String filename = UUID.randomUUID().toString() + extension;
        Path destinationFile = directory.resolve(filename);
        file.transferTo(destinationFile);
        System.out.println("filename :"+"/resource/"+fileType.getDirectory()+"/"+filename);
        return "/resource/"+fileType.getDirectory()+"/"+filename;
    }

}