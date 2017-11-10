/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author cyann
 */
public class Fretboard extends Pane {

    private static final int LEFT = 25;
    private static final int H_BORDER = 5;
    private static final int V_BORDER = 10;
    private static final int STRING_SEPARATION = 25;
    private static final int FRET_SEPARATION = 40;
    private static final int FONT_HEIGHT = 14;
    private static final int TIP_SIZE = 16;
    private static final int SMALL_TIP_SIZE = 10;

    private final Canvas canvas;
    private FretboardModel model;

    public Fretboard() {
        canvas = new Canvas(getWidth(), getHeight());
        getChildren().add(canvas);
        widthProperty().addListener(e -> canvas.setWidth(getWidth()));
        heightProperty().addListener(e -> canvas.setHeight(getHeight()));

        DefaultFretboardModel defaultModel = new DefaultFretboardModel(24);
        defaultModel.addString(FretboardModel.Note.E);
        defaultModel.addString(FretboardModel.Note.A);
        defaultModel.addString(FretboardModel.Note.D);
        defaultModel.addString(FretboardModel.Note.G);
        defaultModel.addString(FretboardModel.Note.B);
        defaultModel.addString(FretboardModel.Note.E);

        defaultModel.addNote(FretboardModel.Note.E, Color.RED);
        defaultModel.addNote(FretboardModel.Note.Gb, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.G, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.A, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.Bb, Color.BLUE);
        defaultModel.addNote(FretboardModel.Note.B, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.Db, Color.BLACK);
        defaultModel.addNote(FretboardModel.Note.D, Color.BLACK);
        model = defaultModel;

    }

    public void setModel(FretboardModel model) {
        this.model = model;
    }

    public FretboardModel getModel() {
        return model;
    }

    private void drawMark(GraphicsContext gc, int fret, int size) {
        gc.fillRect(
                H_BORDER + LEFT + fret * FRET_SEPARATION - FRET_SEPARATION / 2 - size / 2, V_BORDER + 10,
                size, (model.stringCount() - 1) * STRING_SEPARATION - 20);

    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, getWidth(), getHeight());

        // draw head
        gc.setFont(new Font("Arial", FONT_HEIGHT));
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());

        gc.setFill(Color.BLACK);
        gc.fillRect(
                H_BORDER + LEFT - 3, V_BORDER - 1,
                5, (model.stringCount() - 1) * STRING_SEPARATION + 2);

        for (int s = 0; s < model.stringCount(); s++) {
            gc.fillText(model.getTipNote((model.stringCount() - 1) - s, 0),
                    H_BORDER, V_BORDER + s * STRING_SEPARATION + fm.getAscent() / 2);
        }

        // set frets
        gc.setFill(Color.LIGHTGRAY);
        for (int f = 1; f <= model.fretCount(); f++) {
            gc.fillRect(
                    H_BORDER + LEFT + f * FRET_SEPARATION, V_BORDER,
                    2, (model.stringCount() - 1) * STRING_SEPARATION);
        }

        // marks
        drawMark(gc, 3, 15);
        drawMark(gc, 5, 15);
        drawMark(gc, 7, 15);
        drawMark(gc, 9, 15);
        drawMark(gc, 12, 25);
        drawMark(gc, 15, 15);
        drawMark(gc, 17, 15);
        drawMark(gc, 19, 15);
        drawMark(gc, 21, 15);
        drawMark(gc, 24, 25);

        // strings
        gc.setFill(Color.BLACK);
        for (int s = 0; s < model.stringCount(); s++) {
            gc.fillRect(
                    H_BORDER + LEFT, V_BORDER + s * STRING_SEPARATION - 1,
                    H_BORDER + (model.fretCount()) * FRET_SEPARATION, 2);
        }

        gc.setFill(Color.WHITE);
        for (int s = 0; s < model.stringCount(); s++) {
            int ds = model.stringCount() - 1 - s;
            if (model.isVisible(s, 0)) {
                gc.setStroke(model.getTipColor(s, 0));
                gc.strokeOval(
                        H_BORDER + LEFT - SMALL_TIP_SIZE - 2,
                        V_BORDER - SMALL_TIP_SIZE / 2 + ds * STRING_SEPARATION,
                        SMALL_TIP_SIZE, SMALL_TIP_SIZE);
            }
            for (int f = 1; f <= model.fretCount(); f++) {
                if (model.isVisible(s, f)) {
                    gc.fillOval(
                            H_BORDER + LEFT + FRET_SEPARATION / 2 - TIP_SIZE / 2 + (f - 1) * FRET_SEPARATION,
                            V_BORDER - TIP_SIZE / 2 + ds * STRING_SEPARATION,
                            TIP_SIZE, TIP_SIZE);
                    gc.setStroke(model.getTipColor(s, f));
                    gc.strokeOval(
                            H_BORDER + LEFT + FRET_SEPARATION / 2 - TIP_SIZE / 2 + (f - 1) * FRET_SEPARATION,
                            V_BORDER - TIP_SIZE / 2 + ds * STRING_SEPARATION,
                            TIP_SIZE, TIP_SIZE);
                }
            }
        }

        // Paint your custom image here:
        //gc.drawImage(someImage, 0, 0);
    }
}
