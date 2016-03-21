#pragma once

#include <iostream>
#include "sqlite3.h"
#include "SQLiteContract.h"

using namespace std;
using namespace SQLiteContract;
	
class SQLite
{
	private:
		std::string dbName;
		std::string createTableUsuarios;
		std::string createTableAmigos;
		std::string createTablePeticionesAmistad;
		
	public:
		SQLite();
		~SQLite();

		int openConnection(sqlite3 **db);
		void closeConnection(sqlite3 *db);
		void createTables(sqlite3 *db);

		std::string getDataBaseName();
		void setDataBaseName(std::string name);
	
};

