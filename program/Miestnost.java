import java.util.ArrayList;
import java.util.Random;

/**
 * Miestnost vo svete, ktora ma susedne miestnosti a obsahuje nieco
 * Po vojdeni hraca do miestnosti sa aktivuje
 * 
 * @author y0hn
 * @version v0.6
 */
public class Miestnost {
    private static final int MAX_POCET_NEPIRATELOV = 5;
    
    private int indexMiestnosti;
    private Miestnost[] susedneMiestnosti;
    private Stena[] steny;
    private ArrayList<Nepriatel> nepriatelia;
    private boolean dvereOtvorene;
    private Random nahoda;

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
        this.nahoda = new Random();
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
    public void vytvorNepriatelov() {
        int pocet = nahoda.nextInt(MAX_POCET_NEPIRATELOV);
        Rozmer2D rozmer;
        for (int i = 0; i < pocet; i++) {
            do {
                Vektor2D pozicia = ziskajNahodnuPoziciuVnutri();
                rozmer = new Rozmer2D(pozicia, Nepriatel.VELKOST);
            } while (!this.jePlochaRozmeruMimoStien(rozmer) || !this.jePlochaRozmeruMimoNepriatelov(rozmer));
            this.nepriatelia.add(new Nepriatel(rozmer.getPozicia()));
        }
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
     */
    public void tik(Hrac hrac) {
        if (!this.dvereOtvorene && this.nepriatelia.size() == 0) {
            this.nastavVsetkyDvere(true);
        } else {
            for (Nepriatel n : nepriatelia) {
                n.tik(this, hrac);
            }
        }
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
     * @return PRAVDA ak sa neprekryva so ziadnym Nepriatelom
     */
    public boolean jePlochaRozmeruMimoNepriatelov(Rozmer2D rozmer) {
        boolean volne = true;
        for (Nepriatel n : this.nepriatelia) {
            volne &= !n.getTelo().getRozmer().jeRozmerCiastocneVnutri(rozmer);
        }
        return volne;        
    }

    private Vektor2D ziskajNahodnuPoziciuVnutri() {
        int minX = steny[Smer.PRAVO.ordinal()].getRozmery()[0].getIntVeX();
        int minY = steny[Smer.HORE.ordinal()].getRozmery()[0].getIntVeY();

        int maxX = Displej.getRozmer().getIntVeX();
        int maxY = Displej.getRozmer().getIntVeY();
        maxX -= steny[Smer.LAVO.ordinal()].getRozmery()[0].getIntVeX();
        maxY -= steny[Smer.DOLE.ordinal()].getRozmery()[0].getIntVeY();

        //System.out.format("(%d, %d)(%d, %d)", minX, maxX, minY, maxY);
        int x = this.nahoda.nextInt(minX, maxX);
        int y = this.nahoda.nextInt(minY, maxY);
        return new Vektor2D(x, y);
    }
}
