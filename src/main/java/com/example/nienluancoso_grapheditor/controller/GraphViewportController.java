package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.view.InteractionBlocker;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Affine;

public class GraphViewportController {
    private boolean isPanning = false;
    private Point2D lastPanPoint = null;
    private final Affine zoomTransform = new Affine();
    private final static double MIN_ZOOM = 0.5;
    private final static double MAX_ZOOM = 2;
    private double currentZoom = 1;
    private final double ZOOM_STEP = 0.1;
    private Point2D pendingZoomAnchor = null;

    public Affine getZoomTransform() {
        return zoomTransform;
    }

    public void setUpPan(Pane graphPane, InteractionBlocker interactionBlocker) {
        // Mouse Pressed
        graphPane.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                event -> {
                    if (event.getButton() != MouseButton.PRIMARY) {
                        return;
                    }
                    if (event.getTarget() != graphPane) {
                        return;
                    }
                    if (interactionBlocker.isBlocking()) {
                        return;
                    }
                    isPanning = true;
                    lastPanPoint = graphPane.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );
                });
        // Mouse Dragged
        graphPane.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                event -> {
                    if (!isPanning || lastPanPoint == null) {
                        return;
                    }
                    graphPane.setCursor(Cursor.CLOSED_HAND);
                    Point2D currentPoint = graphPane.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );
                    double dx = currentPoint.getX() - lastPanPoint.getX();
                    double dy = currentPoint.getY() - lastPanPoint.getY();
                    zoomTransform.setTx(zoomTransform.getTx() + dx);
                    zoomTransform.setTy(zoomTransform.getTy() + dy);
                    lastPanPoint = currentPoint;
                    event.consume();
                }
        );
        // Mouse Released
        graphPane.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                event -> {
                    if (event.getButton() != MouseButton.PRIMARY) {
                        return;
                    }
                    graphPane.setCursor(Cursor.DEFAULT);
                    isPanning = false;
                    lastPanPoint = null;
                }
        );
    }

    public void applyZoom(Pane graphPane, double zoomValue) {
        Point2D center = new Point2D(
                graphPane.getWidth() / 2.0,
                graphPane.getHeight() / 2.0
        );
        applyZoom(zoomValue, center);
    }

    public void applyZoom(double zoomValue, Point2D anchor) {
        double value = Math.clamp(
                zoomValue, MIN_ZOOM, MAX_ZOOM
        );
        double anchorX = anchor.getX();
        double anchorY = anchor.getY();

        double ratio = value / currentZoom;

        double newTranslateX =
                anchorX - ratio * (anchorX - zoomTransform.getTx());

        double newTranslateY =
                anchorY - ratio * (anchorY - zoomTransform.getTy());
        zoomTransform.setMxx(value);
        zoomTransform.setMyy(value);
        zoomTransform.setTx(newTranslateX);
        zoomTransform.setTy(newTranslateY);
        currentZoom = value;
    }

    public void setUpScrollZoom(Pane graphPane, InteractionBlocker interactionBlocker, Slider zoomSlider) {
        graphPane.addEventFilter(
                ScrollEvent.SCROLL,
                event -> {
                    if (event.getDeltaY() == 0) {
                        return;
                    }
                    if (interactionBlocker.isBlocking()) return;
                    double direction =
                            event.getDeltaY() > 0 ? 1 : -1;
                    double newZoom = Math.clamp(
                            zoomSlider.getValue()
                                    + direction * ZOOM_STEP,
                            MIN_ZOOM,
                            MAX_ZOOM
                    );
                    if (newZoom == zoomSlider.getValue()) {
                        event.consume();
                        return;
                    }
                    // Tọa độ con trỏ trong graphPane.
                    // Truyền điểm neo cho listener của slider.
                    pendingZoomAnchor = graphPane.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );
                    zoomSlider.setValue(newZoom);
                    event.consume();
                }
        );
    }

    public void setUpZoomSlider(Pane graphPane, InteractionBlocker interactionBlocker, Slider zoomSlider, Label zoomSliderLabel) {
        zoomSlider.valueProperty().addListener((
                observable, oldValue, newValue) -> {
            double zoom = newValue.doubleValue();
            if (interactionBlocker.isBlocking()) {
                return;
            }
            Point2D anchor = pendingZoomAnchor;
            pendingZoomAnchor = null;

            if (anchor == null) {
                anchor = new Point2D(
                        graphPane.getWidth() / 2.0,
                        graphPane.getHeight() / 2.0
                );
            }
            applyZoom(zoom, anchor);
            zoomSliderLabel.setText(
                    String.format("%.0f%%", zoom * 100)
            );
        });
    }

    public void zoomIn(Slider zoomSlider) {
        zoomSlider.setValue(zoomSlider.getValue() + ZOOM_STEP);
    }

    public void zoomOut(Slider zoomSlider) {
        zoomSlider.setValue(zoomSlider.getValue() - ZOOM_STEP);
    }

    public void resetZoom(Slider zoomSlider) {
        zoomSlider.setValue(1);
        zoomTransform.setTx(0);
        zoomTransform.setTy(0);
    }
}
