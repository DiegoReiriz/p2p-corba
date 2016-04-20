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
CORBA::Object_var obj;
std::list<chat::VOUser> usuariosActivos;
sqlite3 *db;
SQLite database;
HANDLE ghMutex;

static CORBA::Boolean bindObjectToName(CORBA::ORB_ptr, CORBA::Object_ptr,const char*);
static CORBA::Boolean unbindObjectfromName(CORBA::ORB_ptr, CORBA::Object_ptr, const char*);

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
		virtual ::CORBA::Boolean deleteUser(const ::chat::VOUser& usuario);
		virtual ::chat::listaUsuarios* getFrindList(const ::chat::VOUser& usuario);
		virtual ::chat::listaUsuarios* getUserLike(const ::chat::VOUser& usuario, const ::chat::VOUser& busqueda);
		virtual ::CORBA::Boolean newFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny);
		virtual ::CORBA::Boolean resolveFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny, ::CORBA::Boolean accept);

};

::CORBA::Boolean userManager_i::signIn(::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;
	::CORBA::Boolean resu = true;
	
	cout << "SIGN IN" << endl;
	cout << "=======" << endl;
	cout << usuario.id << endl;
	cout << usuario.nombre << endl;
	cout << usuario.email << endl;
	cout << usuario.hash << endl;
	cout << usuario.salt << endl;
	cout << usuario.avatar << endl;

	res=database.obterUsuario(usuario,db);

	if (res) {
		for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end() && res;/*nothing*/) {
			if (itr->id == usuario.id)
				resu = false;
			++itr;
		}
	}

	if (res && resu) {
		//AVISA AOS AMIGOS CONECTADOS DE QUE SE ACABA DE CONECTAR
		

		list<chat::VOUser>* amigos = database.obterAmigos(usuario, db);
		if(amigos != NULL && usuariosActivos.size() > 0){
			int i = 0;
			for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end();/*nothing*/) {
				bool found = false;
				int j = 0;
				for (std::list<chat::VOUser>::iterator itr2 = amigos->begin(); j<amigos->size() && !found;/*nothing*/) {
					if (itr->id == itr2->id){

						::chat::VOUser* user = new ::chat::VOUser;

						user->id = usuario.id;
						user->nombre = usuario.nombre;
						user->email = usuario.email;
						user->hash = "";
						user->salt = "";
						user->avatar = usuario.avatar;
						user->chat = usuario.chat;

						itr->callback->notifyFriendIn(*user);
						found = true;
					}

					++itr2;
					j++;
				}
				++itr;
				++i;
			}
		}

		usuariosActivos.push_back(usuario);
		list<chat::VOUser>* peticiones = database.obterPeticionsAmistadPendientes(usuario,db);
		if(peticiones != NULL && peticiones->size() > 0){
			for (std::list<chat::VOUser>::iterator itr = peticiones->begin(); itr != peticiones->end();/*nothing*/) {

				::chat::VOUser* user = new ::chat::VOUser;

				user->id = itr->id;
				user->nombre = itr->nombre;
				user->email = itr->email;
				user->hash = "";
				user->salt = "";
				user->avatar = itr->avatar;
				user->chat = itr->chat;

				usuario.callback->notifyFriendRequest(*user);
			
				++itr;
			}
		}
	}

	return res;
}
::CORBA::Boolean userManager_i::signOut(const ::chat::VOUser& usuario) {
	::CORBA::Boolean res = false;
	
	if(usuariosActivos.size() > 0){
		for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end();/*nothing*/) {
		
			if ( (*itr).id == usuario.id )
				usuariosActivos.erase(itr);

			++itr;
		}

		list<chat::VOUser>* amigos = database.obterAmigos(usuario, db);
		if(amigos != NULL){
			int i = 0;
			for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end();/*nothing*/) {
				bool found = false;
				int j = 0;
				for (std::list<chat::VOUser>::iterator itr2 = amigos->begin(); j<amigos->size() && !found;/*nothing*/) {
					if (itr->id == itr2->id) {
						itr->callback->notifyFriendOut(usuario);
						found = true;
					}

					++itr2;
					j++;
				}
				++itr;
				++i;
			}
		}
	}

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
	DWORD dwWaitResult = WaitForSingleObject(
		ghMutex,    // handle to mutex
		INFINITE);  // no time-out interval

	::CORBA::Boolean res = false;

	res = database.alterarUsuario(usuario,db);

	ReleaseMutex(ghMutex);

	return res;
}

::CORBA::Boolean userManager_i::deleteUser(const ::chat::VOUser& usuario) {
	DWORD dwWaitResult = WaitForSingleObject(
		ghMutex,    // handle to mutex
		INFINITE);  // no time-out interval

	::CORBA::Boolean res = false;

	res = database.borrarUsuario(usuario, db);
	
	if(res)
		res = signOut(usuario);


	ReleaseMutex(ghMutex);

	return res;
}

::chat::listaUsuarios* userManager_i::getFrindList(const ::chat::VOUser& usuario) {
	
	DWORD dwWaitResult = WaitForSingleObject(
		ghMutex,    // handle to mutex
		INFINITE);  // no time-out interval


	chat::listaUsuarios* lista = new chat::listaUsuarios;
	lista->length(usuariosActivos.size());
	
	list<chat::VOUser>* amigos=database.obterAmigos(usuario,db);

	int i = 0;
	int k = 0;
	if(amigos != NULL && usuariosActivos.size()>0){
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
				user->hash = "";
				user->salt = "";
				user->avatar = itr->avatar;
				user->chat = itr->chat;
				(*lista)[k] = *user;
				k++;
			}

			++itr;
			++i;
		}
	
		while (k < usuariosActivos.size()) {
			::chat::VOUser* user = new ::chat::VOUser;

			user->id = 0;
			user->nombre = "empty";
			user->email = "empty";
			user->hash = "empty";
			user->salt = "empty";
			user->avatar = "empty";
			(*lista)[k] = *user;
			k++;
		}
	}
	ReleaseMutex(ghMutex);

	return lista;
}

::CORBA::Boolean userManager_i::newFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny) {
	DWORD dwWaitResult = WaitForSingleObject(
		ghMutex,    // handle to mutex
		INFINITE);  // no time-out interval

	
	::CORBA::Boolean res = false;

	res=database.crearPeticionAmistad(origin, destiny, db);

	bool found = false;
	int i = 0;
	if(usuariosActivos.size() > 0){
		for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end() && !found;/*nothing*/) {
			if (itr->id == destiny.id) {
				itr->callback->notifyFriendRequest(origin);
				found = true;
			}

			++itr;
			++i;
		}
	}

	ReleaseMutex(ghMutex);

	return res;
}
::CORBA::Boolean userManager_i::resolveFriendRequest(const ::chat::VOUser& origin, const ::chat::VOUser& destiny, ::CORBA::Boolean accept) {
	DWORD dwWaitResult = WaitForSingleObject(
		ghMutex,    // handle to mutex
		INFINITE);  // no time-out interval


	::CORBA::Boolean res = false;

	if (accept) {	
		res = database.insertarAmigo(origin, destiny, db);

		bool found = false;
		if (usuariosActivos.size() > 0) {
			for (std::list<chat::VOUser>::iterator itr = usuariosActivos.begin(); itr != usuariosActivos.end() && !found;/*nothing*/) {

				if (itr->id == destiny.id) {
					::chat::VOUser* origen = new ::chat::VOUser;

					origen->id = origin.id;
					origen->nombre = origin.nombre;
					origen->email = origin.email;
					origen->hash = "";
					origen->salt = "";
					origen->avatar = origin.avatar;
					origen->chat = origin.chat;
					
					itr->callback->notifyFriendRequestReslution(*origen, true);
					itr->callback->notifyFriendIn(*origen);
					origin.callback->notifyFriendIn(*itr);
					
					found = true;
				}

				++itr;
			}
		}
	}
		
	res = database.borrarPeticionAmistad(origin, destiny, db);
	
	ReleaseMutex(ghMutex);

	return res;
}

::chat::listaUsuarios* userManager_i::getUserLike(const ::chat::VOUser& usuario, const ::chat::VOUser& busqueda) {
	DWORD dwWaitResult = WaitForSingleObject(
		ghMutex,    // handle to mutex
		INFINITE);  // no time-out interval


	int limit = 10;
	chat::listaUsuarios* lista = new chat::listaUsuarios;
	lista->length(0);

	list<chat::VOUser>* amigos = database.buscarPorNombreyEmail(usuario,busqueda,limit, db);
	if (amigos != NULL) {
		lista->length(amigos->size());

		int i = 0;
	
		for (std::list<chat::VOUser>::iterator itr = amigos->begin(); itr != amigos->end();/*nothing*/) {
		
			::chat::VOUser* user = new ::chat::VOUser;

			user->id = itr->id;
			user->nombre = itr->nombre;
			user->email = itr->email;
			user->hash = "";
			user->salt = "";
			user->avatar = itr->avatar;
			(*lista)[i] = *user;
		
			++itr;
			++i;
		}
	}

	ReleaseMutex(ghMutex);

	return lista;
}
//////////////////////////////////////////////////////////////////////


BOOL WINAPI consoleHandler(DWORD signal) {

	//si se detecta sinal de peche da aplicación, se devinculan todos os obxectos rexistrados no servicio de nomes
	if (signal == CTRL_C_EVENT || signal == CTRL_CLOSE_EVENT || signal == RIGHT_CTRL_PRESSED) {
		unbindObjectfromName(orb, obj, "User");
		
		orb->shutdown(true);
		
		cout << "Servidor cerrado correctamente" << endl;
	}

	return TRUE;
}

//////////////////////////////////////////////////////////////////////

int main(int argc, char **argv){


	ghMutex = CreateMutex(
		NULL,              // default security attributes
		FALSE,             // initially not owned
		NULL);             // unnamed mutex

	if (ghMutex == NULL)
	{
		printf("CreateMutex error: %d\n", GetLastError());
		return 1;
	}

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
		obj = orb->resolve_initial_references("RootPOA");
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

		CloseHandle(ghMutex);
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