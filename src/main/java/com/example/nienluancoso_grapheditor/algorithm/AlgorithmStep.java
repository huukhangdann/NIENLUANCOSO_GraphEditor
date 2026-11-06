package com.example.nienluancoso_grapheditor.algorithm;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.*;

public class AlgorithmStep {
    private int lineNumber;
    private String description;
    private Vertex currentVertex;
    private Vertex currentNeighbor;
    private Edge currentEdge;
    private List<Vertex> visitedVertices;
    private List<Vertex> stack;
    private List<Vertex> result;
    private List<Vertex> sortedNeighbors;

    public AlgorithmStep(
            int lineNumber,
            String description,
            Vertex currentVertex,
            Vertex currentNeighbor,
            Edge currentEdge,
            List<Vertex> visitedVertices,
            List<Vertex> stack,
            List<Vertex> result,
            List<Vertex> sortedNeighbors
    ) {
        this.lineNumber = lineNumber;
        this.description = Objects.requireNonNullElse(description, "");
        this.currentVertex = currentVertex;
        this.currentNeighbor = currentNeighbor;
        this.currentEdge = currentEdge;
        this.visitedVertices = List.copyOf(visitedVertices);
        this.stack = List.copyOf(stack);
        this.result = List.copyOf(result);
        this.sortedNeighbors = List.copyOf(sortedNeighbors);
    }

    public int getLineNumber() {
        return lineNumber;
    }
    public String getDescription() {
        return description;
    }
    public Vertex getCurrentVertex() {
        return currentVertex;
    }
    public Vertex getCurrentNeighbor() {
        return currentNeighbor;
    }
    public Edge getCurrentEdge() {
        return currentEdge;
    }
    public List<Vertex> getVisitedVertices() {
        return visitedVertices;
    }
    public List<Vertex> getStack() {
        return stack;
    }
    public List<Vertex> getResult() {
        return result;
    }
    public List<Vertex> getSortedNeighbors() {
        return sortedNeighbors;
    }


    private AlgorithmStep createStep(
            int lineNumber,
            String description,
            Vertex currentVertex,
            Vertex currentNeighbor,
            Edge currentEdge,
            List<Vertex> resultList,
            Stack<Vertex> stack,
            List<Vertex> sortedNeighbors
    ) {
        // Stack snapshot: Top -> Bottom
        return getAlgorithmStep(lineNumber, description, currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors);
    }

    static AlgorithmStep getAlgorithmStep(int lineNumber, String description, Vertex currentVertex, Vertex currentNeighbor, Edge currentEdge, List<Vertex> resultList, Stack<Vertex> stack, List<Vertex> sortedNeighbors) {
        List<Vertex> stackSnapshot = new ArrayList<>();
        for (int i = stack.size() - 1; i >= 0; i--) {
            stackSnapshot.add(stack.get(i));
        }
        return new AlgorithmStep(
                lineNumber,
                description,
                currentVertex,
                currentNeighbor,
                currentEdge,
                resultList,
                stackSnapshot,
                resultList,
                sortedNeighbors
        );
    }

}
