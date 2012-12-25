mysql2h2-converter
==================

A MySQL to H2 SQL conversion library written in Java.

For the moment, it can only parse a MySQL dump and populate Java objects (CreateTableStatement, InsertStatement, ValueList...).

Next steps are:

- start implementing the converter for H2 so that a MySQL dump can be loaded without modification in H2
- implement other statements (SELECT, UPDATE...)
- provide samples for an on-the-fly conversion with datasource-proxy http://code.google.com/p/datasource-proxy/
