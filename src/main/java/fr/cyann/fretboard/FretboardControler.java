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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
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
    public Button btPng;

    @FXML
    Fretboard fbFretboard;

    public FretboardControler() {
        fretboardModel = new DefaultFretboardModel();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    private static void copyRessourceToFile(String path) throws IOException {
            InputStream fi = FretboardControler.class.getClassLoader().getResourceAsStream(path);
            Files.copy(fi, Paths.get(path));
    }
    
    private static File getLazyRessource(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            copyRessourceToFile(path);
        }
        return file;
    }

    private static <T> void loadXMLTo(Class<T> cls, ChoiceBox cb, String path, Function<T, List> accessor) {

        try {
            Serializer serializer = new Persister();

            T list = serializer.read(cls, getLazyRessource(path));
            cb.getItems().addAll(accessor.apply(list));
            cb.getSelectionModel().selectFirst();

        } catch (Exception ex) {
            Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex);
            
            try {
                copyRessourceToFile(path);
            } catch (IOException ex1) {
                Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void setData() {
        cbRootNote.getItems().addAll(FretboardModel.Note.values());
        cbRootNote.getSelectionModel().selectFirst();
        loadXMLTo(Tunes.class, cbTune, "tunes.xml", (t) -> t.getTunes());
        loadXMLTo(Modes.class, cbMode, "modes.xml", (m) -> m.getModes());

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

        btPng.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            btPng_onReleased(event);
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

    protected void btPng_onReleased(MouseEvent event) {
        WritableImage snapshot = fbFretboard.takeSnapshop();

        String fileName = String.format("%s %s - %s.png",
                cbRootNote.getSelectionModel().selectedItemProperty().getValue().toString(),
                cbMode.getSelectionModel().selectedItemProperty().getValue().toString(),
                cbTune.getSelectionModel().selectedItemProperty().getValue().toString());
        File file = new File(fileName);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
        } catch (Exception s) {
        }
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
