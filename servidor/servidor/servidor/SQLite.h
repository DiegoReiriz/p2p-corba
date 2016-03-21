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
		
		void createTables(sqlite3 *db);
		sqlite3 *db;

	public:
		SQLite();
		~SQLite();

		int openConnection();
		void closeConnection();

		std::string getDataBaseName();
		void setDataBaseName(std::string name);
	
};

