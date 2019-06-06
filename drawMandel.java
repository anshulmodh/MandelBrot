import java.io.*;
import java.util.*;
import java.awt.*;
import java.lang.Object;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class drawMandel {
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            int width = Integer.parseInt(args[0]);
            int height = width;
            int itr = Integer.parseInt(args[1]);
            double cx = 0.360240443437614363236125244449545308482607807958585750488375814740195346059218100311752936722773426396233731729724987737320035372683285317664532401218521579554288661726564324134702299962817029213329980895208036363104546639698106204384566555001322985619004717862781192694046362748742863016467354574422779443226982622356594130430232458472420816652623492974891730419252651127672782407292315574480207005828774566475024380960675386215814315654794021855269375824443853463117354448779647099224311848192893972572398662626725254769950976527431277402440752868498588785436705371093442460696090720654908973712759963732914849861213100695402602927267843779747314419332179148608587129105289166676461292845685734536033692577618496925170576714796693411776794742904333484665301628662532967079174729170714156810530598764525260869731233845987202037712637770582084286587072766838497865108477149114659838883818795374195150936369987302574377608649625020864292915913378927790344097552591919409137354459097560040374880346637533711271919419723135538377394364882968994646845930838049998854075817859391340445151448381853615103761584177161812057928;
            double cy = -0.6413130610648031748603750151793020665794949522823052595561775430644485741727536902556370230689681162370740565537072149790106973211105273740851993394803287437606238596262287731075999483940467161288840614581091294325709988992269165007394305732683208318834672366947550710920088501655704252385244481168836426277052232593412981472237968353661477793530336607247738951625817755401065045362273039788332245567345061665756708689359294516668271440525273653083717877701237756144214394870245598590883973716531691124286669552803640414068523325276808909040317617092683826521501539932397262012011082098721944643118695001226048977430038509470101715555439047884752058334804891389685530946112621573416582482926221804767466258346014417934356149837352092608891639072745930639364693513216719114523328990690069588676087923656657656023794484324797546024248328156586471662631008741349069961493817600100133439721557969263221185095951241491408756751582471307537382827924073746760884081704887902040036056611401378785952452105099242499241003208013460878442953408648178692353788153787229940221611731034405203519945313911627314900851851072122990492499999999999999999991;
            double offset = Double.parseDouble(args[2]);
            int numThreads = Integer.parseInt(args[3]);
            int differential = Integer.parseInt(args[4]);
            double diff;
            int num = Integer.parseInt(args[5]);
            int draw = Integer.parseInt(args[6]);
            DrawingPanel panel = new DrawingPanel(width, height);
            Graphics g = panel.getGraphics();
            int[][] points = new int[height][width];
            int animation = Integer.parseInt(args[7]);
            for (int[] x : points) {
                Arrays.fill(x, 16777215);
            }
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            compute(width, height, differential, itr, panel, points, cx, cy, offset, draw, numThreads, num, image,
                    animation);
            num++;
            if (animation == 1) {
                while (offset > 0) {
                    diff = offset / 100;
                    System.out.println();
                    offset -= diff;
                    compute(width, height, differential, itr, panel, points, cx, cy, offset, draw, numThreads, num,
                            image, animation);
                    System.out.println(offset);
                    num++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public static void compute(int width, int height, int differential, int itr, DrawingPanel panel, int[][] points,
            double cx, double cy, double offset, int draw, int numThreads, int num, BufferedImage image, int animation)
            throws IOException, InterruptedException {
        int h = 0;
        int v = 0;
        ArrayList<Mandel> threads = new ArrayList<Mandel>();
        while (v < (height)) {
            System.out.println(v);
            Mandel one = new Mandel(width, height, h, v, differential, itr, panel, points, cx, cy, offset, draw, image);
            checkThreads(threads, numThreads);
            one.start();
            threads.add(one);
            if ((h + differential) <= (width - differential)) {
                h += differential;
            } else {
                h = 0;
                if ((v + differential) <= (height)) {
                    v += differential;
                }
            }
        }
        while (threads.size() > 0) {
            checkThreads(threads, 0);
        }
        // System.out.println(Arrays.deepToString(points));
        if (animation == 1) {
            ImageIO.write(image, "jpg",
                    new File(System.getProperty("user.dir") + "/pics", Integer.toString(num) + ".jpg"));
        }
        else{
            System.out.println("done");
            panel.setPixels(points);
        }
    }

    public static void checkThreads(ArrayList<Mandel> threads, int numThreads) throws InterruptedException {
        while (threads.size() >= numThreads) {
            for (Mandel x : threads) {
                if (!(x.isAlive())) {
                    threads.remove(x);
                    return;
                }
            }
            Thread.sleep(1000);
        }
    }
}