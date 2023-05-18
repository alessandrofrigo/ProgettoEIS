# Ricreare questo progetto Maven

Per creare questo progetto maven da linea di comando digitare (vedere Lezione 5):

	mvn archetype:generate -DgroupId=it.unipd.dei.eis -DartifactId=demo-project -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false

Per creare questo progetto maven in IntelliJ IDEA eseguire i seguenti passi:

1. Cliccare su File
2. Cliccare su New
3. Cliccare su Project
4. Selezionare tra i Generators sulla destra "Maven Archetype"
5. Inserire come nome: "demo-project"
6. Selezionare come archetype "org.apache.maven.archetypes:maven-archetype-quickstart"
7. Specificare la versione 1.4
8. Aprire gli "Advanced Settings"
9. Specificare come GroupId "it.unipd.dei.eis"
10. Specificare come ArtifactId "demo-project"
11. Specificare come Version 0.1
12. Cliccare su "Create"
13. Aggiornare il file pom.xml come in questo progetto

È stata poi creata una cartella "resources" in src/main/ dove è stato inserito il file "application.properties".
Tale file contiene alcune informazioni per configurare l'applicazione.

Per generare i javadocs

    mvn javadoc:javadoc

Inoltre ho creato la cartella site, che contiene i sorgenti per creare il sito. 
Le istruzioni per creare e rendere disponibile il sito sono:

    mvn site
    mvn site:run

Il sito sarà quindi disponibile al seguente indirizzo: [http://localhost:8080/](http://localhost:8080/)