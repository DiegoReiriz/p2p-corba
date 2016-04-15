package chat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static VOUser usuario=new VOUser(); // Usuario que usa la aplicación
    public static LinkedList<VOUser> usuariosConectados = new LinkedList<>(); // Lista de usuarios conectados (Objetos usuario)
    public static LinkedList<VOUser> usuariosPeticiones = new LinkedList<>(); // Lista de usuarios de los que se tiene solicitudes (Objetos usuario)
    public static LinkedList<VOUser> usuariosSolicitudes = new LinkedList<>();// Lista de usuarios a los que se le pueden hacer solicitudes (Objetos usuario)

    // Referencia a las pantallas de las interfaces de la aplicación
    private static Stage pantallaPrincipal;
    private static Stage pantallaPeticion;
    private static Stage pantallaNueva;
    private static Stage pantallaBaja;
    private static Stage pantallaContr;
    private static Stage pantallaRegistro;
    private static Stage pantallaInicio;
    public static HashMap<VOUser, Controller> pantallasChat = new HashMap();

    // Elementos de las interfaces
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

    @FXML
    private TitledPane pnelInicioUsuarios;

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

    @FXML
    private TitledPane pnelPeticionPeticionesAmistad;

    // Elementos de la interfaz Solicitar Petición de amistad
    @FXML
    private TextField txtNuevaFiltro;

    @FXML
    private ListView<String> lstvwNuevaUsuarios;

    // Elementos de la interfaz Chat
    @FXML
    private TextArea txtChatMensajes;

    @FXML
    private TextArea txtChatMensaje;

    // Método para añadir cosas
    public TextArea getTxtChatMensajes(){
        return txtChatMensajes;
    }
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
            usuarioHolder.value=new VOUser((short)0,"",txtIniSesEmail.getText(),txtIniSesContr.getText(),"","", Main.callback, Main.mensaje);
            if(Main.um.signIn(usuarioHolder)){ // Comprobación con la base de datos correcta
                usuario=usuarioHolder.value;
                // Nos traemos la lista de amigos conectados del usuario
                listaUsuariosHolder holder=new listaUsuariosHolder(Main.um.getFrindList(usuario));
                for (VOUser user : holder.value) { // Añadimos a nuestra lista local de amigos conectados los amigos conectados
                    if(user.id==0){
                        System.out.println("Vacio");
                    }
                    else{
                        usuariosConectados.add(user);
                    }
                }
                /* Creación de la nueva ventana */
                pantallaPrincipal=new Stage();
                usuario=usuarioHolder.value;
                Parent root = FXMLLoader.load(getClass().getResource("interfazPrincipal.fxml"));
                pantallaPrincipal.setTitle("CHAtty - Principal \""+usuario.email+"\"");
                pantallaPrincipal.setScene(new Scene(root, 600, 400));
                pantallaPrincipal.setResizable(false);
                pantallaPrincipal.show();
                pantallaPrincipal.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        Main.um.signOut(usuario);
                        Platform.exit();
                        System.exit(0);
                    }
                });
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

    // Métodos Pantalla Registro

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
                usuarioHolder.value=new VOUser((short)0,txtRegistroNombre.getText(),txtRegistroEmail.getText(),txtRegistroContr.getText(),"salt",txtRegistroAvatar.getText(), null, null);
                if(Main.um.signIn(usuarioHolder)){ // Comprobacion con la base de datos correcta
                    lblRegistroErrorEmail.setVisible(true);
                }
                else{
                    //Comprobamos si la url de avatar es correcta, si no lo es lblRegistroError.setVisible(true);

                    Main.um.signUp(usuarioHolder.value);
                    pantallaRegistro.hide();
                    pantallaInicio.setOpacity(1.0);
                    //Sino:
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
        pantallaBaja.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                pantallaBaja.hide();
                pantallaPrincipal.setOpacity(1.0);
            }
        });
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
        pantallaContr.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                pantallaContr.hide();
                pantallaPrincipal.setOpacity(1.0);
            }
        });
        pantallaPrincipal.setOpacity(0.0);
    }

    public void crearPantallaPeticion() throws IOException{
        /* Creación de la nueva ventana */
        pnelInicioUsuarios.setExpanded(false);
        pantallaPeticion=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Peticion.fxml"));
        pantallaPeticion.setTitle("CHAtty - Peticiones de Amistad");
        pantallaPeticion.setScene(new Scene(root, 600, 400));
        pantallaPeticion.setResizable(false);
        pantallaPeticion.show();
        pantallaPeticion.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                pantallaPeticion.hide();
                pantallaPrincipal.setOpacity(1.0);
            }
        });
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
        Main.um.signOut(usuario);
        pantallaPrincipal.hide();
        pantallaInicio.setOpacity(1.0);
    }

    private VOUser usuarioREM=new VOUser();
    public static VOUser usuarioREMaux;
    public VOUser getUsuarioREM() {
        return usuarioREM;
    }

    public void crearChat() throws IOException{
        try{
            int indice =lstvwInicioUsuarios.getSelectionModel().getSelectedIndex();
            if(pantallasChat.containsKey(usuariosConectados.get(indice))){ // Si ya existe no hacemos nada

            }
            else{
                pantallasChat.put(usuariosConectados.get(indice),this);
                usuarioREMaux=usuariosConectados.get(indice);
                Stage chat = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("InterfazChat.fxml"));
                chat.setTitle("CHAtty - Chat con "+usuariosConectados.get(indice).nombre);
                chat.setScene(new Scene(root, 499, 280));
                chat.setResizable(false);
                chat.show();
                chat.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        chat.hide();
                        pantallasChat.remove(usuariosConectados.get(indice)); // AQUI
                    }
                });
            }
        }
        catch(Exception e){}
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

    // Métodos Pantalla Cambiar Contraseña

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
                    if(Main.um.alterUser(new VOUser(usuario.id,usuario.nombre,usuario.email,txtCambNewContr.getText(),usuario.salt,usuario.avatar,usuario.callback,usuario.chat))){
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
        pnelPeticionPeticionesAmistad.setExpanded(false);
        pantallaNueva=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Nueva.fxml"));
        pantallaNueva.setTitle("CHAtty - Solicitar petición de amistad");
        pantallaNueva.setScene(new Scene(root, 600, 400));
        pantallaNueva.setResizable(false);
        pantallaNueva.show();
        pantallaNueva.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                pantallaNueva.hide();
                pantallaPeticion.setOpacity(1.0);
            }
        });
        pantallaPeticion.setOpacity(0.0);
    }

    public void insertarPeticionesAmistad(){
        LinkedList<String> listaPeticiones = new LinkedList<String>();

        for (VOUser user : usuariosPeticiones) { // Introducimos en una lista que será la fuente de datos del listview las peticiones para que pueda verlas
            listaPeticiones.add(user.email);
        }
        lstvwPeticionPeticiones.setItems(FXCollections.observableArrayList(listaPeticiones));
    } // EN PROCESO DE PRUEBA

    public void aceptarPeticion(){
        try{
            String email= lstvwPeticionPeticiones.getSelectionModel().getSelectedItem(); // Obtenemos el elemento seleccionado del listview
            // Buscamos al usuario en la lista de peticiones
            VOUser nuevoAmigo= new VOUser();
            for(VOUser pet : usuariosPeticiones){
                if(pet.email.compareTo(email)==0){
                    nuevoAmigo=pet;
                    break;
                }
            }
            if(Main.um.resolveFriendRequest(usuario,nuevoAmigo,true)){ // Si la operación se realiza con éxito
                usuariosPeticiones.remove(nuevoAmigo);
                insertarPeticionesAmistad();
            }
        }
        catch(Exception e){}
    } // EN PROCESO DE PRUEBA

    public void rechazarPeticion(){
        try {
            String email = lstvwPeticionPeticiones.getSelectionModel().getSelectedItem(); // Obtenemos el elemento seleccionado del listview
            // Buscamos al usuario en la lista de peticiones
            VOUser nuevoAmigo = new VOUser();
            for (VOUser pet : usuariosPeticiones) {
                if (pet.email.compareTo(email) == 0) {
                    nuevoAmigo = pet;
                    break;
                }
            }
            if (Main.um.resolveFriendRequest(usuario, nuevoAmigo, false)) { // Si la operacion se realiza con éxito
                usuariosPeticiones.remove(nuevoAmigo);
                insertarPeticionesAmistad();
            }
        }
        catch(Exception e){}
    } // EN PROCESO DE PRUEBA

    public void salirPeticion(){
        pantallaPeticion.hide();
        pantallaPrincipal.setOpacity(1.0);
    }

    // Métodos Pantalla Solicitar Petición de amistad

    public void buscarUsuarios(){ // FALTA EL MÉTODO PARA BUSCAR LOS USUARIOS // EN PROCESO DE PRUEBA
        listaUsuariosHolder holder=new listaUsuariosHolder(Main.um.getUserLike(usuario,new VOUser((short)0,txtNuevaFiltro.getText(),txtNuevaFiltro.getText(),"","","",null,null)));
        LinkedList<String> listaUsuarios = new LinkedList<String>();
        for (VOUser user : holder.value) { // Añadimos a nuestra lista local de usuarios posibles los resultados de la busqueda (Limitado a 10)
            usuariosSolicitudes.add(user);
            listaUsuarios.add(user.email);
        }
        lstvwNuevaUsuarios.setItems(FXCollections.observableArrayList(listaUsuarios));
        txtNuevaFiltro.setText("");
    }  // EN PROCESO DE PRUEBA

    public void enviarSolicitud(){
        try{
            String email= lstvwNuevaUsuarios.getSelectionModel().getSelectedItem(); // Obtenemos el elemento seleccionado del listview
            // Buscamos al usuario en la lista de solicitudes
            VOUser nuevaSolicitud= new VOUser();
            for(VOUser sol : usuariosSolicitudes){
                if(sol.email.compareTo(email)==0){
                    nuevaSolicitud=sol;
                    break;
                }
            }
            if(Main.um.newFriendRequest(usuario,nuevaSolicitud)){ // Si la operación se realiza con éxito
                while(!usuariosSolicitudes.isEmpty()){ // Vaciamos los usuarios temporales para la solicitud que hay
                    usuariosSolicitudes.remove(0);
                }
                lstvwNuevaUsuarios.setItems(FXCollections.observableArrayList(""));
            }
        }
        catch(Exception e){}
    } // EN PROCESO DE PRUEBA

    public void salirNueva(){
        pantallaNueva.hide();
        pantallaPeticion.setOpacity(1.0);
    }

    // Métodos Pantalla Chat

    public void enviarMensaje(){
        txtChatMensajes.appendText("Yo: "+txtChatMensaje.getText().trim()+"\n");
        txtChatMensajes.appendText("");
        usuarioREM.chat.sendMessge(usuario,txtChatMensaje.getText());
        txtChatMensaje.setText("");
        //pantallaPrincipal.hide();
        //pantallaInicio.hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuarioREM=usuarioREMaux;

        if(usuarioREM != null) {
            System.out.println("DENTRO");
            pantallasChat.put(usuarioREM, this);
        }

        usuarioREMaux=null;
    }
}
