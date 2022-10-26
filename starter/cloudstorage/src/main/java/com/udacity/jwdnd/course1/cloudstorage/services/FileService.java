package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFileListForUser(Integer userId) {
        return fileMapper.getFilesForUser(userId);
    }

    public File getFile(Integer fileId, Integer userId) {
        // Make sure that we retrieve a file only if the user id matches!
        File file = fileMapper.getFileByFileId(fileId);

        if (file != null && file.getUserId() == userId) {
            return file;
        } else {
            return null;
        }
    }

    public boolean uploadMultipartFile(MultipartFile multipartFile, Integer userId) throws IOException {
        File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), userId,
                multipartFile.getBytes());

        File existingFile = fileMapper.getFileByFilenameForUser(multipartFile.getOriginalFilename(), userId);
        if (existingFile != null) {
            // file with that name already exists - no insert!
            return false;
        }

        fileMapper.insert(file);
        return true;
    }

    public void deleteFile(Integer fileId, Integer userId) {
        // Make sure that we delete a file only if the user id matches!
        File file = fileMapper.getFileByFileId(fileId);

        if (file != null && file.getUserId() == userId) {
            fileMapper.delete(fileId);
        }
    }
}