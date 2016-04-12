package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Controller{

    /* Referencia a las pantallas de las interfaces de la aplicación */
    private static Stage pantallaPrincipal;
    private static Stage pantallaBaja;
    private static Stage pantallaContr;
    private static Stage pantallaChat;
    private static Stage pantallaRegistro;
    private static Window pantallaInicio;

    /* Elementos de la interfaz Inicio */
    @FXML
    private TextField txtIniSesEmail;

    @FXML
    private PasswordField txtIniSesContr;

    @FXML
    private Label lblIniSesError;

    @FXML
    private Label lblIniSesExito;

    /* Elementos de la interfaz Registro */
    @FXML
    private CheckBox chbRegistroCond;

    @FXML
    private TextField txtRegistroNombre;

    @FXML
    private TextField txtRegistroAvatar;

    @FXML
    private PasswordField txtRegistroContr;

    @FXML
    private PasswordField txtRegistroConfContr;

    @FXML
    private TextField txtRegistroEmail;

    @FXML
    private Label lblRegistroError;

    @FXML
    private Label lblRegistroErrorEmail;

    @FXML
    private Label lblRegistroErrorContr;

    public void crearPantallaPrincipal() throws IOException{
        pantallaInicio= lblIniSesError.getScene().getWindow();
        /* Comprobaciones de si el inicio de sesión es correcto: */
        if (txtIniSesEmail.getText().isEmpty()||txtIniSesContr.getText().isEmpty()){
            lblIniSesError.setVisible(true);
        }
        else{
            //Comrpobacion con la base de datos correcta
            lblIniSesError.setVisible(false);
            lblIniSesExito.setVisible(false);
            /* Creación de la nueva ventana */
            Stage pantallaPrincipal=new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("interfazPrincipal.fxml"));
            pantallaPrincipal.setTitle("CHAtty - Pantalla Principal");
            pantallaPrincipal.setScene(new Scene(root, 600, 400));
            pantallaPrincipal.setResizable(false);
            pantallaPrincipal.show();
            pantallaInicio.setOpacity(0.0);
            // Fallida:
                //lblIniSesError.setVisible(true);
                //Borrar los campos?
        }
    }

    //
    public void crearPantallaRegistro() throws IOException{
        pantallaInicio= lblIniSesError.getScene().getWindow();
        lblIniSesError.setVisible(false);
        lblIniSesExito.setVisible(false);
        /* Creación de la nueva ventana */
        Stage pantallaRegistro=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Registro.fxml"));
        pantallaRegistro.setTitle("CHAtty - Pantalla Registro de Usuarios");
        pantallaRegistro.setScene(new Scene(root, 463, 343));
        pantallaRegistro.setResizable(false);
        pantallaRegistro.show();
        pantallaInicio.setOpacity(0.0);
    }

    public void cancelarRegistro() throws IOException{
        chbRegistroCond.getScene().getWindow().hide();
        pantallaInicio.setOpacity(1.0);
    }

    public void Registrar() throws IOException{
        lblRegistroErrorContr.setVisible(false);
        lblRegistroError.setVisible(false);
        lblRegistroErrorEmail.setVisible(false);
        // Comprobamos que los campos estan rellenos
        if(txtRegistroAvatar.getText().isEmpty() || txtRegistroEmail.getText().isEmpty() || txtRegistroNombre.getText().isEmpty() || txtRegistroContr.getText().isEmpty() || txtRegistroConfContr.getText().isEmpty() || chbRegistroCond.isSelected()==false){
            lblRegistroError.setVisible(true);
        }
        else{
            // Comprobamos que las contraseñas coinciden
            if(txtRegistroContr.getText().compareTo(txtRegistroConfContr.getText())!=0){
                lblRegistroErrorContr.setVisible(true);
            }
            else{
                //Comprobamos si el email existe en la base de datos, si existe:
                /*
                    lblRegistroErrorEmail.setVisible(true);
                */
                //Sino:
                    //Comprobamos si la url de avatar es correcta, si no lo es fuera
                        //Insertamos en la base de datos el usuario y cerramos la ventana
                lblIniSesExito.setVisible(true);
                chbRegistroCond.getScene().getWindow().hide();
                pantallaInicio.setOpacity(1.0);

            }
        }
        pantallaInicio.setOpacity(1.0);
    }


}
