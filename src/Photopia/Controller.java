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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller {
    private File selectedDir;

    @FXML
    public VBox VBox;

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
    private Label width_description;

    @FXML
    private AnchorPane anchorpane_preview;

    @FXML
    private Slider ascii_contrast;

    @FXML
    private Slider ascii_brightness;

    @FXML
    void ascii_cancel_onClick(MouseEvent event) {
        resetAll();
    }

    @FXML
    void ascii_export_onClick(MouseEvent event) {

    }

    @FXML
    void ascii_preview_onClick(MouseEvent event) throws IOException {
        Alert alert;
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
                int max_val = SwingFXUtils.fromFXImage(image_view.getImage(), null).getWidth() / 50;
                //Get the maximum width possible, largest pixel should be the total pixel of the picture/50(50 pixels in one ASCII character)
                if (width < 1 || width > max_val) {
                    //if the value is not within the range, show alert and stop the process
                    alert = new Alert(Alert.AlertType.WARNING, "Please key in width between 1-" + max_val + " in width, or leave blank to use default setting!");
                    alert.show();
                    //Reset width textfield
                    ascii_width.setText("");
                    return;
                }
            }
        } catch (Exception e) {
            //if input other than integer is in width textfield, show alert and stop the process
            alert = new Alert(Alert.AlertType.WARNING, "Please key in correct integer in width!");
            alert.show();
            //Reset width textfield
            ascii_width.setText("");
            return;
        }

        //Verify whether image_view has image
        Image view;
        try{
            view = image_view.getImage();
        }catch (Exception e){
            alert = new Alert(Alert.AlertType.WARNING,"No image, please import first!");
            alert.show();
            return;
        }

        //Do methods according to the radio selected
        //Monochrome is selected
        if (radio_mono.isSelected()) {
            opt = "mono";
            Image image = ASCII_Image_By_Config(opt, width, (float) ascii_contrast.getValue(), (int) ascii_brightness.getValue(), view);
            image_preview.setImage(image);
            image_preview_label.setText("");
        }
        //Color is selected
        else if (radio_color.isSelected()) {
            opt = "color";
            Image image = ASCII_Image_By_Config(opt, width, (float) ascii_contrast.getValue(), (int) ascii_brightness.getValue(), view);
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
        if (image_view.getImage() != null) {
            //If there is previous image in drop zone, reset textfield in ASCII tab and clear image_preview if there is any
            resetASCIITab();
            image_preview.setImage(null);
        }
        //Retrieve the drag board file
        List<File> files = event.getDragboard().getFiles();
        Image img = new Image(new FileInputStream(files.get(0)));
        //Display the image
        image_view.setImage(img);
        image_label.setText("");
        int max_val = SwingFXUtils.fromFXImage(image_view.getImage(), null).getWidth() / 50;
        //Get the maximum width possible, largest pixel should be the total pixel of the picture/50(50 pixels in one ASCII character)
        width_description.setText("Please key in width between 1-" + max_val + " in width, \nor leave blank to use default setting!");
        //Change notification to tell user the range of the integer input allowed

        image_preview_label.setText("Click \"Preview\" Button to view the image");
        image_preview_label.setFont(Font.font("System", 20));
    }

    @FXML
    void mosaic_cancel_onClick(MouseEvent event) {
        resetAll();
    }

    @FXML
    void mosaic_directory_onClick(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        //When button clicks, directory chooser pop up, the directory path will be store in the class variable
        selectedDir = directoryChooser.showDialog(VBox.getScene().getWindow());
    }

    @FXML
    void mosaic_export_onClick(MouseEvent event) {

    }

    @FXML
    void mosaic_preview_onClick(MouseEvent event) throws IOException {
        int width, num=0;
        String width_str = mosaic_width.getText();
        Alert alert;

        //Get width textfield
        try {
            //If the width text is numeric, no exception will be triggered
            width = Integer.parseInt(width_str);
            //tryParse width setting
            if (width < image_view.getImage().getWidth()/200 || width > image_view.getImage().getWidth()/50) {
                //if the value is not within the range, show alert and stop the process
                alert = new Alert(Alert.AlertType.WARNING, "Please key in integer above "+ image_view.getImage().getWidth()/200 +" and below "+ (int)image_view.getImage().getWidth()/50 + " in width!");
                alert.show();
                //Reset width textfield
                mosaic_width.setText("");
                return;
            }
        } catch (Exception e) {
            //if input other than integer is in width textfield, show alert and stop the process
            alert = new Alert(Alert.AlertType.WARNING, "Please key in correct integer in width!");
            alert.show();
            //Reset width textfield
            mosaic_width.setText("");
            return;
        }

        String dirPath = selectedDir.getPath();

        //Verify whether image_view has image
        Image view;
        try{
            view = image_view.getImage();
        }catch (Exception e){
            alert = new Alert(Alert.AlertType.WARNING,"No image, please import first!");
            alert.show();
            return;
        }

        BufferedImage img = SwingFXUtils.fromFXImage(view, null);
        //Check there are files in directory instead of an empty folder
        try{
            //If there is folder selected
            num = new File(dirPath).list().length;
        }catch (NullPointerException e){
            alert = new Alert(Alert.AlertType.WARNING,"Please choose directory!");
            alert.show();
            return;
        }
        if (num!=0){
            String path = Mosaic.mosaicGenerate(dirPath, width, img);
            image_preview.setImage(new Image(new FileInputStream(path)));
            image_preview_label.setText("");
        }else{
            alert = new Alert(Alert.AlertType.WARNING,"The folder is empty!");
            alert.show();
        }
    }

    @FXML
    void initialize() {

    }

    @FXML
    void radio_color_onClick(MouseEvent event) {
        if (radio_mono.isSelected()) {
            radio_mono.setSelected(false);
        }
    }

    @FXML
    void radio_mono_onClick(MouseEvent event) {
        if (radio_color.isSelected()) {
            radio_color.setSelected(false);
        }
    }

    public static boolean checkFileExtension(String filename) {
        //Check whether the file is image by its extension
        String extension;
        int i = filename.lastIndexOf('.');
        //Substring the extension
        extension = filename.substring(i + 1);
        String[] extensions = {"jpeg", "jpg", "png", "webp"};
        return Arrays.asList(extensions).contains(extension.toLowerCase());
    }

    private Image ASCII_Image_By_Config(String option, int width, float contrast, int brightness, Image image) throws IOException {
        BufferedImage img = SwingFXUtils.fromFXImage(image, null);
        //Convert Image to BufferedImage
        //Convert BufferImage to Base64 String
        String ascii = ASCII.txtToImageByBase64(img, width, contrast, brightness, option);
        String path = ASCII.Base64toImg(ascii);
        //Convert Base64 to image and store it in temp, return the relative path
        return new Image(new FileInputStream(path));
    }

    private void resetAll() {
        resetLabel();
        resetASCIITab();
        resetImage();
        resetMosaicTab();
    }

    private void resetMosaicTab(){
        mosaic_width.setText("");
        mosaic_directory.setText("Choose directory");
    }
    private void resetASCIITab() {
        ascii_brightness.setValue(0);
        ascii_contrast.setValue(1);
        ascii_width.setText("");
        radio_mono.setSelected(false);
        radio_color.setSelected(false);
    }

    private void resetImage() {
        image_preview.setImage(null);
        image_view.setImage(null);
    }

    private void resetLabel() {
        image_preview_label.setText("Upload your image");
        image_label.setText("Drag your image here");
        width_description.setText("*Width determines number of pixels per \nASCII character represents. Leave blank to\nkeep 1 pixel as 1 character.");
    }
}
