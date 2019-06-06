import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.Object;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Mandel extends Thread {
    int width;
    int height;
    int h;
    int v;
    int squareLen;

    int itr;
    DrawingPanel panel;
    int[][] points;
    double centerX;
    double centerY;
    double offset;
    int draw;
    BufferedImage image;

    public Mandel(int wi, int hi, int ho, int vo, int square, int i, DrawingPanel p, int[][] pointz, double cx,
            double cy, double off, int d, BufferedImage buff) {
        width = wi;
        height = hi;
        h = ho;
        v = vo;
        squareLen = square;
        itr = i;
        panel = p;
        points = pointz;
        centerX = cx;
        centerY = cy;
        offset = off;
        draw = d;
        image = buff;
    }

    public void run() {
        double minX = centerX - offset;
        double maxX = centerX + offset;
        double minY = centerY - offset;
        double maxY = centerY + offset;
        double mult = 16777215 / itr;
        // BufferedImage = new BuferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = h; x < h + squareLen; x++) {
            for (int y = v; y < v + squareLen; y++) {
                double a = (((maxX - minX) / width) * x) + minX;
                double b = (((minY - maxY) / height) * y) + maxY;
                Complex cur = new Complex(a, b);
                int num = (int) (numItr(cur, itr) * mult);
                points[y][x] = num;
                image.setRGB(x, y, num);
                if (draw == 1){
                    panel.setPixels(points);
                }
            }
        }
        // ImageIO.write(image, "jpg",
        // new File(System.getProperty("user.dir") + "/pics", Integer.toString(n) +
        // ".jpg"));
    }

    public static double numItr(Complex num, int itr) {
        Complex start = new Complex(num.getA(), num.getB());
        Complex cur = new Complex(num.getA(), num.getB());
        double index = 0;
        while (cur.inMandel()) {
            index++;
            Complex next = cur.square();
            next = next.add(start);
            cur = new Complex(next.getA(), next.getB());
            if (index >= itr) {
                break;
            }
        }
        double z = Math.sqrt((cur.getA() * cur.getA()) + (cur.getB() * cur.getB()));
        return index + 1 - (float) Math.log((Math.log(z) / Math.log(2)));
    }
}
