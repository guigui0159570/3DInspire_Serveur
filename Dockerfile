FROM eclipse-temurin:17.0.10_7-jre

RUN mkdir /var/opt/app
WORKDIR /var/opt/app
RUN mkdir model
RUN mkdir image
RUN mkdir image_profil

COPY build/libs/_3DInspire_Serveur-0.0.1-SNAPSHOT.jar ./app.jar

ENV server.address=0.0.0.0
ENV server.port=80
ENV file.upload-dir-model=/var/opt/app/model
ENV file.upload-dir-image=/var/opt/app/image
ENV file.upload-dir-image-profil=/var/opt/app/image_profil
ENV spring.datasource.url=lien
ENV spring.datasource.username=user
ENV spring.datasource.password=password

ENTRYPOINT ["java", "-jar", "app.jar"]