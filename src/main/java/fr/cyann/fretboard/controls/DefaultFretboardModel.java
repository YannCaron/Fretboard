/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

import fr.cyann.fretboard.data.Note;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 *
 * @author cyann
 */
public class DefaultFretboardModel extends FretboardModel {

    public static final int DEFAULT_FRET_COUNT = 12;

    private int fretCount;
    private final List<Note> tunes;
    private final Map<Note, Color> scale;

    public DefaultFretboardModel() {
        tunes = new ArrayList<>();
        scale = new HashMap<>();

        fretCount = DEFAULT_FRET_COUNT;
    }

    public int getFretCount() {
        return fretCount;
    }

    public void setFretCount(int fretCount) {
        this.fretCount = fretCount;
    }

    public List<Note> getTunes() {
        return tunes;
    }

    public void addNote(Note note, Color color) {
        scale.put(note, color);
    }

    public void clearNotes() {
        scale.clear();
    }

    @Override
    public int stringCount() {
        return tunes.size();
    }

    @Override
    public int fretCount() {
        return fretCount;
    }

    private Note getNote(int string, int fret) {
        int noteIndex = (tunes.get(string).interval() + fret) % 12;
        return Note.valueOf(noteIndex);
    }

    @Override
    public boolean isVisible(int string, int fret) {
        return scale.containsKey(getNote(string, fret));
    }

    @Override
    public Color getTipColor(int string, int fret) {
        return scale.get(getNote(string, fret));
    }

    @Override
    public String getTipNote(int string, int fret) {
        if (fret == 0) {
            return tunes.get(string).name();
        }
        return null;
    }

}
