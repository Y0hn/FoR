
/**
 * 2D Vektor
 * 
 * @author y0hn
 * @version 0.1
 */
public class Vektor {
    private double x;
    private double y;
    /**
     * Vytvori 2-rozmerny Vektor bodu (x,y)
     * @param x pozicia x
     * @param y pozicia y 
     */
    public Vektor(double x, double y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Ziska hodnotu X suradnice
     * @return hodnota X
     */
    public double getX() {
        return this.x;
    }
    /**
     * Ziska hodnotu Y suradnice
     * @return hodnota Y
     */
    public double getY() {
        return this.y;
    }
    /**
     * Ziska zaokruhlenu hodnotu X suradnice
     * @return int hodnota X
     */
    public int getIntX() {
        return (int)Math.round(this.getX());
    }
    /**
     * Ziska zaokruhlenu hodnotu Y suradnice
     * @return int hodnota Y
     */
    public int getIntY() {
        return (int)Math.round(this.getY());        
    }

    /**
     * Vypocita dlzku Vektora
     * @return vzdialenost bodu (x,y) od (0,0)
     */
    public double dlzka() {
        return Math.sqrt(this.x * this.x  + this.y * this.y);
    }
    /**
     * vytvori normalizovany Vektor z povodneho
     * @return Vektor s dlzkou 1
     */
    public Vektor normalizuj() {
        double uhol = (this.x == 0) ? Math.PI / 2 : Math.atan(this.y / this.x);
        double noveX = Math.cos(uhol);
        double noveY = Math.sin(uhol);
        return new Vektor(noveX, noveY);
    }

    /**
     * Vytvori nuloy Vektor 
     * @return Vektor v bode (0,0)
     */
    public static Vektor zero() {
        return new Vektor(0, 0);
    } 
}
