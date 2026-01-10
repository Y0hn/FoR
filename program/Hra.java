import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Trieda pre zjednotenie vsetkych komponentov Hry, 
 * moze Ukladat a Nacitavat svoj stav zo suboru
 * 
 * @author y0hn 
 * @version v0.7
 */
public class Hra {
    /**
     * Rozmer okna Hry
     * (po zmeneni je potrebne upravit vsetky hodnoty velkosti na obrazovke)
     */
    public static final Rozmer2D ROZMER_OKNA = new Rozmer2D(0, 0, 1200, 840);
    /**
     * Cesta k externym suborom Hry (obrazky, hudba)
     */
    public static final String CESTA_K_SUBOROM = "/assets/";
    private static final String CESTA_ULOZENIA = "./saves/";
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

    private Hrac hrac;
    private final Displej displej;
    private final Hudba hudba;
    private Svet svet;
    private Miestnost aktivnaMiestnost;
    private StavHry stavHry;
    private boolean prekryte;

    private Hra() {
        this.displej = new Displej("icon.png", "FrontRooms", Hra.ROZMER_OKNA);
        this.hudba = new Hudba("background.wav", true);
        this.hrac = new Hrac();
        this.hrac.nastavVstup(this.displej.getOkno());
        
        this.stavHry = StavHry.MENU;
        this.prekryte = true;
        this.displej.nastavGrafikuPreStavHry(this.stavHry, true);
        this.displej.nastavHraca(this.hrac);
    }
    
    /**
     * Obnovi vsetky objekty v Hre
     * @param deltaCasu casovy rozdiel od posledneho tiku
     */
    public void tik(double deltaCasu) {
        if (this.stavHry == StavHry.HRA) {
            this.hudba.prehraj();
            this.stavHry = this.aktivnaMiestnost.tik(this.hrac, deltaCasu);
            this.stavHry = this.hrac.tik(this.aktivnaMiestnost, this.stavHry, deltaCasu);

        } else if (!this.prekryte) {
            this.displej.nastavGrafikuPreStavHry(this.stavHry, true);
            this.hudba.zastav();
            this.prekryte = true;
            
        } else if (this.stavHry == StavHry.PAUZA) {
            if (this.displej.ziskajSpatDoMenu()) {
                this.displej.nastavGrafikuPreStavHry(this.stavHry, false);
                
                this.ulozHru();

                this.stavHry = StavHry.MENU;
                this.prekryte = false;                

            } else if (this.displej.ziskajRestart() || this.hrac.pauzaTik()) {
                this.displej.nastavGrafikuPreStavHry(this.stavHry, false);

                this.stavHry = StavHry.HRA;
                this.prekryte = false;
            }

        } else if (this.stavHry == StavHry.MENU) {
            if (this.displej.ziskajHlavnuPonuku(StavHry.MENU.getIndexTlacidla("Štart"))) {
                this.displej.nastavGrafikuPreStavHry(this.stavHry, false);
                this.svet = new Svet(VELKOST_SVETA);
                this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
                
                this.stavHry = StavHry.HRA;
                this.prekryte = false;

            } else if (this.displej.ziskajHlavnuPonuku(StavHry.MENU.getIndexTlacidla("Pokračuj"))) {
                if (this.nacitajHru()) {
                    this.displej.nastavGrafikuPreStavHry(this.stavHry, false);
                    
                    this.stavHry = StavHry.HRA;
                    this.prekryte = false;
                }

            } else if (this.displej.ziskajHlavnuPonuku(StavHry.MENU.getIndexTlacidla("Ukonči Hru"))) {
                System.exit(0);
            }

        } else if (this.displej.ziskajRestart() || this.hrac.getRestart()) {
            this.displej.nastavGrafikuPreStavHry(this.stavHry, false);            
            this.svet = new Svet(VELKOST_SVETA);
            
            this.nacitajMiestnost(this.svet.getZaciatocnaMiestnost());
            this.hrac.ozivHraca();
            
            this.stavHry = StavHry.HRA;
            this.prekryte = false;
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

    /**
     * Ulozi informacie do suborov
     */
    private void ulozHru() {
        FileOutputStream subor;
        ObjectOutputStream vystup;

        try {
            Path dir = Paths.get(CESTA_ULOZENIA);
            Files.createDirectories(dir);

            subor = new FileOutputStream(CESTA_ULOZENIA + "world.sav");
            vystup = new ObjectOutputStream(subor);
            vystup.writeObject(this.svet);
            vystup.close();

            subor = new FileOutputStream(CESTA_ULOZENIA + "room.sav");
            vystup = new ObjectOutputStream(subor);
            vystup.writeObject(this.aktivnaMiestnost);
            vystup.close();

            subor = new FileOutputStream(CESTA_ULOZENIA + "player.sav");
            vystup = new ObjectOutputStream(subor);
            vystup.writeObject(this.hrac);
            vystup.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Nacita informacie zo suborov
     * @return PRAVDA ak je uspesne
     */
    private boolean nacitajHru() {
        FileInputStream subor;
        ObjectInputStream vstup;
        boolean uspech = false;

        Svet starySvet = null;
        Hrac staryHrac = null;
        Miestnost staraMiestnost = null;

        try {
            subor = new FileInputStream(CESTA_ULOZENIA + "world.sav");
            vstup = new ObjectInputStream(subor);
            starySvet = (Svet)vstup.readObject();
            vstup.close();
            
            subor = new FileInputStream(CESTA_ULOZENIA + "room.sav");
            vstup = new ObjectInputStream(subor);
            staraMiestnost = (Miestnost)vstup.readObject();
            vstup.close();
            
            subor = new FileInputStream(CESTA_ULOZENIA + "player.sav");
            vstup = new ObjectInputStream(subor);
            staryHrac = (Hrac)vstup.readObject();
            vstup.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (starySvet == null || staraMiestnost == null || null == hrac) {
                System.out.println("Neuspesne nacitanie zo subora\n");
            } else {
                this.hrac = staryHrac;
                this.svet = starySvet;
                this.aktivnaMiestnost = staraMiestnost;

                this.displej.nastavHraca(staryHrac);
                staryHrac.nastavVstup(this.displej.getOkno());
                staryHrac.obnovZivoty();
                this.nacitajMiestnost(staraMiestnost);
                this.displej.nastavGrafikuStriel(staryHrac);
                
                uspech = true;
            }
        }
        return uspech;
    }
}
