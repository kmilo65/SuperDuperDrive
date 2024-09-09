package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.data.Credential;
import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import com.udacity.jwdnd.course1.cloudstorage.data.User;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value="/credentials")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;

    final String urlRegex = "((https?|ftp)://)?(www\\.)?([\\w-]+)(\\.[\\w-]+)+(:\\d+)?(/[\\w- ;,./?%&=]*)?";



    @GetMapping("/showcredentials")
    private String credentials(@ModelAttribute("Credential")  Credential credential, Authentication authentication, RedirectAttributes attributes){

      /*  String username = authentication.getName();
        Long userId = userService.getUser(username).getUserid();
        var notes=noteService.getNotes(userId);
        attributes.addFlashAttribute("notes",notes);*/
        attributes.addFlashAttribute("activateTab","credentials");
        return "redirect:/home?activateTab=credentials";
    }



    @PostMapping("/addCredential")
    public String addCredential(@ModelAttribute("Credential") Credential credential, Authentication authentication, Model model) {
        var user=new User();
        user=userService.getUser(authentication.getName());
        credential.setUserId(user.getUserid());
        String key=user.getSalt();
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(),key));
        int updateCredentials=0;

        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(credential.getUrl());

        if(credential.getCredentialId()==null ){

            if (matcher.find()){
                this.credentialService.addCredential(credential);

            } else {
                System.out.println("aiqqi ");
                model.addAttribute("updateCredentials",updateCredentials);
                return "result";
            }

        } else  {

            this.credentialService.updateCredential(credential);

        }
        updateCredentials=1;
        model.addAttribute("updateCredentials",updateCredentials);
        model.addAttribute("credentials",this.credentialService.getCredentials(user.getUserid()));
        return "result";
    }

    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Long credentialId,Model model) {
        credentialService.deleteCredential(credentialId);
        model.addAttribute("updateCredentials",1);
        return "result";
    }



}
