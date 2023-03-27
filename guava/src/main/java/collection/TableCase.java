package collection;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Table 是用于代替  Map<K1, Map<K2, V>>这种复杂结构的集合，相当于Table<K1,K2,V> /
 * 实际效果 很像excel表 Table<行,列,值>
 *
 * @author zjy
 */
@Slf4j
public class TableCase {

    /**
     * Table<[ABC], [123]], [A1 A2 A3 B1 B2 B3 C1 C2 C3]]>
     * <p>
     * 实际效果如下：
     * 1    2     3
     * A    A1   A2    A3
     * B    B1   B2    B3
     * C    C1   C2    C3
     */
    public static void tableCase() {
        Table<String, Integer, String> aTable = HashBasedTable.create();

        for (char a = 'A'; a <= 'C'; ++a) {
            for (Integer b = 1; b <= 3; ++b) {
                aTable.put(Character.toString(a), b, String.format("%c%d", a, b));
            }
        }

        System.out.println("column(2)  " + aTable.column(2));
        System.out.println("row(\"B\")  " + aTable.row("B"));
        System.out.println("aTable.get(\"B\", 2) " + aTable.get("B", 2));

        System.out.println("contains(\"D\", 1)  " + aTable.contains("D", 1));
        System.out.println("containsColumn(3)   " + aTable.containsColumn(3));
        System.out.println("containsRow(\"C\")  " + aTable.containsRow("C"));

        Map<Integer, Map<String, String>> integerMapMap = aTable.columnMap();
        System.out.println("columnMap() " + integerMapMap);

        Map<String, Map<Integer, String>> stringMapMap = aTable.rowMap();
        System.out.println("rowMap()    " + stringMapMap);

        System.out.println("remove(\"B\", 3)    " + aTable.remove("B", 3));
        System.out.println("tableCase" + aTable);
    }


    public static void main(String[] args) {
        System.out.println("----------------------tableCase------------------------------");
        tableCase();

    }
}
