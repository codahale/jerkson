v0.7.0: Mar 5, 2013
===================

* Scala 2.9.2

v0.5.0: Oct 07 2011
===================

* Added `canSerialize` and `canDeserialize`.
* Upgraded Jackson to 1.9.0.
* Dropped support for Scala 2.8.1, added support for 2.8.2. Upgrade.
* Dropped support for 2.9.0-1. Upgrade.

v0.4.2: Sep 16 2011
===================

* Added support for case classes with multiple constructors.
* Added support for `snake_cased` JSON field names via the `@JsonSnakeCase`
  annotation.

v0.4.1: Sep 13 2011
===================

* Added `@JsonIgnoreProperties` support.
* No longer serializing `transient` members of case classes.
* Upgraded to Jackson 1.8.x. (Jerkson will now track the highest available
  version in the 1.8.x series.)


v0.4.0: Jul 25 2011
===================

* Upgraded to Jackson 1.8.3.
* Fixed deserialization of empty JSON objects as `JValue` instances.
* Fixed deserialization of `Map[java.lang.Integer, A]` and
  `Map[java.lang.Long, A]` instances.
* Fixed deserialization of case classes in weird bytecode environments like
  Play.
* Added support for case classes nested in objects.
* Allowed for deserializing `BigInt` and `BigDecimal` instances from anything
  those classes can parse as text.
* Added a cache for type manifests.


v0.3.2: Jun 09 2011
===================

* Added `Json.stream[A](Reader)`.
* Fix `NullPointerException` when deserializing `Map` instances from weird JSON
  values.


v0.3.1: Jun 05 2011
===================

* Added support for deserializing `Map[Int, _]` and `Map[Long, _]` instances.


v0.3.0: Jun 04 2011
===================
* Added a very comprehensive set of tests, refactored around support for various
  types. (h/t Daniel Brown)
* Added support for `StringBuilder`, `Array[A]`, `immutable._`, `mutable._`,
  `collection._` classes, `AST` classes, and others.
* Fixed error messages when parsing empty JSON objects as case classes.
* Enabled caching of all serializers and deserializers.
* Switched to Maven for builds.
* Removed the deprecated `Parser#parseStreamOf`.


v0.2.2: May 18 2011
===================

* Upgraded to Jackson 1.7.7.
* Fixed bugs in parsing case classes with other specially-namespaced types.


v0.2.1: May 12 2011
===================

* Fixed bug in parsing case classes with `List[A]` members (and anything else
  which is typed in the `scala` package.


v0.2.0: May 12 2011
===================

* Now cross-built for Scala 2.9.0.
* Changed to parse the embedded Scala signature in case classes by using an
  embedded version of `scalap`. No longer depends on Paranamer.
* Serializing a case class with a `None` field now elides the entire field
  instead of serializing the `Option[A]` as `null`.
* Removed explicit flushes to output.


v0.1.8: May 05 2011
===================

* Upgraded to Jackson 1.7.6.
* Added selectors to `JValue` and friends.
* Extracted out the `Json` trait for extensibility.
* Added support for `Iterator` and `Set` instances.
* Fixed deserialization of empty `Map`s.


v0.1.7: Mar 31 2011
===================

* Upgraded to Jackson 1.7.4.


v0.1.6: Feb 18 2011
===================

* Serialize `None` instances to `null`. (h/t Alex Cruise again)


v0.1.5: Feb 18 2011
===================

* Added ability to actually serialize `Option` instances. (h/t Alex Cruise)


v0.1.4: Jan 17 2011
===================

* Upgraded to Jackson 1.7.1, which fixes the buffer overruns
* Handle empty JSON documents w/o resorting to `EOFException`


v0.1.3: Jan 12 2011
===================

* Quick fix for potential buffer overrun errors in huge case classes (JACKSON-462).


v0.1.2: Jan 07 2011
===================

* Upgraded to [Jackson 1.7.0](http://jackson.codehaus.org/1.7.0/release-notes/VERSION).
* Added support for `Either[A, B]` instances.
* Internal refactoring of `Json`.


v0.1.1: Dec 09 2010
===================

* Upgraded to [Jackson 1.6.3](http://jackson.codehaus.org/1.6.3/release-notes/VERSION).
* Added `StreamingIterator`, `Json.stream`, and deprecated `Json.parseStreamOf`.
* Fixed support for `lazy` `val` and `var` members of case classes.
* Added support for `@JsonIgnore` for case classes.


v0.1.0: Dec 03 2010
===================

* Initial release. Totally awesome.
