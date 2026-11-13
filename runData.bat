@echo off
cls
call gradlew daemon --stop && call gradlew runData --offline
