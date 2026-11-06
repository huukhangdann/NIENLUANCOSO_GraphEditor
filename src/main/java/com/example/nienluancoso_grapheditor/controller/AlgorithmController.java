package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.algorithm.ConnectedComponents;
import com.example.nienluancoso_grapheditor.algorithm.DepthFirstSearch;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmController {
    private final Graph graph;
    private final ConnectedComponents connectedComponents;
    private final DepthFirstSearch depthFirstSearch;

    public AlgorithmController(Graph graph){
        this.graph = graph;
        this.connectedComponents = new ConnectedComponents();
        this.depthFirstSearch = new DepthFirstSearch();
    }

    public List<List<Vertex>> findConnectedComponents() {
        return connectedComponents.execute(graph);
    }

    public List<Vertex> normalDFS(Vertex start){
        return depthFirstSearch.execute(graph, start);
    }

    public void stepByStepDFS(Vertex start){
        List<Vertex> result = new ArrayList<>();
        depthFirstSearch.printSteps(depthFirstSearch.executeWithSteps(graph, start, result));
    }
}
