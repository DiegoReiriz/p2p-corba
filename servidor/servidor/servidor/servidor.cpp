// servidor.cpp: define el punto de entrada de la aplicaci�n de consola.
//

#include "stdafx.h"
#include "echo.hh"
#include "interface.hh"
#include <ios>
#include <iostream>
#include "SQLite.h"
#include "SQLiteContract.h"

using namespace std;

//referencia est�tica a base de datos
sqlite3 *db;
SQLite database;

static CORBA::Boolean bindObjectToName(CORBA::ORB_ptr, CORBA::Object_ptr,const char* bindName);

class Echo_i : public POA_ECHOAPP::Echo
{
public:
	inline Echo_i() {}
	virtual ~Echo_i() {}
	virtual char* echoString(const char* mesg);
};


char* Echo_i::echoString(const char* mesg)
{
	cout << mesg;

	return CORBA::string_dup(mesg);
}

//////////////////////////////////////////////////////////////////////
class userManager_i : public POA_chat::userManager
{
	public :
		inline userManager_i() {}
		virtual ~userManager_i() {}
		virtual ::CORBA::Boolean signIn(const ::chat::VOUser& usuario);
		virtual ::CORBA::Boolean signOut(const ::chat::VOUser& usuario);
		virtual ::CORBA::Boolean signUp(const ::chat::VOUser& usuario);
		virtual ::CORBA::Boolean alterUser(const ::chat::VOUser& usuario);
		virtual ::chat::listaUsuarios* getFrindList(const ::chat::VOUser& usuario);

};

::CORBA::Boolean userManager_i::signIn(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;

	cout << "SIGN IN" << endl;
	cout << "=======" << endl;
	cout << usuario.id << endl;
	cout << usuario.nombre << endl;
	cout << usuario.email << endl;
	cout << usuario.hash << endl;
	cout << usuario.salt << endl;
	cout << usuario.avatar << endl;

	return res;
}
::CORBA::Boolean userManager_i::signOut(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;
	
	database.obterUsuarios(db);

	return res;
}
::CORBA::Boolean userManager_i::signUp(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;

	cout << "SIGN UP" << endl;
	cout << "=======" << endl;
	cout << usuario.id << endl;
	cout << usuario.nombre << endl;
	cout << usuario.email << endl;
	cout << usuario.hash << endl;
	cout << usuario.salt << endl;
	cout << usuario.avatar << endl;

	database.insertarUsuario((const char*)usuario.nombre, (const char*)usuario.email, (const char*)usuario.hash, (const char*)usuario.salt, (const char*)usuario.avatar,db);

	return res;
}
::CORBA::Boolean userManager_i::alterUser(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;
	return res;
}
::chat::listaUsuarios* userManager_i::getFrindList(const ::chat::VOUser& usuario) {
	::chat::listaUsuarios lista;
	return &lista;
}



//////////////////////////////////////////////////////////////////////

int main(int argc, char **argv){

	database.openConnection(&db);
	database.initialize();
	database.createTables(db);
	//SERIA INTERESANTE MIRAR COMO FACER MUTEXS EN c++ PARA CONTROLAR O USO DO OBXECTO CONEXION DA BASE DE DATOS
	
	try {
		//inicializaci�n e xesti�n de CORBA
		CORBA::ORB_var          orb = CORBA::ORB_init(argc, argv);
		CORBA::Object_var       obj = orb->resolve_initial_references("RootPOA");
		PortableServer::POA_var poa = PortableServer::POA::_narrow(obj);

		//PortableServer::Servant_var<Echo_i> myecho = new Echo_i();
		//crease un obxecto servant
		PortableServer::Servant_var<userManager_i> myuserManager = new userManager_i();

		//PortableServer::ObjectId_var myechoid = poa->activate_object(myecho);
		//activase no poa o servant que se  creou
		PortableServer::ObjectId_var myuserManagerid = poa->activate_object(myuserManager);

		//obtense unha referencia ao obxeto e se rexistra no servicio de nomes
		//obj = myecho->_this();
		obj = myuserManager->_this();

		//obtense un string co IOR para depuraci�n
		CORBA::String_var sior(orb->object_to_string(obj));
		cout << sior << endl;

		//rexistrase o obxecto, en base a referencia obtida previamente, no servicio de nombre

		if (!bindObjectToName(orb, obj,"User"))
			return 1;

		//activase o poa
		PortableServer::POAManager_var pman = poa->the_POAManager();
		pman->activate();

		//l�nzase o orb
		orb->run();
			
	}
	catch (CORBA::SystemException& ex) {
		cerr << "Caught CORBA::" << ex._name() << endl;
	}
	catch (CORBA::Exception& ex) {
		cerr << "Caught CORBA::Exception: " << ex._name() << endl;
	}

	system("pause");

	database.closeConnection(db);

	return 0;
}

//////////////////////////////////////////////////////////////////////

static CORBA::Boolean bindObjectToName(CORBA::ORB_ptr orb, CORBA::Object_ptr objref,const char* bindName)
{
	CosNaming::NamingContext_var rootContext;

	try {
		// Obtain a reference to the root context of the Name service:
		CORBA::Object_var obj = orb->resolve_initial_references("NameService");

		// Narrow the reference returned.
		rootContext = CosNaming::NamingContext::_narrow(obj);
		if (CORBA::is_nil(rootContext)) {
			cerr << "Failed to narrow the root naming context." << endl;
			return 0;
		}
	}
	catch (CORBA::NO_RESOURCES&) {
		cerr << "Caught NO_RESOURCES exception. You must configure omniORB "
			<< "with the location" << endl
			<< "of the naming service." << endl;
		return 0;
	}
	catch (CORBA::ORB::InvalidName&) {
		// This should not happen!
		cerr << "Service required is invalid [does not exist]." << endl;
		return 0;
	}

	try {
		// Bind objref with name Echo to the testContext:
		CosNaming::Name objectName;
		objectName.length(1);
		objectName[0].id	= bindName;   // string copied
		//objectName[0].kind = (const char*) "Object"; // string copied
		
		try {
			rootContext->bind(objectName, objref);
		}
		catch (CosNaming::NamingContext::AlreadyBound& ex) {
			rootContext->rebind(objectName, objref);
		}
		// Note: Using rebind() will overwrite any Object previously bound
		//       to /test/Echo with obj.
		//       Alternatively, bind() can be used, which will raise a
		//       CosNaming::NamingContext::AlreadyBound exception if the name
		//       supplied is already bound to an object.
	}
	catch (CORBA::TRANSIENT& ex) {
		cout << "Caught system exception TRANSIENT -- unable to contact the "
			<< "naming service." << endl
			<< "Make sure the naming server is running and that omniORB is "
			<< "configured correctly." << endl;

		return 0;
	}
	catch (CORBA::SystemException& ex) {
		cout << "Caught a CORBA::" << ex._name()
			<< " while using the naming service." << endl;
		return 0;
	}
	return 1;
}

