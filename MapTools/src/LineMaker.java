import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: student
 * Date: 5/14/12
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class LineMaker {
    public static String createLine(Point upperLeft, Point lowerRight) {
        String out = "";
        int totalCircles = (int) upperLeft.distance(lowerRight) / 20 + 1;
        for (int count = 0; count < totalCircles; count++) {
            int distance = 20 * count;
            int rise = upperLeft.y - lowerRight.y;
            int run = upperLeft.x - lowerRight.x;
            double angle;
            if (run != 0) {
                angle = Math.atan(rise / run);
            } else {
                angle = Math.PI / 2;
            }
            int x = (int) Math.round(Math.cos(angle) * distance + upperLeft.x);
            int y = (int) Math.round(Math.sin(angle) * distance + upperLeft.y);
            out += x + "," + y + ";";
        }
        return out;
    }

    public static String createRectangle(Point topLeft, Point bottomRight) {
        String out = "";
        out += createLine(topLeft, new Point(bottomRight.x, topLeft.y));
        out += createLine(topLeft, new Point(topLeft.x, bottomRight.y));
        out += createLine(new Point(topLeft.x, bottomRight.y), bottomRight);
        out += createLine(new Point(bottomRight.x, topLeft.y), bottomRight);
        return out;
    }

    public static String createCircle(int radius, int center, int circleRadius) {
        //TODO Create Circle Method
        return "";
    }
}
