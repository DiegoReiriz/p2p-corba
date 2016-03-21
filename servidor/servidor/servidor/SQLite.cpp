#include "stdafx.h"
#include "SQLite.h"

void SQLite::createTables(sqlite3 * db)
{
	int rc;
	char *error;

	cout << "Creating MyTable ..." << endl;
	const char *sqlCreateTable = "";
	rc = sqlite3_exec(db, sqlCreateTable, NULL, NULL, &error);
	if (rc)
	{
		cerr << "Error executing SQLite3 statement: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_free(error);
	}
	else
	{
		cout << "Created MyTable." << endl << endl;
	}

}

SQLite::SQLite(){
	this->dbName = "MyDB.db";
	this->createTableUsuarios = "CREATE TABLE "+TABLE_USUARIOS::NOMBRE+"(id INTEGER PRIMARY KEY, value STRING);";
	this->createTableAmigos = "";
	this->createTablePeticionesAmistad = "";
}

SQLite::~SQLite()
{
}


int SQLite::openConnection(sqlite3 *db){
	int rc;
		
	// Open Database
	cout << "Opening MyDb.db ..." << endl;

	rc = sqlite3_open("MyDb.db", &db);
		
	//QUITAR DE AQUI
	if (rc)
	{
		cerr << "Error opening SQLite3 database: " << sqlite3_errmsg(db) << endl << endl;
		sqlite3_close(db);
		return 1;
	}
	else
	{
		cout << "Opened MyDb.db." << endl << endl;
	}

	return rc;
}

std::string SQLite::getDataBaseName()
{
	return dbName;
}

void SQLite::setDataBaseName(std::string name)
{
	this->dbName = name;
}

