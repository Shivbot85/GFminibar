import java.util.*;
import java.util.stream.Collectors;

public class PredictiveTyping4 {
    private Map<String, Double> nGramProbabilities;
    private GrammarManager grammarManager;

    public PredictiveWordModel() {
        nGramProbabilities = new HashMap<>();
        grammarManager = new GrammarManager();
    }

    // Step 5: Count the n-grams
    public void train(List<String> corpus, int n) {
        Map<String, Integer> nGramCounts = new HashMap<>();

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
                nGramCounts.put(nGram, nGramCounts.getOrDefault(nGram, 0) + 1);
            }
        }

        // Step 6: Calculate the probability of each n-gram
        int totalNGrams = nGramCounts.values().stream().mapToInt(Integer::intValue).sum();
        for (Map.Entry<String, Integer> entry : nGramCounts.entrySet()) {
            double probability = (double) entry.getValue() / totalNGrams;
            nGramProbabilities.put(entry.getKey(), probability);
        }
    }

    // Step 7: Predictive Words
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

    public static void main(String[] args) {
        // Replace this with code to get the corpus from GrammarManager
        List<String> corpus = grammarManager.getWordsForPredictiveTyping();

        PredictiveTyping4 model = new PredictiveTyping4();
        model.train(corpus, 2); // Use bigrams (n=2)

        String inputPrefix = "a"; // Replace with input from a text field
        List<String> predictions = model.predictWordsStartingWith(inputPrefix);

        System.out.println("Predictions for words starting with '" + inputPrefix + "':");
        for (String prediction : predictions) {
            System.out.println(prediction);
        }
    }
}
