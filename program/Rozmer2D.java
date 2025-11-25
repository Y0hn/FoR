import java.awt.Rectangle;

/**
 * Obsahuje informacie o tvare (obdlzniku) v 2D priestore
 * 
 * @author y0hn
 * @version 0.1
 */
public class Rozmer2D {
    private double poziciaX;
    private double poziciaY;
    private double velkostX;
    private double velkostY;
    /**
     * Vytvori Rozmer2D popisujuci tvar
     * @param x vzdialenost laveho rohu od 0
     * @param y vzdialenost horneho rohu od 0
     * @param vX sirka
     * @param vY vyska
     */
    public Rozmer2D(double x, double y, double vX, double vY) {
        this.poziciaX = x;
        this.poziciaY = y;
        this.velkostX = vX;
        this.velkostY = vY;        
    }
    /**
     * Vytvori Rozmer2D popisujuci tvar z Vektorov
     * @param pozicia [x,y]
     * @param velkost [sirka,vyska]
     */
    public Rozmer2D(Vektor2D pozicia, Vektor2D velkost) {
        this.poziciaX = pozicia.getX();
        this.poziciaY = pozicia.getY();
        
        this.velkostX = velkost.getX();
        this.velkostY = velkost.getY();
    }
    /**
     * Vrati informacie o dimenzii
     * @return pozicia X
     */
    public double getPoziciaX() {
        return this.poziciaX;
    }
    /**
     * Vrati informacie o dimenzii
     * @return pozicia Y
     */    
    public double getPoziciaY() {
        return this.poziciaY;
    }
    /**
     * Vrati informacie o dimenzii
     * @return sirka X
     */    
    public double getVelkostX() {
        return this.velkostX;
    }
    /**
     * Vrati informacie o dimenzii
     * @return vyska Y
     */    
    public double getVelkostY() {
        return this.velkostY;
    }

    /**
     * Vrati dimenziu zaokruhlenu nadol
     * @return (int) pozicia X
     */
    public int getIntPoX() {
        return (int)this.poziciaX;
    }
    /**
     * Vrati dimenziu zaokruhlenu nadol
     * @return (int) pozicia Y
     */    
    public int getIntPoY() {
        return (int)this.poziciaY;
    }
    /**
     * Vrati dimenziu zaokruhlenu nadol
     * @return (int) sirka X
     */    
    public int getIntVeX() {
        return (int)this.velkostX;
    }
    /**
     * Vrati dimenziu zaokruhlenu nadol
     * @return (int) vyska Y
     */    
    public int getIntVeY() {
        return (int)this.velkostY;
    }

    /**
     * Vyvori Vektor2D s vekostou zodpovedajucou pozicii tvaru
     * @return [x,y]
     */
    public Vektor2D getPozicia() {
        return new Vektor2D(this.poziciaX, this.poziciaY);
    }
    /**
     * Vyvori Vektor2D s velkostou zodpovedajucou velkosti tvaru
     * @return [vX,vY]
     */
    public Vektor2D getVelkost() {
        return new Vektor2D(this.velkostX, this.velkostY);
    }

    /**
     * Vytvori obdlznik nesuci rovnake rozmery ako popisovany tvar
     * @return [x,y]x[vX,vY]
     */
    public Rectangle vytvorRectangle() {
        return new Rectangle(this.getIntPoX(), this.getIntPoY(), this.getIntVeX(), this.getIntVeY());
    }
    /**
     * Zisti ci sa pozicia nachadza v rozmere
     * @param pozicia Vektor2D
     * @return PRANDA ak je cast Tela v stene
     */
    public boolean jePoziciaVnutri(Vektor2D pozicia) {
        boolean kolizuje = true;
        /*
        Vektor2D polohaMuru = this.rozmer.getPozicia();
        Vektor2D velkostMuru = this.rozmer.getVelkost();
        Vektor2D polohaTela = telo.getPozicia();
        double polomer = telo.getPolomer();
        // ak hrac prechadza dverami prepne aktivnu Miestnost
        if (!this.aktivny && 0 <= this.vedieDoMiestnosti) {
            Hra.nastavAktivnuMiestnost(this.vedieDoMiestnosti);
        }
        */       
        kolizuje &= pozicia.getX() > this.poziciaX; // kolizia z lava
        kolizuje &= pozicia.getX() < this.poziciaX + this.velkostX; // kolizia z prava
        kolizuje &= pozicia.getY() > this.poziciaY; // kolizia z hora
        kolizuje &= pozicia.getY() < this.poziciaY + this.velkostY; // kolizia z hora

        return kolizuje;
    }
    
    public static Rozmer2D zero() {
        return new Rozmer2D(0, 0, 0, 0);
    }
}
