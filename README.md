# 3D Inspire Serveur
Le serveur de l'application mobile "3D Inspire".

## Comment démarrer le serveur ?
### Via Docker (meilleure façon)
Alors il faut compiler l'appli en jar et après construire le service.

#### Étape 1 - Compiler l'application en Jar
Sur Windows :
````bat
./gradlew.bat bootJar
````

Sur Linux/MacOS
````shell
./gradlew bootJar
````

#### Étape 2 - Construire le service
Sur windows et Linux/MacOS
````shell
docker compose up -d
````
Le "-d" permet de ne pas bloquer le terminal sur l'exécution.
Le service est maintenant actif.

#### Étape 3 - Arrêter le service
Sur windows et Linux/MacOS
````shell
docker compose down -v
````
Le "-v" permet de supprimer le volume crée.

### Via Gradle
Sur Windows :
````bat
./gradlew.bat bootRun
````

Sur Linux/MacOS
````shell
./gradlew bootJar
````

Assurez-vous d'avoir un serveur Postgresql et de changer les données du application.properties en conséquence.