package com.example.nienluancoso_grapheditor.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Vertex> vertexList;
    private int nextVertexID = 1;

    public Graph() {
        this.vertexList = new ArrayList<>();
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public void setVertexList(List<Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    public void addVertex(Vertex vertex){
        vertexList.add(vertex);
        vertex.setId(nextVertexID++);
        System.out.println("Vertex added!");
    }


    @Override
    public String toString() {
        return "Graph{" +
                "vertexList=" + vertexList +
                '}';
    }
}
