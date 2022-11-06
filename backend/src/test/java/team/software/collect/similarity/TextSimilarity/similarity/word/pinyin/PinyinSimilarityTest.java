package team.software.collect.similarity.TextSimilarity.similarity.word.pinyin;


import org.junit.Test;
import team.software.collect.similarity.textSimilarity.similarity.word.pinyin.PinyinSimilarity;

/**
 * @author xuming
 */
public class PinyinSimilarityTest {
    @Test
    public void getSimilarity() throws Exception {
        PinyinSimilarity pinyinSimilarity = new PinyinSimilarity();
        double result = pinyinSimilarity.getSimilarity("教授", "教师");
        System.out.println("教授" + " 教师 " + ":" + result);

    }

}
