package com.heshanthenura.backgroundcolor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.awt.Color;

public class MainController implements Initializable {

    @FXML
    private AnchorPane background;

    @FXML
    private ImageView imgView;

    BufferedImage image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {

            background.setOnDragOver(event -> {
                if (event.getGestureSource() != background && event.getDragboard().hasFiles()) {

                    event.acceptTransferModes(TransferMode.COPY);
                }
                event.consume();
            });

            background.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String fileName = null;
                    for (File file : db.getFiles()) {
                        fileName = file.getName();


                        try {
                            FileInputStream fis = new FileInputStream(file);
                            image = ImageIO.read(fis);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to load image", e);
                        }
                        Map<Integer, Integer> colorCountMap = countColors(image);
                        Color dominantColor = findDominantColor(colorCountMap);
                        String hexColor = String.format("#%02x%02x%02x", dominantColor.getRed(), dominantColor.getGreen(), dominantColor.getBlue());
                        background.setStyle("-fx-background-color: " + hexColor);
                        imgView.setImage(new Image(file.toURI().toString()));
                    }
                    System.out.println("Dropped file: " + fileName);
                }
                event.setDropCompleted(success);
                event.consume();
            });


            background.widthProperty().addListener((observable, oldValue, newValue) -> {
                imgView.setFitWidth(newValue.doubleValue());
            });

        });
    }

    public Map<Integer, Integer> countColors(BufferedImage image) {
        Map<Integer, Integer> colorCountMap = new HashMap<>();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                colorCountMap.put(rgb, colorCountMap.getOrDefault(rgb, 0) + 1);
            }
        }
        return colorCountMap;
    }

    public static Color findDominantColor(Map<Integer, Integer> colorCountMap) {
        int maxCount = 0;
        int dominantRGB = 0;
        for (Map.Entry<Integer, Integer> entry : colorCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                dominantRGB = entry.getKey();
            }
        }
        return new Color(dominantRGB);
    }
}
