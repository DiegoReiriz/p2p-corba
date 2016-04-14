#start ./orbd -ORBInitialPort 2089 -ORB -defaultdb .\NamingService\ &
#start orbd -ORBInitialPort 2089 -J-verbose&
start ./servidor/omniORB-4.2.0/bin/x86_win32/omniNames.exe -ORBsupportBootstrapAgent 1
