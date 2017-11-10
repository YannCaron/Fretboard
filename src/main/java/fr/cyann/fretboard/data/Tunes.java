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
    public static class TuneElement {

        @Attribute
        private String name;

        @ElementList(type = Note.class)
        private ArrayList<Note> strings;

        public String getName() {
            return name;
        }

        public List<Note> getStrings() {
            return strings;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    @ElementList(type = TuneElement.class)
    private ArrayList<TuneElement> tunes;

    public ArrayList<TuneElement> getTunes() {
        return tunes;
    }

    public static void main(String[] args) throws Exception {
        Tunes tunes = new Tunes();
        TuneElement guitareEStandard = new TuneElement();
        guitareEStandard.name = "Guitare E Standard";
        guitareEStandard.strings = new ArrayList<>();
        guitareEStandard.strings.add(Note.E);
        guitareEStandard.strings.add(Note.A);
        guitareEStandard.strings.add(Note.D);
        guitareEStandard.strings.add(Note.G);
        guitareEStandard.strings.add(Note.B);
        guitareEStandard.strings.add(Note.E);
        tunes.tunes = new ArrayList<>();
        tunes.tunes.add(guitareEStandard);

        Serializer serializer = new Persister();
        serializer.write(tunes, System.out);
    }

}
