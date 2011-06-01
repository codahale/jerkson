#!/bin/bash

# todo: print errors on tests

versions=( '2.8.1' '2.9.0' '2.9.0-1' )
cmds=$@

for version in "${versions[@]}"; do
  mvn $cmds -P scala-$version
done
