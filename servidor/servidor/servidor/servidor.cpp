// servidor.cpp: define el punto de entrada de la aplicación de consola.
//

#include "stdafx.h"
#include "echo.hh"
#include "interface.hh"
#include <ios>
#include <iostream>
#include "SQLite.h"
#include <list>

using namespace std;

CORBA::ORB_var orb;
std::list<chat::VOUser> usuariosActivos;
sqlite3 *db;
SQLite database;

static CORBA::Boolean bindObjectToName(CORBA::ORB_ptr, CORBA::Object_ptr,const char*);
static CORBA::Boolean unbindObjectfromName(CORBA::ORB_ptr, CORBA::Object_ptr, const char*);

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
		virtual ::CORBA::Boolean signIn(::chat::VOUser& usuario);
		virtual ::CORBA::Boolean signOut(const ::chat::VOUser& usuario);
		virtual ::CORBA::Boolean signUp(const ::chat::VOUser& usuario);
		virtual ::CORBA::Boolean alterUser(const ::chat::VOUser& usuario);
		virtual ::chat::listaUsuarios* getFrindList(const ::chat::VOUser& usuario);
		virtual ::CORBA::Boolean newFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny);
		virtual ::CORBA::Boolean resolveFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny, ::CORBA::Boolean accept);

};

::CORBA::Boolean userManager_i::signIn(::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;

	cout << "SIGN IN" << endl;
	cout << "=======" << endl;
	cout << usuario.id << endl;
	cout << usuario.nombre << endl;
	cout << usuario.email << endl;
	cout << usuario.hash << endl;
	cout << usuario.salt << endl;
	cout << usuario.avatar << endl;

	res=database.obterUsuario(usuario,db);

	if(res)
		usuariosActivos.push_back(usuario);

	return res;
}
::CORBA::Boolean userManager_i::signOut(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;
	
	for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end();/*nothing*/) {
		
		if ( (*itr).id == usuario.id )
			usuariosActivos.erase(itr);

			++itr;
	}

	//falta notificar aos usuarios das desconexions

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

	res = database.insertarUsuario((const char*)usuario.nombre, (const char*)usuario.email, (const char*)usuario.hash, (const char*)usuario.salt, (const char*)usuario.avatar,db);

	return res;
}

::CORBA::Boolean userManager_i::alterUser(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;

	res = database.alterarUsuario(usuario,db);

	return res;
}

::chat::listaUsuarios* userManager_i::getFrindList(const ::chat::VOUser& usuario) {
	
	chat::listaUsuarios* lista = new chat::listaUsuarios;
	lista->length(usuariosActivos.size());
	
	list<chat::VOUser>* amigos=database.obterAmigos(usuario,db);

	int i = 0;
	for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end();/*nothing*/) {
		bool found = false;
		
		int j = 0;
		for (std::list<chat::VOUser>::iterator itr2 = amigos->begin(); j<amigos->size() && !found;/*nothing*/) {

			if (itr->id == itr2->id)
				found = true;

			++itr2;
			j++;
		}

		if (found){
			::chat::VOUser* user = new ::chat::VOUser;
		
			user->id = itr->id;
			user->nombre = itr->nombre;
			user->email = itr->email;
			user->hash = itr->hash;
			user->salt = itr->salt;
			user->avatar = itr->avatar;
			(*lista)[i] = *user;
		}

		++itr;
		++i;
	}
	
	return lista;
}

::CORBA::Boolean userManager_i::newFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny) {
	::CORBA::Boolean res = false;

	res=database.crearPeticionAmistad(origin, destiny, db);

	return res;
}
::CORBA::Boolean userManager_i::resolveFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny, ::CORBA::Boolean accept) {
	::CORBA::Boolean res = false;

	if (accept)
		res = database.insertarAmigo(origin, destiny, db);
	else
		return false;
	
	res = database.borrarPeticionAmistad(origin, destiny, db);
	
	return res;
}


//////////////////////////////////////////////////////////////////////


BOOL WINAPI consoleHandler(DWORD signal) {

	//si se detecta sinal de peche da aplicación, se devinculan todos os obxectos rexistrados no servicio de nomes
	if (signal == CTRL_C_EVENT || signal == CTRL_CLOSE_EVENT || signal == RIGHT_CTRL_PRESSED) {
		orb->shutdown(true);
		
		cout << "EXCEPCION DONE" << endl;
	}

	return TRUE;
}

//////////////////////////////////////////////////////////////////////

int main(int argc, char **argv){

	//CAPTURANSE AS SINALES DA APLICACIÓN
	if (!SetConsoleCtrlHandler(consoleHandler, TRUE)) {
		printf("\nERROR: Could not set control handler");
		return 1;
	}

	//INICIALIZACIÓN DA BD

	database.openConnection(&db);
	database.initialize();
	database.createTables(db);
	//SERIA INTERESANTE MIRAR COMO FACER MUTEXS EN c++ PARA CONTROLAR O USO DO OBXECTO CONEXION DA BASE DE DATOS
	
	try {
		//inicialización e xestión de CORBA
		orb = CORBA::ORB_init(argc, argv);
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

		//obtense un string co IOR para depuración
		CORBA::String_var sior(orb->object_to_string(obj));
		cout << sior << endl;

		//rexistrase o obxecto, en base a referencia obtida previamente, no servicio de nombre

		if (!bindObjectToName(orb, obj,"User"))
			return 1;

		//activase o poa
		PortableServer::POAManager_var pman = poa->the_POAManager();
		pman->activate();
		

		//lánzase o orb
		orb->run();

		//FALTA CONTROLAR A DESVINCULACION DOS OBXECTOS

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

static CORBA::Boolean unbindObjectfromName(CORBA::ORB_ptr orb, CORBA::Object_ptr objref, const char* bindName) {
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
		objectName[0].id = bindName;   // string copied
									   //objectName[0].kind = (const char*) "Object"; // string copied

		try {
			rootContext->unbind(objectName);
		}
		catch (CosNaming::NamingContext::NotFound& ex) {
			
		}
		
	}
	catch (CORBA::TRANSIENT& ex) {
		
		return 0;
	}
	catch (CORBA::SystemException& ex) {
		
		return 0;
	}
	return 1;
}