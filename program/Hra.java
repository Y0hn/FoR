import java.util.ArrayList;

/**
 * Trieda pre zjednotenie vsetkych komponentov Hry
 * 
 * @author y0hn 
 * @version 0.4
 */
public class Hra {
    public static final Vektor2D ROZMER_HRY = new Vektor2D(600, 400);
    private static Hra instancia;

    /**
     * Zacne novu Hru iba pripade ak uz nebezi ina
     * @return beziacu Hru
     */
    public static Hra start() {
        if (instancia == null) {
            instancia = new Hra();
        }
        return instancia;
    }
    /**
     * Nastavi aktivnu Miestnost podla jej indexu vo Svete
     * @param indexAktivnejMiestnosti index novej aktivnej Miestnosti
     */
    public static void nastavAktivnuMiestnost(int indexAktivnejMiestnosti) {
        instancia.nacitajMiestnost(indexAktivnejMiestnosti);
    }

    private Svet svet;
    private Hrac hrac;
    private Displej displej;
    private Miestnost aktivnaMiestnost;

    /**
     * Sluzi na vytvorenie Hry 
     * @param rozmery su rozmery Displeja v tvare Vektor2D2
     */
    private Hra() {
        this.displej = new Displej("", "FrontRooms", Hra.ROZMER_HRY);
        this.svet = new Svet(50);
        this.hrac = new Hrac();
        
        this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
        this.hrac.nastavVstup(this.displej.getOkno());
        this.displej.nastavHraca(this.hrac);
    }
    
    /**
     * Obnovi Hru
     */
    public void tik() {
        this.hrac.tik(this.aktivnaMiestnost);
        this.displej.obnovHraca(this.hrac);
    }
    /**
     * Zmeni aktivnu Miestnost zobrazovanu na Displeji
     * @param m nova Miestnost
     */
    private void nacitajMiestnost(Miestnost m) {
        this.displej.zmenAktivnuMiestnost(m);
        this.aktivnaMiestnost = m;
    }
    /**
     * Zmeni aktivnu Miestnost zobrazovanu na Displeji
     * @param indexMiestnosti vo Svete
     */
    private void nacitajMiestnost(int indexMiestnosti) {
        Miestnost m = this.svet.getMiestnost(indexMiestnosti);
        this.nacitajMiestnost(m);
    }
}
