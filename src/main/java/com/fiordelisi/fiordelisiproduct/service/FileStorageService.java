package com.fiordelisi.fiordelisiproduct.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileStorageService {

    private final Path rootDir = Paths.get("uploads");

    public FileStorageService() {
        try {
            if (!Files.exists(rootDir)) {
                Files.createDirectories(rootDir);
            }
        } catch (IOException e) {
            log.error("Could not initialize storage folder", e);
            throw new RuntimeException(e);
        }
    }


    public List<String> storeFiles(MultipartFile[] files) {
        List<String> storedPaths = new ArrayList<>();
        if (files == null) return storedPaths;

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            try {
                String originalFilename = file.getOriginalFilename();
                String filename = System.currentTimeMillis() + "_" +
                        (originalFilename == null ? "file" : originalFilename.replaceAll("\\s+", "_"));
                Path targetPath = rootDir.resolve(filename);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                storedPaths.add("/uploads/" + filename);
            } catch (IOException e) {
                log.warn("Failed to store file: " + file.getOriginalFilename(), e);
            }
        }

        return storedPaths;
    }


    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isBlank()) return;

        try {
            Path path = rootDir.resolve(Paths.get(filePath).getFileName().toString());
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.warn("Failed to delete file: " + filePath, e);
        }
    }


    public void deleteFiles(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) return;
        filePaths.forEach(this::deleteFile);
    }
}
