/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.data;

/**
 *
 * @author cyann
 */
public enum Note {
    A(0, "A", true), Bb(1, "A# / Bb", false), B(2, "B", true),
    C(3, "C", true), Db(4, "C# / Db", false), D(5, "D", true), Eb(6, "D# / Eb", false),
    E(7, "E", true), F(8, "F", true), Gb(9, "F# / Gb", false),
    G(10, "G", true), Ab(11, "G# / Ab", false);
    private final int interval;
    private final String name;
    private final boolean white;

    private Note(int interval, String name, boolean white) {
        this.interval = interval;
        this.name = name;
        this.white = white;
    }

    public int interval() {
        return interval;
    }

    public boolean isWhite() {
        return white;
    }

    public static Note valueOf(int interval) {
        return Note.values()[interval];
    }

    @Override
    public String toString() {
        return name;
    }

}
