/**
 * Miestnost vo svete, ktora ma susedne miestnosti a obsahuje nieco
 * Po vojdeni hraca do miestnosti sa aktivuje
 * 
 * @author y0hn
 * @version v0.2
 */
public class Miestnost {
    /**
     * Hrona 0
     * Lava  1
     * Prava 2
     * Dolna 3
     */
    private Miestnost[] susedneMiestnosti;
    private Stena[] steny;
    /**
     * Konstruktor triedy Miestnost
     */
    public Miestnost() {
        this.susedneMiestnosti = new Miestnost[4];
        this.steny = new Stena[4];
    }
    /**
     * Nastavi suseda v predom urcenom smere
     * 
     * @param sused referencia na suseda
     * @param smer smer v ktorom lezi v zavislosti od stredu sucasnej miestnosti
     */
    public void nastavSuseda(Miestnost sused, Smer smer) {
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
            if (this.susedneMiestnosti[i] == null) {
                this.steny[i] = new Stena(Smer.toSmer(i));
            } else {
                this.steny[i] = new Stena(Smer.toSmer(i), this.susedneMiestnosti[i]);
            }
        }
    }
    /**
     * Ziska pocet susedov
     * @return pocet susedov bez hodnot "null"
     */
    public int getPocetSusedov() {
        int pocet = 0;
        for (int i = 0; i < this.susedneMiestnosti.length; i++) {
            if (this.susedneMiestnosti[i] != null) {
                pocet++;
            }
        }
        return pocet;
    }
}
