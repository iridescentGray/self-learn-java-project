package collection;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;

/**
 * Range定义了连续跨度的范围边界，且让边界可比较
 * <p>
 * 概念	        表示范围             	    guava对应功能方法
 * (a..b)	    {x | a < x < b}	            open(C, C)
 * [a..b]	    {x | a <= x <= b}	        closed(C, C)
 * [a..b)	    {x | a <= x < b}    	    closedOpen(C, C)
 * (a..b]	    {x | a < x <= b}	        openClosed(C, C)
 * (a..+∞)	    {x | x > a}	                greaterThan(C)
 * [a..+∞)	    {x | x >= a}	            atLeast(C)
 * (-∞..b)	    {x | x < b}	                lessThan(C)
 * (-∞..b]	    {x | x <= b}	            atMost(C)
 * (-∞..+∞)	    all values	                all()
 *
 * @author zjy
 */
@Slf4j
public class RangeCase {


    public static void buildRange() {
        System.out.println("open:" + Range.open(1, 10));
        System.out.println("closed:" + Range.closed(1, 10));
        System.out.println("closedOpen:" + Range.closedOpen(1, 10));
        System.out.println("openClosed:" + Range.openClosed(1, 10));
        System.out.println("greaterThan:" + Range.greaterThan(10));
        System.out.println("atLeast:" + Range.atLeast(10));
        System.out.println("lessThan:" + Range.lessThan(10));
        System.out.println("atMost:" + Range.atMost(10));
        System.out.println("all:" + Range.all());
        System.out.println("closed:" + Range.closed(10, 10));
        System.out.println("closedOpen:" + Range.closedOpen(10, 10));

        System.out.println("downTo:" + Range.downTo(4, BoundType.OPEN));
        System.out.println("upTo:" + Range.upTo(4, BoundType.CLOSED));
        System.out.println("range:" + Range.range(1, BoundType.CLOSED, 4, BoundType.OPEN));

        //会抛出异常
        try {
            System.out.println("open:" + Range.open(10, 10));
        } catch (Exception e) {
            log.error("error is", e);
        }
    }

    /**
     * contains方法 包含在区间内
     */
    public static void containsRange() {
        System.out.println(Range.closed(1, 3).contains(2));
        System.out.println(Range.closed(1, 3).contains(4));
        System.out.println(Range.lessThan(5).contains(5));
        System.out.println(Range.closed(1, 4).containsAll(Ints.asList(1, 2, 3)));
    }

    /**
     *
     */
    public static void rangeEndpoint() {
        System.out.println("hasLowerBound:" + Range.closedOpen(4, 4).hasLowerBound());
        System.out.println("hasUpperBound:" + Range.closedOpen(4, 4).hasUpperBound());
        System.out.println(Range.closedOpen(4, 4).isEmpty());
        System.out.println(Range.openClosed(4, 4).isEmpty());
        System.out.println(Range.closed(4, 4).isEmpty());
        // Range.open throws IllegalArgumentException
        //System.out.println(Range.open(4, 4).isEmpty());

        System.out.println(Range.closed(3, 10).lowerEndpoint());
        System.out.println(Range.open(3, 10).lowerEndpoint());
        System.out.println(Range.closed(3, 10).upperEndpoint());
        System.out.println(Range.open(3, 10).upperEndpoint());
        System.out.println(Range.closed(3, 10).lowerBoundType());
        System.out.println(Range.open(3, 10).upperBoundType());
    }

    /**
     * range是否包含在需要比较的range中
     */
    public static void encloses() {
        Range<Integer> rangeBase = Range.open(1, 4);
        Range<Integer> rangeClose = Range.closed(2, 3);
        Range<Integer> rangeCloseOpen = Range.closedOpen(2, 4);
        Range<Integer> rangeCloseOther = Range.closedOpen(2, 5);
        System.out.println("rangeBase: " + rangeBase + " Enclose:" + rangeBase.encloses(rangeClose) + " rangeClose:" + rangeClose);
        System.out.println("rangeBase: " + rangeBase + " Enclose:" + rangeBase.encloses(rangeCloseOpen) + " rangeClose:" + rangeCloseOpen);
        System.out.println("rangeBase: " + rangeBase + " Enclose:" + rangeBase.encloses(rangeCloseOther) + " rangeClose:" + rangeCloseOther);
    }

    /**
     * isConnected 判断range是否可连接上
     */
    public static void isConnected() {
        System.out.println(Range.closed(3, 5).isConnected(Range.open(5, 10)));
        System.out.println(Range.closed(0, 9).isConnected(Range.closed(3, 4)));
        System.out.println(Range.closed(0, 5).isConnected(Range.closed(3, 9)));
        System.out.println(Range.open(3, 5).isConnected(Range.open(5, 10)));
        System.out.println(Range.closed(1, 5).isConnected(Range.closed(6, 10)));
    }

    /**
     * intersection
     * 如果两个range相连时，返回最大交集
     * 如果不相连时，直接抛出异常
     */
    public static void intersection() {
        System.out.println(Range.closed(3, 5).isConnected(Range.open(5, 10)));
        System.out.println(Range.closed(0, 9).isConnected(Range.closed(3, 4)));
        System.out.println(Range.closed(0, 5).isConnected(Range.closed(3, 9)));
        System.out.println(Range.open(3, 5).isConnected(Range.open(5, 10)));
        System.out.println(Range.closed(1, 5).isConnected(Range.closed(6, 10)));
    }

    /**
     * span
     * 获取两个range的并集
     * 如果两个range是两连的，则是其最小range
     */
    public static void span() {
        System.out.println(Range.closed(3, 5).isConnected(Range.open(5, 10)));
        System.out.println(Range.closed(0, 9).isConnected(Range.closed(3, 4)));
        System.out.println(Range.closed(0, 5).isConnected(Range.closed(3, 9)));
        System.out.println(Range.open(3, 5).isConnected(Range.open(5, 10)));
        System.out.println(Range.closed(1, 5).isConnected(Range.closed(6, 10)));
    }

    public static void main(String[] args) {
        System.out.println("--------------------buildRange--------------------------------");
        buildRange();
        System.out.println("-------------------containsRange---------------------------------");
        containsRange();
        System.out.println("----------------------rangeEndpoint------------------------------");
        rangeEndpoint();
        System.out.println("-----------------------encloses-----------------------------");
        encloses();
        System.out.println("----------------------isConnected------------------------------");
        isConnected();
        System.out.println("-----------------------intersection-----------------------------");
        intersection();
        System.out.println("----------------------span------------------------------");
        span();

    }
}
