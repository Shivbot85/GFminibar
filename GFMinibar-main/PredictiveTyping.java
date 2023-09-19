import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PredictiveTyping {

    private Map<String, Concr> langs;  //already populated
    private List<String> lexicon;  //already populated
    private Table<String, String, Integer> bigramTable;  // Use Guava Table for corpus


    public PredictiveTyping(Map<String, Concr> langs, List<String> lexicon) {
        this.langs = langs;
        this.lexicon = lexicon;
        this.bigramTable = HashBasedTable.create(); // Initialize a Guava Table
    }

    public void addBigramCount(String word1, String word2, int count) {
        bigramTable.put(word1, word2, count);
    }

    public List<String> getSuggestions(String prefix) {
        // Get bigrams starting with the prefix
        List<String> bigrams = generateBigrams(prefix);

        // Sort the bigrams by bigram counts
        List<String> sortedBigrams = sortBigramsByCounts(bigrams);

        // Get the suggested words from the sorted bigrams
        List<String> suggestions = extractWordsFromBigrams(sortedBigrams);

        return suggestions;
    }

    private List<String> generateBigrams(String prefix) {
        List<String> bigrams = new ArrayList<>();
        String[] words = prefix.split(" ");

        if (words.length >= 2) {
            for (int i = 0; i < words.length - 1; i++) {
                String bigram = words[i] + " " + words[i + 1];
                bigrams.add(bigram);
            }
        }

        return bigrams;
    }

    private List<String> sortBigramsByCounts(List<String> bigrams) {
        List<String> sortedBigrams = new ArrayList<>(bigrams);
        sortedBigrams.sort((bg1, bg2) -> {
            int count1 = bigramTable.get(bg1);
            int count2 = bigramTable.get(bg2);
            return Integer.compare(count2, count1); // Sort in descending order
        });

        return sortedBigrams;
    }

    private List<String> extractWordsFromBigrams(List<String> bigrams) {
        List<String> suggestions = new ArrayList<>();

        for (String bigram : bigrams) {
            String[] words = bigram.split(" ");
            if (words.length == 2 && lexicon.contains(words[1])) {
                suggestions.add(words[1]);
            }
        }

        return suggestions;
    }

    // Other helper methods as needed

    public static void main(String[] args) {
        // Assuming you have initialized langs and lexicon

        PredictiveTyping predictiveTyping = new PredictiveTyping(langs, lexicon);

        // Create a Guava Table and populate it with bigram counts
        Table<String, String, Integer> bigramTable = HashBasedTable.create();
        bigramTable.put("the", "cat", 10);
        bigramTable.put("the", "dog", 5);
        bigramTable.put("quick", "brown", 7);

        // Set the bigramTable in your predictiveTyping instance
        predictiveTyping.setBigramTable(bigramTable);

        // Get suggestions for a prefix
        List<String> suggestions = predictiveTyping.getSuggestions("the");

        // Display the suggestions
        for (String suggestion : suggestions) {
            System.out.println(suggestion);
        }
    }
}
