@echo off
start java -Xmx1024m -jar -Dspring.profiles.active=dev build\libs\SIVAOS-Actions.jar &
