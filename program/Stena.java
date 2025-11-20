
/**
 * Brani Hracovi opustit z heraciu plochu
 * 
 * @author y0hn
 * @version v0.6
 */
public class Stena {
    public static final int SIRKA_STENY = 50;
    public static final double SIRKA_DVERI = 0.6;
    private static final double POMER_MUROV = (double)1 / (double)3;

    /**
     * 0 => Mur1,
     * 1 => Dvere,
     * 2 => Mur2
     */
    private Mur[] mury;

    /**
     * Vytvori celistvu Stenu pre Miestnost v Smere
     * @param smer od stredu miestnosti
     * @param rozmer urcuje velkost displaya
     */
    public Stena(Smer smer) {
        this.mury = new Mur[1];
        Vektor2D rozmer = Hra.getRozmerDisplay();

        // absolutna poloha ku lavemu-hronemu rohu okna
        Vektor2D polohaMuru = Vektor2D.zero();
        if (smer == Smer.Pravo) {
            polohaMuru = new Vektor2D(rozmer.getX() - SIRKA_STENY, 0);
        } else if (smer == Smer.Dole) {
            polohaMuru = new Vektor2D(0, rozmer.getY() - SIRKA_STENY);
        }

        // nastavi velkost Muru
        Vektor2D absSmerVektor2D = smer.toVektor2D().absolutny();                     // ziska Vektor2D (0,1) alebo (1,0)
        Vektor2D velkostMuru = absSmerVektor2D.skalarnySucin(SIRKA_STENY);           // nastavi sirku Muru
        absSmerVektor2D = absSmerVektor2D.vymeneny();                               // vymeni hodnoty
        velkostMuru = velkostMuru.sucet(absSmerVektor2D.sucin(rozmer));    // natiahne Mur na celu Stenu Miestnosti (Display)

        this.mury[0] = new Mur(polohaMuru, velkostMuru);
    }
    /**
     * Vytvori clenitu Stenu medzi Miestnost a jej Suseda.
     * Stena obsahuje dvere, ktore vedu do susednej Miestnosti
     * @param smer od stredu sucastnej Miestnosti
     * @param sused susedna Miestnost
     */
    public Stena(Smer smer, int sused) {
        Vektor2D rozmer = Hra.getRozmerDisplay();
        Vektor2D smerovyVektor2D = smer.toVektor2D();
        this.mury = new Mur[3];
        double pomerPosunuDveri = 1 - SIRKA_DVERI;
        Vektor2D dlzkaMuru = rozmer.skalarnySucin(POMER_MUROV);

        for (int i = 0; i < 3; i++) {
            // absolutna poloha ku lavemu-hronemu rohu okna
            Vektor2D polohaMuru = Vektor2D.zero();
            if (smer == Smer.Pravo) {
                polohaMuru = new Vektor2D(rozmer.getX() - SIRKA_STENY, 0);
            } else if (smer == Smer.Dole) {
                polohaMuru = new Vektor2D(0, rozmer.getX() - SIRKA_STENY);
            }

            // posun oproti predchadzajucemu ziskame rozsirenim vymeneneho sVektor2Da
            Vektor2D posun = smerovyVektor2D.vymeneny().skalarnySucin(i);
            // posun bude absolutny (nezalezi na +/-)
            posun = new Vektor2D(Math.abs(posun.getX()), Math.abs(posun.getY()));
            polohaMuru = polohaMuru.sucet(posun.sucin(dlzkaMuru));
            // posun dveri
            Vektor2D posunDveri = Vektor2D.zero();
            if (i == 1) {
                if (smer == Smer.Pravo) {
                    posunDveri = new Vektor2D(SIRKA_STENY * pomerPosunuDveri, 0);
                } else if (smer == Smer.Dole) {
                    posunDveri = new Vektor2D(0, SIRKA_STENY - SIRKA_DVERI * SIRKA_STENY);
                }
            }
            polohaMuru = polohaMuru.sucet(posunDveri);
            
            // nastavi velkost Muru
            double sirka = (i == 1) ? SIRKA_STENY * SIRKA_DVERI : SIRKA_STENY;
            Vektor2D absSmerVektor2D = smer.toVektor2D().absolutny();                     // ziska Vektor2D (0,1) alebo (1,0)
            Vektor2D velkostMuru = absSmerVektor2D.skalarnySucin(sirka);                // nastavi sirku Muru
            absSmerVektor2D = absSmerVektor2D.vymeneny();                               // vymeni hodnoty
            velkostMuru = velkostMuru.sucet(absSmerVektor2D.sucin(dlzkaMuru));        // natiahne Mur na 1/3 Steny Miestnosti (Display)

            this.mury[i] = new Mur(polohaMuru, velkostMuru);
        }
 
        this.mury[1].vedieDoMiestnosti(sused);
    }
    /**
     * Vrati rozmery vsetkych Murov v Stene
     * @return [[pozicia, velkost], ...] <- Vektor2D[][]
     */
    public Vektor2D[][] getRozmery() {        
        Vektor2D[][] rozmery = new Vektor2D[this.mury.length][];
        
        for (int i = 0; i < this.mury.length; i++) {
            rozmery[i] = this.mury[i].getRozmery();
        }
        return rozmery;
    }

    /**
     * Nastavi stav dveri
     * @param otvorene ak PRAVDA tak sa dvere otvoria
     */
    public void nastavDvere(boolean otvorene) {
        if (this.mury.length == 3) {
            this.mury[1].nastavAktivny(!otvorene);
        }
    }
    /**
     * Reprezentuje cast Steny vo Svete
     */
    public class Mur {
        private Vektor2D pozicia;
        private Vektor2D velkost;
        private boolean aktivny;
        private int vedieDoMiestnosti;

        /**
         * Vytvori cast Steny
         * @param pozicia pozicia laveho-horneho rohu
         * @param velkost velkost obdlznika
         */
        public Mur(Vektor2D pozicia, Vektor2D velkost) {
            this.vedieDoMiestnosti = -1;
            this.pozicia = pozicia;
            this.velkost = velkost;
            this.aktivny = true;
        }
        /**
         * Ziska ci je Mur aktivny (nepriechodny)
         * @return PRAVDA ak je nepriechodny
         */
        public boolean getAktivny() {
            return this.aktivny;
        }
        /**
         * Vrati rozmery Muru
         * @return [pozicia, velkost]
         */
        public Vektor2D[] getRozmery() {
            return new Vektor2D[]{pozicia, velkost};
        }
        
        /**
         * Nastavi priechodnost "dverami" (Murom)
         * @param aktivny ak PRAVDA neda sa prejist (aktivny Mur)
         */
        public void nastavAktivny(boolean aktivny) {
            this.aktivny = aktivny;
        }
        /**
         * Nastavi index Miestnosti za Murom
         * @param index index do ktorej Dvere vedu
         */
        public void vedieDoMiestnosti(int index) {
            this.vedieDoMiestnosti = index;
        }
        /**
         * Zisti ci sa Mur prekryva s Telom
         * @param telo kruhove telo
         * @return PRANDA ak je cast Tela v stene
         */
        public boolean koliziaSTelom(Telo telo) {
            boolean kolizuje = true;
            Vektor2D polohaTela = telo.getPozicia();
            double polomer = telo.getPolomer();

            // ak hrac prechadza dverami prepne aktivnu Miestnost
            if (!this.aktivny && 0 <= this.vedieDoMiestnosti) {
                Hra.nastavAktivnuMiestnost(this.vedieDoMiestnosti);
            }

            kolizuje &= polohaTela.getX() + polomer > this.pozicia.getX(); // kolizia z lava
            kolizuje &= polohaTela.getX() - polomer < this.pozicia.getX() + this.velkost.getX(); // kolizia z prava

            kolizuje &= polohaTela.getY() + polomer > this.pozicia.getY(); // kolizia z hora
            kolizuje &= polohaTela.getY() - polomer < this.pozicia.getY() + this.velkost.getY(); // kolizia z hora

            return kolizuje;
        }
    }
}
