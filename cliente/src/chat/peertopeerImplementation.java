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
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Random;

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
                        String mens=message;
                        if(message.trim().startsWith("/zumbido")){
                            mens=message.replaceFirst("/zumbido","");
                            Window ventana= c.getTxtChatMensajes().getScene().getWindow();
                            double posInix=ventana.getX(), posIniy=ventana.getY();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        ventana.setX(posInix+ new Random().nextInt(5+5+1)-5);
                                        ventana.setY(posIniy+ new Random().nextInt(5+5+1)-5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix+ new Random().nextInt(5+5+1)-5);
                                        ventana.setY(posIniy+ new Random().nextInt(5+5+1)-5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix+ new Random().nextInt(5+5+1)-5);
                                        ventana.setY(posIniy+ new Random().nextInt(5+5+1)-5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix+ new Random().nextInt(5+5+1)-5);
                                        ventana.setY(posIniy+ new Random().nextInt(5+5+1)-5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix+ new Random().nextInt(5+5+1)-5);
                                        ventana.setY(posIniy+ new Random().nextInt(5+5+1)-5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix+ new Random().nextInt(5+5+1)-5);
                                        ventana.setY(posIniy+ new Random().nextInt(5+5+1)-5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                    }
                                    catch(Exception e){}
                                }
                            }).start();
                        }
                        if(mens.compareTo("")!=0) {
                            c.getTxtChatMensajes().appendText(usuario.nombre + ": " + mens.trim() + "\n");
                            c.getTxtChatMensajes().appendText("");
                            try{
                                c.getTxtChatMensajes().notify();
                            }
                            catch(Exception e){}
                        }
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
                        String mens = message;
                        if(message.trim().startsWith("/zumbido")) {
                            mens = message.replaceFirst("/zumbido", "");
                            Window ventana = Controller.pantallasChat.get(usuario).getTxtChatMensajes().getScene().getWindow();
                            double posInix = ventana.getX(), posIniy = ventana.getY();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        ventana.setX(posInix + new Random().nextInt(5 + 5 + 1) - 5);
                                        ventana.setY(posIniy + new Random().nextInt(5 + 5 + 1) - 5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix + new Random().nextInt(5 + 5 + 1) - 5);
                                        ventana.setY(posIniy + new Random().nextInt(5 + 5 + 1) - 5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix + new Random().nextInt(5 + 5 + 1) - 5);
                                        ventana.setY(posIniy + new Random().nextInt(5 + 5 + 1) - 5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix + new Random().nextInt(5 + 5 + 1) - 5);
                                        ventana.setY(posIniy + new Random().nextInt(5 + 5 + 1) - 5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix + new Random().nextInt(5 + 5 + 1) - 5);
                                        ventana.setY(posIniy + new Random().nextInt(5 + 5 + 1) - 5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                        Thread.sleep(100);
                                        ventana.setX(posInix + new Random().nextInt(5 + 5 + 1) - 5);
                                        ventana.setY(posIniy + new Random().nextInt(5 + 5 + 1) - 5);
                                        Thread.sleep(100);
                                        ventana.setX(posInix);
                                        ventana.setY(posIniy);
                                    }
                                    catch (Exception e) {
                                    }
                                }
                            }).start();
                        }
                        if(mens.compareTo("")!=0) {
                            Controller.pantallasChat.get(usuario).getTxtChatMensajes().appendText(usuario.nombre+": "+mens.trim()+"\n");
                            Controller.pantallasChat.get(usuario).getTxtChatMensajes().appendText("");
                            Controller.pantallasChat.get(usuario).getTxtChatMensaje().requestFocus();
                        }
                        Controller.pantallasChat.get(usuario).getTxtChatMensaje().setOnKeyPressed(
                            new EventHandler<KeyEvent>() {
                                @Override
                                public void handle(KeyEvent keyEvent) {
                                    if (keyEvent.getCode() == KeyCode.ENTER) {
                                        if(Controller.pantallasChat.get(usuario).getTxtChatMensaje().getText().trim().compareTo("")!=0){
                                            Controller.pantallasChat.get(usuario).enviarMensaje();
                                        }
                                    }
                                }
                            });
                        try{
                            Controller.pantallasChat.get(usuario).getTxtChatMensajes().notify();
                        }
                        catch(Exception e){}
                    }
                    catch (IOException e){
                    }
                }
            });
        }
    }

    @Override
    public void sendFile(VOUser usuario, byte[] archivo, String nombre) {
        user.chat= usuario.chat;
        user.nombre="CHAtty";
        user.email=usuario.email;
        user.id=usuario.id;
        user.callback=usuario.callback;
        user.hash=usuario.hash;
        user.salt=usuario.salt;
        user.avatar=usuario.avatar;

        sendMessge(usuario,"");

        Platform.runLater(new Runnable() {
            @Override
            public void run(){
                sendMessge(user,"Petición de Archivo Recibida");
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
                        if(f2!=null) {
                            if (!f2.exists()) {
                                f2.createNewFile();
                            }
                            OutputStream out = new FileOutputStream(f2);

                            out.write(archivo, 0, archivo.length);

                            //se ciera el archivo
                            out.close();
                            sendMessge(user, "Archivo guardado en: " + f2.getPath());
                            user.chat = Controller.usuario.chat;
                            user.nombre = "CHAtty";
                            user.email = Controller.usuario.email;
                            user.id = Controller.usuario.id;
                            user.callback = Controller.usuario.callback;
                            user.hash = Controller.usuario.hash;
                            user.salt = Controller.usuario.salt;
                            user.avatar = Controller.usuario.avatar;
                            usuario.chat.sendMessge(user, "El usuario " + Controller.usuario.nombre + " ha aceptado el archivo enviado");
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    sendMessge(usuario,"Petición rechazada");
                    user.chat= Controller.usuario.chat;
                    user.nombre="CHAtty";
                    user.email=Controller.usuario.email;
                    user.id=Controller.usuario.id;
                    user.callback=Controller.usuario.callback;
                    user.hash=Controller.usuario.hash;
                    user.salt=Controller.usuario.salt;
                    user.avatar=Controller.usuario.avatar;
                    usuario.chat.sendMessge(user,"El usuario "+Controller.usuario.nombre+" ha rechazado el archivo enviado");
                }
            }
        });
    }
}
