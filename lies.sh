#!/bin/bash

ant jar
curl -o corrupt.jar "http://central.maven.org/maven2/com/sun/xml/bind/jaxb-xjc/2.2/jaxb-xjc-2.2.jar"
touch test_rules.txt
echo ""
echo "Running JarJar:"
java -cp dist/jarjar-1.5.jar org.pantsbuild.jarjar.Main process test_rules.txt corrupt.jar shaded-corrupt.jar
CODE=$?
echo ""
echo "Exit code: $CODE"
