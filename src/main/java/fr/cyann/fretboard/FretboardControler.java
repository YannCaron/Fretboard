/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard;

import fr.cyann.fretboard.controls.FretboardModel;
import fr.cyann.fretboard.controls.FretboardModel.Note;
import fr.cyann.fretboard.data.Tunes;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
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
    public ChoiceBox<Tunes.TuneElement> cbTune;

    @FXML
    public ChoiceBox<Note> cbRootNote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setData() {
        cbRootNote.getItems().addAll(FretboardModel.Note.values());
        cbRootNote.getSelectionModel().selectFirst();

        try {
            URL tunesLocation = this.getClass().getClassLoader().getResource("tunes.xml");

            Serializer serializer = new Persister();
            File source = new File(tunesLocation.toURI());

            Tunes tunes = serializer.read(Tunes.class, source);
            cbTune.getItems().addAll(tunes.getTunes());
            cbTune.getSelectionModel().selectFirst();
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FretboardControler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
