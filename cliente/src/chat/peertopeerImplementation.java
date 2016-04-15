package chat;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
                        Stage a = new Stage();
                        Parent root = FXMLLoader.load(getClass().getResource("InterfazChat.fxml"));
                        a.setTitle("CHAtty - Chat con "+usuario.nombre);
                        a.setScene(new Scene(root, 499, 280));
                        a.setResizable(false);
                        a.show();
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
    public void sendFile(VOUser usuario, byte[] archivo) {
        System.out.println("PEER TO PEER MESSAGE");
    }
}
