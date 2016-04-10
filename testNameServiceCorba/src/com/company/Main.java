package com.company;

import ECHOAPP.Echo;
import ECHOAPP.EchoHelper;
import chat.VOUser;
import chat.userManager;
import chat.userManagerHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;

import java.util.Properties;


public class Main
{
    public static void main(String args[]){
        try {
            // create and initialize the ORB

            ORB orb = ORB.init(args, null);

            // get the root naming context
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt instead of NamingContext. This is
            // part of the Interoperable naming Service.
//            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//
//            // resolve the Object Reference in Naming
//            String name = "Echo";
//            Echo e= EchoHelper.narrow(
//                    ncRef.resolve_str(name)
//            );
//            e.echoString("FURRULA");

            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // resolve the Object Reference in Naming
            String name = "User";
            userManager um= userManagerHelper.narrow(
                    ncRef.resolve_str(name)
            );

            for(int i=0;i<50;i++) {
                um.signUp(new VOUser((short) 2, "nombre"+i, "email", "hash", "salt", "avatar"));
                //um.signOut(new VOUser((short)1,"nombre","email","hash","salt","avatar"));
            }


        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}

