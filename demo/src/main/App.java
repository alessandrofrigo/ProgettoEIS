package it.unipd.dei.eis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * Classe principale del progetto.
 * Riporta un esempio di utilizzo di Apache Commons CLI per leggere le opzioni da linea di comando.
 * Riporta inoltre un esempio di utilizzo di CoreNLP: data una stringa la suddivide in token, lemma o estrae i sostantivi distinti.
 */
public class App {

    // Part-of-Speech (POS) tag che rappresentano sostantivi (noun)
    protected static Set<String> nounPosTags = new TreeSet<String>() {{
        // ENGLISH
        add("NN");  // noun, singular or mass
        add("NNS"); // noun, plural
        add("NNP"); // proper noun, singular
        add("NNPS"); // proper noun, plural
    }};

    /**
     * Crea una CoreNLP pipeline, data una stringa che specifica la sequenza di annotatori.
     * Per dettagli sulla pipeline vedere {@link edu.stanford.nlp.pipeline.StanfordCoreNLP}.
     * Ad esempio, per estrarre i lemma usare la stringa: "tokenize,ssplit,pos,lemma".
     *
     * @param annotators stringa con la sequenza di annotatori CoreNLP da utilizzare
     * @return una pipeline di tipo {@link edu.stanford.nlp.pipeline.StanfordCoreNLP}
     * @see edu.stanford.nlp.pipeline.StanfordCoreNLP
     */
    public static StanfordCoreNLP createPipeline(String annotators) {
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", annotators);
        // build pipeline
        return new StanfordCoreNLP(props);
    }

    /**
     * Estrae i token dal testo in input, utilizzando la sequenza di annotatori specificata.
     *
     * @param annotators la lista degli annotatori per estrarre i token
     * @param text       il testo da annotare
     * @return insieme dei token distinti estratti dal testo, in minuscolo
     * @since 0.1
     */
    public static Set<String> extractTokens(String annotators, String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(annotators);

        // create a document object
        CoreDocument document = new CoreDocument(text);

        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctTokens = new TreeSet<>();
        // tokens
        for (CoreLabel token : document.tokens()) {

            String tkn = token.originalText().toLowerCase();

            distinctTokens.add(tkn);
        }

        return distinctTokens;
    }

    /**
     * Estrae i lemma dal testo in input, utilizzando la sequenza di annotatori specificata.
     *
     * @param annotators la lista degli annotatori per estrarre i lemma
     * @param text       il testo da annotare
     * @return insieme dei lemma distinti estratti dal testo, in minuscolo
     * @since 0.1
     */
    public static Set<String> extractLemmas(String annotators, String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(annotators);

        // create a document object
        CoreDocument document = new CoreDocument(text);

        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctLemmas = new TreeSet<>();
        // tokens
        for (CoreLabel token : document.tokens()) {

            String lemma = token.get(CoreAnnotations.LemmaAnnotation.class).toLowerCase();

            distinctLemmas.add(lemma);
        }

        return distinctLemmas;
    }

    /**
     * Estrae i sostantivi dal testo in input, utilizzando la sequenza di annotatori specificata.
     *
     * @param annotators la lista degli annotatori per estrarre i sostantivi
     * @param text       il testo da annotare
     * @return insieme dei sostantivi distinti estratti dal testo, in minuscolo
     * @since 0.1
     */
    public static Set<String> extractNouns(String annotators, String text) {

        StanfordCoreNLP nlpPipeline = createPipeline(annotators);

        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annotate the document
        nlpPipeline.annotate(document);

        Set<String> distinctNouns = new TreeSet<>();

        // tokens
        for (CoreLabel token : document.tokens()) {

            // this is the text of the token
            String word = token.get(CoreAnnotations.TextAnnotation.class);
            // this is the POS tag of the token
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

            if (nounPosTags.contains(pos.toUpperCase())) {
                distinctNouns.add(word.toLowerCase());
            }

        }

        return distinctNouns;
    }

    public static void main(String[] args) {

        Options options = new Options();
        // possible actions
        OptionGroup actionGroup = new OptionGroup();
        actionGroup.addOption(new Option("h", "help", false, "Print the help"));
        actionGroup.addOption(new Option("et", "extract-terms", true, "Extract terms from the given string"));

        actionGroup.setRequired(true);
        options.addOptionGroup(actionGroup);

        // possible options
        options.addOption(new Option("pf", true, "Property file path"));
        options.addOption(new Option("np", "nlp-pipeline", true, "CoreNLP Pipeline (tokens_pipeline, lemmas_pipeline, nouns_pipeline)"));

        // parse
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException ex) {
            System.err.println("ERROR - parsing command line:");
            System.err.println(ex.getMessage());
            formatter.printHelp("App -{h,et} [options]", options);
            return;
        }

        if (cmd.hasOption("h")) {

            formatter.printHelp("App -{et} [options]", options);

        } else if (cmd.hasOption("et")) {

            Properties properties = new Properties();
            InputStream input = null;
            try {
                if (cmd.hasOption("pf")) {
                    input = Files.newInputStream(Paths.get(cmd.getOptionValue("pf")));
                    properties.load(input);
                } else {
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    input = loader.getResourceAsStream("application.properties");
                    properties.load(input);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

//            System.out.println(properties);

            String text = cmd.getOptionValue("et");

            String nlpPipeline = cmd.getOptionValue("np");

            String annotators = properties.getProperty(nlpPipeline + ".annotators");

            String extractionMethodName = properties.getProperty(nlpPipeline + ".method");

            try {

                Method extractionMethod = App.class.getMethod(extractionMethodName, String.class, String.class);

                Set<String> terms = (Set<String>) extractionMethod.invoke(null, annotators, text);
                for (String term : terms) {
                    System.out.println(term);
                }

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
