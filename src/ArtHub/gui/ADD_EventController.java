package ArtHub.gui;

import ArtHub.entities.Download;
import ArtHub.entities.Evenement;
import static ArtHub.entities.Evenement.isNotInteger;
import static ArtHub.gui.LoginController.CurrentUser;
import ArtHub.entities.User;
import ArtHub.services.EvenementCRUD;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.xml.bind.DatatypeConverter;
import java.sql.Date;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ADD_EventController implements Initializable {

    @FXML
    private TextField txt_nom;
    @FXML
    private ComboBox<String> txt_categorie = new ComboBox<>();
    @FXML
    private TextField txt_description;
    @FXML
    private Button btnValiderA_event;
    @FXML
    private ComboBox<String> combo_type = new ComboBox<>();
    @FXML
    private JFXDatePicker tDatenaiss;
    @FXML
    private TextField txt_capacite;
    
    @FXML
    private JFXButton upload_image;
    public static String path="";
    @FXML
    private TextField event_location;
    @FXML
    private Label Control;
    private URL urll;
    private ResourceBundle rbb;
    int i=0;
    @FXML
    private AnchorPane anchor;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        Control.setVisible(false);
        combo_type.getItems().addAll("En ligne", "Expos??", "Festival", "Formation", "Autres");

            txt_categorie.getItems().addAll("Dancing", "Theatre", "Slam", "Singing", "Street Art");

    }

    @FXML
    private void addEvent(ActionEvent event) {
         

       
        try {
            
            if(i>0){initialize(urll, rbb)
                    ;}
            i++;  
            System.out.println(i);
            Control.setVisible(false);
            String control = "";
            if (txt_nom.getText() == null || txt_nom.getText().trim().isEmpty() || combo_type.getValue() == null || combo_type.getValue().isEmpty()
                    || txt_categorie.getValue() == null || txt_categorie.getValue().isEmpty() || tDatenaiss.getValue() == null || txt_description.getText() == null
                    || txt_description.getText().trim().isEmpty() || txt_capacite.getText() == null || txt_capacite.getText().trim().isEmpty() || event_location.getText() == null
                    || event_location.getText().trim().isEmpty()) {
                control = "Make sure to fill all the fields";
                Control.setVisible(true);
                Control.setText(control);
            } else if (Evenement.isNotInteger(txt_capacite.getText())) {
                control += "\nEvent capacity should be an integer";
                txt_capacite.clear();
                Control.setText(control);
                Control.setVisible(true);
                txt_capacite.setStyle("background-color: rgba(255,0,0,0.2);");
            } else if (path=="") {
                control += "\nMake sure to upload event picture ";
                Control.setText(control);
                Control.setVisible(true);
                 upload_image.setStyle("background-color: rgba(255,0,0,0.2);");
            } else if (tDatenaiss.getValue().isBefore(LocalDate.now())) {
                control += "\nMake sure to select an upcoming date ";
                Control.setText(control);
                Control.setVisible(true);
                tDatenaiss.setStyle("background-color: rgba(255,0,0,0.2);");
            }else {

                LocalDate Datenaiss = tDatenaiss.getValue();
                String rNom = txt_nom.getText();
                String rType = combo_type.getValue();
                String Categorie = txt_categorie.getValue();
                String rDescription = txt_description.getText();

                String Scapacite = txt_capacite.getText();
                int Capacite = DatatypeConverter.parseInt(Scapacite);
                String location = event_location.getText();

                Evenement e = new Evenement(CurrentUser, Datenaiss, rNom, rType, Categorie, rDescription, Capacite, Capacite, path, location);
                EvenementCRUD evt = new EvenementCRUD();
                evt.ajouterEvenement(e);

                // if (CurrentUser.getRef_admin().equals("0")) {
                Stage stage = (Stage) btnValiderA_event.getScene().getWindow();
                stage.close();

            }
        } catch (Exception ex) {
            Logger.getLogger(ADD_EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private String image_file(ActionEvent event) {
        //upload_image
        path="";
        Stage currentStage = (Stage) upload_image.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File f = new File("C:/Users/21698/Desktop");
        fileChooser.setInitialDirectory(f);
        File selectedFile = fileChooser.showOpenDialog(currentStage);

        if (selectedFile != null) {
            //System.out.println("C:/" + selectedFile.getPath());
            //System.out.println("userfiles/"+UNAME+"/"+ANAME+"/");
            File src = new File(selectedFile.getPath());
            File dest = new File("C:/xampp/php/www/pidev/events/");
            java.nio.file.Path sr = src.toPath();
            java.nio.file.Path ds = new File(dest, src.getName()).toPath();
            File newDes = new File("C:/xampp/php/www/pidev/events/" + selectedFile.getName());
            selectedFile.renameTo(newDes);
        }
        path = "C:/xampp/php/www/pidev/events/" + selectedFile.getName().toString();
        return selectedFile.getName();

    }

    public void downloadFile() throws MalformedURLException, IOException {
        long wut = Download.download("https://github.com/JanStureNielsen/so-downloader/archive/main.zip", "img/so-downloader-source.zip");
        System.out.println(wut);

    }

    @FXML
    private void ChangeLocationPropmt(ActionEvent event) {
        if (combo_type.getValue() == "En ligne") {
            event_location.setPromptText("Add google meet link");
        } else {
            event_location.setPromptText("Add event location");
        }
    }
}
