import java.awt.Dimension;

/**
 * 2D Vektor (reprezentuje bod v Kartezianskej suradnicovej sustave so suradnicami [x,y])
 * 
 * @author y0hn
 * @version v0.5
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
     * @return |V| vzdialenost bodu (x,y) od (0,0)
     */
    public double dlzka() {
        return Math.sqrt(this.x * this.x  + this.y * this.y);
    }
    /**
     * Vrati Vektor patriaci do praveho horneho kvadradnu
     * Obidve suradnice Vektora budu kladne alebo 0; 
     * @return Vektor (|y|,|x|)
     */
    public Vektor absolutny() {
        double noveX = Math.abs(this.x);
        double noveY = Math.abs(this.y);
        return new Vektor(noveX, noveY);
    }
    /**
     * Vymeni hodnoty Vektora
     * @return prevratni Vektor (y,x)
     */
    public Vektor vymeneny() {
        return new Vektor(this.y, this.x);
    }
    /**
     * Vypocita sucet Vektorov
     * @param druhyVektor Vektor
     * @return scitany Vektor (x1+x2, y1+y2)
     */
    public Vektor skalarnySucet(double skalar) {
        double noveX = this.x + skalar;
        double noveY = this.y + skalar;
        return new Vektor(noveX, noveY);
    }
    /**
     * Vypocita sucet Vektorov
     * @param druhyVektor Vektor
     * @return scitany Vektor (x1+x2, y1+y2)
     */
    public Vektor sucet(Vektor druhyVektor) {
        double noveX = this.x + druhyVektor.x;
        double noveY = this.y + druhyVektor.y;
        return new Vektor(noveX, noveY);
    }
    /**
     * Vypocita rozdiel Vektorov
     * @param druhyVektor odcitavany Vektor
     * @return rozdielovy Vektor (x1-x2, y1-y2)
     */
    public Vektor rozdiel(Vektor druhyVektor) {
        double noveX = this.x - druhyVektor.x;
        double noveY = this.y - druhyVektor.y;
        return new Vektor(noveX, noveY);
    }
    /**
     * Vypocita sucin Vektora a skalarneho cisla
     * @param skalar skalarny cinitel
     * @return rozsireny Vektor (x*skalar, y*skalar)
     */
    public Vektor skalarnySucin(double skalar) {
        double noveX = this.x * skalar;
        double noveY = this.y * skalar;
        return new Vektor(noveX, noveY);
    }
    /**
     * Vypocita "sucin" dvoch Vektorov
     * @param druhyVektor rozsirujuci Vektor
     * @return rozsireny Vektor (x1*x2, y1*y2)
     */
    public Vektor sucin(Vektor druhyVektor) {
        double noveX = this.x * druhyVektor.x;
        double noveY = this.y * druhyVektor.y;
        return new Vektor(noveX, noveY);
    }
    /**
     * Vypocita vzdialenost medzi bodmi Vektorov v rovine
     * @param druhyVektor druhy Vektor (x,y)
     * @return vzdialenost medzi nimi |V1-V2|
     */
    public double vzdialenostOd(Vektor druhyVektor) {
        return this.rozdiel(druhyVektor).dlzka();
    }

    /**
     * vytvori normalizovany Vektor z povodneho
     * @return Vektor s dlzkou 1
     */
    public Vektor normalizuj() {
        double uhol = (this.x == 0) ? (0 < this.y ? Math.PI / 2 : (Math.PI / 2) * 3) : Math.atan(this.y / this.x);
        double noveX = Math.cos(uhol);
        double noveY = Math.sin(uhol);
        return new Vektor(noveX, noveY);
    }
    /**
     * Vrati rozmer Vektora ako Dimenziu
     * @return Dimenzia (x,y)
     */
    public Dimension toDimension() {
        return new Dimension(this.getIntX(), this.getIntY());
    }
    /**
     * Navratova pre zjednodusenie citatelnosti vo vypise
     * @return [x,y]
     */
    public String toString() {
        return String.format("[%d,%d]", this.getIntX(), this.getIntY());
    }


    /**
     * Vytvori nuloy Vektor 
     * @return Vektor v bode (0,0)
     */
    public static Vektor zero() {
        return new Vektor(0, 0);
    }
    /**
     * Vytvori nuloy Vektor 
     * @return Vektor v bode (0,0)
     */
    public static Vektor hore() {
        return new Vektor(0, 1);
    }
    /**
     * Vytvori nuloy Vektor 
     * @return Vektor v bode (0,0)
     */
    public static Vektor dole() {
        return new Vektor(0, -1);
    }
    /**
     * Vytvori nuloy Vektor 
     * @return Vektor v bode (0,0)
     */
    public static Vektor lavo() {
        return new Vektor(-1, 0);
    }
    /**
     * Vytvori nuloy Vektor 
     * @return Vektor v bode (0,0)
     */
    public static Vektor pravo() {
        return new Vektor(1, 0);
    }
}
