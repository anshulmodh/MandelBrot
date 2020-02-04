import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.Object;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Mandel extends Thread { // MandelBrot object to draw pictures at certain locations in the mandelbrot set
    int width;          // dimensions of picture
    int height;
    int h;              // number of horizontal thread blocks
    int v;              // number of vertical thread blocks
    int squareLen;      // Size of each thread square
    int itr;            // Accuracy of the picture
    DrawingPanel panel; 
    int[][] points;     
    double centerX;     // center x position 
    double centerY;     // center y position
    double offset;      // radius of the picture
    int draw;           // Draw picture while computing (very slow)
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
        double mult = 16777215 / itr;       // Color multiple for picture RGB(multiple to turn point value into RGB val)
        // BufferedImage = new BuferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = h; x < h + squareLen; x++) {        // Iterate through every block in the picture
            for (int y = v; y < v + squareLen; y++) {
                double a = (((maxX - minX) / width) * x) + minX;
                double b = (((minY - maxY) / height) * y) + maxY;
                Complex cur = new Complex(a, b);                // assign new number to this point
                int num = (int) (numItr(cur, itr) * mult);      // calculate whether this point belongs in the set
                points[y][x] = num;
                image.setRGB(x, y, num);                        // Save calculation and color pixel accordingly
                if (draw == 1){
                    panel.setPixels(points);
                }
            }
        }
        // ImageIO.write(image, "jpg",
        // new File(System.getProperty("user.dir") + "/pics", Integer.toString(n) +
        // ".jpg"));
    }

    public static double numItr(Complex num, int itr) { // Calculate number of iterations before point diverges from set
        Complex start = new Complex(num.getA(), num.getB());
        Complex cur = new Complex(num.getA(), num.getB());
        double index = 0;
        while (cur.inMandel()) {  // Iterate from 0 < n < itr iterations or until point diverges
            index++;
            Complex next = cur.square();
            next = next.add(start);
            cur = new Complex(next.getA(), next.getB());
            if (index >= itr) {
                break;
            }
        }
        double z = Math.sqrt((cur.getA() * cur.getA()) + (cur.getB() * cur.getB())); // Normalize and return
        return index + 1 - (float) Math.log((Math.log(z) / Math.log(2)));
    }
}
