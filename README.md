mysql2h2-converter
==================

A MySQL to H2 SQL conversion library written in Java.

For the moment, you can parse a MySQL dump and convert it to H2 statements, either as an embedded library
or as a standalone tool:

> java -jar converter/target/mysql2h2-converter-tool-0.1-SNAPSHOT.jar input.sql > output.sql

Next steps are:
- implement other statements (SELECT, UPDATE...)
- provide samples for an on-the-fly conversion with datasource-proxy http://code.google.com/p/datasource-proxy/

Other ideas:
- look at jOOQ http://www.jooq.org/ to see if it can be used to model the DML statements and Liquibase http://www.liquibase.org/
  for the DDL part

License
=======
This code is provided under the MIT license.
