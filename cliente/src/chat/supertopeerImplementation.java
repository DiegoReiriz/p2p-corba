package chat;

import org.omg.CORBA.ORB;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class supertopeerImplementation extends supertopeerPOA {

    private ORB orb;

    public void setOrb(ORB orb) {
        this.orb = orb;
    }

    @Override
    public void notifyFriendIn(VOUser usuario) {
        if(!Controller.usuariosConectados.contains(usuario)){
            Controller.usuariosConectados.add(usuario);
            if (SystemTray.isSupported()) {
                final SystemTray systemTray = SystemTray.getSystemTray();
                final TrayIcon trayIcon = new TrayIcon(new ImageIcon("", "omt").getImage(), "CHAtty");
                MouseAdapter mouseAdapter = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        systemTray.remove(trayIcon);
                    }
                };
                try {
                    systemTray.add(trayIcon);
                }
                catch (Exception e) {}
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            trayIcon.displayMessage("Chatty","El usuario "+usuario.nombre+": "+usuario.email+" se ha conectado", TrayIcon.MessageType.INFO);
                            Thread.sleep(3000);
                            systemTray.remove(trayIcon);
                        }
                        catch(Exception e){}
                    }
                }).start();
            }
        }
    }

    @Override
    public void notifyFriendOut(VOUser usuario) {
        for(VOUser user : Controller.usuariosConectados){
            if(user.email.compareTo(usuario.email)==0){
                Controller.usuariosConectados.remove(user);
                if (SystemTray.isSupported()) {
                    final SystemTray systemTray = SystemTray.getSystemTray();
                    final TrayIcon trayIcon = new TrayIcon(new ImageIcon("", "omt").getImage(), "CHAtty");
                    MouseAdapter mouseAdapter = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            systemTray.remove(trayIcon);
                        }
                    };
                    try {
                        systemTray.add(trayIcon);
                    }
                    catch (Exception e) {}
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                trayIcon.displayMessage("Chatty","El usuario "+usuario.nombre+": "+usuario.email+" se ha desconectado", TrayIcon.MessageType.INFO);
                                Thread.sleep(3000);
                                systemTray.remove(trayIcon);
                            }
                            catch(Exception e){}
                        }
                    }).start();
                }
                break;
            }
        }
    }

    @Override
    public void notifyFriendRequest(VOUser usuario) {
        Controller.usuariosPeticiones.add(usuario);
        if (SystemTray.isSupported()) {
            final SystemTray systemTray = SystemTray.getSystemTray();
            final TrayIcon trayIcon = new TrayIcon(new ImageIcon("", "omt").getImage(), "CHAtty");
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    systemTray.remove(trayIcon);
                }
            };
            try {
                systemTray.add(trayIcon);
            }
            catch (Exception e) {}
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        trayIcon.displayMessage("Chatty","El usuario "+usuario.nombre+": "+usuario.email+" desea agregarte a sus CHAtty's", TrayIcon.MessageType.INFO);
                        Thread.sleep(3000);
                        systemTray.remove(trayIcon);
                    }
                    catch(Exception e){}
                }
            }).start();
        }
    }

    @Override
    public void notifyFriendRequestReslution(VOUser usuario, boolean result) {
        if (SystemTray.isSupported()) {
            final SystemTray systemTray = SystemTray.getSystemTray();
            final TrayIcon trayIcon = new TrayIcon(new ImageIcon("", "omt").getImage(), "CHAtty");
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    systemTray.remove(trayIcon);
                }
            };
            try {
                systemTray.add(trayIcon);
            }
            catch (Exception e) {}
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        trayIcon.displayMessage("Chatty","El usuario "+usuario.nombre+": "+usuario.email+" ha aceptado tu solicitud de amistad", TrayIcon.MessageType.INFO);
                        Thread.sleep(3000);
                        systemTray.remove(trayIcon);
                    }
                    catch(Exception e){}
                }
            }).start();
        }
    }

}
