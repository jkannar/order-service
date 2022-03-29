#!/bin/bash

set -e

# curl is not there in the alpine base image, so non-alpine one should be used

until curl --output /dev/null --silent --head --fail "http://config-service:8888/catalog-service/prod"; do
  >&2 echo "Config-Service is not available yet -> order service is waiting..."
  sleep 1
done

>&2 echo "Config-Service is up and running!"

# call the original entry point
java -jar app.jar