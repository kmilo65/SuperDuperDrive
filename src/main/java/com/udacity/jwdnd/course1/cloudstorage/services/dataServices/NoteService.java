package com.udacity.jwdnd.course1.cloudstorage.services.dataServices;

import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;


    //public NoteService(NoteMapper noteMapper) { this.noteMapper = noteMapper; }

    public void addNote(Note note){
        this.noteMapper.insertNote(note);
    }

    public Note getNoteById(Long noteid){
        return this.noteMapper.getNoteById(noteid);
    }

    public List<Note> getNotes(Long userId){
        return this.noteMapper.getAllNotes(userId);
    }

    public List<String> getAllNoteTitle(){
        return this.noteMapper.getAllNoteTitle();
    }

    public void deleteNote(Long noteid){ this.noteMapper.deleteNote(noteid); }

    public void updateNote(Note note){ this.noteMapper.updateNote(note);}

    public Note getNote(Note note) {return this.noteMapper.getNote(note);}

}
