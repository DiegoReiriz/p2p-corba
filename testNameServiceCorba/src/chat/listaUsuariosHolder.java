package chat;


/**
* chat/listaUsuariosHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from interface.idl
* jueves 14 de abril de 2016 18H51' CEST
*/

public final class listaUsuariosHolder implements org.omg.CORBA.portable.Streamable
{
  public chat.VOUser value[] = null;

  public listaUsuariosHolder ()
  {
  }

  public listaUsuariosHolder (chat.VOUser[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = chat.listaUsuariosHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    chat.listaUsuariosHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return chat.listaUsuariosHelper.type ();
  }

}
