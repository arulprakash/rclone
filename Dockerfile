FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/rclone.jar /rclone/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/rclone/app.jar"]
