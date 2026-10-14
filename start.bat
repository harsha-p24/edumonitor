@echo off
setlocal
title EduMonitor - Start

rem ---- Edit PROJECT_DIR if your project lives somewhere else inside WSL ----
set "PROJECT_DIR=~/edumonitor"
set "APP_URL=http://localhost:8081"

echo ==============================================
echo   EduMonitor - one-click start
echo ==============================================
echo.

where wsl >nul 2>&1
if errorlevel 1 (
    echo [ERROR] WSL was not found. Install WSL and Ubuntu first.
    goto :fail
)

echo [1/3] Checking Docker...
set /a tries=0

:wait_docker
wsl bash -lc "docker info >/dev/null 2>&1"
if not errorlevel 1 goto :docker_ok
if %tries%==0 (
    if exist "%ProgramFiles%\Docker\Docker\Docker Desktop.exe" (
        echo       Docker is not running - starting Docker Desktop...
        start "" "%ProgramFiles%\Docker\Docker\Docker Desktop.exe"
    )
)
set /a tries+=1
if %tries% geq 60 (
    echo [ERROR] Docker did not become ready in time. Start Docker Desktop manually, then run this again.
    goto :fail
)
timeout /t 3 /nobreak >nul
goto :wait_docker

:docker_ok
echo       Docker is ready.
echo.
echo [2/3] Starting EduMonitor containers - the first run can take several minutes...
wsl bash -lc "cd %PROJECT_DIR% && bash scripts/start.sh"
if errorlevel 1 (
    echo.
    echo [ERROR] Startup failed - see the messages above.
    goto :fail
)

echo.
echo [3/3] Opening the app in your browser...
start "" "%APP_URL%"
echo.
echo   EduMonitor is running at %APP_URL%
echo   Faculty login - dev data only: shruthi.mg@edumonitor.local / password123
echo   The containers keep running in the background after you close this window.
echo.
pause
endlocal
exit /b 0

:fail
echo.
pause
endlocal
exit /b 1
