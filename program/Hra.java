/**
 * Trieda pre zjednotenie vsetkych komponentov Hry
 * 
 * @author y0hn 
 * @version v0.4
 */
public class Hra {
    private static final Vektor2D ROZMER_HRY = new Vektor2D(600, 400);
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
    public static void nastavAktivnuMiestnost(Miestnost miestnost) {
        instancia.nacitajMiestnost(miestnost);
    }

    private Svet svet;
    private Hrac hrac;
    private Displej displej;
    private Miestnost aktivnaMiestnost;
    private StavHry stavHry;

    private Hra() {
        this.displej = new Displej("", "FrontRooms", Hra.ROZMER_HRY);
        this.svet = new Svet(50);
        this.hrac = new Hrac();
        
        this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
        this.hrac.nastavVstup(this.displej.getOkno());
        this.displej.nastavHraca(this.hrac);
        this.stavHry = StavHry.HRA;
    }
    
    /**
     * Obnovi vsetky objekty v Hre
     */
    public void tik() {
        if (this.stavHry == StavHry.HRA) {
            this.stavHry = this.aktivnaMiestnost.tik(this.hrac);
            this.hrac.tik(this.aktivnaMiestnost);
        } else {
            this.displej.nastavGrafikuPreStavHry(this.stavHry, true);
        }
    }
    
    /**
     * Zmeni aktivnu Miestnost zobrazovanu na Displeji
     * @param m nova Miestnost
     */
    private void nacitajMiestnost(Miestnost m) {
        if (m != null) {
            this.displej.zmenAktivnuMiestnost(m, this.hrac);
            this.aktivnaMiestnost = m;
        }
    }
}
