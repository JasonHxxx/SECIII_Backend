package team.software.collect.similarity.TextSimilarity.word2vec.vec;

import org.junit.Assert;
import org.junit.Test;
import team.software.collect.similarity.textSimilarity.word2vec.vec.Learn;

import java.io.File;

/**
 * @author xuming
 */
public class LearnTest {
    private static final String RAW_CORPUS = "corpus/tianlongbabu.txt";
    private static final String RAW_CORPUS_SPLIT = "corpus/tianlongbabu.split.txt";
    private static final String RAW_CORPUS_SPLIT_MODEL = "corpus/tianlongbabu.split.learn.model";

    @Test
    public void learnFile() throws Exception {
        Learn learn = new Learn();
        long start = System.currentTimeMillis();
        learn.learnFile(new File(RAW_CORPUS_SPLIT));
        System.out.println("use time " + (System.currentTimeMillis() - start));
        learn.saveModel(new File(RAW_CORPUS_SPLIT_MODEL));
        Assert.assertTrue(learn != null);
    }

}
