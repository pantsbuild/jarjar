#!/bin/bash

cdir="/home/ville/Workspace/Vjkoskela/jarjar"

cp netty-transport-native-epoll-4.1.30.Final-linux-x86_64.jar.bak netty-transport-native-epoll-4.1.30.Final-linux-x86_64.jar

./pants run src/main/java/org/pantsbuild/jarjar:main --jvm-run-jvm-options='-Dverbose=true' -- process "${cdir}/rules.txt" "${cdir}/netty-transport-native-epoll-4.1.30.Final-linux-x86_64.jar" "${cdir}/netty-shaded.jar"

rm -rf ./exploded
mkdir -p ./exploded
cp netty-shaded.jar ./exploded/

pushd exploded
unzip netty-shaded.jar
popd
