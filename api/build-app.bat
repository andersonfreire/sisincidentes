@echo off
echo.
echo ============================================================
echo   SisIncidentes - Build do Back-end
echo ============================================================
echo.

echo [+] Limpando e gerando pacote JAR...
call .\mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [!] ERRO: Falha na compilação do Maven.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [+] Build concluído com sucesso!
echo [+] O arquivo JAR está em: api\target\sisincidentes-0.0.1-SNAPSHOT.jar
echo.
echo Para rodar em PRODUCAO execute:
echo java -jar target\sisincidentes-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
echo.
pause
