import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class KanjiQuizApp {
    private static final Map<String, String[]> kanjiData = new HashMap<>();

    static {
        // Format: { Kanji, [ Meaning, Origin ] }
        kanjiData.put("日", new String[]{"sun/day", 
            "Derived from a pictograph of the sun (○ with a dot). " +
            "Ancient Chinese drew it as a circle representing the sun."});
        
        kanjiData.put("月", new String[]{"moon/month", 
            "Originally a crescent moon shape (🌙). " +
            "Over time, it became more angular."});
        
        kanjiData.put("山", new String[]{"mountain", 
            "A stylized drawing of three mountain peaks (𓆗 → 山)."});
        
        kanjiData.put("川", new String[]{"river", 
            "Represents flowing water (𓈖 → 川). " +
            "The lines symbolize river currents."});
        
        kanjiData.put("木", new String[]{"tree/wood", 
            "Depicts a tree with roots (𓇯) and branches (木)."});
        
        kanjiData.put("火", new String[]{"fire", 
            "Resembles flames rising (灬 is the 'fire' radical)."});
        
        kanjiData.put("人", new String[]{"person", 
            "Simplified from a stick figure of a person (𓀀 → 人)."});
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int score = 0;
        int totalQuestions = 5; // Adjust as needed

        System.out.println("🎌 Japanese Kanji Quiz with Historical Origins 🎌");
        System.out.println("Guess the meaning, then learn its history!\n");

        Object[] kanjiList = kanjiData.keySet().toArray();

        for (int i = 0; i < totalQuestions; i++) {
            String randomKanji = (String) kanjiList[random.nextInt(kanjiList.length)];
            String[] data = kanjiData.get(randomKanji);
            String correctMeaning = data[0];
            String history = data[1];

            System.out.println("Kanji: " + randomKanji);
            System.out.print("What does this mean? ");
            String userGuess = scanner.nextLine().trim().toLowerCase();

            if (correctMeaning.toLowerCase().contains(userGuess)) {
                System.out.println("✅ Correct! It means: " + correctMeaning);
                score++;
            } else {
                System.out.println("❌ Wrong! The correct meaning is: " + correctMeaning);
            }

            System.out.println("\n📜 Historical Origin:");
            System.out.println(history + "\n");
        }

        System.out.println("Quiz finished! Your score: " + score + "/" + totalQuestions);
        scanner.close();
    }
}
