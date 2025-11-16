
/**
 * Trieda pre zjednotenie vsetkych komponentov Hry
 * 
 * @author y0hn 
 * @version 0.2
 */
public class Hra {
    private static Hra singleton;
    private static Vektor rozmerDisplej;

    private Miestnost aktivnaMistnost;
    private boolean hranieSa;
    private Svet svet;
    private Hrac hrac;
    private Displej displej;

    /**
     * Zacne novu Hru iba pripade ak uz nebezi ina
     * @return beziacu Hru
     */
    public static Hra start() {
        if (singleton == null) {
            Vektor rozmerDisplej =  new Vektor(600, 600);
            singleton = new Hra(rozmerDisplej);
        }
        return singleton;
    }
    /**
     * Ukonci beziacu Hru
     */
    public static void stop() {
        // zastav hru
    }
    /** 
     * Zisti rozmer Displeja Hry
     * @return vrati Vektor rozmeru Displaya akualnej Hry
     */
    public static Vektor getRozmerDisplay() {
        return rozmerDisplej;
    }
    /**
     * Nastavi aktivnu Miestnost podla jej indexu vo Svete
     * @param indexAktivnejMiestnosti index novej aktivnej Miestnosti
     */
    public static void nastavAktivnuMiestnost(int indexAktivnejMiestnosti) {
        singleton.aktivnaMistnost = singleton.svet.getMiestnost(indexAktivnejMiestnosti);
        singleton.nacitajMiestnost();
    }

    /**
     * Sluzi na spustenie Hry 
     */
    public Hra(Vektor rozmery) {
        rozmerDisplej = rozmery;
        this.hranieSa = true;
        this.svet = new Svet(50);
        this.aktivnaMistnost = svet.getZaciatocnaMiestnost();
        this.hrac = new Hrac();
        
        this.displej = new Displej("", "FrontRooms", rozmerDisplej);
        this.nacitajMiestnost();
    }
    /**
     * Zobrazi aktivnu Miestnost na Displeji
     */
    private void nacitajMiestnost() {
        this.displej.nastavAktivnuMiestnost(aktivnaMistnost);
    }
}
