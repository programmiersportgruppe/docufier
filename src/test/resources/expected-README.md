The Cool-Library
================

### First step

We can easily create a new cool using a "constructor" and get
a really cool cool.

<pre><code>Cool cool = <span style="color:red">new</span> Cool();
assertTrue(cool.getTemperature() < <span style="color:blue">5</span>);</code></pre>

Canonical definition of hotness.

<pre><code>boolean isHot(int temperature) {
    return temperature >= 5;
}</code></pre>

New cools are not canonically hot.

<pre><code>Cool cool = <span style="color:red">new</span> Cool();
assertTrue(!isHot(cool.getTemperature()));</code></pre>

We also want to be able to format some json:

<pre><code>String someJson = «{
  "name" : "dagobert",
  "nephews" : [ "tick", "trick", "track" ]
}»;

String anotherJson = StringUtils.trim(«[ "A", "B", "C" ]»);

<span style="color:gray">// Or a comment?</span>
assertNotEquals(someJson, anotherJson);</code></pre>
