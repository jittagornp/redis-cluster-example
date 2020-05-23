# Redis Cluster Example

# Requires

- Server : 6 Nodes 
  - 3 Masters
  - 3 Slaves 
- OS : Ubuntu 18.04 LTS

# Install

### All Nodes 
1. Clone this Source Code

```sh
$ git clone https://github.com/jittagornp/redis-cluster-example.git
$ cd redis-cluster-example
$ chmod +x *.sh
```

2. Install Docker

```sh
$ ./install-docker.sh
```

3. Install Redis 

```
$ ./docker-run-redis.sh 
```

### On Node 1

1. Change Node IP Address
```sh
vi redis-cluster-example/docker-run-redis-cluster.sh
```

2. Create Cluster 
```sh
$ ./docker-run-redis-cluster.sh
```
