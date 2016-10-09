The Cool-Library
================

### First step

We can easily create a new cool using a "constructor" and get
a really cool cool.

~~~ .java
Cool cool = new Cool();
assertTrue(cool.getTemperature() < 5);
~~~

Canonical definition of hotness.

~~~ .java
boolean isHot(int temperature) {
    return temperature >= 5;
}
~~~

New cools are not canonically hot.

~~~ .java
Cool cool = new Cool();
assertTrue(!isHot(cool.getTemperature()));
~~~
