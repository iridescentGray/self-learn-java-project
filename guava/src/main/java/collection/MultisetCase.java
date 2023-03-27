package collection;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Multiset 是值可重复的Set，多用于统计值出现的次数
 *
 * @author zjy
 */
@Slf4j
public class MultisetCase {

    /**
     * 简单统计某个单词的数量
     */
    public static void simpleWordCountCase() {
        String strWorld = "wer|dffd|ddsa|dfd|dreg|de|dr|ce|ghrt|cf|gt|ser|tg|ghrt|cf|gt|" +
                "ser|tg|gt|kldf|dfg|vcd|fg|gt|ls|lser|dfr|wer|dffd|ddsa|dfd|dreg|de|dr|" +
                "ce|ghrt|cf|gt|ser|tg|gt|kldf|dfg|vcd|fg|gt|ls|lser|dfr";
        String[] words = strWorld.split("\\|");
        Map<String, Integer> countMap = new HashMap<>();
        for (String word : words) {
            Integer count = countMap.get(word);
            if (count == null) {
                countMap.put(word, 1);
            } else {
                countMap.put(word, count + 1);
            }
        }
        System.out.println("countMap：");
        for (String key : countMap.keySet()) {
            System.out.println(key + " count：" + countMap.get(key));
        }
    }

    /**
     * 使用set统计word数量
     */
    public static void multsetWordCountCase() {
        String strWorld = "wer|dr|dr|drdfd|dd|dfd|dfd|dfd|dda|dda|dda|dda|de|dr";
        String[] words = strWorld.split("\\|");
        Multiset<String> wordsMultiset = HashMultiset.create();
        wordsMultiset.addAll(Arrays.asList(words));
        for (String key : wordsMultiset.elementSet()) {
            System.out.println(key + " count：" + wordsMultiset.count(key));
        }
    }

    public static void multsetApiCase() {
        String strWorld = "wer|dr|dr|drdfd|dd|dfd|dfd|dfd|dda|dda|dda|dda|de|dr";
        String[] words = strWorld.split("\\|");
        Multiset<String> wordsMultiset = HashMultiset.create();
        wordsMultiset.addAll(Arrays.asList(words));

        if (!wordsMultiset.contains("peida")) {
            wordsMultiset.add("peida", 2);
        }
        for (String key : wordsMultiset.elementSet()) {
            System.out.println(key + " count：" + wordsMultiset.count(key));
        }


        if (wordsMultiset.contains("peida")) {
            wordsMultiset.setCount("peida", 23);
        }

        System.out.println("============================================");
        for (String key : wordsMultiset.elementSet()) {
            System.out.println(key + " count：" + wordsMultiset.count(key));
        }

        if (wordsMultiset.contains("peida")) {
            wordsMultiset.setCount("peida", 23, 45);
        }

        System.out.println("============================================");
        for (String key : wordsMultiset.elementSet()) {
            System.out.println(key + " count：" + wordsMultiset.count(key));
        }

        if (wordsMultiset.contains("peida")) {
            wordsMultiset.setCount("peida", 44, 67);
        }

        System.out.println("============================================");
        for (String key : wordsMultiset.elementSet()) {
            System.out.println(key + " count：" + wordsMultiset.count(key));
        }
    }


    public static void main(String[] args) {
        System.out.println("----------------------simpleWordCountCase------------------------------");
        simpleWordCountCase();
        System.out.println("----------------------multsetWordCountCase------------------------------");
        multsetWordCountCase();
        System.out.println("----------------------multsetApiCase------------------------------");
        multsetApiCase();

    }
}
