FROM java:openjdk-8u66-jdk

MAINTAINER flurdy

ENV DEBIAN_FRONTEND noninteractive

ENV APPDIR /opt/app

RUN mkdir -p /etc/opt/app && \
  mkdir -p /opt/app

WORKDIR /opt/app

COPY conf /etc/opt/app
ADD target/universal /opt/app/target/universal

ENTRYPOINT ["/opt/app/target/universal/stage/bin/contractcalc","-Dconfig.file=/etc/opt/app/production.conf"]]

EXPOSE 9000
