package team.software.collect.similarity.textSimilarity.similarity.word;


import team.software.collect.similarity.textSimilarity.similarity.util.StringUtil;
import team.software.collect.similarity.textSimilarity.similarity.ISimilarity;

import java.util.ArrayList;
import java.util.List;

/**
 * 词语相似度：字面相似度计算方法
 */
public class CharBasedSimilarity implements ISimilarity {
    private final double alpha = 0.6;
    private final double beta = 0.4;
    private static CharBasedSimilarity instance = null;

    public static CharBasedSimilarity getInstance() {
        if (instance == null) {
            instance = new CharBasedSimilarity();
        }
        return instance;
    }

    private CharBasedSimilarity() {

    }


    @Override
    public double getSimilarity(String word1, String word2) {
        if (StringUtil.isBlank(word1) && StringUtil.isBlank(word2)) {
            return 1.0;
        }
        if (StringUtil.isBlank(word1) || StringUtil.isBlank(word2)) {
            return 0.0;
        }
        if (word1.equalsIgnoreCase(word2)) {
            return 1.0;
        }
        List<Character> sameChars = new ArrayList<>();
        String longString = StringUtil.getLongString(word1, word2);
        String shortString = StringUtil.getShortString(word1, word2);
        for (int i = 0; i < longString.length(); i++) {
            Character ch = longString.charAt(i);
            if (shortString.contains(ch.toString())) {
                sameChars.add(ch);
            }
        }
        double dp = Math.min(1.0 * word1.length() / word2.length(), 1.0 * word2.length() / word1.length());
        double part1 = alpha * (1.0 * sameChars.size() / word1.length() + 1.0 * sameChars.size() / word2.length()) / 2.0;
        double part2 = beta * dp * (getWeightedResult(word1, sameChars) + getWeightedResult(word2, sameChars)) / 2.0;

        return part1 + part2;
    }

    private double getWeightedResult(String word, List<Character> sameChars) {
        double top = 0.0;
        double bottom = 0.0;
        for (int i = 0; i < word.length(); i++) {
            if (sameChars.contains(word.charAt(i))) {
                top += (i + 1);
            }
            bottom += (i + 1);
        }
        return 1.0 * top / bottom;
    }

}
