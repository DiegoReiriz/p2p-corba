#include "stdafx.h"
#include "SQLite.h"

using namespace SQLiteContract;

void SQLite::createTables(sqlite3 * db)
{
	int rc;
	char* error;
	
	cout << "Creating Tables ..." << endl;
	rc = sqlite3_exec(db, this->createTableUsuarios.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;
	}
	else
	{
		rc = sqlite3_exec(db, this->createTableAmigos.c_str(), NULL, NULL, &error);
		if (rc)
		{
			cerr << "Error executing second SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
			sqlite3_free(error);
		}
		else
		{
			rc = sqlite3_exec(db, this->createTablePeticionesAmistad.c_str(), NULL, NULL, &error);
			if (rc)
			{
				cerr << "Error executing third SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
				sqlite3_free(error);
			}
			else
			{
				cout << "Created All Tables." << endl << endl;
			}
		}
	}

}

bool SQLite::insertarUsuario(const char* nombre, const char* email, const char* hash, const char* salt, const char* avatar, sqlite3 * db) {

	int rc;
	char* error;

	std::string insertSQL = "INSERT INTO " + TABLE_USUARIOS::TABLE_NAME +
		"(" + TABLE_USUARIOS::ID + ","+ TABLE_USUARIOS::NOMBRE +","+ TABLE_USUARIOS::EMAIL +","+ TABLE_USUARIOS::HASH+ ","+ TABLE_USUARIOS::SALT +","+ TABLE_USUARIOS::AVATAR +")"+
		" VALUES(null,'"+nombre+"','"+email+"','"+hash+"','"+salt+"','"+avatar+"');";

	cout << "Inserting User ..." << endl;
	cout << insertSQL << endl << endl;

	rc = sqlite3_exec(db, insertSQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	return true;
}



bool SQLite::alterarUsuario(chat::VOUser usuario, sqlite3 * db) {

	int rc;
	char* error;

	std::string updateSQL = "UPDATE " + TABLE_USUARIOS::TABLE_NAME +
		" SET " +
		TABLE_USUARIOS::NOMBRE + "='" + std::string(usuario.nombre) + "', " +
		TABLE_USUARIOS::EMAIL + "='" + std::string(usuario.email) + "', " +
		TABLE_USUARIOS::HASH + "='" + std::string(usuario.hash) + "', " +
		TABLE_USUARIOS::SALT + "='" + std::string(usuario.salt) + "', " +
		TABLE_USUARIOS::AVATAR + "='" + std::string(usuario.avatar) +
		"' WHERE " + TABLE_USUARIOS::ID +"="+ std::to_string(usuario.id) + ";";

	cout << "Updating User ..." << endl;
	cout << updateSQL << endl << endl;

	rc = sqlite3_exec(db, updateSQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	return true;
}


bool SQLite::borrarUsuario(chat::VOUser usuario, sqlite3 * db) {

	int rc;
	char* error;

	std::string updateSQL = "DELETE FROM "+ TABLE_USUARIOS::TABLE_NAME +" where "+ TABLE_USUARIOS::ID +"='"+ std::to_string(usuario.id) +"';";

	cout << "Updating User ..." << endl;
	cout << updateSQL << endl << endl;

	rc = sqlite3_exec(db, updateSQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	return true;
}

bool SQLite::crearPeticionAmistad(chat::VOUser origen,chat::VOUser destino, sqlite3 * db) {

	int rc;
	char* error;

	std::string updateSQL = "INSERT INTO "+TABLE_PETICIONES_AMISTAD::TABLE_NAME+
		" values("+ std::to_string(origen.id) +","+ std::to_string(destino.id) +");";

	cerr<< "Inserting Friend Request ..." << endl;
	cout << updateSQL << endl << endl;

	rc = sqlite3_exec(db, updateSQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	return true;
}

bool SQLite::borrarPeticionAmistad(chat::VOUser origen, chat::VOUser destino, sqlite3 * db) {

	int rc;
	char* error;

	std::string updateSQL = "DELETE FROM "+TABLE_PETICIONES_AMISTAD::TABLE_NAME+
		" WHERE "+TABLE_PETICIONES_AMISTAD::ID_ORIGEN+"="+ std::to_string(origen.id) +
		" AND " + 
		TABLE_PETICIONES_AMISTAD::ID_ORIGEN + "=" + std::to_string(origen.id) + ";";

	cout << "Deleting friend request ..." << endl;
	cout << updateSQL << endl << endl;

	rc = sqlite3_exec(db, updateSQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	return true;
}

bool SQLite::insertarAmigo(chat::VOUser origen, chat::VOUser destino, sqlite3 * db) {

	int rc;
	char* error;

	std::string SQL = "INSERT INTO "+TABLE_AMIGOS::TABLE_NAME+" values("+std::to_string(origen.id)+","+ std::to_string(destino.id) +");";
//INSERT INTO Amigos values(intiddestaqui, intidoraqui);";

	cout << "Inserting friend ..." << endl;
	cout << SQL << endl << endl;

	rc = sqlite3_exec(db, SQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	SQL = "INSERT INTO " + TABLE_AMIGOS::TABLE_NAME + " values(" + std::to_string(destino.id) + "," + std::to_string(origen.id) + ");";
	//INSERT INTO Amigos values(intiddestaqui, intidoraqui);";

	cout << "Inserting friend ..." << endl;
	cout << SQL << endl << endl;

	rc = sqlite3_exec(db, SQL.c_str(), NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing first SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		cout << "Tables are already created" << endl;

		return false;
	}

	return true;
}

list<chat::VOUser>* SQLite::res2User(int rows, int columns, char** result) {
	
	list<chat::VOUser>* usuarios=new list<chat::VOUser>();
	
	for (int cellPosition = 6; cellPosition <= rows*columns; cellPosition+=6)
	{
		chat::VOUser *user = new chat::VOUser();

		user->id = atoi(result[cellPosition + 0]);
		user->nombre = result[cellPosition + 1];
		user->email = result[cellPosition + 2];
		user->hash = result[cellPosition + 3];
		user->salt = result[cellPosition + 4];
		user->avatar = result[cellPosition + 5];

		usuarios->push_back(*user);
	}

	return usuarios;

}

void SQLite::obterUsuarios(sqlite3 *db) {

	int rc=0;
	char* error;

	std::string sqlSelect = "SELECT * FROM "+ TABLE_USUARIOS::TABLE_NAME +";";
	char **results = NULL;
	int rows, columns;
	sqlite3_get_table(db, sqlSelect.c_str(), &results, &rows, &columns, &error);
	if (rc)
	{
		cerr << "Error executing SQLite3 query: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
	}
	else
	{

		/*====================================* 
		 |                                    |
		 |   ATENCIÖN  ALERTA POR SUBNORMAL!  |
		 |                                    |
		 |     LIBERAR A LISTA DE USUARIOS    |
		 |                                    |
		 |  A memoria das estructuras se ge-  |
		 |    nera mediante o operador new    |
		 |                                    |
		 *====================================*/

		std::list<chat::VOUser>* lista = res2User(rows, columns, results);
		for (std::list<chat::VOUser>::iterator itr = lista->begin(); itr != lista->end();/*nothing*/) {
		
			cout << (*itr).id << (*itr).nombre << (*itr).email << (*itr).hash << (*itr).salt << (*itr).avatar << endl;

			++itr;
		}

	}
	sqlite3_free_table(results);

}

list<chat::VOUser>* SQLite::obterAmigos(chat::VOUser usuario, sqlite3 * db)
{

	std::list<chat::VOUser>* lista;

	int rc = 0;
	char* error;

	std::string sqlSelect = "SELECT * FROM " + TABLE_USUARIOS::TABLE_NAME + " WHERE " + TABLE_USUARIOS::ID + "= (SELECT " +
		TABLE_AMIGOS::ID_DESTINO + " FROM " + TABLE_AMIGOS::TABLE_NAME + " WHERE " + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + " = " + std::to_string(usuario.id) + ");";
	char **results = NULL;
	int rows, columns;
	sqlite3_get_table(db, sqlSelect.c_str(), &results, &rows, &columns, &error);
	if (rc)
	{
		cerr << "Error executing SQLite3 query: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		return false;
	}
	else
	{

		/*====================================*
		|                                    |
		|   ATENCIÖN  ALERTA POR SUBNORMAL!  |
		|                                    |
		|     LIBERAR A LISTA DE USUARIOS    |
		|                                    |
		|  A memoria das estructuras se ge-  |
		|    nera mediante o operador new    |
		|                                    |
		*====================================*/

		if (rows < 1)
			return false;


		lista = res2User(rows, columns, results);
		for (std::list<chat::VOUser>::iterator itr = lista->begin(); itr != lista->end();/*nothing*/) {

			cout << (*itr).id << (*itr).nombre << (*itr).email << (*itr).hash << (*itr).salt << (*itr).avatar << endl;

			++itr;
		}

	}
	sqlite3_free_table(results);

	return lista;
}


list<chat::VOUser>* SQLite::obterPeticionsAmistadPendientes(chat::VOUser usuario, sqlite3 * db)
{

	std::list<chat::VOUser>* lista;

	int rc = 0;
	char* error;

	std::string sqlSelect = "SELECT * FROM " + TABLE_USUARIOS::TABLE_NAME + " WHERE " + TABLE_USUARIOS::ID + " SELECT " +
		TABLE_PETICIONES_AMISTAD::ID_ORIGEN + " FROM " + TABLE_PETICIONES_AMISTAD::TABLE_NAME + " WHERE " + TABLE_PETICIONES_AMISTAD::ID_DESTINO + " = " + std::to_string(usuario.id) + ";";
	char **results = NULL;
	int rows, columns;
	sqlite3_get_table(db, sqlSelect.c_str(), &results, &rows, &columns, &error);
	if (rc)
	{
		cerr << "Error executing SQLite3 query: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		return false;
	}
	else
	{

		/*====================================*
		|                                    |
		|   ATENCIÖN  ALERTA POR SUBNORMAL!  |
		|                                    |
		|     LIBERAR A LISTA DE USUARIOS    |
		|                                    |
		|  A memoria das estructuras se ge-  |
		|    nera mediante o operador new    |
		|                                    |
		*====================================*/

		if (rows < 1)
			return false;


		lista = res2User(rows, columns, results);
		for (std::list<chat::VOUser>::iterator itr = lista->begin(); itr != lista->end();/*nothing*/) {

			cout << (*itr).id << (*itr).nombre << (*itr).email << (*itr).hash << (*itr).salt << (*itr).avatar << endl;

			++itr;
		}

	}
	sqlite3_free_table(results);

	return lista;
}

list<chat::VOUser>* SQLite::buscarPorNombreyEmail(chat::VOUser usuario, chat::VOUser busqueda,int limit, sqlite3 * db)
{

	std::list<chat::VOUser>* lista;

	int rc = 0;
	char* error;

	std::string sqlSelect = "SELECT DISTINCT * FROM " + TABLE_USUARIOS::TABLE_NAME +
		" WHERE ((" + TABLE_USUARIOS::NOMBRE + " LIKE \"%" + (char *)busqueda.nombre + "%\"  OR " + TABLE_USUARIOS::EMAIL + " LIKE \"%" + (char *)busqueda.email + "%\") AND " +
		TABLE_USUARIOS::ID + " !=" + std::to_string(usuario.id) + " ) AND " + TABLE_USUARIOS::ID +" NOT IN (SELECT "+TABLE_AMIGOS::ID_DESTINO+
		" FROM "+ TABLE_AMIGOS::TABLE_NAME +" WHERE "+ TABLE_AMIGOS::ID_ORIGEN +"="+ std::to_string(usuario.id)+") AND " + TABLE_USUARIOS::ID + 
		" NOT IN ( SELECT "+TABLE_PETICIONES_AMISTAD::ID_DESTINO+" FROM "+ TABLE_PETICIONES_AMISTAD::TABLE_NAME +" WHERE "+
		TABLE_PETICIONES_AMISTAD::ID_ORIGEN+"="+ std::to_string(usuario.id) +" ) LIMIT "+ std::to_string(limit) +";";
	
	char **results = NULL;
	int rows, columns;
	sqlite3_get_table(db, sqlSelect.c_str(), &results, &rows, &columns, &error);
	if (rc)
	{
		cerr << "Error executing SQLite3 query: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		return false;
	}
	else 
	{

		/*====================================*
		|                                    |
		|   ATENCIÖN  ALERTA POR SUBNORMAL!  |
		|                                    |
		|     LIBERAR A LISTA DE USUARIOS    |
		|                                    |
		|  A memoria das estructuras se ge-  |
		|    nera mediante o operador new    |
		|                                    |
		*====================================*/

		if (rows < 1)
			return false;


		lista = res2User(rows, columns, results);
		for (std::list<chat::VOUser>::iterator itr = lista->begin(); itr != lista->end();/*nothing*/) {

			cout << (*itr).id << (*itr).nombre << (*itr).email << (*itr).hash << (*itr).salt << (*itr).avatar << endl;

			++itr;
		}

	}
	sqlite3_free_table(results);

	return lista;
}

bool SQLite::obterUsuario(chat::VOUser &usuario, sqlite3 * db)
{

	int rc = 0;
	char* error;

	std::string sqlSelect = "SELECT * FROM " + TABLE_USUARIOS::TABLE_NAME + " WHERE "+ TABLE_USUARIOS::EMAIL +"==\""+ std::string(usuario.email) +"\";";
	char **results = NULL;
	int rows, columns;
	sqlite3_get_table(db, sqlSelect.c_str(), &results, &rows, &columns, &error);
	if (rc)
	{
		cerr << "Error executing SQLite3 query: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
		return false;
	}
	else
	{

		/*====================================*
		|                                    |
		|   ATENCIÖN  ALERTA POR SUBNORMAL!  |
		|                                    |
		|     LIBERAR A LISTA DE USUARIOS    |
		|                                    |
		|  A memoria das estructuras se ge-  |
		|    nera mediante o operador new    |
		|                                    |
		*====================================*/

		if (rows < 1)
			return false;

		std::list<chat::VOUser>* lista = res2User(rows, columns, results);
		for (std::list<chat::VOUser>::iterator itr = lista->begin(); itr != lista->end();/*nothing*/) {

			usuario.id = (*itr).id;
			usuario.nombre = (*itr).nombre;
			usuario.email = (*itr).email;
			
			if (strcmp(usuario.hash, (*itr).hash) == 0){
				usuario.hash = (*itr).hash;
			}
			else{
				return false;
			}

			usuario.salt = (*itr).salt;
			usuario.avatar = (*itr).avatar;

			++itr;
		}

	}
	sqlite3_free_table(results);

	return true;
}

SQLite::SQLite(){
	this->dbName = "MyDB.db";
	
	this->createTableUsuarios = "CREATE TABLE " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + " INTEGER PRIMARY KEY, " + TABLE_USUARIOS::NOMBRE + " STRING, " + TABLE_USUARIOS::EMAIL + " STRING UNIQUE, " + TABLE_USUARIOS::HASH + " STRING, " + TABLE_USUARIOS::SALT + " STRING, " + TABLE_USUARIOS::AVATAR + " STRING);";
	this->createTableAmigos = "CREATE TABLE " + TABLE_AMIGOS::TABLE_NAME+"("+TABLE_AMIGOS::ID_ORIGEN+" INTEGER,"+TABLE_AMIGOS::ID_DESTINO+" INTEGER, FOREIGN KEY ("+ TABLE_AMIGOS::ID_ORIGEN +") REFERENCES "+ TABLE_USUARIOS::TABLE_NAME +"("+ TABLE_USUARIOS::ID +") ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (" + TABLE_AMIGOS::ID_DESTINO + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY("+ TABLE_AMIGOS::ID_ORIGEN +","+ TABLE_AMIGOS::ID_DESTINO +"));";
	this->createTablePeticionesAmistad = "CREATE TABLE " + TABLE_PETICIONES_AMISTAD::TABLE_NAME + "(" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + " INTEGER," + TABLE_PETICIONES_AMISTAD::ID_DESTINO + " INTEGER, FOREIGN KEY (" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (" + TABLE_PETICIONES_AMISTAD::ID_DESTINO + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY(" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + "," + TABLE_PETICIONES_AMISTAD::ID_DESTINO + "));";

}

SQLite::~SQLite()
{
	
}

void SQLite::initialize() {
	this->dbName = "MyDB.db";

	this->createTableUsuarios = "CREATE TABLE " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + " INTEGER PRIMARY KEY, " + TABLE_USUARIOS::NOMBRE + " STRING, " + TABLE_USUARIOS::EMAIL + " STRING UNIQUE, " + TABLE_USUARIOS::HASH + " STRING, " + TABLE_USUARIOS::SALT + " STRING, " + TABLE_USUARIOS::AVATAR + " STRING);";
	this->createTableAmigos = "CREATE TABLE " + TABLE_AMIGOS::TABLE_NAME + "(" + TABLE_AMIGOS::ID_ORIGEN + " INTEGER," + TABLE_AMIGOS::ID_DESTINO + " INTEGER, FOREIGN KEY (" + TABLE_AMIGOS::ID_ORIGEN + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (" + TABLE_AMIGOS::ID_DESTINO + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY(" + TABLE_AMIGOS::ID_ORIGEN + "," + TABLE_AMIGOS::ID_DESTINO + "));";
	this->createTablePeticionesAmistad = "CREATE TABLE " + TABLE_PETICIONES_AMISTAD::TABLE_NAME + "(" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + " INTEGER," + TABLE_PETICIONES_AMISTAD::ID_DESTINO + " INTEGER, FOREIGN KEY (" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (" + TABLE_PETICIONES_AMISTAD::ID_DESTINO + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY(" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + "," + TABLE_PETICIONES_AMISTAD::ID_DESTINO + "));";
}

int SQLite::openConnection(sqlite3 **db){
	int rc;
		
	// Open Database
	cout << "Opening MyDb.db ..." << endl;

	rc = sqlite3_open(this->getDataBaseName().c_str(), db);
		
	if (rc)
	{
		cerr << "Error opening SQLite3 database: " << sqlite3_errmsg(*db) << endl << endl;
		sqlite3_close(*db);
		return 1;
	}
	else
	{
		cout << "Opened MyDb.db." << endl << endl;
	}

	return rc;
}

void SQLite::closeConnection(sqlite3 *db) {
	// Close Database
	cout << "Closing MyDb.db ..." << endl;
	sqlite3_close(db);
	cout << "Closed MyDb.db" << endl << endl;
}

std::string SQLite::getDataBaseName()
{
	return dbName;
}

void SQLite::setDataBaseName(std::string name)
{
	this->dbName = name;
}

