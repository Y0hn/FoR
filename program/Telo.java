import java.awt.Color;

import javax.swing.JPanel;

/**
 * Reprezentacia postavy vo svete
 * 
 * @author y0hn 
 * @version v0.7
 */
public class Telo {
    private static final double POMER_VELKOSTI_POSUN_PRI_ZMENE_MIESTNOSTI = 0.05; 
    private static final double PRESNOST_POSUNU_K_STENE = 1;
    private static final double PRESNOST_OPRAVY_POSUNU = 1;

    private int maxZdravie;
    private int zdravie;
    private int poskodenie;
    private Rozmer2D rozmer;
    private Vektor2D smerovyVektor2D;
    private double rychlostPohybu;
    private JPanel grafika;
    
    /**
     * Vytvori Telo pre postavu 
     * @param maxZdravie pociatocny pocet zravia
     * @param pozicia polohovy Vektor2D - zacinajuca polaha v miestnosti
     * @param smer normalizovany smerovy Vektor2D pohybu
     * @param rychlost ovplyvnuje rychlost pohybu tela
     * @param polomerTela polomer kruhoveho tela
     */
    public Telo(int maxZdravie, Rozmer2D rozmer, double rychlost, int poskodenie) {
        this.smerovyVektor2D = Vektor2D.ZERO;
        this.rychlostPohybu = rychlost;
        this.poskodenie = poskodenie;
        this.maxZdravie = maxZdravie;
        this.zdravie = maxZdravie;
        this.rozmer = rozmer;
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
     * Nastavi poziciu a obnovi grafiku tela
     * @param pozicia
     */
    public void setPozicia(Vektor2D pozicia) {
        this.rozmer.setPozicia(pozicia);

        // Vykresli zmenu
        if (this.grafika != null) {
            this.grafika.setLocation(rozmer.getPozicia().vytvorPoint());
        }
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
     * Nastavi grafiku hraca
     * @param grafika JPanel
     */
    public void setGrafika(JPanel grafika, Color farba) {
        grafika.setBounds(this.rozmer.vytvorRectangle());
        grafika.setBackground(farba);
        this.grafika = grafika;
    }
    /**
     * Ziska grafiku hraca
     * @return grafika Tela
     */
    public JPanel getGrafika() {
        return this.grafika;
    }    
    /**
     * Obnovi Telo (Hraca) -> posunie ho v smere ak je to mozne, pripadne prejde do dalsej miestnosti
     * @param aktMiest Miestnost v ktorej sa telo nachadza
     */
    public void tik(Miestnost aktMiest) {
        Vektor2D buducaPozicia = this.ziskajPoziciuDalsiehoPohybu();
        Rozmer2D buduciRozmer = new Rozmer2D(buducaPozicia, this.rozmer.getVelkost());
        
        if (aktMiest.jePlochaRozmeruMimoStien(buduciRozmer)) {
            this.setPozicia(buducaPozicia);
            this.skusIstDoInejMiestnosti(aktMiest);
            } else if (!aktMiest.jePlochaRozmeruMimoStien(this.rozmer)) {
            this.opravPoziciu(aktMiest);
        } else {
            this.prijdiKuStene(aktMiest);
        }
    }
    /**
     * Obnovi Telo (Nepriatela) -> posunie ho v smere ak je to mozne
     * @param aktMiest Miestnost v ktorej sa telo nachadza
     * @param aM aktualna Miestnost
     * @param hrac referencia na objekt Hraca
     * @return PRAVDA ak naraza do Hraca
     */
    public boolean tik(Miestnost aM, Hrac hrac) {
        Vektor2D buducaPozicia = this.ziskajPoziciuDalsiehoPohybu();
        Rozmer2D buduciRozmer = new Rozmer2D(buducaPozicia, this.rozmer.getVelkost());        
        boolean koliziaHrac = hrac.getTelo().getRozmer().jeRozmerCiastocneVnutri(buduciRozmer);

        if (!koliziaHrac && aM.jePlochaRozmeruMimoStien(buduciRozmer)) {
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
     * @param uber velkost zmeny zdravia (+/-)
     * @return PRAVDA ak Telo stale zije
     */
    public boolean zmenZdravie(int zmena) {
        this.zdravie += zmena;
        if (this.maxZdravie < this.zdravie) {
            this.zdravie = this.maxZdravie;
        }
        boolean zijem = 0 < this.zdravie;
        if (!zijem) {
            this.zomri();
        }
        return zijem;
    }

    private Vektor2D ziskajPoziciuDalsiehoPohybu() {        
        Vektor2D pohyb = this.smerovyVektor2D.skalarnySucin(this.rychlostPohybu);
        pohyb = this.rozmer.getPozicia().sucet(pohyb);
        return pohyb;
    }    
    private void prijdiKuStene(Miestnost aktivMiestnost) {
        Rozmer2D kopia = this.rozmer.kopia();
        kopia.setPozicia(kopia.getPozicia().zaokruhli());
        while (aktivMiestnost.jePlochaRozmeruMimoStien(kopia)) {
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

        this.setPozicia(novaPozicia);
    }
    private void opravPoziciu(Miestnost aM) {
        Vektor2D stredMiestnosti = Displej.getStred();
        Vektor2D smerPosunu = stredMiestnosti.rozdiel(this.rozmer.ziskajStred());
        smerPosunu = smerPosunu.normalizuj().skalarnySucin(PRESNOST_OPRAVY_POSUNU);

        // Posuva poziciu ku stredu Miestnosti kym kolizuje so stenou
        while (!(aM.jePlochaRozmeruMimoStien(this.rozmer) && aM.jePlochaRozmeruMimoNepriatelov(this.rozmer, null))) {
            Vektor2D pozicia = this.rozmer.ziskajStred().sucet(smerPosunu);
            pozicia = pozicia.sucet(this.rozmer.getVelkost().skalarnySucin(-0.5));
            this.setPozicia(pozicia);
        }
    }
    private void zomri() {
        this.grafika.setBounds(Rozmer2D.ZERO.vytvorRectangle());
        this.grafika.getParent().remove(this.grafika);
    }
}
