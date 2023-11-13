package com.nowcoder.community.util;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    TrieNode rootNode = new TrieNode();

    //初始化节点方法，读取文件中的所有敏感词
    //当容器实例化这个类，这个方法就会自动被调用
    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));){
            String keyword;
            while ((keyword = reader.readLine()) != null){
                this.addKeyword(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }
    //将一个敏感词添加到前缀树中
    private void addKeyword(String  keyword){
        //每个单词的添加都要从root开始，因为要检测root有没有这个敏感单词的首字母
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++){
            //将单词一个个装入
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);

            if (subNode == null){
                //当前节点没有值等于c的子节点，那就初始化添加它
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }

            //指向子节点，进入下一轮循环
            tempNode = subNode;

            //当前的这个节点是不是一个敏感单词的最后一个节点，是就设置结束标志
            if (i == keyword.length() - 1){
               tempNode.setKeywordEnd(true);
            }
        }
    }
    /*
    * 过滤敏感词
    * @Param text 待过滤的文本
    * @return 过滤后的文本
    * */
    public String filter(String text){
        String allSb = null;
        if (StringUtils.isBlank(text)){
            return null;
        }

        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果字符串(变长)
        StringBuilder stringBuilder = new StringBuilder();

        //头碰到边界
        while (begin < text.length()){
            //触手没碰到边界
            if(position < text.length()) {
                char c = text.charAt(position);

                //跳过符号
                if (isSymbol(c)) {
                    //若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                    if (tempNode == rootNode) {
                        stringBuilder.append(c);
                        begin++;
                    }
                    //无论符号在中间或开头，指针3都向下走一步
                    position++;
                    continue;
                }

                //检测,要以默认在root的想法去想
                //以下一节点作为判断
                tempNode = tempNode.getSubNode(c);
                if (tempNode == null) {
                    //前缀树的屏蔽词的第一个字母没有这个字母
                    stringBuilder.append(text.charAt(begin));
                    //头往前一格并触手回收
                    position = ++begin;
                    //前缀树回原
                    tempNode = rootNode;
                } else if (tempNode.isKeywordEnd()) {
                    //这个字符处确认是敏感词，从begin~positon字符串替换
                    stringBuilder.append(REPLACEMENT);
                    //触手往前移一格，头去触手那里
                    begin = ++position;
                    //前缀树回原
                    tempNode = rootNode;
                } else {
                    if (position < text.length() - 1)
                        position++;
                }
            }else { //触手碰到边界了，但还没有确定答案
                stringBuilder.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }
        }
        return stringBuilder.toString();
    }


    //判断是否为符号
    private boolean isSymbol(Character c){
        //c < 0x2E80 || c > 0x9FFF dot's 东亚范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树
    private class TrieNode {
        //关键词结束标识
        private boolean isKeywordEnd = false;
        public boolean isKeywordEnd(){
            return  isKeywordEnd;
        }
        public void setKeywordEnd(boolean keywordEnd){
            isKeywordEnd = keywordEnd;
        }

        //子节点（key是下节字符，value是下级节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();
        //添加子节点
        public void addSubNode(Character c, TrieNode trieNode){
            subNodes.put(c, trieNode);
        }
        //获取字节点
        public TrieNode getSubNode(Character character){
            return subNodes.get(character);
        }
    }
}
