@echo off
cls
rem Auto hot-reload: launches the client, then starts a Gradle compile watcher.
rem Save any .java file -> Gradle recompiles automatically -> Remold swaps it in.
rem Close the "remold-watch" window (Ctrl+C / Ctrl+D) when you finish.
cd /d "%~dp0"
start "remold-watch" cmd /k "timeout /t 25 >nul & gradlew compileJava -t --offline --console=plain"
gradlew runClient --offline
pause
