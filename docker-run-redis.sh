#!/bin/bash

docker run --name redis --restart=always -d -p 6379:6379 --net=host -v $(pwd)/redis.conf:/usr/local/etc/redis/redis.conf redis:latest redis-server /usr/local/etc/redis/redis.conf --requirepass password
