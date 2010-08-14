call mvn clean
call mvn package -Dmaven.test.skip=true

set SRC=d:\eclipse\restfs
set DST=y:\tmp\restfs\target

del /q %DST%\*.jar
copy %SRC%\target\nofs.restfs*.jar %DST%

pause
