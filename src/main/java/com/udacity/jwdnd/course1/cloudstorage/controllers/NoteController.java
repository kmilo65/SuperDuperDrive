package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import com.udacity.jwdnd.course1.cloudstorage.data.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import com.udacity.jwdnd.course1.cloudstorage.services.dataServices.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value="/notes")
public class NoteController {


    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping("/shownotes")
    private String notes( @ModelAttribute("Note")  Note note,Authentication authentication,RedirectAttributes attributes){

        /**RedirectAttributes attributes**/
        String username = authentication.getName();
        Long userId = userService.getUser(username).getUserid();
        var notes=noteService.getNotes(userId);
        //model.addAttribute(notes);
        attributes.addFlashAttribute("activeTab", "notes");
        /**return "redirect:/home?activeTab=notes";**/
        return "redirect:/home";
    }


    @PostMapping("/saveNote")
    public String addUpdateNote(@ModelAttribute("Note")  Note note, Authentication authentication, Model model) {

        String username = authentication.getName();
        Long userId = userService.getUser(username).getUserid();
        note.setUserId(userId);

        int statusUpdate=0;


        List<String> allNoteTitle = this.noteService.getAllNoteTitle();


        if(note.getNoteId()==null || note.getNoteId()==0) {
            if (allNoteTitle.contains(note.getNoteTitle())) {
                model.addAttribute("statusUpdate",statusUpdate);
                System.out.print(statusUpdate);
                return "result";
            }

            this.noteService.addNote(note);
            //System.out.print(note);

        } else{
            this.noteService.updateNote(note);
           // System.out.print(note);

        }
        statusUpdate=1;
        //model.addAttribute("notes", this.noteService.getNotes(userId));
        System.out.print(statusUpdate);
        System.out.println(" an notes");
        model.addAttribute("statusUpdate",statusUpdate);
        return  "result";
    }

    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote(@PathVariable("noteId") Long noteId, Model model ){
        int statusDelete=1;
        noteService.deleteNote(noteId);
        model.addAttribute("statusDelete",statusDelete);
        System.out.print("estoy aca");
        return "result";

    }

     @GetMapping("/editNote/{noteId}")
        public String showUpdateForm(Note note,  Model model) {
           note=noteService.getNote(note);
           model.addAttribute("note",note );

         return "redirect:/";
    }




}
