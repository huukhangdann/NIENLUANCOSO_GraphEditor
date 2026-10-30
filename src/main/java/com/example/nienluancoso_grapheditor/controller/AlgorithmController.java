package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.algorithm.ConnectedComponents;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.List;

public class AlgorithmController {
    private final Graph graph;
    private final ConnectedComponents connectedComponents;

    public AlgorithmController(Graph graph){
        this.graph = graph;
        this.connectedComponents = new ConnectedComponents();
    }

    public List<List<Vertex>> findConnectedComponents() {
        return connectedComponents.execute(graph);
    }
}
