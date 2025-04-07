import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import java.util.*;

public class KanjiQuizApp extends Application {
    
    private Map<String, String[]> kanjiData = new HashMap<>();
    private List<String> kanjiList = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int totalQuestions = 0;
    
    // UI Components
    private Label kanjiLabel;
    private Label meaningLabel;
    private Label historyLabel;
    private Label scoreLabel;
    private TextField answerField;
    private Button submitButton;
    private Button nextButton;
    private VBox historyBox;
    private StackPane cardPane;
    private boolean showingMeaning = false;
    
    @Override
    public void start(Stage primaryStage) {
        initializeKanjiData();
        
        // Main Layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(
            Color.web("#f5f5f5"), CornerRadii.EMPTY, Insets.EMPTY));
        
        // Header
        Label title = new Label("Japanese Kanji Quiz");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#333333"));
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);
        
        // Center - Flashcard
        setupFlashCard();
        root.setCenter(cardPane);
        
        // Bottom - Controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        answerField = new TextField();
        answerField.setPromptText("Enter meaning...");
        answerField.setPrefWidth(200);
        
        submitButton = createStyledButton("Submit", "#4CAF50");
        nextButton = createStyledButton("Next", "#2196F3");
        nextButton.setDisable(true);
        
        controls.getChildren().addAll(answerField, submitButton, nextButton);
        root.setBottom(controls);
        
        // Score Display
        scoreLabel = new Label("Score: 0/0");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        BorderPane.setAlignment(scoreLabel, Pos.TOP_RIGHT);
        root.setRight(scoreLabel);
        
        // Event Handlers
        submitButton.setOnAction(e -> checkAnswer());
        nextButton.setOnAction(e -> nextQuestion());
        answerField.setOnAction(e -> checkAnswer());
        
        // Scene
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Kanji Quiz");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Start first question
        showQuestion();
    }
    
    private void initializeKanjiData() {
        kanjiData.put("日", new String[]{"sun/day", 
            "Derived from a pictograph of the sun (○ with a dot)"});
        kanjiData.put("月", new String[]{"moon/month", 
            "Originally a crescent moon shape (🌙)"});
        kanjiData.put("山", new String[]{"mountain", 
            "Stylized drawing of three mountain peaks"});
        kanjiData.put("川", new String[]{"river", 
            "Represents flowing water lines"});
        kanjiData.put("木", new String[]{"tree/wood", 
            "Depicts a tree with roots and branches"});
        kanjiData.put("火", new String[]{"fire", 
            "Resembles flames rising"});
        kanjiData.put("人", new String[]{"person", 
            "Simplified from a stick figure"});
        
        kanjiList.addAll(kanjiData.keySet());
        Collections.shuffle(kanjiList);
        totalQuestions = kanjiList.size();
    }
    
    private void setupFlashCard() {
        cardPane = new StackPane();
        cardPane.setPrefSize(400, 300);
        
        // Kanji Display
        kanjiLabel = new Label();
        kanjiLabel.setFont(Font.font("Noto Sans JP", FontWeight.BOLD, 120));
        kanjiLabel.setTextFill(Color.web("#333333"));
        
        // Meaning Display (hidden initially)
        meaningLabel = new Label();
        meaningLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        meaningLabel.setTextFill(Color.web("#4CAF50"));
        meaningLabel.setVisible(false);
        
        // History Display
        historyLabel = new Label();
        historyLabel.setFont(Font.font("Arial", 14));
        historyLabel.setTextFill(Color.web("#666666"));
        historyLabel.setWrapText(true);
        historyLabel.setMaxWidth(350);
        
        historyBox = new VBox(10, meaningLabel, new Separator(), historyLabel);
        historyBox.setAlignment(Pos.CENTER);
        historyBox.setVisible(false);
        
        // Card Background
        Region card = new Region();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        card.setEffect(new DropShadow(10, Color.gray(0.4)));
        card.setPrefSize(400, 300);
        
        cardPane.getChildren().addAll(card, kanjiLabel, historyBox);
        cardPane.setOnMouseClicked(e -> flipCard());
    }
    
    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                       "-fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 4;");
        button.setEffect(new DropShadow(3, Color.gray(0.5)));
        return button;
    }
    
    private void showQuestion() {
        if (currentIndex >= kanjiList.size()) {
            endQuiz();
            return;
        }
        
        String kanji = kanjiList.get(currentIndex);
        kanjiLabel.setText(kanji);
        meaningLabel.setText("Meaning: " + kanjiData.get(kanji)[0]);
        historyLabel.setText("History: " + kanjiData.get(kanji)[1]);
        
        answerField.clear();
        answerField.setDisable(false);
        submitButton.setDisable(false);
        nextButton.setDisable(true);
        showingMeaning = false;
        historyBox.setVisible(false);
        kanjiLabel.setVisible(true);
    }
    
    private void flipCard() {
        if (showingMeaning) return;
        
        RotateTransition rt = new RotateTransition(Duration.millis(300), cardPane);
        rt.setFromAngle(0);
        rt.setToAngle(90);
        rt.setOnFinished(e -> {
            kanjiLabel.setVisible(false);
            historyBox.setVisible(true);
            
            RotateTransition rt2 = new RotateTransition(Duration.millis(300), cardPane);
            rt2.setFromAngle(90);
            rt2.setToAngle(0);
            rt2.play();
        });
        rt.play();
        showingMeaning = true;
    }
    
    private void checkAnswer() {
        String kanji = kanjiList.get(currentIndex);
        String correctAnswer = kanjiData.get(kanji)[0];
        String userAnswer = answerField.getText().trim().toLowerCase();
        
        if (userAnswer.isEmpty()) return;
        
        answerField.setDisable(true);
        submitButton.setDisable(true);
        nextButton.setDisable(false);
        
        if (correctAnswer.toLowerCase().contains(userAnswer)) {
            score++;
            scoreLabel.setText("Score: " + score + "/" + totalQuestions);
            answerField.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
        } else {
            answerField.setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;");
        }
        
        flipCard();
    }
    
    private void nextQuestion() {
        answerField.setStyle("");
        currentIndex++;
        showQuestion();
    }
    
    private void endQuiz() {
        double percentage = (double) score / totalQuestions * 100;
        String result = String.format("Quiz Complete!\nScore: %d/%d (%.0f%%)", 
                                    score, totalQuestions, percentage);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Results");
        alert.setHeaderText(result);
        alert.setContentText(percentage > 70 ? "Great job! 🎌" : "Keep practicing!");
        alert.showAndWait();
        
        // Reset quiz
        currentIndex = 0;
        score = 0;
        Collections.shuffle(kanjiList);
        showQuestion();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
