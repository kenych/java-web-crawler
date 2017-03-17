#!/bin/bash
set -e

exec java -jar lib/crawler-1.0-SNAPSHOT.jar $1 $2 $3
