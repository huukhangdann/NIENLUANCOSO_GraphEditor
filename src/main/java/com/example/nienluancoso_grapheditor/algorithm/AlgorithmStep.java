package com.example.nienluancoso_grapheditor.algorithm;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.*;

public class AlgorithmStep {
    private final int lineNumber;
    private final String description;
    private final Vertex currentVertex;
    private final Vertex currentNeighbor;
    private final Edge currentEdge;
    private final List<Vertex> visitedVertices;
    private final List<Vertex> stack;
    private final List<Vertex> result;
    private final List<Vertex> sortedNeighbors;

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
