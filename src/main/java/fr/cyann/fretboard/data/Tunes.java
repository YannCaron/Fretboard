/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.data;

import fr.cyann.fretboard.controls.FretboardModel.Note;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
public class Tunes {

    @Root
    public static class Tune {

        @Attribute
        private String name;

        @Attribute
        private int fretcount;

        @Attribute
        private Note[] notes;

        public String getName() {
            return name;
        }

        public int getFretcount() {
            return fretcount;
        }

        public Note[] getNotes() {
            return notes;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    @ElementList(type = Tune.class)
    private ArrayList<Tune> tunes;

    public ArrayList<Tune> getTunes() {
        return tunes;
    }

    public static void main(String[] args) throws Exception {
        Tunes tunes = new Tunes();
        Tune guitareEStandard = new Tune();
        guitareEStandard.name = "Guitare E Standard";
        guitareEStandard.notes = new Note[] {Note.E, Note.A, Note.D, Note.G, Note.A, Note.E};
        tunes.tunes = new ArrayList<>();
        tunes.tunes.add(guitareEStandard);

        Serializer serializer = new Persister();
        serializer.write(tunes, System.out);
    }

}
