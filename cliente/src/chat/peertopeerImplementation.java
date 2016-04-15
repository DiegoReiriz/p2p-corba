package chat;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class peertopeerImplementation extends peertopeerPOA {

    private VOUser user=new VOUser();

    @Override
    public void sendMessge(VOUser usuario, String message) {
        boolean found = false;

        for(Controller c : Controller.pantallasChat.values()){
            if(c != null &&
                c.getUsuarioREM().email.compareTo(usuario.email)==0){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                      c.getTxtChatMensajes().appendText(usuario.nombre + ": " + message.trim() + "\n");
                      c.getTxtChatMensajes().appendText("");
                      c.getTxtChatMensajes().notify();
                    }
                });
                found=true;
            }
        }


        if(!found){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        Controller.usuarioREMaux=usuario;
                        Stage chat = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("InterfazChat.fxml"));
                        chat.setTitle("CHAtty - Chat con "+usuario.nombre);
                        chat.setScene(new Scene(root, 499, 280));
                        chat.setResizable(false);
                        chat.show();
                        chat.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                chat.hide();
                                Controller.pantallasChat.remove(usuario); // AQUI
                            }
                        });
                        Controller.pantallasChat.get(usuario).getImvwChatAvatarAmigo().setImage(new Image(usuario.avatar));
                        Controller.pantallasChat.get(usuario).getImvwChatAvatarPropio().setImage(new Image(Controller.usuario.avatar));
                        Controller.pantallasChat.get(usuario).getTxtChatMensajes().appendText(usuario.nombre+": "+message.trim()+"\n");
                        Controller.pantallasChat.get(usuario).getTxtChatMensajes().appendText("");
                        Controller.pantallasChat.get(usuario).getTxtChatMensaje().requestFocus();
                        Controller.pantallasChat.get(usuario).getTxtChatMensaje().setOnKeyPressed(
                                new EventHandler<KeyEvent>() {
                                    @Override
                                    public void handle(KeyEvent keyEvent) {
                                        if (keyEvent.getCode() == KeyCode.ENTER)  {
                                            Controller.pantallasChat.get(usuario).enviarMensaje();
                                        }
                                    }
                                });
                        Controller.pantallasChat.get(usuario).getTxtChatMensajes().notify();
                    }
                    catch (IOException e){
                    }
                }
            });
        }
    }

    @Override
    public void sendFile(VOUser usuario, byte[] archivo, String nombre) {
        user.callback= usuario.callback;
        user.nombre="CHAtty";
        user.email=usuario.email;
        user.id=usuario.id;
        sendMessge(usuario,"");
        sendMessge(user,"\bPetición de Archivo Recibida");

        /*Controller cuser=null;

        *//*Buscase o usuario co que se fala*//*
        for(Controller c : Controller.pantallasChat.values()){
            if(c != null &&
                    c.getUsuarioREM().email.compareTo(Controller.usuario.email)==0){
                cuser=c;
            }
        }*/
        /*VOUser cuser =null;
        *//*Buscase o usuario co que se fala*//*
        for(VOUser c : Controller.usuariosConectados){
            if(c != null && c.email.compareTo(usuario.email)==0){
                cuser=c;
                break;
            }
        }

        this.finalCuser=cuser;*/
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Dialogo de confirmación");
                    alert.setHeaderText("El usuario "+usuario.nombre+" quiere enviarte el archivo "+nombre.substring(nombre.lastIndexOf('\\')));
                    alert.setContentText("Desea almacenar el archivo?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        try {
                            FileChooser fc= new FileChooser();
                            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("File",nombre.substring(nombre.indexOf('.'))));

                            File f2 = fc.showSaveDialog(new Stage());

                            if (!f2.exists()) {
                                f2.createNewFile();
                            }
                            OutputStream out = new FileOutputStream(f2);

                            out.write(archivo, 0, archivo.length);

                            //se ciera el archivo
                            out.close();
                            sendMessge(user,"Archivo guardado en: "+f2.getPath());
                            user.callback=Controller.usuario.callback;
                            user.id=Controller.usuario.id;
                            user.email=Controller.usuario.email;
                            usuario.chat.sendMessge(user,"El usuario "+Controller.usuario.nombre+" ha aceptado el archivo enviado");
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        sendMessge(usuario,"Petición rechazada");
                        user.callback=Controller.usuario.callback;
                        user.id=Controller.usuario.id;
                        user.email=Controller.usuario.email;
                        usuario.chat.sendMessge(user,"El usuario "+Controller.usuario.nombre+" ha rechazado el archivo enviado");
                    }
                }
            }
        );
    }
}
