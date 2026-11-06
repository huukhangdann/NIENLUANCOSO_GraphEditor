package com.example.nienluancoso_grapheditor.controller;

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

import java.util.List;

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
    private Button stepButton;
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
    private Label overallResultsLabel;
    @FXML
    private FlowPane componentDetailsPane;
    @FXML
    private HBox detailHeader;
    @FXML
    private SplitPane detailSplitPane;
    @FXML
    private Button toggleDetailButton;
    @FXML
    private SplitPane workspaceSplitPane;
    private final GraphViewportController graphViewportController = new GraphViewportController();
    private GraphEditorController graphEditorController;
    private AlgorithmController algorithmController;
    private AlgorithmPanelController algorithmPanelController;
    private final Group graphContent = new Group();
    private final InteractionBlocker interactionBlocker = new InteractionBlocker();
    private AlgorithmResultController algorithmResultController;

    @FXML
    private void initialize() {
        algorithmResultController = new AlgorithmResultController(
                resultPanel,
                componentCountValueLabel,
                overallResultsLabel,
                componentDetailsPane,
                detailHeader,
                detailSplitPane,
                toggleDetailButton,
                workspaceSplitPane
        );
        algorithmResultController.initializedView();
        algorithmPanelController = new AlgorithmPanelController(sourceComboBox, targetComboBox, algorithmComboBox,
                status -> algorithmResultController.setResultPanelVisible(status),
                status -> algorithmResultController.setExecutionTraceVisible(status));
        graphEditorController = new GraphEditorController(graphPane, graphContent, interactionBlocker, () -> {
            algorithmPanelController.refreshVertexChoices(graphEditorController.getGraph());
            algorithmPanelController.updateAlgorithmControls(graphEditorController.getGraph(), runButton, resetButton, stepButton, algorithmStatusLabel);
        }, this::showNotification);
        algorithmController = new AlgorithmController(graphEditorController.getGraph());
        setupButtonIcons();
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        graphPane.getChildren().addFirst(graphContent);
        graphContent.getTransforms().add(graphViewportController.getZoomTransform());

        // SetUp for UI/UX
        setupLeftNavigation();
        algorithmPanelController.setupAlgorithmSelection(graphEditorController.getGraph(), runButton, resetButton, stepButton, algorithmStatusLabel);
        algorithmPanelController.updateAlgorithmControls(graphEditorController.getGraph(), runButton, resetButton, stepButton, algorithmStatusLabel);

        graphEditorController.setUpGraphCanvas(notificationView, zoomSliderLabel);
        setUpInteractionBlocker();
        setUpNotificationView();
        setUpGraphPanelClip();

        graphViewportController.setUpZoomSlider(graphPane, interactionBlocker, zoomSlider, zoomSliderLabel);
        graphViewportController.setUpScrollZoom(graphPane, interactionBlocker, zoomSlider);
        graphViewportController.setUpPan(graphPane, interactionBlocker);
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
        runButton.setGraphic(createIcon(Feather.PLAY, 16));
        stepButton.setGraphic(createIcon(Feather.SKIP_FORWARD, 16));
        resetButton.setGraphic(createIcon(Feather.ROTATE_CCW, 16));

        // Trạng thái thuật toán
        algorithmStatusLabel.setGraphic(createIcon(Feather.ACTIVITY, 14));

        zoomOutButton.setGraphic(createIcon(Feather.ZOOM_OUT, 16));
        zoomInButton.setGraphic(createIcon(Feather.ZOOM_IN, 16));

        algorithmResultController.getOverallResultsLabel().setGraphic(createIcon(Feather.BAR_CHART_2, 16));

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
        if ("Count Component".equals(algorithm)) {
            List<List<Vertex>> components = algorithmController.findConnectedComponents();
            algorithmResultController.setTextComponentCountValueLabel(String.valueOf(components.size()));
            algorithmResultController.displayComponentDetails(components);
            graphEditorController.highlightComponents(components);
            algorithmStatusLabel.setText(
                    "Algorithm Status: Completed"
            );
        }
    }

    @FXML
    private void handleToggleDetailsAppCtrler() {
        algorithmResultController.handleToggleDetails();
    }

    public void deleteAll() {
        graphEditorController.deleteAll();
    }

    public void deleteSelected() {
        graphEditorController.deleteSelected();
    }

}


