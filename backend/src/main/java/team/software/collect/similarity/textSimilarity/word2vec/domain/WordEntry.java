package team.software.collect.similarity.textSimilarity.word2vec.domain;

/**
 * 词语属性
 */
public class WordEntry implements Comparable<WordEntry> {
    public String name;
    public float score;

    public WordEntry(String name, float score) {
        this.name = name;
        this.score = score;
    }

    public String toString() {
        return "name:" + this.name + "\tscore:" + this.score;
    }

    @Override
    public int compareTo(WordEntry o) {
        if (this.score < o.score) {
            return 1;
        } else
            return -1;
    }
}
