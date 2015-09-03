SEPIA: Security-oriented PN Framework
=====================================
Petri net framework for security related modeling and reasoning
---------------------------------------------------------------

### About

<img align="right" src="http://iig-uni-freiburg.github.io/images/tools/sepia.png">SEPIA stands for "Security-oriented Petri Net Framework" and provides implementations for various types of Petri nets. Along Place/Transition-Nets, it supports Petri nets with distinguishable token colors. To support information flow analysis of processes, SEPIA defines so-called IF-Nets, tailored for security-oriented workflow modelling, which enable users to assign security-levels (high, low) to transitions, data elements and persons/agents participating in the process execution.

For the usage in editors, Petri nets can be put in graphical containers, which hold visualization information. To preserve compatibility, Petri nets from other frameworks can be imported with the parser functionalities and also be exported for other frameworks using the serializing functionalities.

Additionally, the framework comes with classes for the traversal of Petri nets.

### Library Dependencies

SEPIA builds upon the following tools and encloses them.

* TOVAL, located at [https://github.com/GerdHolz/TOVAL](https://github.com/GerdHolz/TOVAL "TOVAL: Tom's Java Library")
* JAGAL, located at [https://github.com/iig-uni-freiburg/JAGAL](https://github.com/iig-uni-freiburg/JAGAL "JAGAL: Java Graph Library")
* SEWOL, located at [https://github.com/iig-uni-freiburg/SEWOL](https://github.com/iig-uni-freiburg/SEWOL "SEWOL: Security Workflow Library")
* XML Schema Object Model (xsom), located at [https://xsom.java.net/](https://xsom.java.net/ "xsom")
* XML Datatypes Library (xsdlib)
* isorelax, located at [http://iso-relax.sourceforge.net/](http://iso-relax.sourceforge.net/ "isorelax")
* hamcrest, located at [https://github.com/hamcrest](https://github.com/hamcrest "hamcrest")
* Multi Schema Validator (MSV), located at [https://msv.java.net/](https://msv.java.net/ "MSV")
* relaxng-Datatype, located at [https://sourceforge.net/projects/relaxng/](https://sourceforge.net/projects/relaxng/ "relaxng-Datatype")

### Documentation

A detailled documentation of SEPIA can be found under [http://doku.telematik.uni-freiburg.de/sepia](http://doku.telematik.uni-freiburg.de/sepia "http://doku.telematik.uni-freiburg.de/sepia").

### Latest Release

The most recent release is SEPIA 1.0.0, released July 29, 2015.

* [sepia-1.0.0.jar](https://github.com/iig-uni-freiburg/SEPIA/releases/download/v1.0.0/sepia-1.0.0.jar)
* [sepia-1.0.0-sources.jar](https://github.com/iig-uni-freiburg/SEPIA/releases/download/v1.0.0/sepia-1.0.0-sources.jar)
* [sepia-1.0.0-javadoc.jar](https://github.com/iig-uni-freiburg/SEPIA/releases/download/v1.0.0/sepia-1.0.0-javadoc.jar)

To add a dependency on SEPIA using Maven, use the following:

```xml
<dependency>
  <groupId>de.uni.freiburg.iig.telematik</groupId>
  <artifactId>SEPIA</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Older Releases

Older releases can be found under [https://github.com/iig-uni-freiburg/SEPIA/releases](https://github.com/iig-uni-freiburg/SEPIA/releases).
