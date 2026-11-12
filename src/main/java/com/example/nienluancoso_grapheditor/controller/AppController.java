package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.algorithm.AlgorithmStep;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {
    @FXML
    public NotificationView notificationView;
    @FXML
    public BorderPane root;
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
    private Button runStepByStepButton;
    @FXML
    private Button prevStepButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button nextStepButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label algorithmStatusLabel;
    @FXML
    private Label featurePaneLabel;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private VBox resultPanel;
    @FXML
    private Label componentCountValueLabel;
    @FXML
    private FlowPane componentDetailsPane;
    @FXML
    private SplitPane detailSplitPane;
    @FXML
    private TabPane resultTabPane;
    @FXML
    private Tab overviewTab;
    @FXML
    private Tab executionTraceTab;
    @FXML
    private SplitPane workspaceSplitPane;
    @FXML
    private Label resultMetricTitleLabel;
    @FXML
    private VBox componentDetailsPanel;
    @FXML
    private VBox componentCountPanel;
    @FXML
    private ScrollPane componentDetailsScrollPane;
    @FXML
    private Label stepDescriptionLabel;
    @FXML
    private Label stackValueLabel;
    @FXML
    private Label visitedValueLabel;
    @FXML
    private Label traversalValueLabel;
    @FXML
    private Label pseudoLine1;
    @FXML
    private Label pseudoLine2;
    @FXML
    private Label pseudoLine3;
    @FXML
    private Label pseudoLine4;
    @FXML
    private Label pseudoLine5;
    @FXML
    private Label pseudoLine6;
    @FXML
    private Label pseudoLine7;
    @FXML
    private Label pseudoLine8;
    @FXML
    private Label pseudoLine9;
    @FXML
    private Label pseudoLine10;
    @FXML
    private Label pseudoLine11;
    @FXML
    private Label pseudoLine12;


    private final GraphViewportController graphViewportController = new GraphViewportController();
    private GraphEditorController graphEditorController;
    private AlgorithmController algorithmController;
    private AlgorithmPanelController algorithmPanelController;
    private final Group graphContent = new Group();
    private final InteractionBlocker interactionBlocker = new InteractionBlocker();
    private AlgorithmResultController algorithmResultController;
    private AlgorithmTraceController algorithmTraceController;

    @FXML
    private void initialize() {
        algorithmResultController = new AlgorithmResultController(resultPanel, resultTabPane, overviewTab, executionTraceTab, resultMetricTitleLabel,
                componentCountValueLabel, componentDetailsPane, workspaceSplitPane, componentCountPanel, componentDetailsScrollPane);
        algorithmResultController.initializeView();
        algorithmPanelController = new AlgorithmPanelController(sourceComboBox, targetComboBox, algorithmComboBox, status -> algorithmResultController.setResultPanelVisible(status),
                () -> algorithmResultController.showOverviewTab(), componentDetailsPanel);
        graphEditorController = new GraphEditorController(graphPane, graphContent, interactionBlocker, () -> {
            algorithmPanelController.refreshVertexChoices(graphEditorController.getGraph());
            algorithmPanelController.updateAlgorithmControls(graphEditorController.getGraph(), runButton, resetButton, algorithmStatusLabel);
        }, this::showNotification);
        algorithmController = new AlgorithmController(graphEditorController.getGraph());
        setupButtonIcons();
        Map<Integer, Label> integerLabelHashMap = initPseudocodeMap();
        algorithmTraceController = new AlgorithmTraceController(stepDescriptionLabel, stackValueLabel, visitedValueLabel, traversalValueLabel, integerLabelHashMap);

        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        graphPane.getChildren().addFirst(graphContent);
        graphContent.getTransforms().add(graphViewportController.getZoomTransform());

        // SetUp for UI/UX
        setupLeftNavigation();
        algorithmPanelController.setupAlgorithmSelection(algorithmResultController, graphEditorController.getGraph(), runButton, resetButton, algorithmStatusLabel);
        algorithmPanelController.updateAlgorithmControls(graphEditorController.getGraph(), runButton, resetButton, algorithmStatusLabel);

        graphEditorController.setUpGraphCanvas(notificationView, zoomSliderLabel);
        setUpInteractionBlocker();
        setUpNotificationView();
        setUpGraphPanelClip();

        graphViewportController.setUpZoomSlider(graphPane, interactionBlocker, zoomSlider, zoomSliderLabel);
        graphViewportController.setUpScrollZoom(graphPane, interactionBlocker, zoomSlider);
        graphViewportController.setUpPan(graphPane, interactionBlocker);
    }

    private Map<Integer, Label> initPseudocodeMap() {
        Map<Integer, Label> pseudoMap = new HashMap<>();
        pseudoMap.put(1, pseudoLine1);
        pseudoMap.put(2, pseudoLine2);
        pseudoMap.put(3, pseudoLine3);
        pseudoMap.put(4, pseudoLine4);
        pseudoMap.put(5, pseudoLine5);
        pseudoMap.put(6, pseudoLine6);
        pseudoMap.put(7, pseudoLine7);
        pseudoMap.put(8, pseudoLine8);
        pseudoMap.put(9, pseudoLine9);
        pseudoMap.put(10, pseudoLine10);
        pseudoMap.put(11, pseudoLine11);
        pseudoMap.put(12, pseudoLine12);
        return pseudoMap;
    }

    private void handleKeyPressed(KeyEvent keyEvent) {
        graphEditorController.handleKeyPressed(keyEvent);
    }

    // Zoom Handle method
    public void handleZoomIn() {
        graphViewportController.zoomIn(zoomSlider);
    }

    public void handleZoomOut() {
        graphViewportController.zoomOut(zoomSlider);
    }

    public void handleResetZoom() {
        graphViewportController.resetZoom(zoomSlider);
    }


    private FontIcon createIcon(Feather iconType, int size) {
        FontIcon icon = new FontIcon(iconType);
        icon.setIconSize(size);
        icon.setIconColor(Color.web("#52525B"));
        return icon;
    }

    private void setupButtonIcons() {
        // =========================
        // 2. Thanh điều hướng bên trái
        // =========================
        featurePaneLabel.setGraphic(createIcon(Feather.LAYERS, 16));

        // =========================
        // 4. Các nút điều khiển thuật toán
        // =========================
        runStepByStepButton.setGraphic(createIcon(Feather.ACTIVITY, 16));
        runButton.setGraphic(createIcon(Feather.PLAY, 16));
        resetButton.setGraphic(createIcon(Feather.ROTATE_CCW, 16));
        prevStepButton.setGraphic(createIcon(Feather.SKIP_BACK, 16));
        pauseButton.setGraphic(createIcon(Feather.PAUSE, 16));
        nextStepButton.setGraphic(createIcon(Feather.SKIP_FORWARD, 16));

        // Trạng thái thuật toán
        algorithmStatusLabel.setGraphic(createIcon(Feather.ACTIVITY, 14));

        zoomOutButton.setGraphic(createIcon(Feather.ZOOM_OUT, 16));
        zoomInButton.setGraphic(createIcon(Feather.ZOOM_IN, 16));
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

    public void showNotification(String message) {
        notificationView.show(message);
    }

    public void handleRunButton() {
        String algorithm = algorithmComboBox.getValue();
        componentDetailsPanel.setVisible(true);
        if ("Count Component".equals(algorithm)) {
            List<List<Vertex>> components = algorithmController.findConnectedComponents();
            algorithmResultController.setTextComponentCountValueLabel(String.valueOf(components.size()));
            algorithmResultController.displayComponentDetails(components);
            graphEditorController.highlightComponents(components);
            algorithmStatusLabel.setText(
                    "Algorithm Status: Completed"
            );
            algorithmResultController.showOverviewTab();
        }
        else if ("DFS".equals(algorithm)){
            Vertex sourceVertex = sourceComboBox.getValue();
            if(sourceVertex!=null){
                List<Vertex> traversalList = algorithmController.normalDFS(sourceVertex);
                algorithmResultController.displayDFSResult(traversalList);
                graphEditorController.highlightDFS(traversalList);
                algorithmResultController.setTextComponentCountValueLabel(String.valueOf(traversalList.size()));
                algorithmResultController.showOverviewTab();
            }
        }
    }

    @FXML
    private void handleRunStepByStepButton(){
        String algorithm = algorithmComboBox.getValue();
        if ("DFS".equals(algorithm)){
            Vertex sourceVertex = sourceComboBox.getValue();
            if(sourceVertex!=null){
                List<Vertex> traversalList = algorithmController.stepByStepDFS(sourceVertex);
                algorithmController.resetStep();
                algorithmResultController.showExecutionTraceTab();
                algorithmController.nextStep();
                algorithmController.nextStep();
                algorithmController.nextStep();
                algorithmController.nextStep();
                AlgorithmStep currentStep = algorithmController.getCurrentStep();
                algorithmTraceController.displayStep(currentStep);
            }
        }
    }

    public void deleteAll() {
        graphEditorController.deleteAll();
    }

    public void deleteSelected() {
        graphEditorController.deleteSelected();
    }

}


