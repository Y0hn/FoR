/**
 * Miestnost vo svete, ktora ma susedne miestnosti a obsahuje nieco
 * Po vojdeni hraca do miestnosti sa aktivuje
 * 
 * @author y0hn
 * @version v0.5
 */
public class Miestnost {
    /**
     * Hrona 0
     * Lava  1
     * Prava 2
     * Dolna 3
     */
    private int[] susedneMiestnosti;
    private Stena[] steny;
    /**
     * Konstruktor triedy Miestnost
     */
    public Miestnost() {
        this.susedneMiestnosti = new int[4];
        this.steny = new Stena[4];

        for (int i = 0; i < this.susedneMiestnosti.length; i++) {
            this.susedneMiestnosti[i] = -1;
        }
    }
    /**
     * Vrati Stenu v Smere 
     * @param s smer od stredu
     * @return referencia na Stenu
     */
    public Stena getStena(Smer s) {
        return steny[s.toInt()];
    }
    /**
     * Nastavi suseda v predom urcenom smere
     * @param sused index susednej Miestnosti
     * @param smer smer v ktorom lezi v zavislosti od stredu sucasnej miestnosti
     */
    public void nastavSuseda(int sused, Smer smer) {
        int s = smer.toInt();
        if (s < 0 || 3 < s) {
            System.out.println("Zly smer: " + s);
            return;
        }
        this.susedneMiestnosti[s] = sused;
    }
    /**
     * Vytvori steny podla existujucich susedov
     */
    public void vytvorSteny() {
        for (int i = 0; i < 4; i++) {
            if (this.susedneMiestnosti[i] < 0) {
                this.steny[i] = new Stena(Smer.toSmer(i));
            } else {
                this.steny[i] = new Stena(Smer.toSmer(i), this.susedneMiestnosti[i]);
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
    }
}
