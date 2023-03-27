package collection;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;

/**
 * ComparisonChain 用于简化复杂Comparator的构建
 *
 * @author zjy
 */
@Slf4j
public class ComparisonChainCase {

    public static void main(String[] args) {
        Student nameCompare = new Student("aida", 23, 36);
        Student nameCompare2 = new Student("peida", 23, 80);

        Student ageCompare = new Student("aida", 23, 80);
        Student ageCompare2 = new Student("aida", 24, 36);

        Student scoreCompare = new Student("aida", 23, 80);
        Student scoreCompare2 = new Student("aida", 24, 36);

        System.out.println("==========compareTo===========");
        System.out.println(nameCompare.compareTo(nameCompare2));
        System.out.println(ageCompare.compareTo(ageCompare2));
        System.out.println(scoreCompare.compareTo(scoreCompare2));
    }

    static class Student implements Comparable<Student> {
        public String name;
        public int age;
        public int score;

        Student(String name, int age, int score) {
            this.name = name;
            this.age = age;
            this.score = score;
        }

        @Override
        public int compareTo(Student other) {
            return ComparisonChain.start()
                    .compare(name, other.name)
                    .compare(age, other.age)
                    .compare(score, other.score, Ordering.natural().nullsLast())
                    .result();
        }
    }

}
