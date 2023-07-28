Steps to run the Widget Application Project:-

1. Git clone the project.
2. Install Postgresql from the below link:-
   https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
3. Open psql shell and run the sql commands from database-script.sql file. This should create two tables - Wigdet and Editor.
4. Now run the docker file, this should start Swagger at port 8080. Open below link to access:
   http://localhost:8080/swagger-ui/#/
5. Get started with the "createWidget" api in the widget-controller.