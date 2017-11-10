/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard;

import fr.cyann.fretboard.controls.FretboardModel;
import fr.cyann.fretboard.controls.FretboardModel.Note;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author cyann
 */
public class FretboardControler implements Initializable {

    @FXML
    public ChoiceBox<Note> cbRootNote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setData() {
        cbRootNote.getItems().addAll(FretboardModel.Note.values());
        cbRootNote.getSelectionModel().selectFirst();
        
        URL tunesLocation = this.getClass().getClassLoader().getResource("tunes.xml");
        System.out.println(tunesLocation);
    }

}
