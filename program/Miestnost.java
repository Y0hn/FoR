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
    private Miestnost[] susedneMiestnosti;
    private Stena[] steny;
    private ArrayList<Telo> nepriatelia;
    private boolean dvereOtvorene;
    /**
     * Konstruktor triedy Miestnost
     */
    public Miestnost(int index) {
        this.indexMiestnosti = index;
        int pocetStien = Smer.values().length;

        this.steny = new Stena[pocetStien];
        this.nepriatelia = new ArrayList<Telo>();
        this.susedneMiestnosti = new Miestnost[pocetStien];
        this.dvereOtvorene = false;
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
    public void setSused(Miestnost sused, Smer smer) {
        this.susedneMiestnosti[smer.ordinal()] = sused;
    }
    /**
     * Vytvori steny podla existujucich susedov
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
    public void tik() {
        if (!this.dvereOtvorene && this.nepriatelia.size() == 0) {
            this.nastavVsetkyDvere(true);
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
}
