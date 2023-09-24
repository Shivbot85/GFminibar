import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TranslationFormatter {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void formatAndPrintTranslation(String translation, List<String> translations, String fromLanguage) {
        // Get the current date and time
        Date currentTime = new Date();
        String formattedTime = dateFormat.format(currentTime);

        // Print the header
        System.out.println("Time(time of translation): " + formattedTime);
        System.out.println("From:\tTo:");
        System.out.println("(sentence)\t" + fromLanguage);

        // Iterate through the translations
        int numTranslations = translations.size();
        for (int i = 0; i < numTranslations; i += 2) {
            String toLanguage = translations.get(i);
            String translatedText = translations.get(i + 1);

            // Print the language and translation
            System.out.println("\t" + toLanguage);
            System.out.println("\t" + translatedText);
        }

        // Print an empty line for separation
        System.out.println();
    }

    public static void main(String[] args) {
        // Example usage
        String translation = "Hello, how are you?";
        List<String> translations = List.of("English", "Hola, ¿cómo estás?", "Spanish", "Bonjour, comment ça va?", "French");
        String fromLanguage = "English";

        formatAndPrintTranslation(translation, translations, fromLanguage);
    }
}

