
/**
 * Trieda pre zjednotenie vsetkych komponentov Hry
 * 
 * @author y0hn 
 * @version 0.3
 */
public class Hra {
    private static Hra singleton;

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
            Vektor2D rozmerDisplej =  new Vektor2D(600, 400);
            singleton = new Hra(rozmerDisplej);
        }
        return singleton;
    }
    /**
     * Nastavi aktivnu Miestnost podla jej indexu vo Svete
     * @param indexAktivnejMiestnosti index novej aktivnej Miestnosti
     */
    public static void nastavAktivnuMiestnost(int indexAktivnejMiestnosti) {
        singleton.nacitajMiestnost(indexAktivnejMiestnosti);
    }

    /**
     * Sluzi na vytvorenie Hry 
     * @param rozmery su rozmery Displeja v tvare Vektor2D2
     */
    public Hra(Vektor2D rozmery) {
        this.hranieSa = true;
        this.displej = new Displej("", "FrontRooms", rozmery);
        this.svet = new Svet(50);
        this.hrac = new Hrac();
        
        this.nacitajMiestnost(svet.getZaciatocnaMiestnost());
    }
    /**
     * Zmeni aktivnu Miestnost zobrazovanu na Displeji
     * @param m nova Miestnost
     */
    private void nacitajMiestnost(Miestnost m) {
        this.displej.zmenAktivnuMiestnost(m);
    }
    /**
     * Zmeni aktivnu Miestnost zobrazovanu na Displeji
     * @param indexMiestnosti vo Svete
     */
    private void nacitajMiestnost(int indexMiestnosti) {
        Miestnost m = svet.getMiestnost(indexMiestnosti);
        this.nacitajMiestnost(m);
    }
}
