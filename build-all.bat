@echo off
echo Building Common Library...
cd backend\common-lib
call mvn clean install -DskipTests
cd ..\..

echo Building API Gateway...
cd backend\api-gateway
call mvn clean install -DskipTests
cd ..\..

echo Building Core Services...
echo (You can run individual services now)
pause
