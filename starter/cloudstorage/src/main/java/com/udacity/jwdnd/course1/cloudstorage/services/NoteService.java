package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNoteListForUser(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }

    public void saveNote(Integer noteId, String noteTitle, String noteDescription, Integer userId) {
        Note note = new Note(noteId, noteTitle, noteDescription, userId);

        if (noteId == null) {
            noteMapper.insert(note);
        } else {
            noteMapper.update(note);
        }
    }

    public void deleteNote(Integer noteId, Integer userId) {

        Note note = noteMapper.getNoteByNoteId(noteId);

        if (note != null && note.getUserId() == userId) {
            noteMapper.delete(noteId);
        }
    }
}
