package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialAndDecrypted;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credentialService;
    private UserService userService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping
    public String populateLists(Model model, Authentication authentication) {
        User currentUser = userService.getUser(authentication.getName());

        List<File> fileList = fileService.getFileListForUser(currentUser.getUserId());
        List<Note> noteList = noteService.getNoteListForUser(currentUser.getUserId());
        List<Credential> encryptedCredentialList = credentialService.getCredentialListForUser(currentUser.getUserId());
        List<CredentialAndDecrypted> credentialList = credentialService.getDecryptedPasswords(encryptedCredentialList);

        model.addAttribute("fileList", fileList);
        model.addAttribute("noteList", noteList);
        model.addAttribute("credentialList", credentialList);
        return "home";
    }
}
