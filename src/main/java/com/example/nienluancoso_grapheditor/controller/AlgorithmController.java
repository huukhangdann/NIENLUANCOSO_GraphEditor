package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.algorithm.AlgorithmStep;
import com.example.nienluancoso_grapheditor.algorithm.AlgorithmStepManager;
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
    private final AlgorithmStepManager stepManager = new AlgorithmStepManager();

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

    public List<Vertex> stepByStepDFS(Vertex start){
        List<Vertex> resultList = new ArrayList<>();
        List<AlgorithmStep> steps = depthFirstSearch.executeWithSteps(graph, start, resultList);
        stepManager.setSteps(steps);
        return resultList;
    }

    public AlgorithmStep getCurrentStep() {return stepManager.getCurrentStep();}
    public boolean hasNextStep(){ return stepManager.hasNext();}
    public boolean hasPreviousStep(){ return stepManager.hasPrevious();}
    public boolean nextStep() {return stepManager.next();}
    public boolean previousStep() {return stepManager.previous();}
    public void resetStep() {stepManager.reset();}
    public int getCurrentStepIndex(){return stepManager.getCurrentStepIndex();}
    public int getTotalSteps() {return stepManager.getTotalSteps();}

}
