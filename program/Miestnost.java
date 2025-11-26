import java.util.ArrayList;

/**
 * Miestnost vo svete, ktora ma susedne miestnosti a obsahuje nieco
 * Po vojdeni hraca do miestnosti sa aktivuje
 * 
 * @author y0hn
 * @version v0.5
 */
public class Miestnost {
    
    private int indexMiestnosti;
    private int[] susedneMiestnosti;
    private Stena[] steny;
    private ArrayList<Telo> nepriatelia;
    /**
     * Konstruktor triedy Miestnost
     */
    public Miestnost(int index) {
        this.indexMiestnosti = index;
        int pocetStien = Smer.values().length;

        this.susedneMiestnosti = new int[pocetStien];
        this.steny = new Stena[pocetStien];
        this.nepriatelia = new ArrayList<Telo>();

        for (int i = 0; i < pocetStien; i++) {
            this.susedneMiestnosti[i] = -1;
        }
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
    public ArrayList<Telo> getNepriatelia() {
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
    public void setSused(int sused, Smer smer) {
        this.susedneMiestnosti[smer.ordinal()] = sused;
    }
    /**
     * Vytvori steny podla existujucich susedov
     */
    public void vytvorSteny() {
        for (int i = 0; i < 4; i++) {
            if (this.susedneMiestnosti[i] < 0) {
                this.steny[i] = new Stena(Smer.values()[i]);
            } else {
                this.steny[i] = new Stena(Smer.values()[i], this.susedneMiestnosti[i]);
            }
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
        Hra.nastavAktivnuMiestnost(this.indexMiestnosti);
    }
    /**
     * Obnovi vsetky objekty v miestnosti
     */
    public void tik() {
        if (this.nepriatelia.size() == 0) {
            this.nastavVsetkyDvere(true);
        }
    }
    public void prejdiDoDalsejMiestnosti(Smer smer) {
        int index = smer.ordinal();
        Hra.nastavAktivnuMiestnost(this.susedneMiestnosti[index]);
    }
}
