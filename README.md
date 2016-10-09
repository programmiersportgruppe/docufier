Docufier
========

[![Build Status](https://travis-ci.org/programmiersportgruppe/docufier.svg?branch=master)](https://travis-ci.org/programmiersportgruppe/docufier)


Docufier is here so fulfil the age old promise that your
tests can be used as documentation. 

It turns doc-commented JUnit test cases into markdown files, that
expain how to use your code. 

It offers several benefits:

* Share code between documentation and tests.
* Ensures examples are always syntically and functionally correct.
* Helps focusing on the user of the code when writing tests.
* IDE support for writing documentation examples.
* Simplifies keeping documentation up-to-date (at least if you are in
the habit of writing tests for features).


Getting Started
---------------

1. Annotate your test case

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


2. Run `docufier`

$ docufier src/test/java target/doc

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


Installation
------------

Using this little script you can run docufier directly off the maven repo:

~~~~
set -e 
mvn some-magic-to-resolve-class-path

java -cp ${resulting_casspath} "$@"
~~~~
