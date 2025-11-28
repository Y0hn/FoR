import java.awt.Dimension;

/**
 * 2D Vektor2D (reprezentuje bod v Kartezianskej suradnicovej sustave so suradnicami [x,y])
 * 
 * @author y0hn
 * @version v0.9
 */
public class Vektor2D {
    public static final Vektor2D ZERO = new Vektor2D(0, 0);
    public static final Vektor2D HORE = new Vektor2D(0, 1);
    public static final Vektor2D DOLE = new Vektor2D(0, -1);
    public static final Vektor2D PRAVO = new Vektor2D(1, 0);
    public static final Vektor2D LAVO = new Vektor2D(-1, 0);

    private double x;
    private double y;
    /**
     * Vytvori 2-rozmerny Vektor2D bodu (x,y)
     * @param x pozicia x
     * @param y pozicia y 
     */
    public Vektor2D(double x, double y) {
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
     * Vypocita dlzku Vektor2Da
     * @return |V| vzdialenost bodu (x,y) od (0,0)
     */
    public double dlzka() {
        return Math.sqrt(this.x * this.x  + this.y * this.y);
    }
    /**
     * Vrati Vektor2D patriaci do praveho horneho kvadradnu
     * Obidve suradnice Vektor2Da budu kladne alebo 0; 
     * @return Vektor2D (|y|,|x|)
     */
    public Vektor2D absolutny() {
        double noveX = Math.abs(this.x);
        double noveY = Math.abs(this.y);
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vymeni hodnoty Vektor2Da
     * @return prevratni Vektor2D (y,x)
     */
    public Vektor2D vymeneny() {
        return new Vektor2D(this.y, this.x);
    }
    /**
     * Vypocita sucet Vektor2Dov
     * @param druhyVektor2D Vektor2D
     * @return scitany Vektor2D (x1+x2, y1+y2)
     */
    public Vektor2D skalarnySucet(double skalar) {
        double noveX = this.x + skalar;
        double noveY = this.y + skalar;
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vypocita sucet Vektor2Dov
     * @param druhyVektor2D Vektor2D
     * @return scitany Vektor2D (x1+x2, y1+y2)
     */
    public Vektor2D sucet(Vektor2D druhyVektor2D) {
        double noveX = this.x + druhyVektor2D.x;
        double noveY = this.y + druhyVektor2D.y;
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vypocita rozdiel Vektor2Dov
     * @param druhyVektor2D odcitavany Vektor2D
     * @return rozdielovy Vektor2D (x1-x2, y1-y2)
     */
    public Vektor2D rozdiel(Vektor2D druhyVektor2D) {
        double noveX = this.x - druhyVektor2D.x;
        double noveY = this.y - druhyVektor2D.y;
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vypocita sucin Vektor2Da a skalarneho cisla
     * @param skalar skalarny cinitel
     * @return rozsireny Vektor2D (x*skalar, y*skalar)
     */
    public Vektor2D skalarnySucin(double skalar) {
        double noveX = this.x * skalar;
        double noveY = this.y * skalar;
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vypocita "sucin" dvoch Vektor2Dov
     * @param druhyVektor2D rozsirujuci Vektor2D
     * @return rozsireny Vektor2D (x1*x2, y1*y2)
     */
    public Vektor2D sucin(Vektor2D druhyVektor2D) {
        double noveX = this.x * druhyVektor2D.x;
        double noveY = this.y * druhyVektor2D.y;
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vypocita vzdialenost medzi bodmi Vektor2Dov v rovine
     * @param druhyVektor2D druhy Vektor2D (x,y)
     * @return vzdialenost medzi nimi |V1-V2|
     */
    public double vzdialenostOd(Vektor2D druhyVektor2D) {
        return this.rozdiel(druhyVektor2D).dlzka();
    }

    /**
     * vytvori normalizovany Vektor2D z povodneho
     * @return Vektor2D s dlzkou 1
     */
    public Vektor2D normalizuj() {
        double dlzka = this.dlzka();
        if (dlzka == 0) {
            return Vektor2D.ZERO;
        }
        double noveX = this.x / dlzka;
        double noveY = this.y / dlzka;
        return new Vektor2D(noveX, noveY);
    }
    /**
     * vytvori celo-ciselny (zakokruhleny) Vektor2D z povodneho
     * @return Vektor2D s dlzkou 1
     */
    public Vektor2D zaokruhli() {
        double noveX = Math.round(this.x);
        double noveY = Math.round(this.y);
        return new Vektor2D(noveX, noveY);
    }
    /**
     * Vrati rozmer Vektor2Da ako Dimenziu
     * @return Dimenzia (x,y)
     */
    public Dimension toDimension() {
        return new Dimension(this.getIntX(), this.getIntY());
    }
    /**
     * Navratova pre zjednodusenie citatelnosti vo vypise
     * @return [x,y]
     */
    @Override
    public String toString() {
        return String.format("[%d,%d]", this.getIntX(), this.getIntY());
    }
    /**
     * Kontroluje ci sa Vektory2D zhoduju
     * @param o by mal byt Druhy Vektor2D
     */
    @Override
    public boolean equals(Object o) {
        boolean eq = o instanceof Vektor2D;

        if (eq) {
            Vektor2D v = (Vektor2D)o;
            eq &= v.x == this.x;
            eq &= v.y == this.y;
        }

        return eq;
    }
}
