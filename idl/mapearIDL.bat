:: java
idlj -fall interface.idl
rm ../cliente/src/chat/*
cp chat ../cliente/src

:: entorno de pruebas de java
rm ../testNameServiceCorba/src/chat/*
mv chat ../testNameServiceCorba/src

:: c++
.\..\servidor\omniORB-4.2.0\bin\x86_win32\omniidl -bcxx interface.idl
mv interface.hh ../servidor/servidor/servidor
mv interfaceSK.cc ../servidor/servidor/servidor
