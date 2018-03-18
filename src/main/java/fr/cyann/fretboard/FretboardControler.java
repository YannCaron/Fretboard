/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard;

import fr.cyann.fretboard.controls.DefaultFretboardModel;
import fr.cyann.fretboard.controls.DefaultPianoboardModel;
import fr.cyann.fretboard.controls.Fretboard;
import fr.cyann.fretboard.controls.Pianoboard;
import fr.cyann.fretboard.data.Note;
import fr.cyann.fretboard.data.Modes;
import fr.cyann.fretboard.data.Modes.Mode;
import fr.cyann.fretboard.data.Tunes;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author cyann
 */
public class FretboardControler implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FretboardControler.class.getName());
    public static final int FRET_COUNT = 24;

    private final DefaultFretboardModel fretboardModel;
    private final DefaultPianoboardModel pianoboardModel;

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

    @FXML
    Pianoboard pbPianoboard;

    public FretboardControler() {
        fretboardModel = new DefaultFretboardModel();
        pianoboardModel = new DefaultPianoboardModel();
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

    private static InputStream getRessource(String path) {
        return FretboardControler.class.getClassLoader().getResourceAsStream(path);
    }

    private static <T> void loadSystemXMLTo(Class<T> cls, ChoiceBox cb, String path, Function<T, List> accessor) {

        try {
            Serializer serializer = new Persister();

            T list = serializer.read(cls, getRessource(path));
            cb.getItems().addAll(accessor.apply(list));
            cb.getSelectionModel().selectFirst();
        } catch (Exception ex) {
            Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        cbRootNote.getItems().addAll(Note.values());
        cbRootNote.getSelectionModel().selectFirst();
        loadSystemXMLTo(Tunes.class, cbTune, "tunes.xml", (t) -> t.getTunes());
        loadSystemXMLTo(Modes.class, cbMode, "modes.xml", (m) -> m.getModes());
        loadXMLTo(Tunes.class, cbTune, "tunes-user.xml", (t) -> t.getTunes());
        loadXMLTo(Modes.class, cbMode, "modes-user.xml", (m) -> m.getModes());

        fbFretboard.setModel(fretboardModel);
        pbPianoboard.setModel(pianoboardModel);

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

    private void writeSnapshot(WritableImage snapshot, File file) {
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            LOGGER.log(Level.INFO, String.format("File [%s] created successfully.", file.toString()));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    protected void btPng_onReleased(MouseEvent event) {
        String fileName = String.format("%s %s - %s.png",
                cbRootNote.getSelectionModel().selectedItemProperty().getValue().toString(),
                cbMode.getSelectionModel().selectedItemProperty().getValue().toString(),
                cbTune.getSelectionModel().selectedItemProperty().getValue().toString());
        writeSnapshot(fbFretboard.takeSnapshop(), new File(fileName));

        fileName = String.format("%s %s - Piano.png",
                cbRootNote.getSelectionModel().selectedItemProperty().getValue().toString(),
                cbMode.getSelectionModel().selectedItemProperty().getValue().toString());
        writeSnapshot(pbPianoboard.takeSnapshop(), new File(fileName));

    }

    private void changeTune() {
        Tunes.Tune tune = cbTune.getSelectionModel().getSelectedItem();

        fretboardModel.setFretCount(tune.getFretcount());
        fretboardModel.getTunes().clear();
        fretboardModel.getTunes().addAll(Arrays.asList(tune.getNotes()));
    }

    private void changeScale() {
        Note rootNote = cbRootNote.getSelectionModel().getSelectedItem();
        Mode mode = cbMode.getSelectionModel().getSelectedItem();

        fretboardModel.clearNotes();
        pianoboardModel.clearNotes();

        int intervalFromRoot = 0;
        for (int interval : mode.getIntervals()) {
            Note note = Note.valueOf((rootNote.interval() + intervalFromRoot) % 12);
            Color color = Color.BLACK;
            if (intervalFromRoot == 0) {
                color = Color.RED;
            } else if (intervalFromRoot == 6) {
                color = Color.BLUE;
            } else if (intervalFromRoot == 3) {
                color = Color.BLUEVIOLET;
            } else if (intervalFromRoot == 4) {
                color = Color.DARKVIOLET;
            } else if (intervalFromRoot == 7) {
                color = Color.BROWN;
            }

            fretboardModel.addNote(note, color);
            pianoboardModel.addNote(note, color);

            intervalFromRoot += interval;
        }

        fbFretboard.update();
        pbPianoboard.update();
    }

}
