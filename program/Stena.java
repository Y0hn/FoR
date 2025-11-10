/**
 * Brani Hracovi opustit z heraciu plochu
 * 
 * @author y0hn
 * @version v0.3
 */
public class Stena {
    /* Hodnoty v buducnosti ziskavame z Displaya */
    private static Vektor rozmerDisplay = new Vektor(500, 500); 
    private static double sirkaSteny = 20;
    private static double sirkaDveri = 0.6;
    /**
     * 0 => Mur1,
     * 1 => Dvere,
     * 2 => Mur2
     */
    private Mur[] mury;
    /**
     * Vytvori celistvu Stenu pre Miestnost v Smere
     * @param smer od stredu miestnosti
     */
    public Stena(Smer smer) {
        this.mury = new Mur[1];

        // absolutna poloha ku lavemu-hronemu rohu okna
        Vektor polohaMuru = Vektor.zero();
        if (smer == Smer.Pravo) {
            polohaMuru = new Vektor(rozmerDisplay.getX() - sirkaSteny, 0);
        } else if (smer == Smer.Dole) {
            polohaMuru = new Vektor(0, rozmerDisplay.getX() - sirkaSteny);
        }

        // nastavi velkost Muru
        Vektor absSmerVektor = smer.toVektor().absolutny();                     // ziska vektor (0,1) alebo (1,0)
        Vektor velkostMuru = absSmerVektor.skalarnySucin(sirkaSteny);           // nastavi sirku Muru
        absSmerVektor = absSmerVektor.vymeneny();                               // vymeni hodnoty
        velkostMuru = velkostMuru.sucet(absSmerVektor.sucin(rozmerDisplay));    // natiahne Mur na celu Stenu Miestnosti (Display)

        this.mury[0] = new Mur(polohaMuru, velkostMuru);
    }
    /**
     * Vytvori clenitu Stenu medzi Miestnost a jej Suseda.
     * Stena obsahuje dvere, ktore vedu do susednej Miestnosti
     * @param smer od stredu sucastnej Miestnosti
     * @param sused susedna Miestnost
     */
    public Stena(Smer smer, Miestnost sused) {
        Vektor smerovyVektor = smer.toVektor();
        this.mury = new Mur[3];
        double pomerPosunuDveri = 1 - sirkaDveri;
        Vektor dlzkaMuru = rozmerDisplay.skalarnySucin(1 / 3);

        for (int i = 0; i < 3; i++) {
            // absolutna poloha ku lavemu-hronemu rohu okna
            Vektor polohaMuru = Vektor.zero();
            if (smer == Smer.Pravo) {
                polohaMuru = new Vektor(rozmerDisplay.getX() - sirkaSteny, 0);
            } else if (smer == Smer.Dole) {
                polohaMuru = new Vektor(0, rozmerDisplay.getX() - sirkaSteny);
            }

            // posun oproti predchadzajucemu ziskame rozsirenim vymeneneho sVektora
            Vektor posun = smerovyVektor.vymeneny().skalarnySucin(i);
            // posun bude absolutny (nezalezi na +/-)
            posun = new Vektor(Math.abs(posun.getX()), Math.abs(posun.getY()));
            polohaMuru = polohaMuru.sucet(posun.sucin(dlzkaMuru));
            // posun dveri
            Vektor posunDveri = Vektor.zero();
            if (i == 1) {
                if (smer == Smer.Pravo) {
                    posunDveri = new Vektor(sirkaSteny * pomerPosunuDveri, 0);
                } else if (smer == Smer.Dole) {
                    posunDveri = new Vektor(0, sirkaSteny * pomerPosunuDveri / 2);
                } else if (smer == Smer.Hore) {
                    posunDveri = new Vektor(0, sirkaSteny * pomerPosunuDveri / -2);
                }
            }
            polohaMuru = polohaMuru.sucet(posunDveri);
            
            // nastavi velkost Muru
            double sirka = (i == 1) ? sirkaSteny * sirkaDveri : sirkaSteny;
            Vektor absSmerVektor = smer.toVektor().absolutny();                     // ziska vektor (0,1) alebo (1,0)
            Vektor velkostMuru = absSmerVektor.skalarnySucin(sirka);                // nastavi sirku Muru
            absSmerVektor = absSmerVektor.vymeneny();                               // vymeni hodnoty
            velkostMuru = velkostMuru.sucet(absSmerVektor.sucin(dlzkaMuru));        // natiahne Mur na 1/3 Steny Miestnosti (Display)

            this.mury[i] = new Mur(polohaMuru, velkostMuru);
        }

        // otvori dvere
        this.mury[1].nastavAktivny(false);
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
        private Vektor pozicia;
        private Vektor velkost;
        private boolean aktivny;
        private Miestnost vedieDoMiestnosti;

        /**
         * Vytvori cast Steny
         * @param pozicia pozicia laveho-horneho rohu
         * @param velkost velkost obdlznika
         */
        public Mur(Vektor pozicia, Vektor velkost) {
            this.vedieDoMiestnosti = null;
            this.pozicia = pozicia;
            this.velkost = velkost;
            this.aktivny = true;
        }
        /**
         * Nastavi priechodnost "dverami" (Murom)
         * @param aktivny ak PRAVDA neda sa prejist (aktivny Mur)
         */
        public void nastavAktivny(boolean aktivny) {
            this.aktivny = aktivny;
        }
        /**
         * Nastavi Miestnost za Murom
         * @param miestnost do ktorej Dvere vedu
         */
        public void setMiestnost(Miestnost miestnost) {
            this.vedieDoMiestnosti = miestnost;
        }
        /**
         * Ziska ci je Mur aktivny (nepriechodny)
         * @return PRAVDA ak je nepriechodny
         */
        public boolean getAktivny() {
            return this.aktivny;
        }
        /**
         * Zisti ci sa Mur prekryva s Telom
         * @param telo kruhove telo
         * @return PRANDA ak je cast Tela v stene
         */
        public boolean koliziaSTelom(Telo telo) {
            boolean kolizuje = true;
            Vektor polohaTela = telo.getPozicia();
            double polomer = telo.getPolomer();

            // ak hrac prechadza dverami prepne aktivnu Miestnost
            if (!this.aktivny) {
                Miestnost aktivna = this.vedieDoMiestnosti; /* planovana zmena */
            }

            kolizuje &= polohaTela.getX() + polomer > this.pozicia.getX(); // kolizia z lava
            kolizuje &= polohaTela.getX() - polomer < this.pozicia.getX() + this.velkost.getX(); // kolizia z prava

            kolizuje &= polohaTela.getY() + polomer > this.pozicia.getY(); // kolizia z hora
            kolizuje &= polohaTela.getY() - polomer < this.pozicia.getY() + this.velkost.getY(); // kolizia z hora

            return kolizuje;
        }
    }
}
