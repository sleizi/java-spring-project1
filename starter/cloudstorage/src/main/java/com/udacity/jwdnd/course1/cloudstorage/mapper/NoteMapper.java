package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNoteByNoteId(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE noteTitle = #{noteTitle}")
    Note getNoteByNoteTitle(String noteTitle);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotesForUser(Integer userId);

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insert(Note note);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    void delete(Integer noteId);
}
