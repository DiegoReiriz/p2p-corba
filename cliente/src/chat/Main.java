package chat;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.omg.CORBA.*;
import org.omg.CORBA.Object;
import org.omg.CosNaming.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.omg.PortableServer.*;

public class Main extends Application {
    public static userManager um;
    public static supertopeer callback;
    public static peertopeer mensaje;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Inicio.fxml"));
        primaryStage.setTitle("CHAtty");
        primaryStage.setScene(new Scene(root, 282, 194));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        try{
            ORB orb = ORB.init(args,null); // Creamos el objeto ORB con los parámetros de conexión proporcionados en la ejecución

            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            /*crease o servand*/ // Definición de objetos para el envio de callbacks al cliente
            supertopeerImplementation stp=new supertopeerImplementation();
            stp.setOrb(orb);

            peertopeerImplementation ptp=new peertopeerImplementation();

            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(stp);
            callback = supertopeerHelper.narrow(ref);

            org.omg.CORBA.Object ref2 = rootPOA.servant_to_reference(ptp);
            mensaje = peertopeerHelper.narrow(ref2);

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
