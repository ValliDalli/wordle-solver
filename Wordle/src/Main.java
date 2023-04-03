import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.Color;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    // takes the wordle.txt file and loads every word to an arraylist

    //checks if a String contains exactly count times a letter
    private static boolean contains(char letter, String word, int count){
        for (int i=0;i<5;i++) {
            if(word.charAt(i)==letter){
                count--;
            }
        }
        return (count == 0);
    }
    //removes all words that cant be the solution
    private static ArrayList<String> remaining(ArrayList<String> words,String guess,String result){

        ArrayList<String> viableWords=new ArrayList<>();//every word that may be the solution are being added to this list
        Map<Character, Integer> map=help.createDict(guess,result);
        //if status is true it means that there is a letter marked as grey

        boolean viable=true;// if viable is true it means that with the information we have, we cant sort it out

        for(String word:words){


            for(int i=0;i<5;i++){
                char letterResult=result.charAt(i);
                char letterGuess=guess.charAt(i);
                if(letterResult=='g' ){
                    if (letterGuess!=word.charAt(i)) {
                        viable = false;
                        break;
                    }
                }
                else if(letterResult=='y'){
                    if(!help.LetterInWord(letterGuess,word)||(letterGuess==word.charAt(i))){
                        viable =false;
                        break;
                    }

                }
                else {
                    if(map.containsKey(letterResult)){//
                        if (!contains(letterGuess,word,map.get(letterGuess))){
                            viable=false;
                            break;
                        }


                    }
                    else {
                        if (help.LetterInWord(letterGuess,word)){
                            viable=false;
                            break;
                        }
                    }
                }
            }
            if(viable){// if viable is after every test still true the word is viable;
                viableWords.add(word);
            }
            viable=true;

        }
        return viableWords;
    }




    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        ArrayList<String> words=help.setUp();//list of words that may be the solution
        ArrayList<Character> letters=new ArrayList<>();
        String guess="xylyl";
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.nytimes.com/games/wordle/index.html");

        try {
            WebElement closeButton = driver.findElement(By.id("pz-gdpr-btn-closex"));
            closeButton.click();
        }
        catch (NoSuchElementException e) {
        System.out.println("Close button not found: " + e.getMessage());
        }
        try {
            WebElement secondCloseButton= driver.findElement(By.className("game-icon"));
            secondCloseButton.click();
        }
        catch (NoSuchElementException e) {
            System.out.println("Close button not found: " + e.getMessage());
        }
        TimeUnit.MICROSECONDS.sleep(200000);


        Actions actions = new Actions(driver);

        actions.sendKeys(guess).sendKeys(Keys.ENTER).perform();
        TimeUnit.MILLISECONDS.sleep(2000);
        // Get parent element
        WebElement parent = driver.findElement(By.id("wordle-app-game"));

        // Get board element
        WebElement board = parent.findElement(By.cssSelector(".Board-module_board__jeoPS"));
        int count=0;
        // Loop over rows
        for (WebElement row : board.findElements(By.cssSelector(".Row-module_row__pwpBq"))) {

            String[] myStringArray = new String[5];
            // Loop over squares
            int i=0;
            for (WebElement square : row.findElements(By.cssSelector("div[class=''] > .Tile-module_tile__UWEHN"))) {


                // Get color
                String color = square.getCssValue("background-color");
                myStringArray[i]=color;
                // Do something with letter and color
                //System.out.println("Letter: " + letter + ", Color: " + color);
                i++;

            }
            String result=help.result(myStringArray);
            if(count==5||!help.LetterInWord('b',result)&&!help.LetterInWord('y',result)){
                break;
            }

            for(int j=0;j<5;j++){
                if(result.charAt(j)=='g'||result.charAt(j)=='y'&&!letters.contains(guess.charAt(j))){
                    letters.add(guess.charAt(j));
                }
            }
            words=remaining(words,guess,result);
            guess=help.powerGuess(words,letters);

            actions.sendKeys(guess).sendKeys(Keys.ENTER).perform();
            TimeUnit.MILLISECONDS.sleep(2000);



            count++;
        }

        // Close the driver
        TimeUnit.SECONDS.sleep(10);
        driver.quit();
    }
}