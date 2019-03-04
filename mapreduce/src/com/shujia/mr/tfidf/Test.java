package com.shujia.mr.tfidf;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;

public class Test {
    public static void main(String[] args) throws Exception {
        String str = "今天约了姐妹去逛街吃美食，周末玩得很开心啊！";

        StringReader sr = new StringReader(str);
        IKSegmenter ikSegmenter = new IKSegmenter(sr, true);
        Lexeme word = null;
        while ((word = ikSegmenter.next()) != null) {
            String w = word.getLexemeText();
            System.out.println(w);
        }
    }
}
