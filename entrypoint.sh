#!/bin/bash
set -e

/etc/init.d/filebeat start

java -jar lib/crawler-1.0-SNAPSHOT.jar $1 $2 $3


sleep 60
