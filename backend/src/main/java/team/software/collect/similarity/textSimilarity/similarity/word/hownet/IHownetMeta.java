package team.software.collect.similarity.textSimilarity.similarity.word.hownet;

/**
 * Hownet 接口
 */
public interface IHownetMeta {
    String Symbol_Descriptions[][] = {
            {"#", "表示与其相关"},
            {"%", "是其部分"},
            {"$", "可以被该V处置，或是该V的受事、对象、领有物，或内容"},
            {"*", "施事或工具"},
            {"+", "所标记的角色是隐性的，几乎在实际语言中不会出现"},
            {"&", "指向"},
            {"~", "多半是，多半有，很可能"},
            {"@", "可以做V的空间或时间"},
            {"?", "可以使N的材料"},
            {"(", "至于其中的应该是一个词标记"},
            {"^", "不存在，或没有，或不能"},
            {"!", "表示某一属性为一敏感的属性，如味道之与食物"},
            {"[", "标示概念的共性属性"}
    };

    /**
     * γ：具体词与义元的相似度一律为一个较小的常数
     */
    double gamma = 0.2;

    /**
     * δ:任一个非空值与空值的相似度为一个较小的常数，此处为0.2
     */
    double delta = 0.2;

    /**
     * β1实词概念第一基本义原描述式的权重
     */
    double beta1 = 0.5;
    /**
     * β2实词概念其他基本义原描述式的权重
     */
    double beta2 = 0.2;
    /**
     * β3实词概念关系义原描述式的权重
     */
    double beta3 = 0.17;
    /**
     * β4实词概念符号义原描述式的权重
     */
    double beta4 = 0.13;

    /**
     * Θ 计算后面概念的义原与参照概念所有义原的最大相似度, 并乘以两个概念主义原相似度的积(主义原通过该方式起约束作用),
     * 如果数值大于该值时才会起参照作用, 去掉冗余的不重要义原
     */
    double PARAM_THETA = 0.5;
    /**
     * Ω 计算前面概念的义原与参照概念所有义原的最大相似度，并乘以两个概念主义原相似度的积(主义原通过该方式起约束作用),
     * 如果数值大于该值时才会调整前面概念的义原符号, 以起修正作用
     */
    double PARAM_OMEGA = 0.8;
    /**
     * 阈值
     */
    double PARAM_XI = 0.6;


}
