#!/bin/bash

set -euo pipefail

dockerBuild() {
  echo "== Docker Build =="
  echo -e  "Repo: $1"
  echo -e "Version: $POM_VERSION"
  mvn spring-boot:build-image -Dspring-boot.build-image.imageName=$1:$POM_VERSION -Dmaven.test.skip=true
}

dockerTag() {
  echo "== Docker Tag =="
  echo -e  "Repo: $1"
  echo -e "Version: $POM_VERSION"
  docker tag $1:$POM_VERSION ${{ secrets.DOCKER_HUB_REPO }}:POM_VERSION
}

dockerPush() {
  echo "== Docker Push =="
  echo -e  "Repo: $1"
  echo -e "Version: $POM_VERSION"
  docker push $1:POM_VERSION
}

POM_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '\[')
echo "== Docker =="
case $1 in
build)
  dockerBuild $2 $POM_VERSION
  ;;
tag)
  dockerTag $2 $POM_VERSION
  ;;
push)
  dockerPush $2 $POM_VERSION
  ;;
*)
  echo "Unknown command"
  ;;
esac