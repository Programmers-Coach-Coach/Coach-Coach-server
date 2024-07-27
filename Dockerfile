FROM mysql:8.0.37

ENV MYSQL_DATABASE=coachcoach
ENV MYSQL_USER=coachcoach
ENV MYSQL_PASSWORD=coach1234

COPY *.sql /docker-entrypoint-initdb.d/

EXPOSE 3306
