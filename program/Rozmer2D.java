import java.awt.Point;
import java.awt.Rectangle;

/**
 * Obsahuje informacie o tvare (obdlzniku) v 2D priestore
 * 
 * @author y0hn
 * @version v0.4
 */
public class Rozmer2D {
    public static final Rozmer2D ZERO = new Rozmer2D(0, 0, 0, 0);

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
     * Vyvori Vektor2D s vekostou zodpovedajucou pozicii tvaru
     * @return [x,y]
     */
    public void setPozicia(Vektor2D pozicia) {
        this.poziciaX = pozicia.getX();
        this.poziciaY = pozicia.getY();
    }
    /**
     * Vytvori stred pre Rozmer tvaru
     * @return stred (x,y)
     */
    public Vektor2D ziskajStred() {
        Vektor2D stred = new Vektor2D(this.poziciaX, this.poziciaY);
        stred = stred.sucet(new Vektor2D(this.velkostX, this.velkostY).skalarnySucin(0.5));
        return stred;
    }

    /**
     * Vytvori obdlznik nesuci rovnake rozmery ako popisovany tvar
     * @return (x,y)x[vX,vY]
     */
    public Rectangle vytvorRectangle() {
        return new Rectangle(this.getIntPoX(), this.getIntPoY(), this.getIntVeX(), this.getIntVeY());
    }

    /**
     * Vytvori bod na rovnakej pozicii ako tvar
     * @return
     */
    public Point vytvorPointPozicie() {
        return new Point(this.getIntPoX(), this.getIntPoY());
    }

    /**
     * Zisti ci sa pozicia nachadza v rozmere
     * @param pozicia Vektor2D
     * @return PRANDA ak je cast Tela v stene
     */
    public boolean jePoziciaVnutri(Vektor2D pozicia) {
        boolean kolizuje = true;
        
        kolizuje &= pozicia.getX() > this.poziciaX; // kolizia z lava
        kolizuje &= pozicia.getX() < this.poziciaX + this.velkostX; // kolizia z prava
        kolizuje &= pozicia.getY() > this.poziciaY; // kolizia z hora
        kolizuje &= pozicia.getY() < this.poziciaY + this.velkostY; // kolizia z hora

        return kolizuje;
    }
    /**
     * Zisti ci sa cely Rozmer2D nachadza v rozmere
     * @param pozicia Vektor2D
     * @return PRAVDA ak je cely Rozmer2D v Rozmere2D
     */
    public boolean jeRozmerVnutri(Rozmer2D rozmer) {
        boolean vnutri = true;
        for (int i = 0; i < 4 && vnutri; i++) {
            Vektor2D posun;            
            switch (i) {
                case 1:
                    posun = Vektor2D.PRAVO.sucin(rozmer.getVelkost());
                    break;
                case 2:
                    posun = Vektor2D.HORE.sucin(rozmer.getVelkost());
                    break;
                case 3:
                    posun = rozmer.getVelkost();
                    break;
                default: // 0
                    posun = Vektor2D.ZERO;
                    break;
            }
            vnutri &= this.jePoziciaVnutri(rozmer.getPozicia().sucet(posun));
        }
        return vnutri;
    }
    /**
     * Zisti ci sa cast Rozmeru2D nachadza v Rozmere
     * @param pozicia Vektor2D
     * @return PRAVDA ak je cast Rozmer2D v Rozmere2D
     */
    public boolean jeRozmerCiastocneVnutri(Rozmer2D rozmer) {
        boolean vnutri = false;
        for (int i = 0; i < 4 && !vnutri; i++) {
            Vektor2D posun;            
            switch (i) {
                case 1:
                    posun = Vektor2D.PRAVO.sucin(rozmer.getVelkost());
                    break;
                case 2:
                    posun = Vektor2D.HORE.sucin(rozmer.getVelkost());
                    break;
                case 3:
                    posun = rozmer.getVelkost();
                    break;
                default: // 0
                    posun = Vektor2D.ZERO;
                    break;
            }
            vnutri |= this.jePoziciaVnutri(rozmer.getPozicia().sucet(posun));
        }
        return vnutri;
    }

    /**
     * Zmeni poziciu Rozmeru
     * @param posun Vektor2D
     */
    public void pricitajVektor2DKPozicii(Vektor2D posun) {
        this.poziciaX += posun.getX();
        this.poziciaY += posun.getY();
    }

    /**
     * Vytvori kopiu Rozmeru
     * @return novy Rozmer s rovnakymi atributmi
     */
    public Rozmer2D kopia() {
        return new Rozmer2D(this.poziciaX, this.poziciaY, this.velkostX, this.velkostY);
    }

    /**
     * Navratova pre zjednodusenie citatelnosti vo vypise
     * @return (x,y)[xV,yV]
     */
    @Override
    public String toString() {
        return String.format("(%.0f, %.0f)[%.0f, %.0f]", this.poziciaX, this.poziciaY, this.velkostX, this.velkostY);
    }
    /**
     * Kontroluje ci sa Rozmery2D zhoduju
     * @param o by mal byt Druhy Rozmery2D
     * @return PRAVDA ak sa hodnoty ich atributov zhoduju
     */
    @Override
    public boolean equals(Object object) {
        boolean rovnake = object instanceof Rozmer2D;
        if (rovnake) {
            Rozmer2D rozmer = (Rozmer2D)object;
            rovnake &= this.poziciaX == rozmer.poziciaX;
            rovnake &= this.poziciaY == rozmer.poziciaY;
            rovnake &= this.velkostX == rozmer.velkostX;
            rovnake &= this.velkostY == rozmer.velkostY;
        }
        return rovnake;
    }
}
