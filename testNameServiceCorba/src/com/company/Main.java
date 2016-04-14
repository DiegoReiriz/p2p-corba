package com.company;

import ECHOAPP.Echo;
import ECHOAPP.EchoHelper;
import chat.*;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Properties;

public class Main
{
    public static void main(String args[]){
        try {
            // create and initialize the ORB

            ORB orb = ORB.init(args, null);

            POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootPOA.the_POAManager().activate();

            /*crease o servand*/
            supertopeerImplementation stp=new supertopeerImplementation();
            stp.setOrb(orb);

            peertopeerImplementation ptp=new peertopeerImplementation();

            org.omg.CORBA.Object ref = rootPOA.servant_to_reference(stp);
            supertopeer callback = supertopeerHelper.narrow(ref);

            org.omg.CORBA.Object ref2 = rootPOA.servant_to_reference(ptp);
            peertopeer mensaje = peertopeerHelper.narrow(ref2);

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
            userManager um = userManagerHelper.narrow(
                    ncRef.resolve_str(name)
            );

//            for(int i=0;i<50;i++) {
//                if(!um.signUp(new VOUser((short) 2, "nombre"+i, "email", "hash", "salt", "avatar")))
//                    System.out.println("Ha fallado la operación");
//
//            }

            if (!um.signUp(new VOUser((short) 1, "nombre1", "email1", "hash1", "salt1", "avatar1",callback,mensaje)))
                System.out.println("Ha fallado la operación de registro");
            if (!um.signUp(new VOUser((short) 2, "nombre2", "email2", "hash2", "salt2", "avatar2",callback,mensaje)))
                System.out.println("Ha fallado la operación de registro");
            if (!um.signUp(new VOUser((short) 2, "nombre3", "email3", "hash3", "salt3", "avatar3",callback,mensaje)))
                System.out.println("Ha fallado la operación de registro");


            System.out.println("===========================");
            System.out.println("| SEGUNDA ZONA DEPURACION |");
            System.out.println("===========================\n\n");

            VOUserHolder usr = new VOUserHolder();
            usr.value = new VOUser((short) 1, "", "email1", "hash1", "", "",callback,mensaje);

            if (!um.signIn(usr)) {
                System.out.println("Ha fallado la operación");
            }else {
                System.out.println(usr.value.id);
                System.out.println(usr.value.nombre);
                System.out.println(usr.value.email);
                System.out.println(usr.value.hash);
                System.out.println(usr.value.salt);
                System.out.println(usr.value.avatar);
            }

            usr.value = new VOUser((short) 2, "nombre2", "email2", "hash2", "salt2", "avatar2",callback,mensaje);
            if (!um.signIn(usr)){
                System.out.println("Ha fallado la operación");
            }else{
                System.out.println(usr.value.id);
                System.out.println(usr.value.nombre);
                System.out.println(usr.value.email);
                System.out.println(usr.value.hash);
                System.out.println(usr.value.salt);
                System.out.println(usr.value.avatar);
            }

            usr.value=new VOUser((short)3,"nombre3","email3","hash3","salt3","avatar3",callback,mensaje);
            if(!um.signIn(usr)) {
                System.out.println("Ha fallado la operación");
            }else {

                System.out.println("=====================");
                System.out.println("  AMIGOS CONECTADOS  ");
                System.out.println("=====================");

                //ao recuperar a lista de usuarios, debe de estar creandose un hilo e proboca que non se peche a aplicación
                listaUsuariosHolder holder=new listaUsuariosHolder(um.getFrindList(usr.value));

                for (VOUser user : holder.value) {
                    System.out.println(user.id);
                    System.out.println(user.nombre);
                    System.out.println(user.email);
                    System.out.println(user.hash);
                    System.out.println(user.salt);
                    System.out.println(user.avatar);
                }
            }

            um.newFriendRequest(new VOUser((short)3,"nombre3","email3","hash3","salt3","avatar3",callback,mensaje),
                    new VOUser((short)2,"nombre2","email2","hash2","salt2","avatar2",callback,mensaje));

            um.resolveFriendRequest(new VOUser((short)2,"nombre2","email2","hash2","salt2","avatar2",callback,mensaje),
                    new VOUser((short)3,"nombre3","email3","hash3","salt3","avatar3",callback,mensaje),true);

//            if(!um.signOut(new VOUser((short)2,"nombre2","email2","hash2","salt2","avatar2")))
//                System.out.println("Ha fallado la operación");
//


        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}

