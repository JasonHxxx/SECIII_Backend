package team.software.collect.similarity.textSimilarity.similarity.sentence.editdistance;


import team.software.collect.similarity.textSimilarity.similarity.ISimilarity;

/**
 * 编辑距离
 */
public abstract class EditDistance implements ISimilarity {
    @Override
    public double getSimilarity(String str1, String str2) {
        SuperString<WordEditUnit> S = SuperString.createWordSuperString(str1);
        SuperString<WordEditUnit> T = SuperString.createWordSuperString(str2);
        //return 1 - (getEditDistance(S, T)) / (Math.max(S.length(), T.length()));
        return 1.0 / (getEditDistance(S, T) + 1);
    }

    public abstract double getEditDistance(SuperString<? extends EditUnit> S, SuperString<? extends EditUnit> T);
}
