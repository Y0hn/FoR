import java.util.ArrayList;
import java.util.Random;

/**
 * Drzi informacie o celom hernom Svete
 * 
 * @author y0hn 
 * @version v0.5
 */
public class Svet {
    private int indexPociatocnejMiestnosti;
    private ArrayList<Miestnost> miestnosti;
    
    /**
     * Konstruktor Sveta
     * Vytvori hreny Svet so stanovenym poctom miestnosti
     * Taktiez vytvori prepoje (navaznosti) medzi miestnostami
     * 
     * @param velkost nastavuje pocet miestnosti vo svete
     */
    public Svet(int velkost) {
        this.miestnosti = new ArrayList<Miestnost>();
        Random nahoda = new Random();

        ArrayList<Smer> smery = new ArrayList<Smer>();   

        Miestnost miestnost = new Miestnost(0);
        Smer smer; // Urcuje smer k dalsej miestnosti (L, P, D)

        // Vygeneruje Suseda Miestnosti
        while (this.miestnosti.size() + 1 < velkost) {
            Miestnost sused = new Miestnost(this.miestnosti.size() + 1);

            // zabezpecenie aby nesiel naspat z L do P a opacne
            Smer poslednySmer = null;
            if (0 < smery.size()) {
                poslednySmer = smery.get(smery.size() - 1);
            }
            // Vygeneruje iny smer aby nesiel naspat
            do {
                smer = this.vygenerujNahodnySmer(nahoda);
            } while (smer == Smer.HORE || poslednySmer != null && smer.jeOpacny(poslednySmer));

            miestnost.setSused(sused, smer);
            sused.setSused(miestnost, smer.opacny());

            this.miestnosti.add(miestnost);
            miestnost = sused;
            smery.add(smer);
        }
        // prida poslednu miestnost
        this.miestnosti.add(miestnost);

        // Prida mozne prechody medzi miestnostami
        for (int i = 0; i < smery.size(); i++) {
            // ak sme sa posunuli dole
            if (smery.get(i) == Smer.DOLE) {
                int hornyPosun = -1;
                int dolnyPosun = 2;
                
                // ak sme v ramci pola
                while (0 <= i + hornyPosun && i + dolnyPosun < smery.size()) {
                    // ak su rozmery protichodne
                    if (smery.get(i + hornyPosun).jeOpacny(smery.get(i + dolnyPosun - 1))) {
                        Miestnost horna = this.miestnosti.get(i + hornyPosun);
                        Miestnost dolna = this.miestnosti.get(i + dolnyPosun);
                        horna.setSused(dolna, Smer.DOLE);
                        dolna.setSused(horna, Smer.HORE);
                        hornyPosun--;
                        dolnyPosun++;
                    } else {
                        break;
                    }
                }
            }
        }

        // nastavi Miestnost v krotrej hrac zacne hru
        // Miestnost je medzi prvou 1/5 a 4/5 celkoveho poctu
        this.indexPociatocnejMiestnosti = (int)Math.round(this.miestnosti.size() * 0.2 + nahoda.nextDouble() * (this.miestnosti.size() * 0.6));

        int koniec = (nahoda.nextBoolean()) ? 0 : this.miestnosti.size() - 1;
        this.miestnosti.get(koniec).setVyhradenaPlocha(VyhradenaPlocha.VYHERNA_PLOCHA);

        int pocetZon = (this.miestnosti.size() / 10);
        for (int i = 0; i < pocetZon; i++) {
            int index;
            do {
                index = i * 10 + 1 + nahoda.nextInt(8);
            } while (this.indexPociatocnejMiestnosti == index);
            this.miestnosti.get(index).setVyhradenaPlocha(VyhradenaPlocha.UZDRAVOVACIA_PLOCHA);;
        }

        // Vytvori Nepriatelov v praznych Miestnostiach
        for (Miestnost m : this.miestnosti) {
            m.vytvorSteny();
            int index = m.getIndex();
            if (m.getVyhradenaPlocha() == null && this.indexPociatocnejMiestnosti != index) {
                m.vytvorNepriatelov(nahoda);
            }
        }
    }

    /**
     * Vrati index Zacinajej Miestnosti 
     * @return index Miesnosti v kotorej zacina hra 
     */
    public Miestnost getZaciatocnaMiestnost() {
        return this.miestnosti.get(this.indexPociatocnejMiestnosti);
    }
    
    /**
     * Ziska Miestnost z pola Miestnosti, 
     * ak je index nespravny vrati hodnotu NULL
     * @param index poradove cislo Miestnosti vo Svete 
     * @return Miestnost na mieste index v poli
     */
    public Miestnost getMiestnost(int index) {
        if (0 <= index && index < this.miestnosti.size()) {
            return this.miestnosti.get(index);
        } else {
            return null;
        }
    }

    private Smer vygenerujNahodnySmer(Random nahoda) {
        int smernik = nahoda.nextInt(Smer.values().length);
        return Smer.values()[smernik];
    }
}
