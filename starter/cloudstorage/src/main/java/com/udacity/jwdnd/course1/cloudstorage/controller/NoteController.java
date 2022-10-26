package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
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
@RequestMapping("/note")
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/save")
    public String handleNoteSave(@RequestParam(value = "noteId", required = false) Integer noteId,
                                 @RequestParam("noteTitle") String noteTitle,
                                 @RequestParam("noteDescription") String noteDescription,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {

        User currentUser = userService.getUser(authentication.getName());
        noteService.saveNote(noteId, noteTitle, noteDescription, currentUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "notes");
        redirectAttributes.addFlashAttribute("changeSuccess", true);
        return "redirect:/home";
    }

    @GetMapping("/delete/{noteId}")
    public String handleNoteDelete(@PathVariable Integer noteId, RedirectAttributes redirectAttributes, Authentication authentication) {
        User currentUser = userService.getUser(authentication.getName());
        noteService.deleteNote(noteId, currentUser.getUserId());

        redirectAttributes.addFlashAttribute("activeTab", "notes");
        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        return "redirect:/home";
    }
}
