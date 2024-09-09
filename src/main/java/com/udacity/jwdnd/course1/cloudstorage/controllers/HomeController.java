package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.data.Credential;
import com.udacity.jwdnd.course1.cloudstorage.data.File;
import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import com.udacity.jwdnd.course1.cloudstorage.data.User;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;

    @GetMapping("/home")
    public String getHome(File file, Credential credential, Note note, Authentication authentication, Model model) {
        var user=new User();
        user=userService.getUser(authentication.getName());
        var credentials=credentialService.getCredentials(user.getUserid());
        var notes=noteService.getNotes(user.getUserid());
        String key=user.getSalt();
        List<File> files=fileService.filesUpload(user.getUserid());
        model.addAttribute("key",key);
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("notes",notes);
        model.addAttribute("credentials", credentials );
        model.addAttribute("files",files);

        return "home";

    }

    @GetMapping("/")
    public String getHome2(){
        return "redirect:/home";
    }

}
