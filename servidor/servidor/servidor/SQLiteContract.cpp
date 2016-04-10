#include "stdafx.h"
#include "SQLiteContract.h"

namespace SQLiteContract
{
	
	const std::string TABLE_USUARIOS::TABLE_NAME = "Usuarios";
	const std::string TABLE_USUARIOS::ID = "id";
	const std::string TABLE_USUARIOS::NOMBRE = "nombre";
	const std::string TABLE_USUARIOS::EMAIL = "email";
	const std::string TABLE_USUARIOS::HASH = "hash";
	const std::string TABLE_USUARIOS::SALT = "salt";
	const std::string TABLE_USUARIOS::AVATAR = "avatar";

	const std::string TABLE_AMIGOS::TABLE_NAME = "Amigos";
	const std::string TABLE_AMIGOS::ID_ORIGEN = "id_origen";
	const std::string TABLE_AMIGOS::ID_DESTINO = "id_destino";
	
	const std::string TABLE_PETICIONES_AMISTAD::TABLE_NAME = "PeticionesAmistad";
	const std::string TABLE_PETICIONES_AMISTAD::ID_ORIGEN = "id_origen";
	const std::string TABLE_PETICIONES_AMISTAD::ID_DESTINO = "id_destino";
	const std::string TABLE_PETICIONES_AMISTAD::MENSAJE = "mensaje";
	const std::string TABLE_PETICIONES_AMISTAD::FECHA = "fecha";
	
}