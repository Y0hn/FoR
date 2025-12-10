import java.util.ArrayList;
import java.util.Random;

/**
 * Miestnost vo svete, ktora ma susedne miestnosti a obsahuje nieco
 * Po vojdeni hraca do miestnosti sa aktivuje
 * 
 * @author y0hn
 * @version v0.7
 */
public class Miestnost {
    private static final int MIN_POCET_NEPIRATELOV = 1;
    private static final int MAX_POCET_NEPIRATELOV = 5;
    private static final int ONESKORENIE_NEPRIATELOV = 250; // ms
    private static final double DOSAH_DVERI = 150;
    
    private int indexMiestnosti;
    private Miestnost[] susedneMiestnosti;
    private Stena[] steny;
    private ArrayList<Nepriatel> nepriatelia;
    private boolean dvereOtvorene;
    private VyhradenaPlocha vyhradenaPlocha;
    private long casAktivacie;

    /**
     * Konstruktor triedy Miestnost
     */
    public Miestnost(int index) {
        this.indexMiestnosti = index;
        int pocetStien = Smer.values().length;

        this.steny = new Stena[pocetStien];
        this.nepriatelia = new ArrayList<Nepriatel>();
        this.susedneMiestnosti = new Miestnost[pocetStien];
        this.dvereOtvorene = false;
        this.vyhradenaPlocha = null;
    }
    /**
     * Vrati cislo Miestnosti v poradi
     * @return ciselna referencia na Miestnost vo Svete 
     */
    public int getIndex() {
        return this.indexMiestnosti;
    }
    /**
     * Vrati Stenu v Smere 
     * @param s smer od stredu
     * @return referencia na Stenu
     */
    public Stena getStena(Smer s) {
        return this.steny[s.ordinal()];
    }
    /**
     * Ziska list Nepriatelov
     * @return list Nepriatelov
     */
    public ArrayList<Nepriatel> getNepriatelia() {
        return this.nepriatelia;
    }
    /**
     * Odstrani Nepriatela z listu Nepriatelov v Miestnosti
     * @param nepriatel referencia na nepriatela
     */
    public void odstranNepriatela(Nepriatel nepriatel) {
        this.nepriatelia.remove(nepriatel);
        nepriatel.zruzGrafiku();
    }
    /**
     * Ziska Rozmery2D vsetkych Stien (Murov) v Miestnosti
     * @return rozmery v tvare Rozmer2D[stena][mur]
     */
    public Rozmer2D[][] getRozmery2D() {
        Rozmer2D[][] rozmery = new Rozmer2D[this.steny.length][];
        for (int i = 0; i < rozmery.length; i++) {
            rozmery[i] = this.steny[i].getRozmery();
        }
        return rozmery;
    }
    /**
     * Ziska Rozmer vyhernej plochy v Miestnosti
     * @return vrati hodnotu iba v pripade ak je Miestnost konecna inak ma hodnotu NULL
     */
    public VyhradenaPlocha getVyhradenaPlocha() {
        return this.vyhradenaPlocha;
    }
    /**
     * Nastavi suseda v predom urcenom smere
     * @param sused index susednej Miestnosti
     * @param smer smer v ktorom lezi v zavislosti od stredu sucasnej miestnosti
     */
    public void setSused(Miestnost sused, Smer smer) {
        this.susedneMiestnosti[smer.ordinal()] = sused;
    }
    /**
     * Vytvori Steny podla existujucich susedov
     */
    public void vytvorSteny() {
        for (int i = 0; i < 4; i++) {
            if (this.susedneMiestnosti[i] != null) {
                this.steny[i] = new Stena(Smer.values()[i], true);
            } else {
                this.steny[i] = new Stena(Smer.values()[i], false);
            }
        }
    }
    /**
     * Vytvori Nepriatelov na nahodnych suradniciach
     */
    public void vytvorNepriatelov(Random nahoda) {
        int pocet = MAX_POCET_NEPIRATELOV - MIN_POCET_NEPIRATELOV;
        pocet = nahoda.nextInt(pocet);
        pocet += MIN_POCET_NEPIRATELOV;
        Rozmer2D rozmer;

        for (int i = 0; i < pocet; i++) {
            boolean mimoStien;
            boolean mimoDveri;
            boolean mimoOstatnych;
            do {
                Vektor2D pozicia = this.ziskajNahodnuPoziciuVnutri(nahoda);
                rozmer = new Rozmer2D(pozicia, Nepriatel.VELKOST);

                mimoDveri = this.jeRozmerMimoDosahuDveri(rozmer);
                mimoStien = this.jePlochaRozmeruMimoStien(rozmer);
                mimoOstatnych = this.jePlochaRozmeruMimoNepriatelov(rozmer, null);
            } while (!mimoStien || !mimoDveri || !mimoOstatnych);
            this.nepriatelia.add(new Nepriatel(rozmer.getPozicia()));
        }
    }
    /**
     * Prideli Miestnosti Vyhradenu Plochu 
     */
    public void setVyhradenaPlocha(VyhradenaPlocha plocha) {
        this.vyhradenaPlocha = plocha;
    }
    /**
     * Nastavi stav pre vsetky dvere v Miestnosti
     * @param otvorene ak PRAVDA dvere sa otvoria (vypnu)
     */
    public void nastavVsetkyDvere(boolean otvorene) {
        for (Stena stena : this.steny) {
            stena.nastavDvere(otvorene);
        }
        Hra.nastavAktivnuMiestnost(this);
        this.dvereOtvorene = true;
    }
    /**
     * Obnovi vsetky objekty v miestnosti
     * @param hrac Objekt hraca
     * @return novy Stav Hry
     */
    public StavHry tik(Hrac hrac, double deltaCasu) {
        StavHry stavHry = StavHry.HRA;

        if (!this.dvereOtvorene && this.nepriatelia.size() == 0) {
            this.nastavVsetkyDvere(true);
        } else if (this.casAktivacie < System.currentTimeMillis()) {
            for (Nepriatel n : this.nepriatelia) {
                if (n.tik(this, hrac, deltaCasu)) {
                    stavHry = StavHry.PREHRA;
                    break;
                }
            }
        }

        if (this.vyhradenaPlocha != null) {
            stavHry = this.vyhradenaPlocha.tik(hrac);
        }
        return stavHry;
    }
    /**
     * Volane pri vykresleni Miestnosti,
     * nastavuje casovac pre na spusetnie pohybu Nepriatelov
     */
    public void aktivuj() {
        this.casAktivacie = System.currentTimeMillis() + Miestnost.ONESKORENIE_NEPRIATELOV;
    }
    /**
     * Presunie hraca do Miestnosti
     * @param smer od stredu sucastnej Miestnosti
     */
    public void prejdiDoDalsejMiestnosti(Smer smer) {
        int index = smer.ordinal();
        Hra.nastavAktivnuMiestnost(this.susedneMiestnosti[index]);
    }
    /**
     * Zkontroluje ci sa prelina Rozmer s Rozmerom Steny v Miestnosti
     * @param rozmer kontrlovany rozmer
     * @return PRAVDA ak sa neprekryva so ziadnou Stenou
     */
    public boolean jePlochaRozmeruMimoStien(Rozmer2D rozmer) {
        boolean volne = true;
        for (Rozmer2D[] rozmerySteny : this.getRozmery2D()) {
            for (Rozmer2D rozmerMuru : rozmerySteny) {
                volne &= !rozmerMuru.jeRozmerCiastocneVnutri(rozmer);
                if (!volne) {
                    break;
                }
            }
            if (!volne) {
                break;
            }
        }
        return volne;
    }
    /**
     * Zkontroluje ci sa prelina Rozmer s Rozmermi Nepriatelov v Miestnosti
     * @param rozmer kontrlovany rozmer
     * @param okrem ignorovany rozmer Nepiratela
     * @return PRAVDA ak sa neprekryva so ziadnym Nepriatelom
     */
    public boolean jePlochaRozmeruMimoNepriatelov(Rozmer2D rozmer, Rozmer2D okrem) {
        boolean volne = true;
        for (Nepriatel n : this.nepriatelia) {
            Rozmer2D r = n.getTelo().getRozmer();
            if (okrem == null || okrem != r) {
                volne &= !r.jeRozmerCiastocneVnutri(rozmer);
            }
        }
        return volne;        
    }

    private boolean jeRozmerMimoDosahuDveri(Rozmer2D rozmer) {
        boolean mimoDosah = true;
        for (Stena s : this.steny) {
            Rozmer2D r = s.getRozmerDveri();
            if (r != null) {
                mimoDosah &= DOSAH_DVERI < r.vzdialenostStredov(rozmer);
                if (!mimoDosah) {
                    break;
                }
            }
        }
        return mimoDosah;
    }

    private Vektor2D ziskajNahodnuPoziciuVnutri(Random nahoda) {
        int maxX = Displej.getRozmer().getIntVeX();
        int maxY = Displej.getRozmer().getIntVeY();

        int x = nahoda.nextInt(maxX);
        int y = nahoda.nextInt(maxY);
        return new Vektor2D(x, y);
    }

    @Override
    public boolean equals(Object o) {
        boolean rovnake = false;
        if (o instanceof Miestnost) {
            Miestnost m = (Miestnost)o;
            rovnake = m.indexMiestnosti == this.indexMiestnosti;
        }
        return rovnake;
    } 
}
