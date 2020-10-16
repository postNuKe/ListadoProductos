XMLUnit for Java 2.x
====================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.xmlunit/xmlunit-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.xmlunit/xmlunit-core)

[![Build Status XMLUnit 2.x for Java](https://travis-ci.org/xmlunit/xmlunit.svg?branch=master)](https://travis-ci.org/xmlunit/xmlunit) [![Coverage Status](https://coveralls.io/repos/xmlunit/xmlunit/badge.svg)](https://coveralls.io/r/xmlunit/xmlunit)

XMLUnit is a library that supports testing XML output in several ways.

XMLUnit 2.x is a complete rewrite of XMLUnit and actually doesn't
share any code with XMLUnit for Java 1.x.

Some goals for XMLUnit 2.x:

* create .NET and Java versions that are compatible in design while
  trying to be idiomatic for each platform
* remove all static configuration (the old XMLUnit class setter methods)
* focus on the parts that are useful for testing
  - XPath
  - (Schema) validation
  - comparisons
* be independent of any test framework

Even though active development happens for XMLUnit 2.x, XMLUnit 1.x
for Java is still supported and will stay at
[sourceforge](https://sourceforge.net/projects/xmlunit/).

## Documentation

* [Developer Guide](https://github.com/xmlunit/xmlunit/wiki)
* [User's Guide](https://github.com/xmlunit/user-guide/wiki)

## Help Wanted!

If you are looking for something to work on, we've compiled a
[list](HELP_WANTED.md) of known issues.

Please see the [contributing guide](CONTRIBUTING.md) for details on
how to contribute.

## Latest Release

The latest releases are available as
[GitHub releases](https://github.com/xmlunit/xmlunit/releases) or via
[Maven Central](http://search.maven.org/#search|ga|1|org.xmlunit).

The core library is

```xml
<dependency>
  <groupId>org.xmlunit</groupId>
  <artifactId>xmlunit-core</artifactId>
  <version>x.y.z</version>
</dependency>
```

## SNAPSHOT builds

We are providing SNAPSHOT builds from
[Sonatypes OSS Nexus Repository](https://oss.sonatype.org/content/repositories/snapshots/org/xmlunit/),
you need to add

```xml
<repository>
  <id>snapshots-repo</id>
  <url>https://oss.sonatype.org/content/repositories/snapshots</url>
  <releases><enabled>false</enabled></releases>
  <snapshots><enabled>true</enabled></snapshots>
</repository>
```

to your Maven settings.

## Examples

These are some really small examples, more is available as part of the
[user guide](https://github.com/xmlunit/user-guide/wiki)

### Comparing Two Documents

```java
Source control = Input.fromFile("test-data/good.xml").build();
Source test = Input.fromByteArray(createTestDocument()).build();
DifferenceEngine diff = new DOMDifferenceEngine();
diff.addDifferenceListener(new ComparisonListener() {
        public void comparisonPerformed(Comparison comparison, ComparisonResult outcome) {
            Assert.fail("found a difference: " + comparison);
        }
    });
diff.compare(control, test);
```

or using the fluent builder API

```java
Diff d = DiffBuilder.compare(Input.fromFile("test-data/good.xml"))
             .withTest(createTestDocument()).build();
assert !d.hasDifferences();
```

or using Hamcrest with `CompareMatcher`

```java
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
...

assertThat(createTestDocument(), isIdenticalTo(Input.fromFile("test-data/good.xml")));
```

or using AssertJ with `XmlAssert`

```java

import static org.xmlunit.assertj.XmlAssert.assertThat;
...

assertThat(createTestDocument())
            .and(Input.fromFile("test-data/good.xml"))
            .areIdentical();
```

### Asserting an XPath Value

```java
Source source = Input.fromString("<foo>bar</foo>").build();
XPathEngine xpath = new JAXPXPathEngine();
Iterable<Node> allMatches = xpath.selectNodes("/foo", source);
assert allMatches.iterator().hasNext();
String content = xpath.evaluate("/foo/text()", source);
assert "bar".equals(content);
```

or using Hamcrest with `HasXPathMatcher`, `EvaluateXPathMatcher`

```java
assertThat("<foo>bar</foo>", HasXPathMatcher.hasXPath("/foo"));
assertThat("<foo>bar</foo>", EvaluateXPathMatcher.hasXPath("/foo/text()",
                                                           equalTo("bar")));
```

or using AssertJ with `XmlAssert`

```java

import static org.xmlunit.assertj.XmlAssert.assertThat;
...

assertThat("<foo>bar</foo>").hasXPath("/foo");
assertThat("<foo>bar</foo>").valueByXPath("/foo/text()").isEqualTo("bar");
```

### Validating a Document Against an XML Schema

```java
Validator v = Validator.forLanguage(Languages.W3C_XML_SCHEMA_NS_URI);
v.setSchemaSources(Input.fromUri("http://example.com/some.xsd").build(),
                   Input.fromFile("local.xsd").build());
ValidationResult result = v.validateInstance(Input.fromDocument(createDocument()).build());
boolean valid = result.isValid();
Iterable<ValidationProblem> problems = result.getProblems();
```

or using Hamcrest with `ValidationMatcher`

```java
import static org.xmlunit.matchers.ValidationMatcher.valid;
...

assertThat(createDocument(), valid(Input.fromFile("local.xsd")));
```

or using AssertJ with `XmlAssert`

```java
import static org.xmlunit.assertj.XmlAssert.assertThat;
...

assertThat(createDocument()).isValidAgainst(Input.fromFile("local.xsd"));
```

## Requirements

XMLUnit requires Java6 except for the AssertJ module which requires
Java7.

The `core` library provides all functionality needed to test XML
output and hasn't got any dependencies.  It uses JUnit 4.x for its own
tests.

If you are using Java 9 or later the core also depends on the JAXB
API. This used to be part of the standard class library but has been
split out of it with Java 9.

If you want to use `Input.fromJaxb` - i.e. you want to serialize plain
Java objects to XML as input - then you also need to add a dependency
on the JAXB implementation.  Starting with XMLUnit 2.6.4, xmlunit-core
optionally depends on the JAXB reference implementation and its
transitive dependencies.

The core library is complemented by Hamcrest 1.x matchers and AssertJ
assertions.  There also exists a `legacy` project that provides the
API of XMLUnit 1.x on top of the 2.x core library.

While the Hamcrest matchers are built against Hamcrest 1.x they are
supposed to work with Hamcrest 2.x as well.

## Checking out XMLUnit for Java

XMLUnit for Java uses a git submodule for test resources it shares
with XMLUnit.NET.  You can either clone this repository using `git
clone --recursive` or run `git submodule update --init` inside
your fresh working copy after cloning normally.

If you have checked out a working copy before we added the submodule,
you'll need to run `git submodule update --init` once.

## Building

XMLUnit for Java builds using Apache Maven 3.x, mainly you want to run

```sh
$ mvn install
```

in order to compile all modules and run the tests.
