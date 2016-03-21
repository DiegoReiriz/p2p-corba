// servidor.cpp: define el punto de entrada de la aplicación de consola.
//

#include "stdafx.h"
#include <ios>
#include <iostream>
#include "SQLite.h"


using namespace std;

int main(){
		
	SQLite gestorDB;

	gestorDB.setDataBaseName("TEST");
	cout << gestorDB.getDataBaseName() << "\nPulse una tecla para continuar";	
	cin.get();


	cout << "Creando Tablas";
	
	cin.get();
	

	return 0;

}

