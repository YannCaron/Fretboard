/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard;

import fr.cyann.fretboard.controls.DefaultFretboardModel;
import fr.cyann.fretboard.controls.Fretboard;
import fr.cyann.fretboard.controls.FretboardModel;
import fr.cyann.fretboard.controls.FretboardModel.Note;
import fr.cyann.fretboard.data.Modes;
import fr.cyann.fretboard.data.Modes.Mode;
import fr.cyann.fretboard.data.Tunes;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author cyann
 */
public class FretboardControler implements Initializable {

    public static final int FRET_COUNT = 24;

    private final DefaultFretboardModel fretboardModel;

    @FXML
    public ChoiceBox<Note> cbRootNote;

    @FXML
    public ChoiceBox<Tunes.Tune> cbTune;

    @FXML
    public ChoiceBox<Modes.Mode> cbMode;

    @FXML
    Fretboard fbFretboard;

    public FretboardControler() {
        fretboardModel = new DefaultFretboardModel();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private static <T> void loadXMLTo(Class<T> cls, ChoiceBox cb, String path, Function<T, List> accessor) {

        try {
            URL location = FretboardControler.class.getClassLoader().getResource(path);

            Serializer serializer = new Persister();
            File source = new File(location.toURI());

            T list = serializer.read(cls, source);
            cb.getItems().addAll(accessor.apply(list));
            cb.getSelectionModel().selectFirst();

        } catch (URISyntaxException ex) {
            Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setData() {
        cbRootNote.getItems().addAll(FretboardModel.Note.values());
        cbRootNote.getSelectionModel().selectFirst();
        loadXMLTo(Tunes.class, cbTune, "tunes.xml", (t) -> t.getTunes());
        loadXMLTo(Modes.class, cbMode, "modes.xml", (m) -> m.getModes());

        /*defaultModel.addString(FretboardModel.Note.E);

        defaultModel.addNote(FretboardModel.Note.E, Color.RED);
        defaultModel.addNote(FretboardModel.Note.Gb, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.G, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.A, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.Bb, Color.BLUE);
        defaultModel.addNote(FretboardModel.Note.B, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.Db, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.D, Color.BLACK);*/
        fbFretboard.setModel(fretboardModel);

        cbTune.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tunes.Tune> observable, Tunes.Tune oldValue, Tunes.Tune newValue) -> {
            cbTune_onSelectedItemChanged(oldValue, newValue);
        });
        
        cbRootNote.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Note> observable, Note oldValue, Note newValue) -> {
            cbRootNote_onSelectedItemChanged(oldValue, newValue);
        });
        
        cbMode.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Mode> observable, Mode oldValue, Mode newValue) -> {
            cbMode_onSelectedItemChanged(oldValue, oldValue);
        });
        
        changeTune();
        changeScale();
    }

    protected void cbTune_onSelectedItemChanged(Tunes.Tune oldValue, Tunes.Tune tune) {
        changeTune();
        changeScale();
    }
    
    protected void cbRootNote_onSelectedItemChanged(Note oldValue, Note rootNote) {
        changeScale();
    }
    
    protected void cbMode_onSelectedItemChanged(Mode oldValue, Mode mode) {
        changeScale();
    }

    private void changeTune() {
        Tunes.Tune tune = cbTune.getSelectionModel().getSelectedItem();
        
        fretboardModel.setFretCount(tune.getFretcount());
        fretboardModel.getTunes().clear();
        fretboardModel.getTunes().addAll(tune.getStrings());
    }
    
    private void changeScale() {
        Note rootNote = cbRootNote.getSelectionModel().getSelectedItem();
        Mode mode = cbMode.getSelectionModel().getSelectedItem();

        fretboardModel.clearNotes();
        
        int interval = 0;
        for (boolean is : mode.getIntervals()) {
            if (is) {
                Note note = Note.valueOf((rootNote.interval() + interval) % 12);
                Color color = (interval == 0) ? Color.RED : (interval == 6) ? Color.BLUE : Color.BLACK;
                fretboardModel.addNote(note, color);
            }
            interval++;
        }

        fbFretboard.update();
    }

}
