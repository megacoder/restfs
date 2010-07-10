#!/bin/bash

NOFS_VERS=`unzip -c target/nofs.restfs-*.jar META-INF/maven/nofs/nofs.restfs/pom.xml | grep -i "<nofs.version>" | sed 's/nofs\.version//g' | sed 's/<>//g' | sed 's/<\/>//g' | sed 's/ //g'`

echo "detected NOFS version: $NOFS_VERS"

DB4O_LIBS=`find . -name "db4o*.jar"`
NOFS_LIBS=`find . -name "nofs*$NOFS_VERS*.jar" | grep -v "restfs"`
RESTFS_JAR=`find . -name "nofs.restfs-*.jar"`
CLASSPATH_LIBS=`echo $DB4O_LIBS $NOFS_LIBS | sed 's/ /:/g'`
JAVAFS_LIB=`find . -name "libjavafs.so" | sed 's/\/libjavafs\.so//g'`

DRIVER_FACTORY="nofs.metadata.AnnotationDriver.Factory"
PERSIST_FACTORY="nofs.Factories.Db4oPersistenceFactory"
MOUNT_POINT=$1
DB_FILE=restfs.data.db
META_FILE=restfs.metadata.db

LD_LIBRARY_PATH=$JAVA_LIB:$FUSE_HOME/lib java \
	-classpath $CLASSPATH_LIBS
	-Dorg.apache.commons.logging.Log=fuse.logging.FuseLog \
	-Dfuse.logging.level=$LOGLEVEL \
	-Xmx$HEAPSIZE \
	nofs.Application.Main $DRIVER_FACTORY $PERSIST_FACTORY $RESTFS_JAR $MOUNT_POINT $DB_FILE $META_FILE
