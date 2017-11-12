/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.cyann.fretboard.controls;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

public abstract class BoardPane<M extends BoardModel> extends Pane {

    private final Canvas canvas;
    private M model;

    public BoardPane() {
        canvas = new Canvas(getWidth(), getHeight());
        getChildren().add(canvas);
        widthProperty().addListener(e -> canvas.setWidth(getWidth()));
        heightProperty().addListener(e -> canvas.setHeight(getHeight()));

        this.model = createModel();
    }

    public void update() {
        this.requestLayout();
    }

    private void setBounds() {
        setWidth(calculateWidth());
        setHeight(calculateHeight());
    }

    public M getModel() {
        return model;
    }
    
    public void setModel(M model) {
        this.model = model;
    }
    
    abstract M createModel();
    
    abstract int calculateWidth();

    abstract int calculateHeight();

    abstract void draw(GraphicsContext gc, Canvas canvas, M model);

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        setBounds();
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, getWidth(), getHeight());
        draw(gc, canvas, model);

    }

    public WritableImage takeSnapshop() {
        setBounds();
        WritableImage snapshot = new WritableImage((int) getWidth(), (int) getHeight());
        canvas.snapshot(null, snapshot);
        return snapshot;
    }
}
