package com.example.nienluancoso_grapheditor.model;

import java.nio.file.WatchEvent;
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
                "vertexList=" + vertexList +
                '}';
    }

    public void addEdge(Edge edge){
        if(!isValid(edge))
            return;
        edgeList.add(edge);
        System.out.println("Edge created!");
    }

    public boolean isValid(Edge edge){
        if(edge.getFirstVertex() == edge.getSecondVertex())
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
            if((e.getFirstVertex() == edge.getFirstVertex() && (e.getSecondVertex() == edge.getSecondVertex())))
                return true;
            if((e.getFirstVertex() == edge.getSecondVertex()) && (e.getSecondVertex() == edge.getFirstVertex()))
                return true;
        }
        return false;
    }
}
