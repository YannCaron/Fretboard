/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard;

import fr.cyann.fretboard.controls.Fretboard;
import fr.cyann.fretboard.controls.FretboardModel;
import fr.cyann.fretboard.controls.FretboardModel.Note;
import fr.cyann.fretboard.data.Modes;
import fr.cyann.fretboard.data.Tunes;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author cyann
 */
public class FretboardControler implements Initializable {

    @FXML
    public ChoiceBox<Note> cbRootNote;

    @FXML
    public ChoiceBox<Tunes.TuneElement> cbTune;

    @FXML
    public ChoiceBox<Tunes.TuneElement> cbMode;

    @FXML Fretboard fbFretboard;
    
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
        
        // System.out.println(fbFretboard.getModel());
        // TODO tobe continued
    }

}
