#pragma once

#include <string>

namespace SQLiteContract
{
	class TABLE_USUARIOS 
	{
		public:
			static const std::string TABLE_NAME;
			static const std::string ID;
			static const std::string NOMBRE;
			static const std::string EMAIL;
			static const std::string HASH;
			static const std::string SALT;
			static const std::string AVATAR;
	};

	class TABLE_AMIGOS {
		public:
			static const std::string TABLE_NAME;
			static const std::string ID_ORIGEN;
			static const std::string ID_DESTINO;
	};

	class TABLE_PETICIONES_AMISTAD {
		public:
			static const std::string TABLE_NAME;
			static const std::string ID_ORIGEN;
			static const std::string ID_DESTINO;
			static const std::string MENSAJE;
			static const std::string FECHA;
	};
	
}

