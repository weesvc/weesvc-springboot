#!/bin/sh

docker run -it --name weesvc-springboot --rm -p 8080:8080 \
 github.com/weesvc/weesvc-springboot:0.0.1-SNAPSHOT
