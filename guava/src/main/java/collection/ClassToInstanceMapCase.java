package collection;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassToInstanceMap  Class ----Map---> 实例
 *
 * @author zjy
 */
@Slf4j
public class ClassToInstanceMapCase {

    /**
     * Person.class ----Map--->  Person实例
     */
    public static void instanceMapCase() {
        ClassToInstanceMap<Person> classToInstanceMap = MutableClassToInstanceMap.create();
        Person person = new Person("peida", 20);
        classToInstanceMap.putInstance(Person.class, person);
        Person person1 = classToInstanceMap.getInstance(Person.class);
        System.out.println("person1 name :" + person1.name + " age:" + person1.age);
    }

    public static void main(String[] args) {
        System.out.println("----------------------instanceMapCase------------------------------");
        instanceMapCase();

    }

    private static class Person {
        public String name;
        public int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
