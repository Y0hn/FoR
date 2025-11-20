
/**
 * Write a description of class Rozmer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rozmer2D {
    private double poziciaX;
    private double poziciaY;
    private double velkostX;
    private double velkostY;
    /**
     * Constructor for objects of class Rozmer
     */
    public Rozmer2D(double x, double y, double vX, double vY) {
        this.poziciaX = x;
        this.poziciaY = y;
        this.velkostX = vX;
        this.velkostY = vY;        
    }
    /**
     * Constructor for objects of class Rozmer
     */
    public Rozmer2D(Vektor2D pozicia, Vektor2D velkost) {
        this.poziciaX = pozicia.getX();
        this.poziciaY = pozicia.getY();
        this.velkostX = velkost.getX();
        this.velkostY = velkost.getY();
    }
}
