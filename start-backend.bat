@echo off
echo Starting InternTrack backend (Spring Boot)...
cd /d "%~dp0backend"
call mvn spring-boot:run
