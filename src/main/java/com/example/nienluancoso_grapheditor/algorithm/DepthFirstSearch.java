package com.example.nienluancoso_grapheditor.algorithm;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.*;

public class DepthFirstSearch {
    public List<Vertex> execute(Graph graph, Vertex start){
        Set<Vertex> visitedVertex = new HashSet<>();
        List<Vertex> resultList = new ArrayList<>();
        Stack<Vertex> S = new Stack<>();
        S.push(start);
        List<Vertex> sortedVertices = new ArrayList<>(graph.getVertexList());
        sortedVertices.sort((v1, v2) ->
                v2.getLabel().compareTo(v1.getLabel()));
        while(!S.isEmpty()){
            Vertex u = S.pop();
            if(visitedVertex.contains(u)) continue;
            visitedVertex.add(u);
            resultList.add(u);
            for(Vertex v : sortedVertices){
                if(graph.isAdjacent(u, v) && !visitedVertex.contains(v)){
                    S.add(v);
                }
            }
        }
        return resultList;
    }
}
