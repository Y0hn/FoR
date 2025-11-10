
/**
 * Spustacia Trieda
 * 
 * @author y0hn 
 * @version 0.1
 */
public class Hra {
    private static Hra singleton;

    private Vektor rozmerDisplej;
    private int aktivnaMistnost;
    private boolean hranieSa;
    private Hrac hrac;

    /**
     * Sluzi na spustenie Hry 
     */
    public static void main(String[] args) {
        singleton = new Hra();
        singleton.rozmerDisplej = new Vektor(500, 500);
        singleton.hranieSa = true;
        
        Svet svet = new Svet(50);
        singleton.aktivnaMistnost = svet.getZaciatocnaMiestnost();
        singleton.hrac = new Hrac();
        
        Displej displej = new Displej("FrontRooms", singleton.rozmerDisplej);

        // hra sa hra
        while (singleton.hranieSa) {
            displej.vykresli();
        }

        System.exit(0);
    }
    /** 
     * Ukonci hru 
     */
    public static void zavri() {
        singleton.hranieSa = false;
    }

    /** 
     * Zisti rozmer Displeja Hry
     * @return vrati Vektor rozmeru Displaya akualnej Hry
     */
    public static Vektor getRozmerDisplay() {
        return singleton.rozmerDisplej;
    }
    /**
     * Nastavi aktivnu Miestnost podla jej indexu vo Svete
     * @param indexAktivnejMiestnosti index novej aktivnej Miestnosti
     */
    public static void nastavAktivnuMiestnost(int indexAktivnejMiestnosti) {
        singleton.aktivnaMistnost = indexAktivnejMiestnosti;
    }
}
