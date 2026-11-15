@echo off
chcp | findstr /C:"65001" > nul && echo is utf-8 || chcp 65001
call gradlew daemon --stop
cls
rem Auto hot-reload: launches the client, then starts a Gradle compile watcher.
rem Save any .java file -> Gradle recompiles automatically -> Remold swaps it in.
rem Close the "remold-watch" window (Ctrl+C / Ctrl+D) when you finish.
cd /d "%~dp0"
start "remold-watch" cmd /k "timeout /t 25 >nul & gradlew compileJava -t --offline --console=plain"
call gradlew runClient --offline
pause
