package team.software.collect.similarity.textSimilarity.similarity.util;

/**
 * 遍历接口
 */
public interface TraverseEvent<T> {
    /**
     * 遍历每一个
     *
     * @param item
     * @return
     */
    boolean visit(T item);
}
