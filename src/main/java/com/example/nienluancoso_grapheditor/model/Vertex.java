package com.example.nienluancoso_grapheditor.model;

import com.example.nienluancoso_grapheditor.view.VertexView;

public class Vertex {
    private int id;
    private double x;
    private String label;
    private double y;

    public Vertex() {
    }

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void update(VertexView vertexView){
        this.x = vertexView.getCenterX();
        this.y = vertexView.getCenterY();
    }
}
