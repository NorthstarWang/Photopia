package Photopia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;

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
    void ascii_cancel_onClick(MouseEvent event) {

    }

    @FXML
    void ascii_export_onClick(MouseEvent event) {

    }

    @FXML
    void ascii_preview_onClick(MouseEvent event) {

    }

    private boolean checkFileExtension(String filename){//Check whether the file is image by its extension
        String extension;
        int i = filename.lastIndexOf('.');//Substring the extension
        extension = filename.substring(i+1);
        String[] extensions = {"jpeg","jpg","png","webp"};
        return Arrays.asList(extensions).contains(extension.toLowerCase());
    }

    @FXML
    void image_drag(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()){
            String fileName = db.getFiles().get(0).getName();
            if (checkFileExtension(fileName)){
                event.acceptTransferModes(TransferMode.MOVE);//If image, the drag & drop action is permitted.
            }else{
                event.acceptTransferModes(TransferMode.NONE);
            }

        }
    }

    @FXML
    void image_drop(DragEvent event) throws FileNotFoundException {
        List<File> files = event.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));
        image_view.setImage(img);
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
}
