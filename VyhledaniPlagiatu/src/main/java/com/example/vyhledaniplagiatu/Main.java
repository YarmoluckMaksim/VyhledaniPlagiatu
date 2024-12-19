package com.example.vyhledaniplagiatu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);}

    @Override
    public void start(Stage primaryStage) {





        primaryStage.setTitle("Vyhledání plagiátu\n"); // main window

        TextArea mainTextArea = new TextArea();   // field for input main text
        mainTextArea.setPromptText("Enter the main text here...");
        mainTextArea.setWrapText(true);

        TextField searchField = new TextField();   // field for input "search word"
        searchField.setPromptText("Enter a word or phrase to search for");

        TextFlow resultTextFlow = new TextFlow();  // field for result with  underlined words
        resultTextFlow.setStyle("-fx-padding: 10; -fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 1px;");

        ScrollPane scrollPane = new ScrollPane(resultTextFlow);  //ScrollPane for "field for input main text" and "field for result with  underlined words"
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefHeight(300);


        Button searchButton = new Button("Search");
        Button exitButton = new Button("Exit");

        Label resultLabel = new Label();






        // Logik button "Search"
        searchButton.setOnAction(event -> {
            String mainText = mainTextArea.getText();
            String searchWord = searchField.getText();
            if (mainText.isEmpty()) {
                resultLabel.setText("Error: The body text is empty.");
                return;}
            if (searchWord.isEmpty()) {
                resultLabel.setText("Error: Enter a search term.");
                return;}
            int count = countOccurrencesKMP(mainText.toLowerCase(), searchWord.toLowerCase());
            resultLabel.setText("Word or phrase \"" + searchWord + "\" occurs " + count + " time.");
            highlightOccurrences(mainText, searchWord, resultTextFlow);});






        // Logik button "Exit"
        exitButton.setOnAction(event -> System.exit(0));

        // form layout
        VBox inputBox = new VBox(10, new Label("Main text:"), mainTextArea,
                new Label("Search word or phrase:"), searchField, resultLabel, resultTextFlow, scrollPane);
        inputBox.setStyle("-fx-padding: 10;");


        HBox buttonBox = new HBox(10, searchButton, exitButton);
        buttonBox.setStyle("-fx-padding: 10; -fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(inputBox);
        root.setBottom(buttonBox);

        // launch
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();}


    // hlavni algorytmus Knuth‐Morris‐Pratt
    public static int countOccurrencesKMP(String text, String pattern) {
        int[] paternus = paternus(pattern); // udelat paternus

        int indexText = 0; // hlavni
        int indexWord = 0; // slovo

        int count = 0;
        while (indexText < text.length()) {
            if (text.charAt(indexText) == pattern.charAt(indexWord)) {
                indexText++;
                indexWord++;}
            if (indexWord == pattern.length()) {
                count++;
                indexWord = paternus[indexWord - 1];
                indexText--;
            } else if (indexText < text.length() && text.charAt(indexText) != pattern.charAt(indexWord)) {
                if (indexWord != 0) {
                    indexWord = paternus[indexWord - 1]; } else {
                    indexText++;}}}
        return count;}


    // paternus
    public static int[] paternus(String pattern) {
        int[] array = new int[pattern.length()];
        int index = 0;
        int sufix = 1;
        while (sufix < pattern.length()) {
            if (pattern.charAt(index) == pattern.charAt(sufix)) {
                index++;
                array[sufix] = index;
                sufix++;} else {
                if (index != 0) {
                    index = array[index - 1];} else {
                    array[sufix] = 0;
                    sufix++;}}}
        return array;
    }

    // podtrhnout slova
    public static void highlightOccurrences(String text, String searchWord, TextFlow resultTextFlow) {
        resultTextFlow.getChildren().clear();
        String lowerText = text.toLowerCase();
        String lowerSearchWord = searchWord.toLowerCase();
        int[] lps = paternus(lowerSearchWord);
        int i = 0;
        int j = 0;
        int lastEnd = 0;



        while (i < lowerText.length()) {
            if (lowerText.charAt(i) == lowerSearchWord.charAt(j)) {



                i++;
                j++;}
            if (j == lowerSearchWord.length()) {


























                

                if (i - j > lastEnd) {
                    Text normalText = new Text(text.substring(lastEnd, i - j));
                    resultTextFlow.getChildren().add(normalText);}

                Text highlightedText = new Text(text.substring(i - j, i));
                highlightedText.setStyle("-fx-underline: true; -fx-fill: red;");
                resultTextFlow.getChildren().add(highlightedText);



                lastEnd = i;
                j = lps[j - 1];
            } else if (i < lowerText.length() && lowerText.charAt(i) != lowerSearchWord.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;}}}



        if (lastEnd < text.length()) {
            Text remainingText = new Text(text.substring(lastEnd));
            resultTextFlow.getChildren().add(remainingText);}}
}
