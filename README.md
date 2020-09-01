TicketMagpie
============

This project aims at demonstrating various security vulnerabilities such as the ones listed in the [OWASP Top10](https://www.owasp.org/index.php/Category:OWASP_Top_Ten_Project).

Configuration
-------------

This project requires [Maven 3](https://maven.apache.org/) and
[Java Development Kit version 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html/).
The project does not work with Java 9.

1. Extract, configure and install Maven on your machine, in an appropriate location: https://maven.apache.org/install.html.
2. Extract and install Java Development Kit on your machine, in an appropriate location e.g. `c:\Program Files (x86)\Java\` on Windows, `/System/Library/Java/` on Mac OSX, `/usr/java/` on Linux.
3. Configure the `JAVA_HOME` environment variable and path on your machine. Instructions here: [WINDOWS](https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html),
[MAC](https://www.mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/),
[LINUX](http://www.cyberciti.biz/faq/linux-unix-set-java_home-path-variable/).
4. You may need to also configure the Path variable: https://www.java.com/en/download/help/path.xml
5. Download the TicketMagpie-master project to your local machine and install to an appropriate location e.g `C:\Users\[username]\ticketmagpie` (on Windows).
**Make sure to rename ticketmagpie-master to ticketmagpie**

Running the project
-------------------

Once you have configured and installed Maven and Java Development Kit and TicketMagpie, you can start the application by running this command from the root folder of the project:

```bash
mvn spring-boot:run
```

The application will then be available at [localhost](http://localhost:8080).

Running the project in Docker
-----------------------------

The application is published on the docker hub. You can run it like this, with the in-memory database:

```bash
docker run -e "SPRING_PROFILES_ACTIVE=hsqldb" -p8080:8080 "codecop/ticketmagpie"
```

The application will then be available at [localhost](http://localhost:8080).

Database configuration
----------------------

By default, the application expects a MySQL database to be available on localhost, default port 3306.
The application will use the user `root` to connect to a database called `ticketmagpie`.

If you have MySQL or [MariaDB](https://mariadb.org/) installed, initially create the database with:

```bash
mysql -u root < src/main/resources/create-mysql-database.sql
```

If you have Docker installed, run the latest MySQL docker image with suitable configuration:

```bash
docker run -d -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
              -e MYSQL_DATABASE=ticketmagpie -p 3306:3306 "mysql:latest"
```

You can pass custom database configuration as follows:

```bash
mvn spring-boot:run -Dspring.datasource.url=jdbc:mysql://MYSQL_SERVER:PORT/DB_NAME \
                    -Dspring.datasource.username=USER \
                    -Dspring.datasource.password=PASSWORD
```

If you do not have a database server, you can run the application with an HSQLDB in-memory database:

```bash
mvn spring-boot:run -Dspring.profiles.active=hsqldb
```

Email configuration
-------------------

To send password recovery email you need to configure an SMTP server with credentials.

You can pass the email configuration as follows:

```bash
mvn spring-boot:run -Dmail.smtp.host=SMTP_SERVER \
                    -Dmail.smtp.port=587 \
                    -Dmail.smtp.username=USER@DOMAIN.COM \
                    -Dmail.smtp.password=PASSWORD
```
