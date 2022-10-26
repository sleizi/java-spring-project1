package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/file")
public class FileController {
    private FileService fileService;
    private UserService userService;

    @Value("${files.max-file-size}")
    private DataSize maximumFileSize;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) throws IOException {

        if (file.getSize() > maximumFileSize.toBytes()) {
            redirectAttributes.addFlashAttribute("fileTooLargeError", true);
            return "redirect:/home";
        }

        User currentUser = userService.getUser(authentication.getName());
        boolean uploadResult = fileService.uploadMultipartFile(file, currentUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "files");
        if (uploadResult) {
            redirectAttributes.addFlashAttribute("changeSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("changeError", true);
        }
        return "redirect:/home";
    }

    @GetMapping("/delete/{fileId}")
    public String handleFileDelete(@PathVariable Integer fileId, RedirectAttributes redirectAttributes, Authentication authentication) {
        User currentUser = userService.getUser(authentication.getName());
        fileService.deleteFile(fileId, currentUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "files");
        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        return "redirect:/home";
    }

    @GetMapping("/view/{fileId}")
    public void handleFileView(@PathVariable Integer fileId, HttpServletResponse response, Authentication authentication)
            throws IOException {
        User currentUser = userService.getUser(authentication.getName());
        File file = fileService.getFile(fileId, currentUser.getUserId());

        if (file == null) return;

        response.setContentType(file.getContentType());
        response.setHeader("Content-Disposition", "filename=\"" + file.getFilename() + "\"");
        response.setContentLengthLong(file.getFileSize());
        OutputStream os = response.getOutputStream();

        try {
            os.write(file.getFileData(), 0, file.getFileData().length);
        } catch (Exception e) {
        } finally {
            os.close();
        }
    }
}
