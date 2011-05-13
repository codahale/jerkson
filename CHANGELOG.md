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
