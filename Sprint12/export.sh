#!/bin/bash

# compiling source code
javac -parameters -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d ./bin --enable-preview --release 20 $(find src -name '*.java')

# adding framework librairies as dependencies
    # r-w-x_file
    mkdir exportedRwx
    cd ./exportedRwx
    jar xvf ../lib/r-w-x_file.jar
    cp -r ./fileActivity ../bin
    cd ../
    rm -r ./exportedRwx

    # gson
    mkdir exportedGson
    cd ./exportedGson
    jar xvf ../lib/gson-2.8.2.jar
    cp -r ./com ../bin
    cd ../
    rm -r ./exportedGson

# exporting the framework to a jar file
cd ./bin
jar cvf ../springy-1.0.0.jar *

# removing framework librairies dependencies
rm -r ./fileActivity

cd ../

# documentation
javadoc -classpath $(find lib/ -name '*.jar' | tr '\n' ':') -d doc -sourcepath src etu2006.framework.annotations etu2006.framework.config etu2006.framework.enumerations etu2006.framework.exceptions etu2006.framework.handler etu2006.framework.renderer etu2006.framework.servlet etu2006.framework.utils etu2006.framework.utils.helpers etu2006.framework.utils.javaObject etu2006.framework.utils.map