package com.example.nienluancoso_grapheditor.model;

public class Edge {
    private Vertex firstVertex;
    private Vertex secondVertex;
    private int weight;

    public Edge(Vertex firstVertex, Vertex secondVertex, int weight) {
        this.firstVertex = firstVertex;
        this.secondVertex = secondVertex;
        this.weight = weight;
    }

    public Vertex getFirstVertex() {
        return firstVertex;
    }

    public void setFirstVertex(Vertex firstVertex) {
        this.firstVertex = firstVertex;
    }

    public Vertex getSecondVertex() {
        return secondVertex;
    }

    public void setSecondVertex(Vertex secondVertex) {
        this.secondVertex = secondVertex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
