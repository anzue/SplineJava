import java.awt.*;
import java.util.Arrays;
public class Main {
 
   
    public static double f(double x){
       //return 5*x-x*x+5;
       // return 3*Math.cos(x)+3;
       return Math.abs(7*Math.cos(x));
       //return (x*x - 2 * x + 1)/10;
      // return x*x / 10;
    }
   
    public static double[] X = new double[10002];
    public static double[] Y = new double[10002];
    
    //public static double[] CX = new double[10002];
    public static double[] CY = new double[10002];
    
    public static int Count = 0;
   
   
    public static void build(double a,double b,double c,double d,double l,double r){
        System.out.printf("%.1f+%.1f(x-%.1f)+%.1f(x-%.1f)^2+%.1f(x-%.1f)^3 on x=[%.1f, %.1f ]\n",a,b,r,c,r,d,r,l,r);
       
        for(double x = l;x<r;x+=(r-l)/30){
            double tx = x-r;
            double y = a+tx*(b+tx*(c+tx*d));
            //CX[Count] = x;
            CY[Count] = f(x);
            X[Count] = x;
            Y[Count++] = y;
          //  System.out.println(x+" "+y);
        }
    }
   
    public static void solve(double[] x,double[] y,int n){
 
        double[] h = new double[1001];
        double[] l = new double[1001];
        double[] delta = new double[1001];
        double[] lambda = new double[1001];
        double[] a = new double[1001];
        double[] b = new double[1001];
        double[] c = new double[1001];
        double[] d = new double[1001];
       
        for(int k=1; k<=n; k++){
            h[k] = x[k] - x[k-1];
            l[k] = (y[k] - y[k-1])/h[k];
        }
        delta[1] = - ((double)h[2])/(2*(h[1]+h[2]));
        lambda[1] = ((double)1.5)*(l[2] - l[1])/(h[1]+h[2]);
        for(int k=3; k<=n; k++){
           delta[k-1] = - h[k]/(2*h[k-1] + 2*h[k] + h[k-1]*delta[k-2]);
           lambda[k-1] = (3*l[k] - 3*l[k-1] - h[k-1]*lambda[k-2]) /
                         (2*h[k-1] + 2*h[k] + h[k-1]*delta[k-2]);
        }
        c[0] = 0;
        c[n] = 0;
        for(int k=n; k>=2; k--){
            c[k-1] = delta[k-1]*c[k] + lambda[k-1];
        }
        for(int k=1; k<=n; k++){
           a[k] = y[k];
           d[k] = (c[k] - c[k-1])/(3*h[k]);
           b[k] = l[k] + (2*c[k]*h[k] + h[k]*c[k-1])/3;
        }
      /*
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
        System.out.println(Arrays.toString(c));
        System.out.println(Arrays.toString(d));
        */
       
       
        for(int k=1;k<=n;++k){
            build(a[k],b[k],c[k],d[k],x[k-1],x[k]);      
        }
       
    }
   
    public static void main(String[] args) {
        int DivPoints = 10;
        double L = 0;
        double R = 10;
      
        double[] x = new double[DivPoints+1];
        double[] y = new double[DivPoints+1];
        
        double[] a = new double[DivPoints+1];
        double[] b = new double[DivPoints+1];
        double[] c = new double[DivPoints+1];
        double[] f = new double[DivPoints+1];
        double[] r = new double[DivPoints+1];
        
        for (int i = 0; i <= DivPoints; i++) {
            x[i] = L + (R-L)*(double)i*1./DivPoints;
            y[i] = f(x[i]);
        //    System.out.print("x = " + x[i]);
        //    System.out.printf("%s %.3f %s"," y = ",+y[i],"\n");
        }
       
    solve(x,y,DivPoints);
    new Draw(X,Y,CY,Count);
    }
}
 
class Draw extends javax.swing.JFrame {
 
    private double[] x;
    private double[] y;
    private double[] cy;
    private int[] xTrue;
    private int[] yTrue;
    private int[] cyTrue;

    private Dimension size = new Dimension(700,500); 
    private Dimension startPointXoY = new Dimension(40,450); 
    private int scale = 60; 
    private int l = 0;
    private int n = 0;
    public Draw(double[] x,double[] y,double[] cy,int n) {
        this.n = n;
        this.x = x;
        this.y = y;
        this.cy = cy;
        xTrue = new int[n];
        yTrue = new int[n];
        cyTrue = new int[n];
        /*
         for (int i = 0; i < n; i++) {
           
            System.out.print("x = " + xTrue[i]);
            System.out.print(" y = "+yTrue[i]+"\n");
        }*/
       
        reBuildArreys();
        /*
          for (int i = 0; i < n; i++) {
           
            System.out.print("x = " + xTrue[i]);
            System.out.print(" y = "+yTrue[i]+"\n");
        }*/
        initInterface();
    }
 
    @Override
    public void paint(Graphics g) {
        
            g.setColor(Color.WHITE);
            g.fillRect(0,0,size.width,size.height);
            g.setColor(Color.BLACK);
            l = 1;
            for (int i = 0; i <= 20; i++) {
                g.drawString(String.valueOf(i),startPointXoY.width + scale* i-5,startPointXoY.height + (startPointXoY.width - 25));
                g.drawString(String.valueOf(i),25,startPointXoY.height - scale * i+5);
            }
            g.drawLine(startPointXoY.width,startPointXoY.width,startPointXoY.width,startPointXoY.height);
            g.drawLine(startPointXoY.width,startPointXoY.height,2000,startPointXoY.height);
    
 
        g.drawPolyline(xTrue,yTrue,n);
        g.setColor(Color.RED);
        g.drawPolyline(xTrue,cyTrue,n);
    }
 
    private void initInterface() {
        setSize(size);
        setResizable(false);
        setTitle("Сплайн");
        setVisible(true);
    }
 
    private void reBuildArreys() {
        for (int i = 0; i < n; i++) {
            x[i] *= scale;
            xTrue[i] = startPointXoY.width +  ((int)x[i]);
            y[i] *= scale;
            cy[i]*=scale;
            yTrue[i] = startPointXoY.height - ((int) y[i]);
            cyTrue[i] = startPointXoY.height - ((int) cy[i]);
        }
    }
}