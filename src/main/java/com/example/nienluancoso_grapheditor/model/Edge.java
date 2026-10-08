package com.example.nienluancoso_grapheditor.model;

public class Edge {
    private Vertex vertex1;
    private Vertex vertex2;
    private int weight;

    public Edge(Vertex vertex1, Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
      //  this.weight = weight;
    }

    public Vertex getVertex1() {
        return vertex1;
    }

    public void setVertex1(Vertex vertex1) {
        this.vertex1 = vertex1;
    }

    public Vertex getVertex2() {
        return vertex2;
    }

    public void setVertex2(Vertex vertex2) {
        this.vertex2 = vertex2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean containsVertex(Vertex vertex){
        return (vertex1==vertex || vertex2==vertex);
    }

    @Override
    public String toString() {
        return "Edge{" +
                vertex1.getId() + vertex2.getId() +
                ", weight=" + weight +
                '}';
    }
}
