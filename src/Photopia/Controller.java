package Photopia;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ascii_preview;

    @FXML
    private Button ascii_export;

    @FXML
    private Button ascii_cancel;

    @FXML
    private RadioButton radio_mono;

    @FXML
    private RadioButton radio_color;

    @FXML
    private TextField ascii_width;

    @FXML
    private Button mosaic_preview;

    @FXML
    private Button mosaic_export;

    @FXML
    private Button mosaic_cancel;

    @FXML
    private TextField mosaic_width;

    @FXML
    private Button mosaic_directory;

    @FXML
    private ImageView image_view;

    @FXML
    private Label image_label;

    @FXML
    private Label image_preview_label;

    @FXML
    private ImageView image_preview;

    @FXML
    private AnchorPane anchorpane_preview;

    @FXML
    void ascii_cancel_onClick(MouseEvent event) {

    }

    @FXML
    void ascii_export_onClick(MouseEvent event) {

    }

    @FXML
    void ascii_preview_onClick(MouseEvent event) throws IOException {
        String opt;
        //Contain the value of the radio button that has been clicked
        int width;
        //Contain the width that key in
        String width_str = ascii_width.getText();
        //Get width textfield
        try {
            //If the width text is numeric, no exception will be triggered
            if (width_str.isEmpty()) {
                //default setting
                width = 0;
            } else {
                width = Integer.parseInt(width_str);
                //tryParse width setting
                if (width < 100 || width > 800) {
                    //if the value is not within the range, show alert and stop the process
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please key in width between 100-800 in width, or leave blank to use default setting!");
                    alert.show();
                    //Reset width textfield
                    ascii_width.setText("");
                    return;
                }
            }
        } catch (Exception e) {
            //if input other than integer is in width textfield, show alert and stop the process
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please key in correct integer in width!");
            alert.show();
            //Reset width textfield
            ascii_width.setText("");
            return;
        }
        //Do methods according to the radio selected
        //Monochrome is selected
        if (radio_mono.isSelected()) {
            opt = "mono";
            Image image = ASCII_Image_By_Config(opt, image_view.getImage());
            image_preview.setImage(image);
            image_preview_label.setText("");

        }
        //Color is selected
        else if (radio_color.isSelected()) {
            opt = "color";
            Image image = ASCII_Image_By_Config(opt, image_view.getImage());
            image_preview.setImage(image);
            image_preview_label.setText("");
        }
        //if no radio has been selected when the button has been clicked
        else {
            image_preview_label.setText("Please configure the settings first");
        }
    }

    @FXML
    void image_drag(DragEvent event) {
        //Retrieve the drag board info
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            String fileName = db.getFiles().get(0).getName();
            if (checkFileExtension(fileName)) {
                //If the file extension shows the dragged file is an image, the drag & drop action is permitted.
                event.acceptTransferModes(TransferMode.MOVE);
            } else {
                //else no file can be dropped in the area
                event.acceptTransferModes(TransferMode.NONE);
            }

        }
    }

    @FXML
    void image_drop(DragEvent event) throws FileNotFoundException {
        //Retrieve the drag board file
        List<File> files = event.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));
        //Display the image
        image_view.setImage(img);
        image_label.setText("");
        image_preview_label.setText("Click \"Preview\" Button to view the image");
        image_preview_label.setFont(Font.font("System", 20));
    }

    @FXML
    void mosaic_cancel_onClick(MouseEvent event) {

    }

    @FXML
    void mosaic_directory_onClick(MouseEvent event) {

    }

    @FXML
    void mosaic_export_onClick(MouseEvent event) {

    }

    @FXML
    void mosaic_preview_onClick(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert ascii_preview != null : "fx:id=\"ascii_preview\" was not injected: check your FXML file 'window.fxml'.";
        assert ascii_export != null : "fx:id=\"ascii_export\" was not injected: check your FXML file 'window.fxml'.";
        assert ascii_cancel != null : "fx:id=\"ascii_cancel\" was not injected: check your FXML file 'window.fxml'.";
        assert radio_mono != null : "fx:id=\"radio_mono\" was not injected: check your FXML file 'window.fxml'.";
        assert radio_color != null : "fx:id=\"radio_color\" was not injected: check your FXML file 'window.fxml'.";
        assert ascii_width != null : "fx:id=\"ascii_width\" was not injected: check your FXML file 'window.fxml'.";
        assert mosaic_preview != null : "fx:id=\"mosaic_preview\" was not injected: check your FXML file 'window.fxml'.";
        assert mosaic_export != null : "fx:id=\"mosaic_export\" was not injected: check your FXML file 'window.fxml'.";
        assert mosaic_cancel != null : "fx:id=\"mosaic_cancel\" was not injected: check your FXML file 'window.fxml'.";
        assert mosaic_width != null : "fx:id=\"mosaic_width\" was not injected: check your FXML file 'window.fxml'.";
        assert mosaic_directory != null : "fx:id=\"mosaic_directory\" was not injected: check your FXML file 'window.fxml'.";
        assert image_view != null : "fx:id=\"image_view\" was not injected: check your FXML file 'window.fxml'.";

    }

    private boolean checkFileExtension(String filename) {
        //Check whether the file is image by its extension
        String extension;
        int i = filename.lastIndexOf('.');
        //Substring the extension
        extension = filename.substring(i + 1);
        String[] extensions = {"jpeg", "jpg", "png", "webp"};
        return Arrays.asList(extensions).contains(extension.toLowerCase());
    }

    private Image ASCII_Image_By_Config(String option, Image image) throws IOException {
        BufferedImage img = SwingFXUtils.fromFXImage(image, null);
        //Convert Image to BufferedImage
        //Convert BufferImage to Base64 String
        String ascii = switch (option) {
            case "mono" -> ASCII.txtToImageByBase64(img);
            default -> "";
        };
        String path = ASCII.Base64toImg(ascii);
        //Convert Base64 to image and store it in temp, return the relative path
        return new Image(new FileInputStream(path));
    }
}
