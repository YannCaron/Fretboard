/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import fr.cyann.fretboard.data.Note;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author cyann
 */
public class Pianoboard extends BoardPane<PianoboardModel> {

    private static final int H_BORDER = 5;
    private static final int V_BORDER = 10;
    private static final int KEY_WIDTH = 40;
    private static final int KEY_HEIGHT = 150;
    private static final int BLACK_KEY_WIDTH = 20;
    private static final int BLACK_KEY_HEIGHT = 100;
    private static final int TIP_SIZE = 16;

    public Pianoboard() {
        super();
    }

    @Override
    PianoboardModel createModel() {
        return new DefaultPianoboardModel();
    }

    @Override
    int calculateWidth() {
        return H_BORDER * 2 + KEY_WIDTH * 7;
    }

    @Override
    int calculateHeight() {
        return V_BORDER * 2 + KEY_HEIGHT;
    }

    void drawTip(GraphicsContext gc, int x, int y) {
        gc.fillOval(x, y, TIP_SIZE, TIP_SIZE);
        gc.strokeOval(x, y, TIP_SIZE, TIP_SIZE);
    }

    @Override
    void draw(GraphicsContext gc, Canvas canvas, PianoboardModel model) {
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2d);

        // key
        for (int i = 0; i < 7; i++) {
            gc.strokeRect(H_BORDER + i * KEY_WIDTH, V_BORDER, KEY_WIDTH, KEY_HEIGHT);
        }

        // black key
        for (int i = 1; i < 7; i++) {
            if (i != 3) {
                gc.fillRect(H_BORDER + i * KEY_WIDTH - BLACK_KEY_WIDTH / 2,
                        V_BORDER, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
            }
        }

        // set tips
        int whiteKey = 0;
        gc.setFill(Color.WHITE);
        gc.setLineWidth(1d);
        for (int i = 0; i < 12; i++) {
            Note note = Note.valueOf((i + 3) % 12);
            if (getModel().isVisible(note)) {
                gc.setStroke(getModel().getTipColor(note));

                if (note.isWhite()) {
                    drawTip(gc,
                            H_BORDER + KEY_WIDTH / 2 - TIP_SIZE / 2 + whiteKey * KEY_WIDTH,
                            V_BORDER + (int) (KEY_HEIGHT * 0.80d));
                } else {
                    drawTip(gc,
                            H_BORDER - TIP_SIZE / 2 + whiteKey * KEY_WIDTH,
                            V_BORDER + (int) (BLACK_KEY_HEIGHT * 0.75d));
                }
            }
            if (note.isWhite()) {
                whiteKey++;
            }
        }

    }

}
