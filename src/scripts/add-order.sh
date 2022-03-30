#!/usr/bin/env bash

set -e

curl -vX POST -H "Content-Type: application/json" --data @$1 http://localhost:9002/orders