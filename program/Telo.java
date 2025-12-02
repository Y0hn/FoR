/**
 * Reprezentacia postavy vo svete
 * 
 * @author y0hn 
 * @version v0.7
 */
public class Telo {
    private static final double POMER_VELKOSTI_POSUN_PRI_ZMENE_MIESTNOSTI = 0.05; 
    private static final double PRESNOST_POSUNU_K_STENE = 1;

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
    public Telo(int maxZdravie, Rozmer2D rozmer, double rychlost) {
        this.smerovyVektor2D = Vektor2D.ZERO;
        this.rychlostPohybu = rychlost;
        this.maxZdravie = maxZdravie;
        this.zdravie = maxZdravie;
        this.rozmer = rozmer;
    }
    
    public Rozmer2D getRozmer() {
        return this.rozmer;
    }
    public void setPozicia(Vektor2D pozicia) {
        this.rozmer.setPozicia(pozicia);
        this.smerovyVektor2D = Vektor2D.ZERO;
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
     * @param aktMiest Miestnost v ktorej sa telo nachadza
     * @return
     */
    public boolean tik(Miestnost aktMiest) {
        Vektor2D buducaPozicia = this.ziskajPoziciuDalsiehoPohybu();
        Rozmer2D buduciRozmer = new Rozmer2D(buducaPozicia, this.rozmer.getVelkost());
        
        boolean moznyPohyb = this.jePohybMedziStenami(buduciRozmer, aktMiest);

        if (moznyPohyb) {
            if (this.jePohybMimoNepriatelov(buduciRozmer, aktMiest, null)) {
                this.rozmer.setPozicia(buducaPozicia);
                this.skusIstDoInejMiestnosti(aktMiest);
            }
        } else {
            this.prijdiKuStene(aktMiest);
        }
        return moznyPohyb;
    }

    public boolean tik(Miestnost aM, Hrac hrac) {
        Vektor2D buducaPozicia = this.ziskajPoziciuDalsiehoPohybu();
        Rozmer2D buduciRozmer = new Rozmer2D(buducaPozicia, this.rozmer.getVelkost());
        
        boolean koliziaHrac = hrac.getTelo().getRozmer().jeRozmerCiastocneVnutri(buduciRozmer);
        boolean moznyPohyb = this.jePohybMedziStenami(buduciRozmer, aM);

        if (moznyPohyb && !koliziaHrac) {
            if (this.jePohybMimoNepriatelov(buduciRozmer, aM, this.rozmer)) {
                this.rozmer.setPozicia(buducaPozicia);
                this.skusIstDoInejMiestnosti(aM);
            }
        } else if (!koliziaHrac) {
            this.prijdiKuStene(aM);
        }
        return moznyPohyb && !koliziaHrac;
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
    private boolean jePohybMedziStenami(Rozmer2D novyRozmer, Miestnost aktivMiestnost) {
        boolean mozne = true;
        for (Rozmer2D[] rozmerySteny : aktivMiestnost.getRozmery2D()) {
            for (Rozmer2D rozmerMuru : rozmerySteny) {
                mozne &= !rozmerMuru.jeRozmerCiastocneVnutri(novyRozmer);
                if (!mozne) {
                    break;
                }
            }
            if (!mozne) {
                break;
            }
        }
        return mozne;        
    }
    private boolean jePohybMimoNepriatelov(Rozmer2D novyRozmer, Miestnost aktivMiestnost, Rozmer2D okrem) {
        boolean mozne = true;
        for (Nepriatel n : aktivMiestnost.getNepriatelia()) {
            if (okrem == null || n.getTelo().getRozmer() != okrem) {
                mozne &= !n.getTelo().getRozmer().jeRozmerCiastocneVnutri(novyRozmer);
            } 
        }
        return mozne;        
    }
    private void prijdiKuStene(Miestnost aktivMiestnost) {
        Rozmer2D kopia = this.rozmer.kopia();
        kopia.setPozicia(kopia.getPozicia().zaokruhli());
        while (this.jePohybMedziStenami(kopia, aktivMiestnost)) {
            this.rozmer = kopia.kopia();
            Vektor2D pohyb = this.smerovyVektor2D.skalarnySucin(PRESNOST_POSUNU_K_STENE);
            kopia.setPozicia(kopia.getPozicia().sucet(pohyb));
        }
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
        
        Vektor2D posun = this.rozmer.getVelkost().skalarnySucin(POMER_VELKOSTI_POSUN_PRI_ZMENE_MIESTNOSTI);
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
