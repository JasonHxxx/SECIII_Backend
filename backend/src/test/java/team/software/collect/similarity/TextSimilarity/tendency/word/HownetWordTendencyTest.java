package team.software.collect.similarity.TextSimilarity.tendency.word;

import org.junit.Test;
import team.software.collect.similarity.textSimilarity.tendency.word.HownetWordTendency;

/**
 * @author xuming
 */
public class HownetWordTendencyTest {
    @Test
    public void getTendency() throws Exception {
        HownetWordTendency hownet = new HownetWordTendency();
        String word = "美好";
        double sim = hownet.getTendency(word);
        System.out.println(word + ":" + sim);
        System.out.println("混蛋:" + hownet.getTendency("混蛋"));
    }

}
