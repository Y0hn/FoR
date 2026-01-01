import java.io.Serializable;

/**
 * Reprezentacia postavy vo svete
 * 
 * @author y0hn 
 * @version v0.9
 */
public class Telo implements Serializable {
    private static final double POMER_VELKOSTI_POSUN_PRI_ZMENE_MIESTNOSTI = 0.3; 
    private static final double PRESNOST_POSUNU_K_STENE = 0.1;
    private static final double PRESNOST_OPRAVY_POSUNU = 1;

    private final int maxZdravie;
    private int zdravie;
    private final int poskodenie;
    private Rozmer2D rozmer;
    private Vektor2D pohybovyVektor2D;
    private Vektor2D smerovyVektor2D;
    private final double rychlostPohybu;
    private OtacanaGrafika grafika;
    private boolean zmenaZdravia;
    /**
     * Vytvori Telo pre postavu 
     * 
     * @param maxZdravie pociatocny pocet zravia
     * @param rozmer Rozmer stvorca opisujuceho Tela
     * @param rychlost ovplyvnuje rychlost pohybu tela
     * @param poskodenie poskodenie sposobovane ostanym
     */
    public Telo(int maxZdravie, Rozmer2D rozmer, double rychlost, int poskodenie) {
        this.smerovyVektor2D = Vektor2D.ZERO;
        this.pohybovyVektor2D = Vektor2D.ZERO;
        this.rychlostPohybu = rychlost;
        this.poskodenie = poskodenie;
        this.maxZdravie = maxZdravie;
        this.zdravie = maxZdravie;
        this.rozmer = rozmer;
        this.zmenaZdravia = false;
    }
    /**
     * Ziska Rozmer2D Tela
     * @return rozmer
     */
    public Rozmer2D getRozmer() {
        return this.rozmer;
    }
    /**
     * Ziska poskodenie sposobovane Tymto Telom
     * @return velkost poskodenia
     */
    public int getPoskodenie() {
        return this.poskodenie;
    }
    /**
     * Ziska aktualne zdravie
     * @return zdravie
     */
    public int getZdravie() {
        return this.zdravie;
    }
    /**
     * Ziska Maximalne zdravie
     * @return maxZdravie
     */
    public int getMaxZdravie() {
        return this.maxZdravie;
    }
    /**
     * Nastavi poziciu a obnovi grafiku tela
     * @param pozicia nova pozicia Tela
     */
    public void setPozicia(Vektor2D pozicia) {
        this.rozmer.setPozicia(pozicia);

        // Vykresli zmenu
        this.obnovGrafiku();
    }
    /**
     * Nastavi smerovy Vektor2D
     * @param smerovyVektor -> smer Otocenia Tela
     */
    public void setPohybVektor(Vektor2D pohybovyVektor) {
        this.pohybovyVektor2D = pohybovyVektor.normalizuj();
    }
    /**
     * Nastavi pohybovy Vektor2D
     * @param smerovyVektor -> smer Pohybu Tela
     */
    public void setSmerovyVektor(Vektor2D smerovyVektor) {
        this.smerovyVektor2D = smerovyVektor;
    }
    /**
     * Nastavi grafiku hraca
     * @param grafika JPanel
     */
    public void setGrafika(OtacanaGrafika grafika) {
        grafika.setBounds(this.rozmer.vytvorRectangle());
        this.grafika = grafika;
    }
    /**
     * Obnovi grafiku tela a jej rotaciu
     * @param smer
     */
    public void obnovGrafiku() {
        if (this.grafika != null) {
            this.grafika.setLocation(this.rozmer.getPozicia().vytvorPoint());
            this.grafika.setUhol(this.smerovyVektor2D.getUhol());
            this.grafika.repaint();
        }
    }
    /**
     * Ziska grafiku hraca
     * @return grafika Tela
     */
    public OtacanaGrafika getGrafika() {
        return this.grafika;
    }
    /**
     * Obnovi Telo (Hraca) -> posunie ho v smere ak je to mozne, pripadne prejde do dalsej miestnosti
     * @param aktMiest Miestnost v ktorej sa telo nachadza
     * @param deltaCasu casovy rozdiel od posledneho tiku
     */
    public boolean tik(Miestnost aktMiest, double deltaCasu) {
        Vektor2D buducaPozicia = this.ziskajPoziciuDalsiehoPohybu(this.pohybovyVektor2D, deltaCasu);
        Rozmer2D buduciRozmer = new Rozmer2D(buducaPozicia, this.rozmer.getVelkost());
        
        if (!this.pohybovyVektor2D.equals(Vektor2D.ZERO)) {
            this.smerovyVektor2D = this.pohybovyVektor2D;
        }

        if (aktMiest.jeRozmerMimoStien(buduciRozmer)) {
            this.setPozicia(buducaPozicia);
            this.skusIstDoInejMiestnosti(aktMiest);
        } else if (!aktMiest.jeRozmerMimoStien(this.rozmer)) {
            this.opravPoziciu(aktMiest);
        } else {
            this.prijdiKuStene(aktMiest);

            Vektor2D posunH = this.ziskajPoziciuDalsiehoPohybu(this.pohybovyVektor2D.roznasobenie(Vektor2D.PRAVO), deltaCasu);
            buduciRozmer.setPozicia(posunH);
            boolean horizontalny = aktMiest.jeRozmerMimoStien(buduciRozmer);

            Vektor2D posunV = this.ziskajPoziciuDalsiehoPohybu(this.pohybovyVektor2D.roznasobenie(Vektor2D.HORE), deltaCasu);
            buduciRozmer.setPozicia(posunV);
            boolean vertikalny = !horizontalny && aktMiest.jeRozmerMimoStien(buduciRozmer);

            if (horizontalny) {
                this.setPozicia(posunH);
            } else if (vertikalny) {
                this.setPozicia(posunV);
            }
        }

        boolean zdravieZmenene = this.zmenaZdravia;
        if (zdravieZmenene) {
            this.zmenaZdravia = false;
        }
        return zdravieZmenene;
    }
    /**
     * Obnovi Telo (Nepriatela) -> posunie ho v smere ak je to mozne
     * @param aM aktualna Miestnost
     * @param hrac referencia na objekt Hraca
     * @param deltaCasu casovy rozdiel od posledneho tiku
     * @return PRAVDA ak naraza do Hraca
     */
    public boolean tik(Miestnost aM, Hrac hrac, double deltaCasu) {
        Vektor2D buducaPozicia = this.ziskajPoziciuDalsiehoPohybu(this.pohybovyVektor2D, deltaCasu);
        Rozmer2D buduciRozmer = new Rozmer2D(buducaPozicia, this.rozmer.getVelkost());        
        boolean koliziaHrac = hrac.getTelo().getRozmer().jeRozmerPrekryty(buduciRozmer);

        if (!this.pohybovyVektor2D.equals(Vektor2D.ZERO)) {
            this.smerovyVektor2D = this.pohybovyVektor2D;
        }
        
        if (!koliziaHrac && aM.jeRozmerMimoStien(buduciRozmer)) {
            if (aM.jePlochaRozmeruMimoNepriatelov(buduciRozmer, this.rozmer)) {
                this.setPozicia(buducaPozicia);
            }
        } else if (!koliziaHrac) {
            this.prijdiKuStene(aM);
        }
        return koliziaHrac;
    }
    /**
     * Zmeni zdravie Tela
     * @param zmena velkost zmeny zdravia (+/-)
     * @return PRAVDA ak Telo stale zije
     */
    public boolean zmenZdravie(int zmena) {
        if (this.maxZdravie < zmena) {
            this.zdravie = this.maxZdravie;
        } else {
            this.zdravie += zmena;
            if (this.maxZdravie < this.zdravie) {
                this.zdravie = this.maxZdravie;
            }
        }
        this.zmenaZdravia = true;
        return 0 < this.zdravie;
    }

    private Vektor2D ziskajPoziciuDalsiehoPohybu(Vektor2D pohybovyVektor, double deltaCasu) {        
        Vektor2D pohyb = pohybovyVektor.sucinSoSkalarom(this.rychlostPohybu);
        pohyb = pohyb.sucinSoSkalarom(deltaCasu);
        pohyb = this.rozmer.getPozicia().sucet(pohyb);
        return pohyb;
    }    
    private void prijdiKuStene(Miestnost aktivMiestnost) {
        Rozmer2D kopia = this.rozmer.kopia();
        kopia.setPozicia(kopia.getPozicia());

        while (aktivMiestnost.jeRozmerMimoStien(kopia)) {
            this.rozmer = kopia.kopia();
            Vektor2D pohyb = this.pohybovyVektor2D.sucinSoSkalarom(PRESNOST_POSUNU_K_STENE);
            kopia.setPozicia(kopia.getPozicia().sucet(pohyb));
        }
    }
    private void skusIstDoInejMiestnosti(Miestnost aktualnaMiestnost) {
        Vektor2D stred = this.rozmer.ziskajStred();
        
        if (!Hra.ROZMER_OKNA.jePoziciaVnutri(stred)) {
            Vektor2D posun = Hra.ROZMER_OKNA.getVelkost();
            posun = posun.sucinSoSkalarom(-0.5);

            stred = stred.sucet(posun);
            stred = stred.normalizuj();

            double x = (int)Math.round(stred.getX());
            double y = (int)Math.round(stred.getY());
            Smer s = Smer.toSmer(new Vektor2D(x, y));

            this.invertujPolohu();
            aktualnaMiestnost.prejdiDverami(s);
        }
    }
    /**
     * Zmeni pohohu po vstupe do Miestnosti aby bola na opacnej strane ako pri vstupe
     */
    private void invertujPolohu() {
        Vektor2D stredMiestnost = Hra.ROZMER_OKNA.getVelkost().sucinSoSkalarom(0.5);
        Vektor2D stredovaPozicia = this.rozmer.ziskajStred();

        // posunie [0,0] do stredu miestnosti
        stredovaPozicia = stredovaPozicia.rozdiel(stredMiestnost);
        
        Vektor2D posun = this.rozmer.getVelkost().sucinSoSkalarom(POMER_VELKOSTI_POSUN_PRI_ZMENE_MIESTNOSTI);
        posun = stredovaPozicia.normalizuj().zaokruhli().roznasobenie(posun);

        Vektor2D invert = stredovaPozicia.normalizuj().zaokruhli().absolutny().sucinSoSkalarom(-1);
        invert = invert.sucet(invert.absolutny().vymeneny());

        // invertuje poziciu v Miestnosti
        Vektor2D novaPozicia = stredovaPozicia.roznasobenie(invert);
        novaPozicia = novaPozicia.sucet(posun);
        novaPozicia = novaPozicia.rozdiel(this.rozmer.getVelkost().sucinSoSkalarom(0.5));
        novaPozicia = novaPozicia.sucet(stredMiestnost);

        this.setPozicia(novaPozicia);
    }
    private void opravPoziciu(Miestnost aM) {
        Vektor2D stredMiestnosti = Hra.ROZMER_OKNA.ziskajStred();
        Vektor2D smerPosunu = stredMiestnosti.rozdiel(this.rozmer.ziskajStred());
        smerPosunu = smerPosunu.normalizuj().sucinSoSkalarom(PRESNOST_OPRAVY_POSUNU);

        // Posuva poziciu ku stredu Miestnosti kym kolizuje so stenou
        while (!(aM.jeRozmerMimoStien(this.rozmer) && aM.jePlochaRozmeruMimoNepriatelov(this.rozmer, null))) {
            Vektor2D pozicia = this.rozmer.ziskajStred().sucet(smerPosunu);
            pozicia = pozicia.sucet(this.rozmer.getVelkost().sucinSoSkalarom(-0.5));
            this.setPozicia(pozicia);
        }
    }
}
