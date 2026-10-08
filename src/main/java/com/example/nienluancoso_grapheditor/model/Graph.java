package com.example.nienluancoso_grapheditor.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Vertex> vertexList;
    private List<Edge> edgeList;
    private int nextVertexID = 1;

    public Graph() {
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
    }

    public List<Vertex> getVertexList() {
        return vertexList;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setVertexList(List<Vertex> vertexList) {
        this.vertexList = vertexList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public void addVertex(Vertex vertex) {
        vertexList.add(vertex);
        vertex.setId(nextVertexID++);
        System.out.println("Vertex added!");
    }

    public int getSize() {
        return vertexList.size();
    }

    @Override
    public String toString() {
        return "Graph{" +
                "vertexList=" + vertexList + "\n"
                + "edgeList=" + edgeList + "}";
    }

    public boolean addEdge(Edge edge){
        if(!isValidEdge(edge))
            return false;
        edgeList.add(edge);
        System.out.println("Edge created!");
        return true;
    }

    public boolean isValidEdge(Edge edge){
        if(edge.getVertex1() == edge.getVertex2())
            return false;
        if(hasEdge(edge))
            return false;
        return true;
    }

    public void removeEdge(Edge edge){
        edgeList.remove(edge);
        System.out.println("Edge removed!");
    }

    // Check exist edge for undirected graph
    public boolean hasEdge(Edge edge){
        boolean flag = false;
        for(Edge e: edgeList){
            if((e.getVertex1() == edge.getVertex1() && (e.getVertex2() == edge.getVertex2())))
                return true;
            if((e.getVertex1() == edge.getVertex2()) && (e.getVertex2() == edge.getVertex1()))
                return true;
        }
        return false;
    }

    public void removeVertex(Vertex vertex){
        // Find relative edge
        edgeList.removeIf(edge -> edge.containsVertex(vertex));
        vertexList.remove(vertex);
    }
}
