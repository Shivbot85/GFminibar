
public class Main {
    public static void main(String[] args) {

        try{
            GrammarManager gm = new GrammarManager();
            gm.loadGrammar("Phrasebook.pgf");
            //System.out.println(gm.getCategories());


            RandomGenerator randomizer = new RandomGenerator(gm);
            String r = randomizer.generate("Eng");
            System.out.println(r);
            //System.out.println(gm.getLanguages());
            System.out.println(gm.getTranslation(r, "Eng", "Spa"));
            //gm.getTranslation("we want expensive bread", "Eng", "All");
            //gm.tree("we want expensive bread", "Eng");


            // SETTING UP THE WORDS
            /** List<String> words = gm.loadWords;
             * for(int w = 0 ; w <words.size(); w++){
             * Label numberLabel = new Label(words.get(w));
             //
             **/
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }
}