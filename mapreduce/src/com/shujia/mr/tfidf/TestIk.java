package com.shujia.mr.tfidf;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

public class TestIk {
    public static void main(String[] args) throws IOException {

        String s = "今天我和一班好朋友约好了一起吃大餐";
        StringReader sr = new StringReader(s);
        IKSegmenter ikSegmenter = new IKSegmenter(sr, true);
        Lexeme word = null;
        while ((word = ikSegmenter.next())!=null){
            String lexemeText = word.getLexemeText();
            System.out.println(lexemeText);
        }

    }
}
