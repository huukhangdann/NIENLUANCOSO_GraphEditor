package com.example.nienluancoso_grapheditor.algorithm;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;

import java.util.*;

import static com.example.nienluancoso_grapheditor.algorithm.AlgorithmStep.getAlgorithmStep;

/*
[ ------------------------ PSEUDO CODE ------------------------ ]

1.  Khởi tạo visited = {}, Khoi tạo Stack S = rỗng
2.  Push đỉnh start vào S
3.  WHILE S không rỗng:
4.      Lấy đỉnh trên cùng u = Pop(S)
5.      IF u đã được duyệt:
6.          Bỏ qua u
7.      Thêm u vào kết quả, đánh dấu u đã duyệt
8.      Sắp xếp các đỉnh kề v theo thứ tự nhãn tăng dần
9.      Xét các đỉnh kề v theo thứ tự ngược lại:
10.         IF v chưa được duyệt:
11.             Push v vào S
12. RETURN kết quả duyệt

 */

public class DepthFirstSearch {

    // Normal run
    public List<Vertex> execute(Graph graph, Vertex start){
        Set<Vertex> visitedVertex = new HashSet<>();
        List<Vertex> resultList = new ArrayList<>();
        Stack<Vertex> S = new Stack<>();
        S.push(start);
        List<Vertex> sortedVertices = new ArrayList<>(graph.getVertexList());
        sortedVertices.sort((v2, v1) ->
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


    // Step-by-step run
    public List<AlgorithmStep> executeWithSteps(Graph graph, Vertex start, List<Vertex> resultList) {
        if (graph == null || start == null || !graph.getVertexList().contains(start)) {
            throw new IllegalArgumentException("Đồ thị hoặc đỉnh bắt đầu không hợp lệ.");
        }

        List<AlgorithmStep> steps = new ArrayList<>();

        Set<Vertex> visitedVertex = new HashSet<>();
        Stack<Vertex> stack = new Stack<>();

        Vertex currentVertex = null;
        Vertex currentNeighbor = null;
        Edge currentEdge = null;
        List<Vertex> sortedNeighbors = new ArrayList<>();

        // 1: Khởi tạo visited và Stack
        steps.add(createStep( 1, "Khởi tạo tập visited và Stack rỗng",
                currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));

        stack.push(start);
        // 2: Push đỉnh bắt đầu vào Stack
        steps.add(createStep(2, "Đưa đỉnh bắt đầu " + start.getLabel() + " vào Stack", currentVertex,
                currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));

        // 3: Kiểm tra Stack
        while (true) {
            boolean hasElement = !stack.isEmpty();
            steps.add(createStep(3, hasElement ? "Stack không rỗng, tiếp tục duyệt" : "Stack rỗng, kết thúc vòng lặp",
                    currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors ));
            if (!hasElement) {
                break;
            }

            // 4: Pop đỉnh u
            Vertex u = stack.pop();
            currentVertex = u;
            sortedNeighbors.clear();
            steps.add(createStep(4, "Lấy đỉnh " + u.getLabel() + " ra khỏi Stack",
                    currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));

            // 5: Kiểm tra u đã được duyệt chưa
            boolean isVisited = visitedVertex.contains(u);
            steps.add(createStep(5, isVisited ? "Đỉnh " + u.getLabel() + " đã được duyệt" : "Đỉnh " + u.getLabel() + " chưa được duyệt",
                    currentVertex, currentNeighbor,  currentEdge, resultList, stack, sortedNeighbors));

            // 6: Bỏ qua nếu đã duyệt
            if (isVisited) {
                steps.add(createStep(6, "Bỏ qua đỉnh " + u.getLabel(),
                        currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));
                continue;
            }

            // 7: Đánh dấu đã duyệt và thêm vào kết quả
            visitedVertex.add(u);
            resultList.add(u);
            steps.add(createStep(7, "Đánh dấu đỉnh " + u.getLabel() + " đã duyệt và thêm vào kết quả",
                    currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));

            // 8: Lấy các đỉnh kề và sắp xếp tăng dần
            sortedNeighbors.clear();
            for (Vertex v : graph.getVertexList()) {
                if (graph.isAdjacent(u, v)) {
                    sortedNeighbors.add(v);
                }
            }
            sortedNeighbors.sort(
                    Comparator.comparing(Vertex::getLabel)
            );
            steps.add(createStep(8, "Sắp xếp các đỉnh kề của " + u.getLabel() + " theo nhãn tăng dần",
                    currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors ));

            // 9: Xét các đỉnh kề theo thứ tự ngược lại
            for (int i = sortedNeighbors.size() - 1; i >= 0; i--) {
                Vertex v = sortedNeighbors.get(i);
                currentNeighbor = v;
                currentEdge = getEdge(graph, u, v);
                steps.add(createStep(9, "Xét đỉnh kề " + v.getLabel(), currentVertex,
                        currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));

                // 10: Kiểm tra v đã được duyệt chưa
                boolean neighborVisited = visitedVertex.contains(v);

                steps.add(createStep(10, neighborVisited ? "Đỉnh " + v.getLabel() + " đã được duyệt" : "Đỉnh " + v.getLabel() + " chưa được duyệt",
                        currentVertex, currentNeighbor, currentEdge, resultList, stack, sortedNeighbors ));

                // 11: Push nếu chưa được duyệt
                if (!neighborVisited) {
                    stack.push(v);
                    steps.add(createStep(11, "Đưa đỉnh " + v.getLabel() + " vào Stack", currentVertex,
                            currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));
                }
            }
            currentNeighbor = null;
            currentEdge = null;
            sortedNeighbors.clear();
        }

        // 12: Trả về kết quả
        steps.add(createStep(12,"Hoàn thành thuật toán DFS", currentVertex,
                currentNeighbor, currentEdge, resultList, stack, sortedNeighbors));

        return List.copyOf(steps);
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

    private Edge getEdge(Graph graph, Vertex u, Vertex v) {
        for (Edge edge : graph.getEdgeList()) {
            if ((edge.getVertex1() == u && edge.getVertex2() == v)
                    || (edge.getVertex1() == v && edge.getVertex2() == u)) {
                return edge;
            }
        }
        return null;
    }

    public void printSteps(List<AlgorithmStep> steps) {
        for (int i = 0; i < steps.size(); i++) {
            AlgorithmStep step = steps.get(i);

            System.out.println("========== STEP " + (i + 1) + " ==========");
            System.out.println("Pseudocode line: " + step.getLineNumber());
            System.out.println("Description: " + step.getDescription());

            System.out.println("Current vertex: " + step.getCurrentVertex());
            System.out.println("Current neighbor: " + step.getCurrentNeighbor());
            System.out.println("Current edge: " + step.getCurrentEdge());

            System.out.println("Visited: " + step.getVisitedVertices());
            System.out.println("Stack (Top -> Bottom): " + step.getStack());
            System.out.println("Result: " + step.getResult());
            System.out.println("Sorted neighbors: " + step.getSortedNeighbors());

            System.out.println();
        }
    }
}
