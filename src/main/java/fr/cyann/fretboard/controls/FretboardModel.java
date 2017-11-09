/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

import javafx.scene.paint.Color;

/**
 *
 * @author cyann
 */
public abstract class FretboardModel {

    public enum Note {
        A(0, "A"), 
        Bb(1, "A# / Bb"), 
        B(2, "B"), 
        C(3, "C"), 
        Db(4, "C# / Db"), 
        D(5, "D"), 
        Eb(6, "D# / Eb"), 
        E(7, "E"), 
        F(8, "F"), 
        Gb(9, "F# / Gb"), 
        G(10, "G"), 
        Ab(11, "G# / Ab");

        private final int interval;
        private final String name;

        private Note(int interval, String name) {
            this.interval = interval;
            this.name = name;
        }

        public int interval() {
            return interval;
        }
        
        public static Note valueOf(int interval) {
            return Note.values()[interval];
        }

        @Override
        public String toString() {
            return name;
        }
        
    }

    public abstract int stringCount();

    public abstract int fretCount();

    public abstract boolean isVisible(int string, int fret);

    public abstract Color getTipColor(int string, int fret);

    public abstract String getTipNote(int string, int fret);

}
