#!/usr/bin/env bash
set -e
set -u

mvn -Dtest=org.buildobjects.doctest.guineapig.CoolTestSuite clean test
mvn test
