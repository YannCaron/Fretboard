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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author cyann
 */
public class Fretboard extends BoardPane<FretboardModel> {

    private static final int LEFT = 25;
    private static final int H_BORDER = 5;
    private static final int V_BORDER = 10;
    private static final int STRING_SEPARATION = 25;
    private static final int FRET_SEPARATION = 40;
    private static final int FONT_HEIGHT = 14;
    private static final int TIP_SIZE = 16;
    private static final int SMALL_TIP_SIZE = 10;

    public Fretboard() {
        super();
    }

    private static void drawMark(GraphicsContext gc, int fretCount, int stringCount, int size) {
        gc.fillRect(
                H_BORDER + LEFT + fretCount * FRET_SEPARATION - FRET_SEPARATION / 2 - size / 2, V_BORDER + 10,
                size, (stringCount - 1) * STRING_SEPARATION - 20);

    }

    @Override
    FretboardModel createModel() {
        return new EmptyFretboardModel();
    }
    
    @Override
    int calculateWidth() {
        return H_BORDER * 4 + LEFT + getModel().fretCount() * FRET_SEPARATION;
    }

    @Override
    int calculateHeight() {
        return V_BORDER * 2 + (getModel().stringCount() - 1) * STRING_SEPARATION;
    }

    @Override
    void draw(GraphicsContext gc, Canvas canvas, FretboardModel model) {

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
        drawMark(gc, 3, model.stringCount(), 15);
        drawMark(gc, 5, model.stringCount(), 15);
        drawMark(gc, 7, model.stringCount(), 15);
        drawMark(gc, 9, model.stringCount(), 15);
        drawMark(gc, 12, model.stringCount(), 25);
        drawMark(gc, 15, model.stringCount(), 15);
        drawMark(gc, 17, model.stringCount(), 15);
        drawMark(gc, 19, model.stringCount(), 15);
        drawMark(gc, 21, model.stringCount(), 15);
        drawMark(gc, 24, model.stringCount(), 25);

        // strings
        gc.setFill(Color.BLACK);
        for (int s = 0; s < model.stringCount(); s++) {
            gc.fillRect(
                    H_BORDER + LEFT, V_BORDER + s * STRING_SEPARATION - 1,
                    V_BORDER + (model.fretCount()) * FRET_SEPARATION, 2);
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
    }

}
