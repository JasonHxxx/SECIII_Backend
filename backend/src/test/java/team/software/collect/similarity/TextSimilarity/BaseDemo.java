package team.software.collect.similarity.TextSimilarity;

import team.software.collect.similarity.textSimilarity.Similarity;

/**
 * @author xuming
 */
public class BaseDemo {
    public static void main(String[] args) {

        System.out.println("首次编译运行....");
        double result = Similarity.cilinSimilarity("电动车","自行车");
        System.out.println(result);
    }
}
