/**
 * Reprezentacia postavy vo svete
 * 
 * @author y0hn 
 * @version v0.5
 */
public class Telo {
    private static final double POMER_POSUN = 0.1; 

    private int maxZdravie;
    private int zdravie;
    private Rozmer2D rozmer;
    private Vektor2D smerovyVektor2D;
    private double rychlostPohybu;
    
    /**
     * Vytvori Telo pre postavu 
     * @param maxZdravie pociatocny pocet zravia
     * @param pozicia polohovy Vektor2D - zacinajuca polaha v miestnosti
     * @param smer normalizovany smerovy Vektor2D pohybu
     * @param rychlost ovplyvnuje rychlost pohybu tela
     * @param polomerTela polomer kruhoveho tela
     */
    public Telo(int maxZdravie, Rozmer2D rozmer, Vektor2D smer, double rychlost) {
        this.maxZdravie = maxZdravie;
        this.zdravie = maxZdravie;
        this.rozmer = rozmer;
        this.smerovyVektor2D = smer;
        this.rychlostPohybu = rychlost;
    }
    /**
     * Vytvori zjednodusene Telo 
     * @param pozicia polohovy Vektor2D - zacinajuca polaha v miestnosti
     * @param smer normalizovany smerovy Vektor2D pohybu
     * @param rychlost ovplyvnuje rychlost pohybu tela
     * @param polomerTela polomer kruhoveho tela
     */
    public Telo(Rozmer2D rozmer, Vektor2D smer, double rychlost) {
        this.rozmer = rozmer;
        this.smerovyVektor2D = smer;
        this.rychlostPohybu = rychlost;
    }
    public Rozmer2D getRozmer() {
        return this.rozmer;
    }
    /**
     * Natstavi smerovy Vektor2D Tela
     * 
     * @param smerovyVektor novy smerovy Vektor2D
     */
    public void setPohybVektor(Vektor2D smerovyVektor) {
        this.smerovyVektor2D = smerovyVektor.normalizuj();
    }
    
    /**
     * Obnovi Telo -> posunie ho v smere ak je to mozne, pripadne prejde do dalsej miestnosti
     */
    public void tik(Miestnost aktMiest) {
        Vektor2D v = this.ziskajPoziciuDalsiehoPohybu();
        if (this.jePohybMozny(v, aktMiest)) {
            this.rozmer.setPozicia(v);
            this.skusIstDoInejMiestnosti(aktMiest);
        }
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
        pohyb = this.rozmer.getPozicia().sucet(pohyb);
        return pohyb;
    }
    private boolean jePohybMozny(Vektor2D novaPozicia, Miestnost aktivMiestnost) {
        Rozmer2D novyRozmer = new Rozmer2D(novaPozicia, this.rozmer.getVelkost());
        boolean mozne = true;
        for (Telo t : aktivMiestnost.getNepriatelia()) {
            mozne &= !t.getRozmer().jeRozmerCiastocneVnutri(novyRozmer);
        }        
        for (Rozmer2D[] rozmerySteny : aktivMiestnost.getRozmery2D()) {
            for (Rozmer2D rozmer : rozmerySteny) {
                mozne &= !rozmer.jeRozmerCiastocneVnutri(novyRozmer);
            }
        }
        return mozne;
    }
    private void skusIstDoInejMiestnosti(Miestnost aktualnaMiestnost) {
        Vektor2D stred = this.rozmer.ziskajStred();
        
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
        Vektor2D stredovaPozicia = this.rozmer.ziskajStred();

        // posunie [0,0] do stredu miestnosti
        stredovaPozicia = stredovaPozicia.rozdiel(stredMiestnost);
        
        Vektor2D posun = this.rozmer.getVelkost().skalarnySucin(POMER_POSUN * 0.5);
        posun = stredovaPozicia.normalizuj().zaokruhli().sucin(posun);

        Vektor2D invert = stredovaPozicia.normalizuj().zaokruhli().absolutny().skalarnySucin(-1);
        invert = invert.sucet(invert.absolutny().vymeneny());

        // invertuje poziciu v Miestnosti
        Vektor2D novaPozicia = stredovaPozicia.sucin(invert);
        novaPozicia = novaPozicia.sucet(posun);
        novaPozicia = novaPozicia.rozdiel(this.rozmer.getVelkost().skalarnySucin(0.5));
        novaPozicia = novaPozicia.sucet(stredMiestnost);

        this.rozmer.setPozicia(novaPozicia);
    }
}
