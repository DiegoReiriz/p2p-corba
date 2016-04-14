package chat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Controller{
    public static VOUser usuario=new VOUser();
    public LinkedList<VOUser> usuariosConectados=new LinkedList<VOUser>();

    /* Referencia a las pantallas de las interfaces de la aplicación */
    private static Stage pantallaPrincipal;
    private static Stage pantallaPeticion;
    private static Stage pantallaNueva;
    private static Stage pantallaBaja;
    private static Stage pantallaContr;
    private static Stage pantallaChat;
    private static Stage pantallaRegistro;
    private static Stage pantallaInicio;

    // Elementos de la interfaz Inicio
    @FXML
    private TextField txtIniSesEmail;

    @FXML
    private PasswordField txtIniSesContr;

    @FXML
    private Label lblIniSesError;

    // Elementos de la interfaz Registro
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

    // Elementos de la interfaz Principal
    @FXML
    private ListView<String> lstvwInicioUsuarios;

    // Elementos de la interfaz Baja
    @FXML
    private TextField txtBajaEmail;

    @FXML
    private PasswordField txtBajaContr;

    @FXML
    private CheckBox chbBajaConfirm;

    @FXML
    private Label lblBajaError;

    // Elementos de la interfaz Cambiar Contraseña
    @FXML
    private PasswordField txtCambContr;

    @FXML
    private PasswordField txtCambNewContr;

    @FXML
    private PasswordField txtCambConfContr;

    @FXML
    private Label lblCambErrorContr;

    @FXML
    private Label lblCambContrNoVale;

    @FXML
    private Label lblCambContrNoCoinc;

    // Elementos de la interfaz Peticiones
    @FXML
    private ListView<String> lstvwPeticionPeticiones;

    // Elementos de la interfaz Solicitar Petición de amistad
    @FXML
    private TextField txtNuevaFiltro;

    @FXML
    private ListView lsvwNuevaUsuarios;

    // Elementos de la interfaz Chat
    @FXML
    private TextArea txtChatMensajes;

    @FXML
    private TextArea txtChatMensaje;

    ////////////////////////////
    //    Métodos Pantallas   //
    ////////////////////////////

    // Métodos Pantalla Inicio de Sesión

            public void crearPantallaPrincipal() throws IOException{
                pantallaInicio= (Stage) lblIniSesError.getScene().getWindow();
                /* Comprobaciones de si el inicio de sesión es correcto: */
                if (txtIniSesEmail.getText().isEmpty()||txtIniSesContr.getText().isEmpty()){
                    lblIniSesError.setVisible(true);
                }
                else{
                    VOUserHolder usuarioHolder = new VOUserHolder();
                    usuarioHolder.value=new VOUser((short)0,"",txtIniSesEmail.getText(),txtIniSesContr.getText(),"","", Main.callback,Main.mensaje);
                    if(Main.um.signIn(usuarioHolder)){ // Comprobación con la base de datos correcta
                        usuario=usuarioHolder.value;
                        // Nos traemos la lista de amigos conectados del usuario
                        listaUsuariosHolder holder=new listaUsuariosHolder(Main.um.getFrindList(usuario)); // Devuelve bien la lista?
                        System.out.println("Amigos");
                        for (VOUser user : holder.value) { // Añadimos a nuestra lista local de amigos conectados los amigos conectados
                            usuariosConectados.add(user);
                            System.out.println(user.nombre);
                        }
                        /* Creación de la nueva ventana */
                        pantallaPrincipal=new Stage();
                        usuario=usuarioHolder.value;
                        Parent root = FXMLLoader.load(getClass().getResource("interfazPrincipal.fxml"));
                        pantallaPrincipal.setTitle("CHAtty - Principal");
                        pantallaPrincipal.setScene(new Scene(root, 600, 400));
                        pantallaPrincipal.setResizable(false);
                        pantallaPrincipal.show();
                        txtIniSesContr.setText("");
                        txtIniSesEmail.setText("");
                        lblIniSesError.setVisible(false);
                        pantallaInicio.setOpacity(0.0);
                    }
                    else{
                        lblIniSesError.setVisible(true);
                    }
                }
            }

            public void SalirInicio(){
                pantallaInicio= (Stage) lblIniSesError.getScene().getWindow();
                pantallaInicio.close();
            }

    // Métodos Pantalla Registro

            public void crearPantallaRegistro() throws IOException{
                pantallaInicio= (Stage) lblIniSesError.getScene().getWindow();
                lblIniSesError.setVisible(false);
                /* Creación de la nueva ventana */
                pantallaRegistro=new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Registro.fxml"));
                pantallaRegistro.setTitle("CHAtty - Registro de Usuario");
                pantallaRegistro.setScene(new Scene(root, 463, 343));
                pantallaRegistro.setResizable(false);
                pantallaRegistro.show();
                pantallaInicio.setOpacity(0.0);
            }

            public void cancelarRegistro(){
                pantallaRegistro.hide();
                pantallaInicio.setOpacity(1.0);
            }

            public void Registrar(){
                lblRegistroErrorContr.setVisible(false);
                lblRegistroError.setVisible(false);
                lblRegistroErrorEmail.setVisible(false);
                // Comprobamos que los campos estan rellenos
                if(txtRegistroAvatar.getText().isEmpty() || txtRegistroEmail.getText().isEmpty() || txtRegistroNombre.getText().isEmpty() || txtRegistroContr.getText().isEmpty() || txtRegistroConfContr.getText().isEmpty() || !chbRegistroCond.isSelected()){
                    lblRegistroError.setVisible(true);
                }
                else{
                    // Comprobamos que las contraseñas coinciden
                    if(txtRegistroContr.getText().compareTo(txtRegistroConfContr.getText())!=0){
                        lblRegistroErrorContr.setVisible(true);
                    }
                    else{
                        //Comprobamos si el email existe en la base de datos, si existe:
                        VOUserHolder usuarioHolder = new VOUserHolder();
                        usuarioHolder.value=new VOUser((short)0,txtRegistroNombre.getText(),txtRegistroEmail.getText(),txtRegistroContr.getText(),"salt",txtRegistroAvatar.getText(),Main.callback,Main.mensaje);
                        if(Main.um.signIn(usuarioHolder)){ // Comprobacion con la base de datos correcta
                            lblRegistroErrorEmail.setVisible(true);
                        }
                        else{
                            //Comprobamos si la url de avatar es correcta, si no lo es lblRegistroError.setVisible(true);

                            //Sino:
                            Main.um.signUp(usuarioHolder.value);
                            pantallaRegistro.hide();
                            pantallaInicio.setOpacity(1.0);
                        }
                    }
                }
            }

    // Métodos Pantalla Principal (( FALTA CREAR CHAT ))

            public void crearPantallaBaja() throws IOException{
                /* Creación de la nueva ventana */
                pantallaBaja=new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Baja.fxml"));
                pantallaBaja.setTitle("CHAtty - Baja de Usuario");
                pantallaBaja.setScene(new Scene(root, 376, 227));
                pantallaBaja.setResizable(false);
                pantallaBaja.show();
                pantallaPrincipal.setOpacity(0.0);
            }

            public void crearPantallaContr() throws IOException{
                /* Creación de la nueva ventana */
                pantallaContr=new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Contr.fxml"));
                pantallaContr.setTitle("CHAtty - Cambiar Contraseña");
                pantallaContr.setScene(new Scene(root, 304, 235));
                pantallaContr.setResizable(false);
                pantallaContr.show();
                pantallaPrincipal.setOpacity(0.0);
            }

            public void crearPantallaPeticion() throws IOException{
                /* Creación de la nueva ventana */
                pantallaPeticion=new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Peticion.fxml"));
                pantallaPeticion.setTitle("CHAtty - Peticiones de Amistad");
                pantallaPeticion.setScene(new Scene(root, 600, 400));
                pantallaPeticion.setResizable(false);
                pantallaPeticion.show();
                pantallaPrincipal.setOpacity(0.0);
            }

            public void insertarAmigosConectados(){
                LinkedList<String> listaAmigos = new LinkedList<String>();

                for (VOUser user : usuariosConectados) { // Introducimos en una lista que será la fuente de datos del listview los amigo para que pueda verlos
                    listaAmigos.add(user.nombre+": "+user.email);
                }
                lstvwInicioUsuarios.setItems(FXCollections.observableArrayList(listaAmigos));
            }

            public void cerrarSesion(){
                pantallaPrincipal.hide();
                pantallaInicio.setOpacity(1.0);
            }

            public void crearChat(){
                // Buscar como sacar elementos de la listview
                // lstvwInicioUsuarios.ge
            }

    // Métodos Pantalla Baja Usuario ((BAJA))

        public void cancelarBaja(){
            pantallaBaja.hide();
            pantallaPrincipal.setOpacity(1.0);
        }

        public void baja(){
            lblBajaError.setVisible(false);
            // Comprobamos que los campos estan rellenos
            if(txtBajaContr.getText().isEmpty()||txtBajaEmail.getText().isEmpty() || !chbBajaConfirm.isSelected()){
                lblBajaError.setVisible(true);
            }
            else{
                // Comprobamos que la autentificación es correcta
                VOUserHolder usuarioHolder = new VOUserHolder();
                usuarioHolder.value=new VOUser((short)0,"",txtBajaEmail.getText(),txtBajaContr.getText(),"","",usuario.callback,usuario.chat);
                if(Main.um.signIn(usuarioHolder)){ // Comprobación con la base de datos correcta
                    // Terminar chats activos (notificar desconexión) + lo de abajo
                    //Método de borrado
                    Main.um.deleteUser(usuario);
                    System.out.println("Has borrado tu cuenta");
                    pantallaBaja.hide();
                    pantallaPrincipal.hide();
                    pantallaInicio.hide();
                }
                else{
                    lblBajaError.setVisible(true);
                }
            }
        }

    // Métodos Pantalla Cambiar Contraseña ((CAMBIAR)

            public void cancelarCambio(){
                pantallaContr.hide();
                pantallaPrincipal.setOpacity(1.0);
            }

            public void cambiar(){
                lblCambErrorContr.setVisible(false);
                lblCambContrNoVale.setVisible(false);
                lblCambContrNoCoinc.setVisible(false);
                // Comprobamos que los campos estan rellenos
                if(txtCambContr.getText().isEmpty()||txtCambNewContr.getText().isEmpty() || txtCambConfContr.getText().isEmpty()){
                    lblCambErrorContr.setVisible(true);
                }
                else{
                    // Comprobamos que la nueva contraseña y su confirmación son iguales
                    if(txtCambNewContr.getText().compareTo(txtCambConfContr.getText())!=0){
                        lblCambContrNoCoinc.setVisible(true);
                    }
                    else{
                        // Comprobamos que la contraseña es correcta
                        VOUserHolder usuarioHolder = new VOUserHolder();
                        usuarioHolder.value=usuario;
                        if(Main.um.signIn(usuarioHolder)){//Si es correcta cambiamos la contraseña
                            if(Main.um.alterUser(new VOUser(usuario.id,usuario.nombre,usuario.email,txtCambNewContr.getText(),usuario.salt,usuario.avatar,usuario.callback,usuario.chat))){ // fallo en sentencia UsuariosSet
                                usuario.hash=txtCambNewContr.getText();
                                pantallaContr.hide();
                                pantallaPrincipal.setOpacity(1.0);
                            }else{
                                lblCambErrorContr.setVisible(true);
                            }
                        }
                        else{
                            lblCambContrNoVale.setVisible(true);
                        }
                    }
                }
            }

    // Métodos Pantalla Petición de Amistad

            public void crearPantallaNueva() throws IOException{
                /* Creación de la nueva ventana */
                pantallaNueva=new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Nueva.fxml"));
                pantallaNueva.setTitle("CHAtty - Solicitar petición de amistad");
                pantallaNueva.setScene(new Scene(root, 600, 400));
                pantallaNueva.setResizable(false);
                pantallaNueva.show();
                pantallaPeticion.setOpacity(0.0);
            }

            public void aceptarPeticion(){
                // Obtenemos el elemento seleccionado del listview y rellenamos los datos en añadir amigo
                // Eliminamos la fila del listview
            }

            public void rechazarPeticion(){
                // Obtenemos el elemento seleccionado del listview y eliminamos la peticion de la base de datos solo
                // Eliminamos la fila del listview
            }

            public void salirPeticion(){
                pantallaPeticion.hide();
                pantallaPrincipal.setOpacity(1.0);
            }

    // Métodos Pantalla Solicitar Petición de amistad

    public void buscarUsuarios(){
        // Obtenemos los datos del TextField y realizamos función de buscar Usuarios
        // Vaciamos el listview e introducimos los nuevos datos en el listview
        txtNuevaFiltro.setText("");
    }

    public void enviarSolicitud(){
        // Obtenemos el elemento eleccionado del listview y rellenamos los datos en enviarSolicitud
        txtNuevaFiltro.setText("");
        // Borramos los datos del listview
    }

    public void salirNueva(){
        pantallaNueva.hide();
        pantallaPeticion.setOpacity(1.0);
    }

    // Métodos Pantalla Chat

    public void enviarMensaje(){
        pantallaChat.hide();
        pantallaPrincipal.hide();
        pantallaInicio.hide();
    }
}
