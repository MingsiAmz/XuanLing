@echo off
chcp | findstr /C:"65001" > nul && echo is utf-8 || chcp 65001
cls
call gradlew daemon --stop && call gradlew runData --offline
