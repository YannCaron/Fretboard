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
public class DummyFretboardModel extends FretboardModel {

    String scale = "-XBX-XX-R-XX";

    @Override
    public int stringCount() {
        return 6;
    }

    @Override
    public int fretCount() {
        return 24;
    }

    private static int getTune(int string) {
        switch (string) {
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 15;
            case 4:
                return 19;
            case 5:
                return 23;
            default:
                return 0;
        }
    }

    private static int getIndex(int string, int fret) {
        return (fret + getTune(string)) % 12;
    }

    @Override
    public boolean isVisible(int string, int fret) {
        int index = getIndex(string, fret);
        return scale.charAt(index) != '-';
    }

    @Override
    public Color getTipColor(int string, int fret) {
        int index = getIndex(string, fret);
        if (scale.charAt(index) == 'R') {
            return Color.RED;
        } else if (scale.charAt(index) == 'B') {
            return Color.BLUE;
        }
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
