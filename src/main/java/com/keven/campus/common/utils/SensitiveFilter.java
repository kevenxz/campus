package com.keven.campus.common.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词过滤
 *
 * @author Keven
 * @version 1.0
 */

//@Component
public class SensitiveFilter {

    //日志
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);


    // 根节点   前缀树的根节点为空
    private TrieNode rootNode = new TrieNode();

    // 替换符
    private static final String REPLACEMENT = "***";

    /**
     * 初始化 方法
     */
    @PostConstruct//当容器在实例化bean 之后 ，调用构造器之后，这个方法就会被自动调用(即初始化把树构造好)
    public void init() {
        try (
                //把敏感词文件读出来,从类路径加载资源
                //加载的是字节流
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //先转为字符流(处理字符),再转化为缓冲流(提升速率)
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {//一行一个敏感词
                // 把敏感词添加到前缀树
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }

    }

    /**
     * 将一个敏感词添加到前缀树中,当前类自己初始化时构造的
     * 所以为私有
     *
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;

        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            //获取子节点（因为前缀树一个节点有很多个子节点）所以用hash
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {//子节点为空就初始化
                //初始化子节点
                subNode = new TrieNode();
                //把子节点挂在父节点下面
                tempNode.addSubNode(c, subNode);
            }
            // 下一轮的父节点变为当前的节点
            tempNode = subNode;

            // 设置单词结束的标识,即后缀树一个敏感词的结尾
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词(被外部调用的)
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        // 指针1 指向前缀树（默认指向根）
        TrieNode tempNode = rootNode;
        // 双指针处理text 文本
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        //结果
        StringBuilder result = new StringBuilder();

        //begin开始每一个字符的遍历
        while (begin < text.length()) {
            char c = text.charAt(position);

            //跳过符号
            if (isSymbol(c) && position < text.length() - 1) {//符号position往下加的条件不能是最后一个
                //若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                //无论符号在开头或者中间，指针3都向下走一步
                position++;
                continue;
            }

            // 不是符号，则检查下级节点(开始前缀树的检测)
            tempNode = tempNode.getSubNode(c);
            //为空，说明树中没有这个节点
            if (tempNode == null) {
                // 以begin索引的字符 不是敏感词
                result.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;//两个指针都指向begin的下一位
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd) {
                //发现敏感词，将begin~position字符串替换掉
                result.append(REPLACEMENT);
                //进入这个字符串之后的字符位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else if ((position + 1) == text.length()) {
                //fabc  敏感词 fabcd abc  但是 到c没有了,就要包begin定位到a(而且position不能越界)
                result.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            } else {
                //检查下一个字符
                position++;
            }

        }
        //遍历结束，把剩余的字符串加上
        result.append(text.substring(begin));
        return result.toString();
    }

    /**
     * 判断是否为符号
     *
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        //字母或者数字就返回false
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    //内部类 前缀树
    private class TrieNode {

        //关键词结束标识
        private boolean isKeyWordEnd = false;

        //子节点（key是下级字符，value是下级节点）
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
