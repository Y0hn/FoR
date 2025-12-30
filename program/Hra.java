/**
 * Trieda pre zjednotenie vsetkych komponentov Hry
 * 
 * @author y0hn 
 * @version v0.6
 */
public class Hra {
    public static final Rozmer2D ROZMER_OKNA = new Rozmer2D(0, 0, 600, 400);
    private static final int VELKOST_SVETA = 50;
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

    private final Hrac hrac;
    private final Displej displej;
    private Svet svet;
    private Miestnost aktivnaMiestnost;
    private StavHry stavHry;
    private boolean prekryte;

    private Hra() {
        this.displej = new Displej("assets/icon.png", "FrontRooms", Hra.ROZMER_OKNA);
        this.svet = new Svet(VELKOST_SVETA);
        this.hrac = new Hrac();
        
        this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
        this.hrac.nastavVstup(this.displej.getOkno());
        this.displej.nastavHraca(this.hrac);
        this.stavHry = StavHry.MENU;
        this.prekryte = false;
        this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
    }
    
    /**
     * Obnovi vsetky objekty v Hre
     * @param deltaCasu casovy rozdiel od posledneho tiku
     */
    public void tik(double deltaCasu) {
        if (this.stavHry == StavHry.HRA) {
            this.stavHry = this.aktivnaMiestnost.tik(this.hrac, deltaCasu);
            this.stavHry = this.hrac.tik(this.aktivnaMiestnost, this.stavHry, deltaCasu);

        } else if (!this.prekryte) {
            this.displej.nastavGrafikuPreStavHry(this.stavHry, true);
            this.prekryte = true;
            
        } else if (this.stavHry == StavHry.PAUZA) {
            if (this.displej.ziskajSpatDoMenu()) {
                this.displej.nastavGrafikuPreStavHry(this.stavHry, false);   
                this.prekryte = false;
                this.stavHry = StavHry.MENU;

            } else if (this.displej.ziskajRestart() || this.hrac.pauzaTik()) {
                this.displej.nastavGrafikuPreStavHry(this.stavHry, false);   
                this.prekryte = false;
                this.stavHry = StavHry.HRA;
            }

        } else if (this.stavHry == StavHry.MENU) {
            if (this.displej.ziskajHlavnuPonuku(StavHry.MENU.getIndexTlacidla("Štart"))) {
                this.displej.nastavGrafikuPreStavHry(this.stavHry, false);
                this.stavHry = StavHry.HRA;
                this.prekryte = false;

            } else if (this.displej.ziskajHlavnuPonuku(StavHry.MENU.getIndexTlacidla("Ukonči Hru"))) {
                System.exit(0);
            }

        } else if (this.displej.ziskajRestart()) {
            this.displej.nastavGrafikuPreStavHry(this.stavHry, false);            
            this.svet = new Svet(VELKOST_SVETA);
            
            this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
            this.prekryte = false;
            this.hrac.ozivHraca();
            this.stavHry = StavHry.HRA;
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
            m.aktivuj();
        }
    }
}
