import java.awt.Point;

/**
 * Created with IntelliJ IDEA.
 * User: student
 * Date: 5/14/12
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static final int circleRadius = 20;

    public static void main(String[] args) {
        System.out.println(LineMaker.createRectangle(new Point(0, 0), new Point(1000, 1000)) + LineMaker.createLine(new Point(500, 0), new Point(500, 440)) + LineMaker.createLine(new Point(500, 560), new Point(500, 1000)));
    }
}
