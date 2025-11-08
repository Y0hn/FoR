
/**
 * Enumeration trieda Smer
 * Obsahuje popisuje smer (pohybu) v rovine
 * 
 * @author y0hn
 * @version v0.1
 */
public enum Smer {
    Hore,
    Lavo,
    Pravo,
    Dole;

    /**
     * Priradi zo smeru hodnotu int 
     * @return hodnota v tvare int
     */
    public int toInt() {
        int i = -1;
        switch (this) {
            case Hore:
                i = 0;                
                break;
            case Lavo:
                i = 1;                
                break;
            case Pravo:
                i = 2;                
                break;
            case Dole:
                i = 3;                
                break;
        
            default:
                break;
        }
        return i;
    }
        /**
     * Priradi zo smeru hodnotu Vektor 
     * @return hodnota v tvare int
     */
    public Vektor toVektor() {
        Vektor v = Vektor.zero();
        switch (this) {
            case Hore:
                v = Vektor.hore();                
                break;
            case Lavo:
                v = Vektor.lavo();                
                break;
            case Pravo:
                v = Vektor.pravo();                
                break;
            case Dole:
                v = Vektor.dole();                
                break;
        
            default:
                break;
        }
        return v;
    }
    /**
     * Vytvara opacny smer ku smeru 
     * @param smer vlozeny smer
     * @return opacny smer
     */
    public Smer opacny() {
        Smer novySmer = toSmer(Math.abs(this.toInt() - 3));
        return novySmer;
    }
    /**
     * Vytvori Smer na zaklade priradenej hodnoty
     * @param smernik
     * @return priradena hodnota Smer
     */
    public static Smer toSmer(int smernik) {
        Smer s = null;
        switch (smernik) {
            case 0:
                s = Smer.Hore;                
                break;
            case 1:
                s = Smer.Lavo;                
                break;
            case 2:
                s = Smer.Pravo;                
                break;
            case 3:
                s = Smer.Dole;                
                break;
        
            default:
                break;
        }
        return s;
    }

    /**
     * Kontroluje vztah medzi smermi
     * @param s1 prvy smer
     * @param s2 druhy smer
     * @return PRAVDA ak su navzajom protichodne
     */
    public static boolean opacneSmery(Smer s1, Smer s2) {
        boolean opacne;

        opacne  = (s1 == Lavo && Pravo == s2);
        opacne |= (s2 == Pravo && Lavo == s1);
        opacne |= (s2 == Hore && Dole == s1);
        opacne |= (s2 == Dole && Hore == s1);

        return opacne;
    }
}
