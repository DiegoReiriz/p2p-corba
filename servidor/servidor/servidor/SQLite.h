#pragma once

#include <iostream>
#include "sqlite3.h"
#include "SQLiteContract.h"
#include <list>
#include "interface.hh"

using namespace std;
using namespace SQLiteContract;
	
class SQLite
{
	private:
		list<chat::VOUser>* res2User(int rows, int columns, char** result);
		std::string dbName;
		std::string createTableUsuarios;
		std::string createTableAmigos;
		std::string createTablePeticionesAmistad;

	public:
		SQLite();
		~SQLite();

		void initialize();
		int openConnection(sqlite3 **db);
		void closeConnection(sqlite3 *db);
		void createTables(sqlite3 *db);
		bool insertarUsuario(const char* nombre, const char* email, const char* hash, const char* salt, const char* avatar, sqlite3 * db);
		void obterUsuarios(sqlite3 *db);
		list<chat::VOUser>* obterAmigos(chat::VOUser usuario, sqlite3 * db);
		list<chat::VOUser>* obterPeticionsAmistadPendientes(chat::VOUser usuario, sqlite3 * db);
		bool alterarUsuario(chat::VOUser usuario, sqlite3 * db);
		bool obterUsuario(chat::VOUser &usuario,sqlite3 *db);
		bool crearPeticionAmistad(chat::VOUser origen, chat::VOUser destino, sqlite3 *db);
		bool borrarPeticionAmistad(chat::VOUser origen, chat::VOUser destino, sqlite3 *db);
		bool insertarAmigo(chat::VOUser origen, chat::VOUser destino, sqlite3 *db);

		std::string getDataBaseName();
		void setDataBaseName(std::string name);
	
};

