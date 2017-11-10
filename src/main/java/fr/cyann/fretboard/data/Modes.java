/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.data;

import java.util.ArrayList;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author cyann
 */
@Root
public class Modes {

    @Root
    public static class Mode {

        @Attribute
        private String name;

        @Attribute
        private boolean[] intervals;

        public String getName() {
            return name;
        }

        public boolean[] getIntervals() {
            return intervals;
        }

        @Override
        public String toString() {
            return name;
        }
        
    }

    @ElementList(type = Mode.class)
    private ArrayList<Mode> modes;

    public ArrayList<Mode> getModes() {
        return modes;
    }

    public static void main(String[] args) throws Exception {

        Modes modes = new Modes();
        modes.modes = new ArrayList<>();
        Mode dorian = new Mode();
        dorian.name = "dorian";
        dorian.intervals = new boolean[]{true, false, true, false, true, true, false, true, false, true, false, true};
        modes.modes.add(dorian);

        Serializer serializer = new Persister();
        serializer.write(modes, System.out);
    }

}
