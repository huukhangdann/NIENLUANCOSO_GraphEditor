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
    private SplitPane detailSplitPane;
    @FXML
    private Button toggleDetailButton;
    private boolean detailsVisible = true;
    @FXML
    private VBox resultPanel;
    @FXML
    private HBox detailHeader;
    @FXML
    private SplitPane workspaceSplitPane;
    @FXML
    private Label featurePaneLabel;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Label componentCountValueLabel;
    @FXML
    private Label overallResultsLabel;
    @FXML
    private FlowPane componentDetailsPane;

    private final GraphViewportController graphViewportController = new GraphViewportController();
    private GraphEditorController graphEditorController;

    private final Group graphContent = new Group();
    private final InteractionBlocker interactionBlocker = new InteractionBlocker();

    @FXML
    private void initialize() {
        graphEditorController = new GraphEditorController(graphPane, graphContent, interactionBlocker, () -> {
            refreshVertexChoices();
            updateAlgorithmControls();
        }, this::showNotification);

        detailsVisible = true;
        workspaceSplitPane.getItems().remove(resultPanel);
        setExecutionTraceVisible(false);
        updateToggleIcon(true);
        setupButtonIcons();
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        graphPane.getChildren().addFirst(graphContent);
        graphContent.getTransforms().add(graphViewportController.getZoomTransform());

        // SetUp for UI/UX
        setupLeftNavigation();
        setupAlgorithmSelection();
        updateAlgorithmControls();

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
    public void handleZoomIn(){
        graphViewportController.zoomIn(zoomSlider);
    }
    public void handleZoomOut(){
        graphViewportController.zoomOut(zoomSlider);
    }
    public void handleResetZoom(){
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

        overallResultsLabel.setGraphic(createIcon(Feather.BAR_CHART_2, 16));

    }

    private void updateAlgorithmControls() {
        String algorithm = algorithmComboBox.getValue();
        boolean hasVertices = !graphEditorController.getGraph().getVertexList().isEmpty();
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

                    setResultPanelVisible(newValue != null);

                    boolean showTrace = !"Count Component".equals(newValue);
                    setExecutionTraceVisible(showTrace);

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

    private void setResultPanelVisible(boolean visible) {
        if (visible) {
            if (!workspaceSplitPane.getItems().contains(resultPanel)) {
                workspaceSplitPane.getItems().add(resultPanel);
            }

            workspaceSplitPane.setDividerPositions(0.67);
            workspaceSplitPane.setDividerPositions(0.67);
        } else {
            workspaceSplitPane.getItems().remove(resultPanel);
        }
    }

    private void refreshVertexChoices() {
        Vertex selectedSource = sourceComboBox.getValue();
        Vertex selectedTarget = targetComboBox.getValue();
        sourceComboBox.getItems().setAll(graphEditorController.getGraph().getVertexList());
        targetComboBox.getItems().setAll(graphEditorController.getGraph().getVertexList());
        if (graphEditorController.getGraph().getVertexList().contains(selectedSource)) {
            sourceComboBox.setValue(selectedSource);
        } else {
            sourceComboBox.setValue(null);
        }
        if (graphEditorController.getGraph().getVertexList().contains(selectedTarget)) {
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
            List<List<Vertex>> components = graphEditorController.getGraph().findConnectedComponent();
            componentCountValueLabel.setText(
                    String.valueOf(components.size())
            );
            displayComponentDetails(components);

            // Hiển thị các đỉnh thuộc từng thành phần
            StringBuilder details = new StringBuilder();

            for (int i = 0; i < components.size(); i++) {
                List<Vertex> component = components.get(i);
                double hue = (i * 137.508) % 360;
                double saturation = 0.23 + (i % 3) * 0.06;
                double brightness = 0.98;
                for (Vertex vertex : component) {
                    VertexView view = graphEditorController.getVertexView(vertex);
                    if (view != null) {
                        view.changeColorCircle(
                                Color.hsb(hue, saturation, brightness)
                        );
                    }
                }
            }
            algorithmStatusLabel.setText(
                    "Algorithm Status: Completed"
            );
        }
    }

    @FXML
    private void handleToggleDetails() {
        detailsVisible = !detailsVisible;
        detailHeader.setVisible(detailsVisible);
        detailHeader.setManaged(detailsVisible);
        detailSplitPane.setVisible(detailsVisible);
        detailSplitPane.setManaged(detailsVisible);
        updateToggleIcon(detailsVisible);
    }

    private void updateToggleIcon(boolean visible) {
        FontIcon icon;
        if (visible) {
            icon = new FontIcon(Feather.CHEVRON_DOWN);
        } else {
            icon = new FontIcon(Feather.CHEVRON_RIGHT);
        }
        icon.setIconSize(14);
        icon.setIconColor(javafx.scene.paint.Color.valueOf("#52525b"));
        toggleDetailButton.setText("");
        toggleDetailButton.setGraphic(icon);
    }

    private void displayComponentDetails(List<List<Vertex>> components) {
        componentDetailsPane.getChildren().clear();
        for (int i = 0; i < components.size(); i++) {
            List<Vertex> component = components.get(i);
            double hue = (i * 137.508) % 360;
            double saturation = 0.23 + (i % 3) * 0.06;
            double brightness = 0.98;
            Color componentColor = Color.hsb(hue, saturation, brightness);
            VBox card = new VBox(10);
            card.setPadding(new Insets(12));
            card.setMinWidth(120);
            card.setMaxWidth(220);
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #E4E4E7;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;"
            );
            Label title = new Label("Component " + (i + 1));
            title.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #3F3F46;"
            );
            FlowPane verticesPane = new FlowPane();
            verticesPane.setHgap(6);
            verticesPane.setVgap(6);
            String colorHex = String.format(
                    "#%02X%02X%02X",
                    (int) Math.round(componentColor.getRed() * 255),
                    (int) Math.round(componentColor.getGreen() * 255),
                    (int) Math.round(componentColor.getBlue() * 255)
            );

            for (Vertex vertex : component) {
                Label vertexLabel = new Label(vertex.getLabel());
                vertexLabel.setMinSize(30, 30);
                vertexLabel.setPrefSize(30, 30);
                vertexLabel.setAlignment(Pos.CENTER);
                vertexLabel.setStyle(
                        "-fx-background-color: " + colorHex + ";" +
                                "-fx-background-radius: 50%;" +
                                "-fx-text-fill: #27272A;" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;"
                );
                verticesPane.getChildren().add(vertexLabel);
            }
            // Cho FlowPane tự xuống hàng theo chiều rộng card
            verticesPane.setPrefWrapLength(190);

            // Card tự tính chiều cao theo số hàng đỉnh
            card.setMinWidth(220);
            card.setPrefWidth(220);
            card.setMaxWidth(220);

            // Không cố định chiều cao card
            card.setMinHeight(Region.USE_PREF_SIZE);
            card.setMaxHeight(Region.USE_PREF_SIZE);

            // Hiển thị trực tiếp danh sách đỉnh bên trong card
            card.getChildren().setAll(title, verticesPane);
            componentDetailsPane.getChildren().add(card);
        }
    }

    private void setExecutionTraceVisible(boolean visible) {
        detailHeader.setVisible(visible);
        detailHeader.setManaged(visible);

        detailSplitPane.setVisible(visible);
        detailSplitPane.setManaged(visible);

        detailsVisible = visible;
        updateToggleIcon(visible);
    }

    public void deleteAll() {
        graphEditorController.deleteAll();
    }

    public void deleteSelected() {
        graphEditorController.deleteSelected();
    }

}


