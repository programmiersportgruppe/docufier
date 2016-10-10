Docufier
========

[![Build Status](https://travis-ci.org/programmiersportgruppe/docufier.svg?branch=master)](https://travis-ci.org/programmiersportgruppe/docufier)


Docufier is here to fulfil the age-old promise that your
tests can be used as documentation. 

It turns doc-commented JUnit test cases into markdown files, that
expain how to use your code. 

It offers several benefits:

* Share code between documentation and tests.
* Ensures examples are always syntactically and functionally correct.
* Helps focusing on the user of the code when writing tests.
* IDE support for writing documentation examples.
* Simplifies keeping documentation up-to-date (at least if you are in
the habit of writing tests for features).


Getting Started
---------------

### Step 1: Annotate Test Case

~~~ .java
/** [DOC file=README.md]     
    The Cool-Library
    ================
*/

public class CoolTestSuite {

    /**    
    ### First step
    
    We can easily create a new cool using a "constructor" and get
    a really cool cool. 
    */ 
    
    @Test
    public void ensureANewCoolIsNotHot() {
        Cool cool = new Cool();
        assertTrue(cool.getTemperature() < 5);         
    }
    
    /**
    [NO-DOC]
    Sometimes we write a test that is not really instructional.
    */
    @Test
    public void ensureEqualityWorks() {
        Cool oneCool = new Cool();
        Cool anotherCool = new Cool();
        
        assertEquals(oneCool, anotherCool);         
    }
       
}
~~~


### Step 2: Run `docufier`

~~~ .java 
new Docufier("src/test/java", "target/doc");
~~~

Will yield a file `README.md` in target/doc that looks like this

    The Cool-Library
    ================
    
    ### First step
    
    We can easily create a new cool using a "constructor" and get
    a really cool cool.
    
    ~~~ .java
    Cool cool = new Cool();
    assertTrue(cool.getTemperature() < 5);         
    ~~~

How to run Docufier
-------------------

Docufier can either be run directly from Java code or as part of a
maven build using the `docufier-plugin`.

### Running from Java

Include docufier as a dependency, e.g. using maven:

~~~ .xml
<dependency>
    <groupId>org.buildobjects</groupId>
    <artifactId>docufier</artifactId>
    <version>0.0.1</version>
</dependency>
~~~

Instantiate a new docufier instance, with the (test) source directory and
the target directory:

~~~ .java 
new Docufier("src/test/java", "target/doc");
~~~

### Running as part of your Maven Build

To run docufier in the `generate-resources` phase add the following
to the pom.xml:

~~~ .xml
<build>
    <plugins>

        <plugin>
            <groupId>org.buildobjects</groupId>
            <artifactId>docufier-plugin</artifactId>
            <version>0.0.1</version>
            <executions>
                <execution>
                    <phase>generate-resources</phase>
                    <goals>
                        <goal>docufy</goal>
                    </goals>
                </execution>
            </executions>            
        </plugin>
    </plugins>
</build>
~~~

Per default the plugin tries to read test cases from `src/test/java` and writes to `target/doc`.

These directories can be configured by adding a configuration element to the plugin element:
 
~~~ .xml
<configuration>
    <outputDirectory>src/test/java</outputDirectory>
    <outputDirectory>target/md</outputDirectory>
</configuration>
~~~
