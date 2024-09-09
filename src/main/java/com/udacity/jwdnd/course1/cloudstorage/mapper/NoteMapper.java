package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.data.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userId=#{userId}")
    List<Note> getAllNotes(Long userId);


    @Select("SELECT DISTINCT RTRIM(LTRIM(notetitle)) FROM NOTES")
    List<String> getAllNoteTitle();


    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNoteById(Long noteId);

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(Note note);

    @Insert("INSERT INTO NOTES (notetitle,notedescription,userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);


    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription}, userId=#{userId} WHERE noteid=#{noteId}")
    void updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteId}")
    void deleteNote(Long noteid);
}

