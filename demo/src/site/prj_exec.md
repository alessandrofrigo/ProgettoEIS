# Creazione jar ed esecuzione App

### Compilare il progetto Maven

Per creare il file jar relativo al progetto e comprensivo delle dipendenze, dopo essersi posizionati nella cartella principale del progetto digitare:

    mvn package

Nella cartella "target/" verranno generati

- demo-project-0.1.jar
- demo-project-0.1-jar-with-dependencies.jar

Il secondo file jar (detto anche uber-jar) comprende anche tutte le dipendenze.

### Eseguire il progetto Maven

Per eseguire il progetto, dalla cartella principale, eseguire il comando:

    java -cp target/demo-project-0.1-jar-with-dependencies.jar \
        it.unipd.dei.eis.App \
        -et "This is just a test to check if the library is working" \
        -np tokens_pipeline

oppure

    java -jar target/demo-project-0.1-jar-with-dependencies.jar \
        -et "This is just a test to check if the library is working" \
        -np tokens_pipeline

La seconda opzione utilizza direttamente la mainClass specificata nem pom.xml.

Le pipeline per estrarre i termini attualmente supportate dal progetto sono

- tokens_pipeline (estrae i token)
- lemmas_pipeline (estrae i [https://en.wikipedia.org/wiki/Lemma_(morphology)](lemma))
- nouns_pipeline (estrae i sostantivi)

Gli annotatori ed i metodi usati per l'estrazione sono specificati nel file

    src/main/java/resources/application.properties

di cui si riporta di seguito il contenuto

    tokens_pipeline.annotators=tokenize,ssplit,pos
    tokens_pipeline.method=extractTokens
    
    lemmas_pipeline.annotators=tokenize,ssplit,pos,lemma
    lemmas_pipeline.method=extractLemmas
    
    nouns_pipeline.annotators=tokenize,ssplit,pos
    nouns_pipeline.method=extractNouns

E' possibile leggere un altro file properties specificando l'opzione `-pf`. 
