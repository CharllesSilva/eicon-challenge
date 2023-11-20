#!/bin/bash
wait_for_service() {
  local host=$1
  local port=$2

  until nc -z $host $port >/dev/null 2>&1; do
    echo "Waiting for availability $host:$port..."
    sleep 1
  done
}

wait_for_service db 3306

echo "All services are ready. Starting the application..."