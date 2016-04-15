package chat;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class peertopeerImplementation extends peertopeerPOA {

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
        try {
            File f2 = new File("./"+nombre);
            System.out.println("PATH: "+f2.getAbsolutePath());

            if(!f2.exists()) {
                System.out.print("Non existe o archivo e crease");
                f2.createNewFile();
            }
            OutputStream out = new FileOutputStream(f2);

            out.write(archivo, 0, archivo.length);

            //se ciera el archivo
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
