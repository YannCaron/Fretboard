/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

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

    private final int fretCount;
    private final List<Note> tunes;
    private final Map<Note, Color> scale;

    public DefaultFretboardModel(int fretCount) {
        this.fretCount = fretCount;
        tunes = new ArrayList<>();
        scale = new HashMap<>();
    }

    public void addString(Note e) {
        tunes.add(e);
    }
    
    public void addNote(Note note, Color color) {
        scale.put(note, color);
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