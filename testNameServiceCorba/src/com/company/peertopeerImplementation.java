package com.company;

import chat.VOUser;
import chat.peertopeerPOA;
import chat.supertopeerPOA;
import org.omg.CORBA.ORB;

/**
 * Created by Diego on 13/04/2016.
 */
public class peertopeerImplementation extends peertopeerPOA {

    @Override
    public void sendMessge(VOUser usuario, String message) {
        System.out.println("PEER TO PEER MESSAGE");
    }

    @Override
    public void sendFile(VOUser usuario, byte[] archivo) {
        System.out.println("PEER TO PEER MESSAGE");
    }
}
