/**
 * Reprezentacia postavy vo svete
 * 
 * @author y0hn 
 * @version v0.4
 */
public class Telo {
    private static final double POMER_POSUN = 0.1; 

    private int maxZdravie;
    private int zdravie;
    private Vektor2D pozicia;
    private Vektor2D smerovyVektor2D;
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
        this.smerovyVektor2D = smer;
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
        this.smerovyVektor2D = smer;
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
     * Natstavi smerovy Vektor2D Tela
     * 
     * @param smerovyVektor novy smerovy Vektor2D
     */
    public void setPohybVektor(Vektor2D smerovyVektor) {
        this.smerovyVektor2D = smerovyVektor.normalizuj();
    }
    public void nastavPoziciu(Vektor2D novaPozicia) {
        this.pozicia = novaPozicia;
    }
    
    /**
     * Obnovi Telo -> posunie ho v smere ak je to mozne, pripadne prejde do dalsej miestnosti
     */
    public void tik(Miestnost aktMiest) {
        Vektor2D v = this.ziskajPoziciuDalsiehoPohybu();
        if (this.jePohybMozny(v, aktMiest)) {
            this.nastavPoziciu(v);
            this.skusIstDoInejMiestnosti(aktMiest);
        }
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

    private Vektor2D ziskajPoziciuDalsiehoPohybu() {        
        Vektor2D pohyb = this.smerovyVektor2D.skalarnySucin(this.rychlostPohybu);
        pohyb = this.pozicia.sucet(pohyb);
        return pohyb;
    }
    private boolean jePohybMozny(Vektor2D novaPozicia, Miestnost aktivMiestnost) {
        boolean mozne = true;
        novaPozicia = novaPozicia.sucet(new Vektor2D(this.polomerTela, this.polomerTela));

        Vektor2D[] okraje = new Vektor2D[4];
        for (int i = 0; i < okraje.length; i++) {
            double x = (i < 2) ? this.polomerTela : -this.polomerTela;
            double y = (i % 2 == 0) ? this.polomerTela : -this.polomerTela;
            Vektor2D okraj = new Vektor2D(x, y);            
            okraje[i] = novaPozicia.sucet(okraj);
        }

        for (Telo t : aktivMiestnost.getNepriatelia()) {
            mozne &= !this.dotykaSa(t);
        }
        for (int i = 0; mozne && i < okraje.length; i++) {
            for (Rozmer2D[] rozmerySteny : aktivMiestnost.getRozmery2D()) {
                for (Rozmer2D rozmer : rozmerySteny) {
                    mozne &= !rozmer.jePoziciaVnutri(okraje[i]);
                }
            }
        }

        return mozne;
    }
    private void skusIstDoInejMiestnosti(Miestnost aktualnaMiestnost) {
        Vektor2D stred = this.pozicia.sucet(new Vektor2D(this.polomerTela, this.polomerTela));
        
        if (!Displej.getRozmer().jePoziciaVnutri(stred)) {
            Vektor2D posun = Displej.getRozmer().getVelkost();
            posun = posun.skalarnySucin(-0.5);

            stred = stred.sucet(posun);
            stred = stred.normalizuj();

            double x = (int)Math.round(stred.getX());
            double y = (int)Math.round(stred.getY());
            Smer s = Smer.toSmer(new Vektor2D(x, y));

            this.invertujPolohu();
            aktualnaMiestnost.prejdiDoDalsejMiestnosti(s);
        }
    }
    /**
     * Zmeni pohohu po vstupe do Miestnosti aby bola na opacnej strane ako pri vstupe
     */
    private void invertujPolohu() {
        Vektor2D stredMiestnost = Displej.getRozmer().getVelkost().skalarnySucin(0.5);
        Vektor2D velkostTela = new Vektor2D(this.polomerTela, this.polomerTela);
        
        // presunie sa do stredu Tela
        Vektor2D stredovaPozicia = this.pozicia.sucet(velkostTela);
        // posunie [0,0] do stredu miestnosti
        stredovaPozicia = stredovaPozicia.rozdiel(stredMiestnost);
        
        Vektor2D posun = velkostTela.skalarnySucin(POMER_POSUN);
        posun = stredovaPozicia.normalizuj().zaokruhli().sucin(posun);

        Vektor2D invert = stredovaPozicia.normalizuj().zaokruhli().absolutny().skalarnySucin(-1);
        invert = invert.sucet(invert.absolutny().vymeneny());

        // invertuje poziciu v Miestnosti
        Vektor2D novaPozicia = stredovaPozicia.sucin(invert);
        novaPozicia = novaPozicia.sucet(posun);
        novaPozicia = novaPozicia.rozdiel(velkostTela);
        novaPozicia = novaPozicia.sucet(stredMiestnost);

        this.pozicia = novaPozicia;
    }
}
