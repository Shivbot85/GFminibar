import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.List;

public class PredictiveTyping3 {
    private GrammarManager grammarManager;
    private Table<String, String, List<String>> bigramTable;


    public PredictiveTyping3() {
        grammarManager = new GrammarManager();
        bigramTable = HashBasedTable.create(); // Initialize a Guava Table for bigram data
    }

    public List<String> getWordsForPredictiveTyping(String userInput) {
        String currentLanguage = grammarManager.getLanguage(); // Get the current language
        List<String> categories = grammarManager.getCategories();
        // Choose a category based on your application logic
        String category = categories.isEmpty() ? "" : categories.get(0); // Select the first category as an example

        List<String> words = grammarManager.loadWords(currentLanguage, category, userInput);



        return words;
    }

    // Add a bigram to the Guava Table
    public void addBigram(String word1, String word2, List<String> suggestions) {
        bigramTable.put(word1, word2, suggestions);
    }

    // Get predictive suggestions based on user input and bigram data
    public List<String> getPredictiveSuggestions(String userInput) {
        List<String> wordsFromUserInput = getWordsForPredictiveTyping(userInput);
        if (wordsFromUserInput.isEmpty()) {
            // No user input, return empty suggestions
            return List.of();
        }

        // Get the last word from the user input
        String lastWord = wordsFromUserInput.get(wordsFromUserInput.size() - 1);

        // Lookup bigram data for the last word
        List<String> suggestions = bigramTable.row(lastWord).values();



        return suggestions;
    }

    public static void main(String[] args) {
        PredictiveTyping2 predictiveTyping = new PredictiveTyping2();

        // Example usage:
        String userInput = "The quick";
        List<String> predictiveSuggestions = predictiveTyping.getPredictiveSuggestions(userInput);

        // Print the predictive suggestions
        System.out.println("Predictive Suggestions:");
        for (String suggestion : predictiveSuggestions) {
            System.out.println(suggestion);
        }
    }
}
