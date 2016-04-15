package chat;

import org.omg.CORBA.ORB;

public class supertopeerImplementation extends supertopeerPOA {

    private ORB orb;

    public void setOrb(ORB orb) {
        this.orb = orb;
    }

    @Override
    public void notifyFriendIn(VOUser usuario) {

        System.out.println("SE HA CONECTADO EL USUARIO: "+usuario.email);

        if(!Controller.usuariosConectados.contains(usuario)){
            Controller.usuariosConectados.add(usuario);

        }
    }

    @Override
    public void notifyFriendOut(VOUser usuario) {
        for(VOUser user : Controller.usuariosConectados){
            if(user.email.compareTo(usuario.email)==0){
                Controller.usuariosConectados.remove(user);
                break;
            }
        }
    }

    @Override
    public void notifyFriendRequest(VOUser usuario) {
        Controller.usuariosPeticiones.add(usuario);
    }

    @Override
    public void notifyFriendRequestReslution(VOUser usuario, boolean result) {
        // ALERTA
    }

}
