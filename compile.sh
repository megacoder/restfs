#!/bin/bash

mvn package -Dmaven.test.skip=true
NOFS_VERS=`cat pom.xml | grep -i "<nofs.version>" | sed 's/nofs\.version//g' | sed 's/<>//g' | sed 's/<\/>//g' | sed 's/ //g'`

mkdir -p lib

rm -f lib/nofs*.jar
rm -f lib/db4o*.jar

for ARTIFACT in nofs nofs.Common.Interfaces nofs.Db4o nofs.library nofs.Library.Annotations nofs.metadata.interfaces nofs.metadata.AnnotationDriver
do
	cp ~/.m2/repository/nofs/$ARTIFACT/$NOFS_VERS/$ARTIFACT-$NOFS_VERS.jar lib/
done

DB4O_VERS=`unzip -c lib/nofs.Db4o-$NOFS_VERS.jar META-INF/maven/nofs/nofs.Db4o/pom.xml | grep -i "<db4o.version>" | sed 's/db4o\.version//g' | sed 's/<>//g' | sed 's/<\/>//g'`

for ARTIFACT in db4o-bloat db4o-java5 db4o-nqopt
do
	SRCFILE=~/.m2/repository/com/db4o/$ARTIFACT/$DB4O_VERS/$ARTIFACT-$DB4O_VERS.jar
	SRCFILE=`echo $SRCFILE | sed 's/ //g'`
	cp $SRCFILE lib/
done

cp ~/.m2/repository/commons-logging/commons-logging/1.1.1/*.jar lib/
cp ~/.m2/repository/dom4j/dom4j/1.6.1/*.jar lib/
cp ~/.m2/repository/org/apache/jackrabbit/jackrabbit-webdav/2.1.0/*.jar lib/
cp ~/.m2/repository/org/apache/jackrabbit/jackrabbit-jcr-commons/2.1.0/*.jar lib/
cp ~/.m2/repository/org/slf4j/slf4j-api/1.6.0/*.jar lib/
cp ~/.m2/repository/org/slf4j/jcl-over-slf4j/1.5.8/*.jar lib/
cp ~/.m2/repository/commons-httpclient/commons-httpclient/3.0/*.jar lib/
cp ~/.m2/repository/commons-codec/commons-codec/1.2/*.jar lib/
cp ~/.m2/repository/org/antlr/antlr/3.1.3/*.jar lib/
cp ~/.m2/repository/org/antlr/antlr-runtime/3.1.3/*.jar lib/

