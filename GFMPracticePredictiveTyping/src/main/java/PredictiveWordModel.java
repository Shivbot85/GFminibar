import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.*;

public class PredictiveWordModel {
    private Table<String, String, Integer> nGramCounts;
    private Map<String, Double> nGramProbabilities;
    private List<String> corpus;

    public PredictiveWordModel() {
        nGramCounts = HashBasedTable.create();
        nGramProbabilities = new HashMap<>();
        corpus = new ArrayList<>();
    }

    // Step 5: Count the n-grams

    public void train(List<String> corpus, int n) {
        if (nGramCounts == null) {
            nGramCounts = HashBasedTable.create();
        }

        for (String sentence : corpus) {
            String[] words = sentence.toLowerCase().split(" ");

            for (int i = 0; i < words.length - n + 1; i++) {
                StringBuilder nGramBuilder = new StringBuilder();

                for (int j = 0; j < n; j++) {
                    nGramBuilder.append(words[i + j]);
                    if (j < n - 1) {
                        nGramBuilder.append(" ");
                    }
                }

                String nGram = nGramBuilder.toString();
                String prefix = nGram.substring(0, n - 1);
                Integer count = nGramCounts.get(prefix, nGram);
                if (count == null) {
                    count = 0;
                }
                nGramCounts.put(prefix, nGram, count + 1);
            }
        }

        // Step 6: Calculate the probability of each n-gram
        for (Table.Cell<String, String, Integer> cell : nGramCounts.cellSet()) {
            String prefix = cell.getRowKey();
            int totalCount = nGramCounts.row(prefix).values().stream().mapToInt(Integer::intValue).sum();

            double probability = (double) cell.getValue() / totalCount;
            nGramProbabilities.put(cell.getColumnKey(), probability);
        }
    }

    // Step 7: Predictive Words for a given prefix
    public List<String> predictWordsStartingWith(String prefix) {
        List<String> predictions = new ArrayList<>();

        for (Map.Entry<String, Double> entry : nGramProbabilities.entrySet()) {
            String nGram = entry.getKey();
            if (nGram.startsWith(prefix)) {
                predictions.add(nGram);
            }
        }

        // Order predictions by probability in descending order
        predictions.sort((nGram1, nGram2) ->
                -Double.compare(nGramProbabilities.get(nGram1), nGramProbabilities.get(nGram2)));

        return predictions;
    }

    // Predictive Words for incomplete sentences where the prefix is the last word
    // Predictive Words for incomplete sentences where the prefix is the last word
    public List<String> predictWordsForIncompleteSentence(String incompleteSentence) {
        String[] words = incompleteSentence.toLowerCase().split(" ");

        if (words.length == 0) {
            return null; // No predictions for an empty incomplete sentence
        }

        List<String> predictions = new ArrayList<>();

        // Consider the previous sentences in the corpus
        for (String sentence : corpus) {
            String[] sentenceWords = sentence.toLowerCase().split(" ");
            int sentenceLength = sentenceWords.length;

            if (sentenceLength <= words.length) {
                continue; // Skip shorter sentences
            }

            boolean matches = true;

            for (int i = 0; i < words.length; i++) {
                if (!sentenceWords[sentenceLength - words.length + i].equals(words[i])) {
                    matches = false;
                    break;
                }
            }

            if (matches && sentenceLength > words.length) {
                String prediction = sentenceWords[sentenceLength - 1];
                predictions.add(prediction);
            }
        }

        // If no predictions from the corpus, consider the current sentence
        if (predictions.isEmpty()) {
            String lastWord = words[words.length - 1];
            predictions = predictWordsStartingWith(lastWord);
        }

        // Remove the prefix from the predictions
        List<String> resultPredictions = new ArrayList<>();
        for (String prediction : predictions) {
            String[] predictionWords = prediction.split(" ");
            if (predictionWords.length > 1) {
                resultPredictions.add(predictionWords[1]); // Assuming bigrams
            }
        }

        return resultPredictions.isEmpty() ? null : resultPredictions;
    }

    public void addSentenceToCorpus(String sentence) {
        // Ensure the corpus list is initialized
        if (corpus == null) {
            corpus = new ArrayList<>();
        }

        // Add the sentence to the corpus list
        corpus.add(sentence);
    }

    public static void main(String[] args) {
        PredictiveWordModel model = new PredictiveWordModel();

        // Add sentences to the corpus
        model.addSentenceToCorpus("the quick brown fox jumps over a lazy dog");
        model.addSentenceToCorpus("a cat sat on the mat");
        model.addSentenceToCorpus("she sells seashells by a seashore");

        // Use bigrams (n=2) and train the model on the added corpus
        model.train(model.corpus, 2);

        String inputPrefix = "the";
        List<String> predictions = model.predictWordsStartingWith(inputPrefix);

        System.out.println("Predictions for words starting with '" + inputPrefix + "':");

        // Extract and print just the predicted words (without the prefix)
        for (String prediction : predictions) {
            String[] predictionWords = prediction.split(" ");
            if (predictionWords.length > 1) {
                System.out.println(predictionWords[1]); // Assuming bigrams
            }
        }

        // Example of predicting words for an incomplete sentence
        String incompleteSentence = "she the";
        List<String> incompletePredictions = model.predictWordsForIncompleteSentence(incompleteSentence);

        System.out.println("\nPredictions for incomplete sentence: '" + incompleteSentence + "':");
        if (incompletePredictions != null) {
            for (String prediction : incompletePredictions) {
                System.out.println(prediction);
            }
        } else {
            System.out.println("No predictions available.");
        }
    }
}
