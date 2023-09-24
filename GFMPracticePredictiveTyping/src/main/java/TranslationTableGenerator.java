import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TranslationTableGenerator {
    public static void generateTranslationTable(String timeOfTranslation, String fromLanguage, String toLanguages, List<String> sentences, List<String> translations) {
        // Create a new Word document
        XWPFDocument document = new XWPFDocument();

        // Create a table
        XWPFTable table = document.createTable(sentences.size() + 1, toLanguages.size() + 2);

        // Set table width
        CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
        width.setType(STTblWidth.DXA);
        width.setW(BigInteger.valueOf(8000)); // Adjust the width as needed

        // Set table header
        XWPFTableRow headerRow = table.getRow(0);
        setTableCell(headerRow.getCell(0), "Time of Translation");
        setTableCell(headerRow.addNewTableCell(), "From");

        for (int i = 0; i < toLanguages.size(); i++) {
            setTableCell(headerRow.addNewTableCell(), toLanguages.get(i));
        }

        // Fill in the table with data
        for (int i = 0; i < sentences.size(); i++) {
            XWPFTableRow row = table.getRow(i + 1);
            setTableCell(row.getCell(0), timeOfTranslation);
            setTableCell(row.getCell(1), fromLanguage);

            for (int j = 0; j < toLanguages.size(); j++) {
                int dataIndex = i * toLanguages.size() + j;
                if (dataIndex < translations.size()) {
                    setTableCell(row.getCell(j + 2), sentences.get(i));
                    setTableCell(row.getCell(j + 2), translations.get(dataIndex));
                }
            }
        }

        // Save the document to a file
        try (FileOutputStream outputStream = new FileOutputStream("TranslationTable.docx")) {
            document.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setTableCell(XWPFTableCell cell, String text) {
        XWPFParagraph paragraph = cell.getParagraphs().get(0);
        XWPFRun run = paragraph.createRun();
        run.setText(text);
    }

    public static void main(String[] args) {
        String timeOfTranslation = "2023-09-15 15:30";
        String fromLanguage = "English";
        List<String> toLanguages = List.of("Spanish", "French", "German");
        List<String> sentences = List.of("Hello", "How are you?", "Goodbye");
        List<String> translations = List.of(
                "Hola", "Comment ça va ?", "Auf Wiedersehen",
                "Hola", "Comment ça va ?", "Auf Wiedersehen",
                "Hola", "Comment ça va ?", "Auf Wiedersehen"
        );

        generateTranslationTable(timeOfTranslation, fromLanguage, toLanguages, sentences, translations);
    }
}

