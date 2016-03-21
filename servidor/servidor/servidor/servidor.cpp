// servidor.cpp: define el punto de entrada de la aplicación de consola.
//

#include "stdafx.h"
#include <ios>
#include <iostream>
#include "SQLite.h"

using namespace std;

int main(){
	
	sqlite3 *db;
	SQLite gestorDB;
	

	gestorDB.openConnection(&db);
	gestorDB.createTables(db);
	gestorDB.closeConnection(db);

	cin.get();

	return 0;

}

