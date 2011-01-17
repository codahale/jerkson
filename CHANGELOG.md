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