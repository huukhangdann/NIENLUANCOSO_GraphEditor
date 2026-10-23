package com.example.nienluancoso_grapheditor.model;

import java.util.*;

public class Graph {
    private List<Vertex> vertexList;
    private List<Edge> edgeList;
    private int nextVertexID = 1;
    private final static Set<String> usedLabels = new HashSet<>();

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
        vertex.setLabel(generateNextLabel());
        usedLabels.add(vertex.getLabel());
        System.out.println("Vertex added!");
    }

    public void makeNull(){
        this.vertexList = new ArrayList<>();
        this.edgeList = new ArrayList<>();
        int nextVertexID = 1;
        usedLabels.clear();
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
        usedLabels.remove(vertex.getLabel());
    }

    private String generateNextLabel() {
        int index = 0;
        while (true) {
            String label = generateLabel(index);
            if (!usedLabels.contains(label)) {
                return label;
            }
            index++;
        }
    }

    private String generateLabel(int index) {
        StringBuilder label = new StringBuilder();

        do {
            label.insert(0, (char) ('A' + index % 26));
            index = index / 26 - 1;
        } while (index >= 0);

        return label.toString();
    }

    public static Set<String> getUsedLabels(){
        return usedLabels;
    }

    // Algorithms methods

    public int degree(Vertex u){
        int degree = 0;
        for(Edge e : edgeList){
            if(e.getVertex1()==u || e.getVertex2()==u)
                degree++;
        }
        return degree;
    }

    public boolean isAdjacent(Vertex u, Vertex v){
        for(Edge e : edgeList){
            if((e.getVertex1() == u && e.getVertex2() == v) ||
            (e.getVertex1() == v && e.getVertex2() == u)){
                return true;
            }
        }
        return false;
    }

    public List<Vertex> DFS(Vertex start){
        Set<Vertex> visitedVertex = new HashSet<>();
        List<Vertex> resultList = new ArrayList<>();
        Stack<Vertex> S = new Stack<>();
        S.push(start);
        vertexList.sort((o2, o1) -> o1.getLabel().compareTo(o2.getLabel()));
        System.out.println(" after sort: " + vertexList);
        while(!S.isEmpty()){
            Vertex u = S.pop();
            if(visitedVertex.contains(u)) continue;
            visitedVertex.add(u);
            resultList.add(u);
            for(Vertex v : vertexList){
                if(isAdjacent(u, v) && !visitedVertex.contains(v)){
                    S.add(v);
                }
            }
        }
        return resultList;
    }

    public List<List<Vertex>> findConnectedComponent(){
        return null;
    }

}
