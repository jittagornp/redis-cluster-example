#!/bin/bash

server1=127.0.0.1
server2=127.0.0.1 
server3=127.0.0.1 
server4=127.0.0.1 
server5=127.0.0.1 
server6=127.0.0.1 
redis-cli --cluster create $server1:6379 $server2:6379 $server3:6379 $server4:6379 $server5:6379 $server6:6379 --cluster-replicas 1
