/**
 * Enumeration trieda Smer
 * Obsahuje a popisuje Smer (pohybu) v rovine
 * 
 * @author y0hn
 * @version v0.5
 */
public enum Smer {
    HORE(0, -1),
    PRAVO(1, 0),
    DOLE(0, 1),
    LAVO(-1, 0);

    /**
     * Vytvori Smer na zaklade priradenej hodnoty
     * @param smernik
     * @return priradena hodnota Smer, ak nenajdeny tak NULL
     */
    public static Smer toSmer(Vektor2D smernik) {
        Smer s = null;
        
        // kvoli pozicii [0,0] v lavom hornom rohu su HORE a DOLE zamenene
        if (smernik.equals(Vektor2D.DOLE)) {
            s = Smer.HORE;
        } else if (smernik.equals(Vektor2D.HORE)) {
            s = Smer.DOLE;
        } else if (smernik.equals(Vektor2D.PRAVO)) {
            s = Smer.PRAVO;
        } else if (smernik.equals(Vektor2D.LAVO)) {
            s = Smer.LAVO;
        }
        return s;
    }

    private final Vektor2D vektor;
    
    Smer(double x, double y) {
        this.vektor = new Vektor2D(x, y).normalizuj();
    }

    /**
     * Priradi zo Smeru hodnotu Vektor2D 
     * @return hodnota v tvare int
     */
    public Vektor2D getVektor2D() {
        return this.vektor;
    }
    /**
     * Kontroluje vztah medzi Smermi
     * @param smer druhy Smer
     * @return PRAVDA ak su navzajom protichodne
     */
    public boolean jeOpacny(Smer smer) {
        return this.opacny() == smer;
    }
    /**
     * Vytvara opacny Smer ku Smeru 
     * @param smer vlozeny Smer
     * @return opacny Smer
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
