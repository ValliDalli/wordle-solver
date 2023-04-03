import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Random;

public class help {
    public static ArrayList<String> setUp() throws FileNotFoundException {
        Scanner s = new Scanner(new File("source/wordle.txt"));
        ArrayList<String> list = new ArrayList<>();
        while (s.hasNext()){
            list.add(s.next());
        }
        s.close();
        return list;
    }
    public static String powerGuess(ArrayList<String> words,ArrayList<Character> letters) throws FileNotFoundException {
        //make a diffrent guess if words size is small
        if(words.size()<=10||letters.size()>3){
            Random random = new Random();
            int randomNumber = random.nextInt(words.size());
            return words.get(randomNumber);

        }
        Map<Character, Integer> dictionary = new HashMap<>();
        for(String word: words) {
            for (int i = 0; i < 5; i++) {
                char letter = word.charAt(i);
                if (!dictionary.containsKey(letter)) {
                    dictionary.put(letter, 1);
                } else {
                    dictionary.put(letter, dictionary.get(letter) + 1);
                }


            }

        }
        for (char letter : letters) {
            dictionary.remove(letter);
        }
            int highscore=0;
            String bestWord="";

            ArrayList<String> allWorlds=setUp();

            Map<Character, Integer> deleted=new HashMap<>();
            for(String item:allWorlds){
                int score=0;
                for(int i=0;i<5;i++){
                    if(dictionary.containsKey(item.charAt(i))){
                        score+=dictionary.get(item.charAt(i));
                        deleted.put(item.charAt(i),dictionary.get(item.charAt(i)));
                        dictionary.remove(item.charAt(i));
                    }
                }
                if(score>=highscore){
                    highscore=score;
                    bestWord=item;
                }
                dictionary.putAll(deleted);
                deleted.clear();

            }
            return bestWord;
    }




    public static boolean LetterInWord(char letter,String word){
        if(word.isEmpty()){
            return false;
        }
        for(int i=0;i<word.length();i++){
            if(word.charAt(i)==letter){
                return true;
            }

        }
        return false;
    }
    public static Map<Character,Integer> createDict(String guess, String result){
        Map<Character, Integer> map = new HashMap<>();
        String greys="";
        for(int i=4;i>=0;i--){
            char letter=result.charAt(i);
            if(letter=='b'){
                greys+=guess.charAt(i);

            } else if (letter == 'g' || letter == 'y') {
                if(LetterInWord(guess.charAt(i),greys)){
                    map.put(guess.charAt(i),1);
                }
                else if(map.containsKey(letter)){
                    map.put(guess.charAt(i),map.get(guess.charAt(i))+1);
                }

            }

        }
        return map;
    }
    public static String result(String[] colors){
        StringBuilder result= new StringBuilder();
        String grey="rgba(58, 58, 60, 1)";//for grey we use b because green also starts with g
        String yellow="rgba(181, 159, 59, 1)";
        String green="rgba(83, 141, 78, 1)";
        for (int i=0;i<5;i++) {

            if(colors[i].equals(yellow)){
                result.append('y');
            }
            else if(colors[i].equals(green)) {
                result.append('g');
            }
            else {
                result.append('b');
            }
        }
        return result.toString();
    }
}
