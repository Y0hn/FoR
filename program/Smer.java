/**
 * Enumeration trieda Smer
 * Obsahuje a popisuje smer (pohybu) v rovine
 * 
 * @author y0hn
 * @version v0.5
 */
public enum Smer {
    HORE(0, -1),
    PRAVO(1, 0),
    DOLE(0, 1),
    LAVO(-1, 0),;

    private Vektor2D vektor;
    Smer(double x, double y) {
        this.vektor = new Vektor2D(x, y).normalizuj();
    }

    /**
     * Vytvori Smer na zaklade priradenej hodnoty
     * @param smernik
     * @return priradena hodnota Smer
     */
    public static Smer toSmer(int smernik) {
        Smer s = null;
        
        if (0 <= smernik && smernik < Smer.values().length) {
            s = Smer.values()[smernik];
        }

        return s;
    }
    /**
     * Vytvori Smer na zaklade priradenej hodnoty
     * @param smernik
     * @return priradena hodnota Smer
     */
    public static Smer toSmer(Vektor2D smernik) {
        Smer s = null;
        
        // kvoli pozicii [0,0] v lavom hornom rohu su HORE a DOLE zamenene
        if (smernik.equals(Vektor2D.dole())) {
            s = Smer.HORE;
        } else if (smernik.equals(Vektor2D.hore())) {
            s = Smer.DOLE;
        } else if (smernik.equals(Vektor2D.pravo())) {
            s = Smer.PRAVO;
        } else if (smernik.equals(Vektor2D.lavo())) {
            s = Smer.LAVO;
        }
        return s;
    }

    /**
     * Priradi zo smeru hodnotu Vektor2D 
     * @return hodnota v tvare int
     */
    public Vektor2D getVektor2D() {
        return this.vektor;
    }
    /**
     * Kontroluje vztah medzi smermi
     * @param smer druhy smer
     * @return PRAVDA ak su navzajom protichodne
     */
    public boolean jeOpacny(Smer smer) {
        return this.opacny() == smer;
    }
    /**
     * Vytvara opacny smer ku smeru 
     * @param smer vlozeny smer
     * @return opacny smer
     */
    public Smer opacny() {
        Smer opacnySmer = null;
        
        switch (this) {
            case HORE:
                opacnySmer = Smer.DOLE;                
                break;
            case LAVO:
                opacnySmer = Smer.PRAVO;                
                break;
            case PRAVO:
                opacnySmer = Smer.LAVO;                
                break;
            case DOLE:
                opacnySmer = Smer.HORE;                
                break;
        
            default:
                break;
        }

        return opacnySmer;
    }
}
