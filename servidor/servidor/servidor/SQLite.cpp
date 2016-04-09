#include "stdafx.h"
#include "SQLite.h"

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

SQLite::SQLite(){
	this->dbName = "MyDB.db";
	
	this->createTableUsuarios = "CREATE TABLE "+TABLE_USUARIOS::NOMBRE+"(id INTEGER AUTO INCREMENT PRIMARY KEY, "+TABLE_USUARIOS::NOMBRE+" STRING, " + TABLE_USUARIOS::EMAIL + " STRING, " + TABLE_USUARIOS::HASH + " STRING, " + TABLE_USUARIOS::SALT + " STRING, " + TABLE_USUARIOS::AVATAR + " STRING);";
	this->createTableAmigos = "CREATE TABLE " + TABLE_AMIGOS::TABLE_NAME+"("+TABLE_AMIGOS::ID_ORIGEN+" INTEGER,"+TABLE_AMIGOS::ID_DESTINO+" INTEGER, FOREIGN KEY ("+ TABLE_AMIGOS::ID_ORIGEN +") REFERENCES "+ TABLE_USUARIOS::TABLE_NAME +"("+ TABLE_USUARIOS::ID +") ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (" + TABLE_AMIGOS::ID_DESTINO + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY("+ TABLE_AMIGOS::ID_ORIGEN +","+ TABLE_AMIGOS::ID_DESTINO +"));";
	this->createTablePeticionesAmistad = "CREATE TABLE " + TABLE_PETICIONES_AMISTAD::TABLE_NAME + "(" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + " INTEGER," + TABLE_PETICIONES_AMISTAD::ID_DESTINO + " INTEGER, FOREIGN KEY (" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (" + TABLE_PETICIONES_AMISTAD::ID_DESTINO + ") REFERENCES " + TABLE_USUARIOS::TABLE_NAME + "(" + TABLE_USUARIOS::ID + ") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY(" + TABLE_PETICIONES_AMISTAD::ID_ORIGEN + "," + TABLE_PETICIONES_AMISTAD::ID_DESTINO + "));";

}

SQLite::~SQLite()
{
	
}


int SQLite::openConnection(sqlite3 **db){
	int rc;
		
	// Open Database
	cout << "Opening MyDb.db ..." << endl;

	rc = sqlite3_open("MyDb.db", db);
		
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

