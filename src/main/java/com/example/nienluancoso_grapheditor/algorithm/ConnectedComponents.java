package com.example.nienluancoso_grapheditor.algorithm;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConnectedComponents {

    private final DepthFirstSearch dfs = new DepthFirstSearch();

    public List<List<Vertex>> execute(Graph graph){
        Set<Vertex> visitedVertex = new HashSet<>();
        List<List<Vertex>> resultList = new ArrayList<>();
        for(Vertex u : graph.getVertexList()){
            if(visitedVertex.contains(u)) continue;
            List<Vertex> thisComponent = dfs.execute(graph, u);
            visitedVertex.addAll(thisComponent);
            resultList.add(thisComponent);
        }
        return resultList;
    }
}
