
/**
 * Reprezentacia postavy vo svete
 * 
 * @author y0hn 
 * @version v0.2
 */
public class Telo {
    private int maxZdravie;
    private int zdravie;
    private Vektor2D pozicia;
    private Vektor2D smer;
    private double rychlostPohybu;
    private double polomerTela;
    /**
     * Vytvori Telo pre postavu 
     * @param maxZdravie pociatocny pocet zravia
     * @param pozicia polohovy Vektor2D - zacinajuca polaha v miestnosti
     * @param smer normalizovany smerovy Vektor2D pohybu
     * @param rychlost ovplyvnuje rychlost pohybu tela
     * @param polomerTela polomer kruhoveho tela
     */
    public Telo(int maxZdravie, Vektor2D pozicia, Vektor2D smer, double rychlost, double polomerTela) {
        this.maxZdravie = maxZdravie;
        this.zdravie = maxZdravie;
        this.pozicia = pozicia;
        this.smer = smer;
        this.rychlostPohybu = rychlost;
        this.polomerTela = polomerTela;
    }
    /**
     * Vytvori zjednodusene Telo 
     * @param pozicia polohovy Vektor2D - zacinajuca polaha v miestnosti
     * @param smer normalizovany smerovy Vektor2D pohybu
     * @param rychlost ovplyvnuje rychlost pohybu tela
     * @param polomerTela polomer kruhoveho tela
     */
    public Telo(Vektor2D pozicia, Vektor2D smer, double rychlost, double polomerTela) {
        this.pozicia = pozicia;
        this.smer = smer;
        this.rychlostPohybu = rychlost;
        this.polomerTela = polomerTela;
    }
    /**
     * Ziska poziciu Tela
     * @return absolutna pozicia Tela v Miestnosti
     */
    public Vektor2D getPozicia() {
        return this.pozicia;
    }
    /**
     * Ziska polomer Tela
     * @return polomer kruhoveho Tela
     */
    public double getPolomer() {
        return this.polomerTela;
    }

    /**
     * Zisti ci sa Tela dotykaju
     * @param druheTelo referencia na merany objekt
     * @return PRAVDA ak je ich vzajomna vzdialenost mensia ako sucet ich polomerov
     */
    public boolean dotykaSa(Telo druheTelo) {
        return this.pozicia.vzdialenostOd(druheTelo.pozicia) < this.polomerTela + druheTelo.polomerTela;
    }
    /**
     * Zmeni zdravie Tela
     * @param uber velkost zmeny zdravia (+/-)
     * @return PRAVDA ak Telo stale zije
     */
    public boolean zmenZdravie(int zmena) {
        this.zdravie += zmena;
        if (this.maxZdravie < this.zdravie) {
            this.zdravie = this.maxZdravie;
        }
        return this.zdravie <= 0;
    }
    /**
     * Usmerni smerovy Vektor2D Tela smerom Vektor2D
     * (normalizuje)
     * @param smerovyVektor novy smerovy Vektor2D
     */
    // pohybVSmere
    public void pohybVektor(Vektor2D smerovyVektor) {
        this.smer = smerovyVektor.normalizuj();
        Vektor2D pohyb = this.smer.skalarnySucin(this.rychlostPohybu);
        this.pozicia = this.pozicia.sucet(pohyb);
    }
}
