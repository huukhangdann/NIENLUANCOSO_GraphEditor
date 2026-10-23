package com.example.nienluancoso_grapheditor;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;


public class HelloController {
    @FXML
    public NotificationView notificationView;
    @FXML
    public BorderPane root;
    @FXML
    private VBox topContainer;
    @FXML
    private HBox botContainer;
    @FXML
    private Pane graphPane;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Label zoomSliderLabel;
    @FXML
    private ToggleGroup navigationGroup;
    @FXML
    private ToggleButton algorithmButtonNav;
    @FXML
    private ToggleButton graphButtonNav;
    @FXML
    private ToggleButton exerciseButtonNav;
    @FXML
    private VBox algorithmPanel;
    @FXML
    private VBox graphPanel;
    @FXML
    private VBox exercisePanel;
    @FXML
    private ComboBox<String> algorithmComboBox;
    @FXML
    private ComboBox<Vertex> sourceComboBox;
    @FXML
    private ComboBox<Vertex> targetComboBox;
    @FXML
    private Button runButton;
    @FXML
    private Button stepButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label algorithmStatusLabel;

    // Zoom Canvas
    private final Group graphContent = new Group();
    private double currentZoom = 1;
    private final static double MIN_ZOOM = 0.5;
    private final static double MAX_ZOOM = 2;
    private final double ZOOM_STEP = 0.1;
    private final Affine zoomTransform = new Affine();
    private Point2D pendingZoomAnchor = null;

    // Pan canvas
    private boolean isPanning = false;
    private Point2D lastPanPoint = null;

    private final Graph graph = new Graph();
    private Label label = new Label();
    private VertexView firstVertexClicked = null;
    private EdgeView selectedEdgeView = null;
    private Line previewLine = null;
    private final Map<Vertex, VertexView> vertexVertexViewMap = new HashMap<>();
    private final Map<Edge, EdgeView> edgeEdgeViewMap = new HashMap<>();
    private final Map<Edge, WeightView> edgeWeightViewMap = new HashMap<>();
    private final InteractionBlocker interactionBlocker = new InteractionBlocker();


    @FXML
    private void initialize() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        graphPane.getChildren().addFirst(graphContent);
        graphContent.getTransforms().add(zoomTransform);

        // SetUp for UI/UX
        setupLeftNavigation();
        setupAlgorithmSelection();
        updateAlgorithmControls();

        setUpGraphCanvas();
        setUpInteractionBlocker();
        setUpNotificationView();
        setUpGraphPanelClip();
        setUpZoomSlider();
        setUpScrollZoom();
        setUpPan();
    }

    private void updateAlgorithmControls() {
        String algorithm = algorithmComboBox.getValue();
        boolean hasVertices = !graph.getVertexList().isEmpty();
        boolean hasAlgorithm = algorithm != null;
        boolean needsSource = hasAlgorithm
                && !algorithm.equals("Count Component");
        boolean needsTarget = "Moore Dijkstra".equals(algorithm);
        boolean validSource = !needsSource
                || sourceComboBox.getValue() != null;
        boolean validTarget = !needsTarget
                || targetComboBox.getValue() != null;
        boolean canRun = hasVertices
                && hasAlgorithm
                && validSource
                && validTarget;
        runButton.setDisable(!canRun);
        stepButton.setDisable(!canRun);

        // Chưa có kết quả thuật toán để reset ở giai đoạn này.
        resetButton.setDisable(true);
        if (!hasAlgorithm) {
            algorithmStatusLabel.setText("Algorithm Status: Choose an algorithm");
        } else if (!hasVertices) {
            algorithmStatusLabel.setText("Algorithm Status: No vertices available");
        } else if (!validSource || !validTarget) {
            algorithmStatusLabel.setText("Algorithm Status: Select required vertices");
        } else {
            algorithmStatusLabel.setText("Algorithm Status: Ready");
        }
    }

    private void setupAlgorithmSelection() {
        sourceComboBox.setPromptText("Choose source Vertex");
        targetComboBox.setPromptText("Choose target Vertex");
        sourceComboBox.setItems(FXCollections.observableArrayList());
        targetComboBox.setItems(FXCollections.observableArrayList());
        StringConverter<Vertex> vertexConverter = new StringConverter<>() {
            @Override
            public String toString(Vertex vertex) {
                return vertex == null ? null : vertex.getLabel();
            }

            @Override
            public Vertex fromString(String label) {
                return null;
            }
        };
        sourceComboBox.setConverter(vertexConverter);
        targetComboBox.setConverter(vertexConverter);
        algorithmComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    sourceComboBox.setValue(null);
                    targetComboBox.setValue(null);

                    updateAlgorithmInputs();
                    updateAlgorithmControls();
                }
        );

        sourceComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateAlgorithmControls()
        );

        targetComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateAlgorithmControls()
        );

        updateAlgorithmInputs();
        refreshVertexChoices();
    }

    private void refreshVertexChoices() {
        Vertex selectedSource = sourceComboBox.getValue();
        Vertex selectedTarget = targetComboBox.getValue();
        sourceComboBox.getItems().setAll(graph.getVertexList());
        targetComboBox.getItems().setAll(graph.getVertexList());
        if (graph.getVertexList().contains(selectedSource)) {
            sourceComboBox.setValue(selectedSource);
        } else {
            sourceComboBox.setValue(null);
        }
        if (graph.getVertexList().contains(selectedTarget)) {
            targetComboBox.setValue(selectedTarget);
        } else {
            targetComboBox.setValue(null);
        }
    }

    private void updateAlgorithmInputs() {
        refreshVertexChoices();
        String algorithm = algorithmComboBox.getValue();
        boolean hasAlgorithm = algorithm != null;
        boolean needsSource = hasAlgorithm
                && !algorithm.equals("Count Component");
        boolean needsTarget = "Moore Dijkstra".equals(algorithm);
        sourceComboBox.setDisable(!needsSource);
        targetComboBox.setDisable(!needsTarget);
    }

    private void setupLeftNavigation() {
        navigationGroup.selectedToggleProperty().addListener(
                (observable, oldToggle, newToggle) -> {
                    if (newToggle == null) {
                        navigationGroup.selectToggle(
                                oldToggle != null ? oldToggle : algorithmButtonNav
                        );
                        return;
                    }
                    switchLeftPanel(newToggle);
                }
        );
        
        navigationGroup.selectToggle(algorithmButtonNav);
    }

    private void switchLeftPanel(Toggle selectedToggle) {
        setPanelVisible(
                algorithmPanel,
                selectedToggle == algorithmButtonNav
        );
        setPanelVisible(
                graphPanel,
                selectedToggle == graphButtonNav
        );
        setPanelVisible(
                exercisePanel,
                selectedToggle == exerciseButtonNav
        );
    }

    private void setPanelVisible(VBox panel, boolean visible) {
        panel.setVisible(visible);
        panel.setManaged(visible);
    }

    private void setUpPan() {
        // Mouse Pressed
        graphPane.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                event -> {
                    if (event.getButton() != MouseButton.PRIMARY) {
                        return;
                    }
                    if (event.getTarget() != graphPane) {
                        return;
                    }
                    if (interactionBlocker.isBlocking()) {
                        return;
                    }
                    isPanning = true;
                    lastPanPoint = graphPane.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );
                });
        // Mouse Dragged
        graphPane.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                event -> {
                    if (!isPanning || lastPanPoint == null) {
                        return;
                    }
                    graphPane.setCursor(Cursor.CLOSED_HAND);
                    Point2D currentPoint = graphPane.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );
                    double dx = currentPoint.getX() - lastPanPoint.getX();
                    double dy = currentPoint.getY() - lastPanPoint.getY();
                    zoomTransform.setTx(zoomTransform.getTx() + dx);
                    zoomTransform.setTy(zoomTransform.getTy() + dy);
                    lastPanPoint = currentPoint;
                    event.consume();
                }
        );
        // Mouse Released
        graphPane.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                event -> {
                    if (event.getButton() != MouseButton.PRIMARY) {
                        return;
                    }
                    graphPane.setCursor(Cursor.DEFAULT);
                    isPanning = false;
                    lastPanPoint = null;
                }
        );
    }


    private void setUpScrollZoom() {
        graphPane.addEventFilter(
                ScrollEvent.SCROLL,
                event -> {
                    if (event.getDeltaY() == 0) {
                        return;
                    }
                    if(interactionBlocker.isBlocking()) return;
                    double direction =
                            event.getDeltaY() > 0 ? 1 : -1;
                    double newZoom = Math.clamp(
                            zoomSlider.getValue()
                                    + direction * ZOOM_STEP,
                            MIN_ZOOM,
                            MAX_ZOOM
                    );
                    if (newZoom == zoomSlider.getValue()) {
                        event.consume();
                        return;
                    }
                    // Tọa độ con trỏ trong graphPane.
                    Point2D anchor = graphPane.sceneToLocal(
                            event.getSceneX(),
                            event.getSceneY()
                    );
                    // Truyền điểm neo cho listener của slider.
                    pendingZoomAnchor = anchor;
                    zoomSlider.setValue(newZoom);
                    event.consume();
                }
        );
    }


    private void setUpZoomSlider() {
        zoomSlider.valueProperty().addListener((
                observable, oldValue, newValue) -> {
            double zoom = newValue.doubleValue();

            if(interactionBlocker.isBlocking()) {
                return;
            }

            Point2D anchor = pendingZoomAnchor;
            pendingZoomAnchor = null;

            if (anchor == null) {
                anchor = new Point2D(
                        graphPane.getWidth() / 2.0,
                        graphPane.getHeight() / 2.0
                );
            }
            applyZoom(zoom, anchor);


            zoomSliderLabel.setText(
                    String.format("%.0f%%", zoom * 100)
            );
        });
    }

    private void setUpGraphPanelClip() {
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(graphPane.widthProperty());
        clip.heightProperty().bind(graphPane.heightProperty());

        graphPane.setClip(clip);
    }

    private void setUpNotificationView() {
        notificationView.layoutXProperty().bind(
                graphPane.widthProperty()
                        .subtract(notificationView.widthProperty())
                        .subtract(20)
        );

        notificationView.setLayoutY(20);
    }

    private void setUpInteractionBlocker() {
        interactionBlocker.prefHeightProperty().bind(graphPane.heightProperty());
        interactionBlocker.prefWidthProperty().bind(graphPane.widthProperty());
        graphPane.getChildren().add(interactionBlocker);
        interactionBlocker.block(false);
        zoomSlider.disableProperty()
                .bind(interactionBlocker.blockingProperty());
    }

    public void setUpGraphCanvas() {
        notificationView.setMouseTransparent(true);
        zoomSliderLabel.getStyleClass().add("zoom-slider-label");
        // Text Test Canvas
        onTestTextCanvas(true);

        // Pane Click
        handleGraphPaneClicked();

        // Mouse moved for preview line
        handleMouseMoved();

    }

    private void onTestTextCanvas(boolean value) {
        if (value) {
            label.setText("Create your first vertex here!".toUpperCase());
            label.setId("testLayoutText");
            label.layoutXProperty().bind(graphPane.widthProperty().subtract(label.widthProperty()).divide(2));
            label.layoutYProperty().bind(graphPane.heightProperty().subtract(label.heightProperty()).divide(2));
            graphPane.getChildren().add(label);
        } else {
            label.setVisible(false);
            label.setManaged(false);
        }
    }

    public void handleGraphPaneClicked() {
        graphPane.setOnMouseClicked(event -> {
            if (graph.getSize() == 0)
                onTestTextCanvas(false);
            if (previewLine != null) {
                removePreviewLine();
            }
            if (interactionBlocker.isBlocking()) {
                event.consume();
                return;
            }
            if (selectedEdgeView != null) {
                selectedEdgeView.onSelect(false);
                selectedEdgeView = null;
            }
            if (!VertexView.hasSelectedVertex()) {
                if ((event.getClickCount() == 2) && (event.isStillSincePress())) {
                    // Change coordinate to graphContent
                    Point2D eventPoint = graphContent.sceneToLocal(
                            new Point2D(event.getSceneX(), event.getSceneY()));
                    double x = eventPoint.getX();
                    double y = eventPoint.getY();
                    Vertex vertex = new Vertex(x, y);
                    graph.addVertex(vertex);
                    VertexView vertexView = new VertexView(vertex);
                    vertexVertexViewMap.put(vertex, vertexView);
                    setVertexViewCallback(vertexView);
                    // Làm mới lựa chọn trong comboBox UI
                    refreshVertexChoices();
                    // Kiểm tra lại điều kiện run
                    updateAlgorithmControls();

                    // VertexView clicked
                    vertexView.handleVertexClicked();

                    // Vertex dragged
                    vertexView.handleVertexDragged(graphContent);


                    graphContent.getChildren().add(vertexView);
                }
            } else {
                VertexView.getSelectedVertex().onSelect(false);
            }
        });
    }

    private void handleMouseMoved() {
        graphPane.setOnMouseMoved(event -> {
            if (previewLine != null) {
                Point2D mouse = graphContent.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
                previewLine.setEndX(mouse.getX());
                previewLine.setEndY(mouse.getY());
            }
        });
    }

    private void createPreviewLine() {
        previewLine = new Line();
        previewLine.getStyleClass().add("preview-line");
        previewLine.setStartX(firstVertexClicked.getCenterX());
        previewLine.setStartY(firstVertexClicked.getCenterY());
        previewLine.setEndX(firstVertexClicked.getCenterX());
        previewLine.setEndY(firstVertexClicked.getCenterY());
        graphContent.getChildren().add(previewLine);
        previewLine.toBack();
        VertexView.setOnBlockDragging(true);
    }

    public void createEdge(Vertex u, Vertex v) {
        Edge edge = new Edge(u, v);
        if (graph.addEdge(edge)) {
            // Init and connect model-view
            EdgeView edgeView = new EdgeView(vertexVertexViewMap.get(u), vertexVertexViewMap.get(v), edge);
            setEdgeCallBack(edgeView);
            edgeEdgeViewMap.put(edge, edgeView);
            WeightView weightView = new WeightView(edge, edgeView, graphContent);
            edgeWeightViewMap.put(edge, weightView);

            setWeightViewCallback(weightView);
            graphContent.getChildren().addAll(edgeView, weightView);
            edgeView.toBack();
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            weightView.toFront();
        }
    }

    private void setWeightViewCallback(WeightView weightView) {
        weightView.setOnWeightEntered(() -> { //callback entered weight
            interactionBlocker.toBack();
            interactionBlocker.block(false);
            interactionBlocker.setVisible(false);
        });
        weightView.setOnEditingWeight(() -> {
            interactionBlocker.setVisible(true);
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            weightView.toFront();
        });
        weightView.setOnNotification(this::showNotification);
        interactionBlocker.setOnClickedBlocker(this::showNotification);
    }

    private void setVertexViewCallback(VertexView vertexView) {
        // set callback when vertexView clicked
        vertexView.setOnClicked(vertexClicked -> {
            // first Vertex clicked
            if (firstVertexClicked == null) {
                firstVertexClicked = vertexClicked;
                if (selectedEdgeView != null) {
                    selectedEdgeView.onSelect(false);
                    selectedEdgeView = null;
                }
                // Create preview line
                createPreviewLine();
            }
            // second Vertex clicked
            else {
                createEdge(firstVertexClicked.getVertex(), vertexClicked.getVertex());
                removePreviewLine();
                vertexClicked.onSelect(false);
                firstVertexClicked = null;
            }
        });
        vertexView.setOnEditingLabel(() -> {
            interactionBlocker.setVisible(true);
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            vertexView.toFront();

        });
        vertexView.setOnLabelEntered(() -> {
            interactionBlocker.toBack();
            interactionBlocker.block(false);
            interactionBlocker.setVisible(false);

            refreshVertexChoices();
        });
        vertexView.setOnNotification(this::showNotification);
        interactionBlocker.setOnClickedBlocker(this::showNotification);
    }


    private void setEdgeCallBack(EdgeView edgeView) {
        edgeView.setSelectEdgeViewCallback((selectedEV) -> {
            // select edge when selecting vertex
            this.selectedEdgeView = selectedEV;
            if (firstVertexClicked != null) {
                firstVertexClicked.onSelect(false);
                removePreviewLine();
            }
        });
    }

    public void removePreviewLine() {
        if (previewLine != null) {
            graphContent.getChildren().remove(previewLine);
            previewLine = null;
        }
        firstVertexClicked = null;
        VertexView.setOnBlockDragging(false);

    }

    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            deleteSelected();
        }
    }

    public void showNotification(String message) {
        notificationView.show(message);
    }

    public void deleteSelected() {
        if(interactionBlocker.isBlocking()) return;
        if (firstVertexClicked != null) {
            deleteSelectedVertex();
        } else if (selectedEdgeView != null) {
            deleteSelectedEdge();
        }

    }

    private void deleteSelectedEdge() {
        graph.removeEdge(selectedEdgeView.getEdge());
        graphContent.getChildren().remove(selectedEdgeView);
        graphContent.getChildren().remove(selectedEdgeView.getWeightView());
        edgeWeightViewMap.remove(selectedEdgeView.getEdge());
        edgeEdgeViewMap.remove(selectedEdgeView.getEdge());
        selectedEdgeView = null;
    }

    private void deleteSelectedVertex() {
        for (Edge e : graph.getEdgeList()) {
            if (e.containsVertex(firstVertexClicked.getVertex())) {
                graphContent.getChildren().remove(edgeEdgeViewMap.get(e));
                edgeEdgeViewMap.remove(e);
                graphContent.getChildren().remove(edgeWeightViewMap.get(e));
                edgeWeightViewMap.remove(e);
            }
        }
        graph.removeVertex(firstVertexClicked.getVertex());
        // Làm mới lựa chọn trong comboBox UI
        refreshVertexChoices();
        // Tính lại điều kiện Run
        updateAlgorithmControls();

        vertexVertexViewMap.remove(firstVertexClicked.getVertex());
        graphContent.getChildren().remove(firstVertexClicked);
        firstVertexClicked.onSelect(false);
        firstVertexClicked = null;
        if (previewLine != null)
            removePreviewLine();
    }

    public void deleteAll(){
        graphContent.getChildren().clear();
        graph.makeNull();
        // Làm mới lựa chọn trong comboBox UI
        refreshVertexChoices();
        // Tính lại điều kiện Run
        updateAlgorithmControls();
    }

    // Zoom Methods
    public void applyZoom(double zoomValue) {
        Point2D center = new Point2D(
                graphPane.getWidth() / 2.0,
                graphPane.getHeight() / 2.0
        );
        applyZoom(zoomValue, center);
    }

    private void applyZoom(double zoomValue, Point2D anchor) {
        double value = Math.clamp(
                zoomValue, MIN_ZOOM, MAX_ZOOM
        );

        double anchorX = anchor.getX();
        double anchorY = anchor.getY();

        double ratio = value / currentZoom;

        double newTranslateX =
                anchorX - ratio * (anchorX - zoomTransform.getTx());

        double newTranslateY =
                anchorY - ratio * (anchorY - zoomTransform.getTy());

        zoomTransform.setMxx(value);
        zoomTransform.setMyy(value);
        zoomTransform.setTx(newTranslateX);
        zoomTransform.setTy(newTranslateY);

        currentZoom = value;
    }

    public void handleZoomIn() {
        zoomSlider.setValue(zoomSlider.getValue() + ZOOM_STEP);
    }

    public void handleZoomOut() {
        zoomSlider.setValue(zoomSlider.getValue() - ZOOM_STEP);
    }

    public void handleResetZoom() {
        zoomSlider.setValue(1);
        zoomTransform.setTx(0);
        zoomTransform.setTy(0);
    }

    public void handleRunButton(){
        if ("Count Component".equals(algorithmComboBox.getValue())) {
            // test algorithm
            System.out.println("COMPONENT COUNT: " + graph.findConnectedComponent().size());
            System.out.println("COMPONENT " + graph.findConnectedComponent());
        }
    }
}

