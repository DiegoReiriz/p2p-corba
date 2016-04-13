package com.company;

import chat.VOUser;
import chat.supertopeerPOA;
import org.omg.CORBA.ORB;

/**
 * Created by Diego on 13/04/2016.
 */
public class supertopeerImplementation extends supertopeerPOA {

    private ORB orb;

    public void setOrb(ORB orb) {
        this.orb = orb;
    }

    @Override
    public void notifyFriendIn(VOUser usuario) {
        System.out.println("MENSAXE DO SERVIDOR --> CONECTOUSE UN AMIGO");
    }

    @Override
    public void notifyFriendOut(VOUser usuario) {
        System.out.println("MENSAXE DO SERVIDOR");
    }

    @Override
    public void notifyFriendRequest(VOUser usuario) {
        System.out.println("MENSAXE DO SERVIDOR");
    }

    @Override
    public void notifyFriendRequestReslution(VOUser usuario, boolean result) {
        System.out.println("MENSAXE DO SERVIDOR");
    }

}
