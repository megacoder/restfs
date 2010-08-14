#!/bin/bash

NOFS_VERS=`unzip -c target/nofs.restfs-*.jar META-INF/maven/nofs/nofs.restfs/pom.xml | grep -i "<nofs.version>" | sed 's/nofs\.version//g' | sed 's/<>//g' | sed 's/<\/>//g' | sed 's/ //g'`

echo "detected NOFS version: $NOFS_VERS"

FUSE_LIB_PATH=/usr/lib

FUSEJ_LIBS=`find . -name "fuse-j.jar"`
DB4O_LIBS=`find . -name "db4o*.jar"`
NOFS_LIBS=`find . -name "nofs*$NOFS_VERS*.jar" | grep -v "restfs"`
COMMONS_HTTP=`find . -name "commons-httpclient*.jar"`
COMMONS_CODEC=`find . -name "commons-codec*.jar"`
RESTFS_JAR=`find . -name "nofs.restfs-*.jar"`
DOM4J_JAR=`find . -name "dom4j*.jar"`
LOG_JAR=`find . -name "commons-logging*.jar"`
JACKRABBIT_LIBS=`find . -name "jackrabbit*.jar"`
SLFJ_LIBS=`find . -name "*slf4j*.jar"`
CLASSPATH_LIBS=`echo $COMMONS_HTTP $COMMONS_CODEC $FUSEJ_LIBS $DB4O_LIBS $JACKRABBIT_LIBS $SLFJ_LIBS $DOM4J_JAR $LOG_JAR $NOFS_LIBS | sed 's/ /:/g'`
JAVAFS_LIB=`find . -name "libjavafs.so" | sed 's/\/libjavafs\.so//g'`

DRIVER_FACTORY="nofs.metadata.AnnotationDriver.Factory"
PERSIST_FACTORY="nofs.Factories.Db4oPersistenceFactory"
MOUNT_POINT=$1
DB_FILE=restfs.data.dat
META_FILE=restfs.metadata.dat
HEAPSIZE="384m"
LOGLEVEL="DEBUG"
ARGS=`echo -ne "nofs.Application.Main $DRIVER_FACTORY $PERSIST_FACTORY $RESTFS_JAR $MOUNT_POINT $DB_FILE $META_FILE"`
ARGS=`echo $ARGS | sed 's/  / /g'`

rm -f *.dat

if [ ! -e "$DB_FILE" ]
then
	touch $DB_FILE
fi

if [ ! -e "$META_FILE" ]
then
	touch $META_FILE
fi

if [ ! -e "$MOUNT_POINT" ]
then
	mkdir -p $MOUNT_POINT
fi

echo "driver factory  = $DRIVER_FACTORY"
echo "persist factory = $PERSIST_FACTORY"
echo "restfs jar      = $RESTFS_JAR"
echo "mount point     = $MOUNT_POINT"
echo "data store      = $DB_FILE"
echo "metadata store  = $META_FILE"
echo "classpath       = $CLASSPATH_LIBS"
echo "arguments       = $ARGS"
echo
echo "launching restfs...."
echo

#DEBUG_ARGS="-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=Main"
DEBUG_ARGS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"

LD_LIBRARY_PATH=$JAVAFS_LIB:$FUSE_LIB_PATH java \
	$DEBUG_ARGS \
	-classpath $CLASSPATH_LIBS \
	-Dorg.apache.commons.logging.Log=fuse.logging.FuseLog \
	-Dfuse.logging.level=$LOGLEVEL \
	-Xmx$HEAPSIZE \
	$ARGS
