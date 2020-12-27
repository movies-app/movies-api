#!/bin/bash

set -euo pipefail

dockerBuild() {
  echo "== Docker Build =="
  echo "Repo: $1"
  echo "Version: $2"
  mvn spring-boot:build-image -Dspring-boot.build-image.imageName=$1:$2 -Dmaven.test.skip=true
}

dockerTag() {
  echo "== Docker Tag =="
  echo "Repo: $1"
  echo "Version: $2"
  docker tag $1:$2 $1:$2
}

dockerPush() {
  echo "== Docker Push =="
  echo "Repo: $1"
  echo "Version: $2"
  docker push $1:$2
}

POM_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '\[')
echo "== Docker =="
echo "Repo: $1"
echo "Version: $POM_VERSION"

#case $1 in
#build)
#  dockerBuild $2 $POM_VERSION
#  ;;
#tag)
#  dockerTag $2 $POM_VERSION
#  ;;
#push)
#  dockerPush $2 $POM_VERSION
#  ;;
#*)
#  echo "Unknown command"
#  ;;
#esac