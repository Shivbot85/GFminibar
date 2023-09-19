import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PredictiveTyping2 {

    private Map<String, Concr> langs;  //
    private Table<String, String, Boolean> bigramTable;  // Use Guava Table for bigrams

    public PredictiveTyping(Map<String, Concr> langs) {
        this.langs = langs;
        this.bigramTable = HashBasedTable.create(); // Initialize a Guava Table for bigrams
    }

    public void addBigram(String word1, String word2) {
        bigramTable.put(word1, word2, true);
    }

    public List<String> getSuggestions(String prefix) {
        // Get bigrams starting with the prefix
        List<String> bigrams = generateBigrams(prefix);

        // Get the suggested words from the bigrams
        List<String> suggestions = extractWordsFromBigrams(bigrams);

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

    private List<String> extractWordsFromBigrams(List<String> bigrams) {
        List<String> suggestions = new ArrayList<>();

        for (String bigram : bigrams) {
            String[] words = bigram.split(" ");
            if (words.length == 2) {
                // Check if the second word of the bigram exists in the lexicon
                Concr languageX = langs.get("your_language_here"); // Replace with the appropriate language identifier
                if (languageX != null && languageX.isInLexicon(words[1])) {
                    suggestions.add(words[1]);
                }
            }
        }

        return suggestions;
    }



    public static void main(String[] args) {
        // Assuming you have initialized langs

        PredictiveTyping predictiveTyping = new PredictiveTyping(langs);

        // Allow users to input words and add bigrams to the Guava Table
        predictiveTyping.addBigram("the", "cat");
        predictiveTyping.addBigram("cat", "is");
        predictiveTyping.addBigram("is", "fast");

        // Get suggestions for a prefix
        List<String> suggestions = predictiveTyping.getSuggestions("the cat");

        // Display the suggestions
        for (String suggestion : suggestions) {
            System.out.println(suggestion);
        }
    }
}
