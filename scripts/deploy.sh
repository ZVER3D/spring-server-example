#!/usr/bin/env bash

mvm clean package

echo 'Copy files to server...'

scp -i ~/.ssh/id_rsa_contabo \
  target/server-0.0.1-SNAPSHOT.jar \
  admin@server.com:/home/admin/

echo 'Restarting server...'

ssh -i ~/.ssh/id_rsa_contabo admin@server.com << EOF

pgrep java | xargs kill -9
nohup java -jar server-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Done!'