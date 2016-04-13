package chat;

import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.omg.CosNaming.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static userManager um;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        primaryStage.setTitle("CHAtty");
        primaryStage.setScene(new Scene(root, 282, 194));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try{
            ORB orb = ORB.init(args,null); // Creamos el objeto ORB con los parámetros de conexión proporcionados en la ejecución
            Object objRef = orb.resolve_initial_references("NameService"); // Objeto que referencia al servicio de nombres
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef); // Referencia al Contexto del servivio de nombre
            String name = "User";
            um = userManagerHelper.narrow(ncRef.resolve_str(name));  // Resolvemos la referencia de objetos en el servicio de nombres
            launch(args);
        }
        catch(Exception e){
            e.printStackTrace(System.err);
        }

    }
}
