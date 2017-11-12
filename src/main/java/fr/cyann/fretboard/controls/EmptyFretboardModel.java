/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

import fr.cyann.fretboard.data.Note;
import javafx.scene.paint.Color;

/**
 *
 * @author cyann
 */
public class EmptyFretboardModel extends FretboardModel {

    @Override
    public int stringCount() {
        return 6;
    }

    @Override
    public int fretCount() {
        return 24;
    }

    @Override
    public boolean isVisible(int string, int fret) {
        return false;
    }

    @Override
    public Color getTipColor(int string, int fret) {
        return Color.BLACK;
    }

    @Override
    public String getTipNote(int string, int fret) {
        if (fret == 0) {
            if (string == 0) {
                return Note.E.name();
            }
            if (string == 1) {
                return Note.A.name();
            }
            if (string == 2) {
                return Note.D.name();
            }
            if (string == 3) {
                return Note.G.name();
            }
            if (string == 4) {
                return Note.B.name();
            }
            if (string == 5) {
                return Note.E.name();
            }
        }
        return null;
    }

}
