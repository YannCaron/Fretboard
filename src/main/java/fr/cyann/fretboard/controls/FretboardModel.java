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
        C, Db, D, Eb, E, F, Gb, G, Ab, A, Bb, B;
    }
    
    public abstract int stringCount();

    public abstract int fretCount();
    
    public abstract boolean isVisible(int string, int fret);
    
    public abstract Color getTipColor(int string, int fret);

    public abstract String getTipNote(int string, int fret);
    
}
