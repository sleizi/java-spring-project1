package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/credential")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String handleNoteSave(@RequestParam(value = "credentialId", required = false) Integer credentialId,
                                 @RequestParam("url") String url,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {

        User currentUser = userService.getUser(authentication.getName());
        credentialService.saveCredential(credentialId, url, username, password, currentUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "credentials");
        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/home";
    }

    @GetMapping("/delete/{credentialId}")
    public String handleNoteDelete(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes, Authentication authentication) {
        User currentUser = userService.getUser(authentication.getName());
        credentialService.deleteCredential(credentialId, currentUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "credentials");
        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        return "redirect:/home";
    }
}
