FROM maven
WORKDIR home/
RUN sh -c 'git clone https://github.com/kudoR/mail-parser.git && cd mail-parser && mvn clean install'
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /home/mail-parser/target/mail-parser-1.0.1.jar" ]