import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version v0.3
 */
public class Hrac {
    private static final int MAX_ZIVOT_HRACA = 10;
    private static final Vektor2D VELKOST_HRACA = new Vektor2D(50, 50);
    private static final double RYCHLOST_HRACA = 10;

    private Telo telo;
    private boolean[] pohybVSmere;
    private JPanel grafika;

    /**
     * Vytvori hraca vo svete
     * @param svet Svet v ktorom hrac zacina hru 
     */
    public Hrac() {
        Rozmer2D rozmer = new Rozmer2D(Displej.getStred(), VELKOST_HRACA);
        this.telo = new Telo(MAX_ZIVOT_HRACA, rozmer, Smer.DOLE.getVektor2D(), RYCHLOST_HRACA);

        this.pohybVSmere = new boolean[Smer.values().length];
        for (int i = 0; i < this.pohybVSmere.length; i++) {
            this.pohybVSmere[i] = false;
        }
    }
    /**
     * Vrati Telo Hraca
     * @return reprazentacia Hraca vo Svete
     */
    public Telo getTelo() {
        return this.telo;
    }
    public void setGrafika(JPanel grafika) {
        this.grafika = grafika;
    }

    /**
     * Obnovi vlastnosti Hraca
     * @param aktMiest sucastna Miestnost
     */
    public void tik(Miestnost aktMiest) {
        Vektor2D v = this.ziskajPohybovyVektor2D();
        this.telo.setPohybVektor(v);
        this.telo.tik(aktMiest);

        // Vykresli zmenu
        if (this.grafika != null && !v.equals(Vektor2D.ZERO)) {
            this.grafika.setLocation(this.telo.getRozmer().getPozicia().vyvtorPoint());
        }
    }
    /**
     * Nastavi odposluch na klavesove vstupy k oknu
     * @param okno hlavne zobrazenie Hry
     */
    public void nastavVstup(JFrame okno) {
        KeyAdapter ka = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                Hrac.this.vstup(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) {
                Hrac.this.koniecVstupu(e.getKeyCode());
            }
        };
        okno.addKeyListener(ka);
    }

    private void vstup(int vstup) {
        this.keySetSmer(vstup, true);

    }
    private void koniecVstupu(int vstup) {
        this.keySetSmer(vstup, false);
    }
    /**
     * Mapuje dolezite klavesy na klavesnici ako vstupy pre ovladanie 
     * @param klaves Index klavesu
     * @param pridaj PRAVDA ak bol staceny, NEPRAVDA ak pusteny
     */
    private void keySetSmer(int klaves, boolean pridaj) {   
        int index = -1;

        switch (klaves) {
            case 87: // W
                index = Smer.HORE.ordinal();
                break;
            case 65: // A
                index = Smer.LAVO.ordinal();
                break;
            case 83: // S
                index = Smer.DOLE.ordinal();
                break;
            case 68: // D
                index = Smer.PRAVO.ordinal();
                break;

            case 38: // sipka Hore
                break;
            case 37: // sipka Lavo
                break;
            case 40: // sipka Dole
                break;
            case 39: // sipka Pravo
                break;

            default:
                break;
        }

        if (index != -1) {
            this.pohybVSmere[index] = pridaj;
        }
    }
    private Vektor2D ziskajPohybovyVektor2D() {   
        Vektor2D v = Vektor2D.ZERO;

        for (int i = 0; i < this.pohybVSmere.length; i++) {
            if (this.pohybVSmere[i]) {
                v = v.sucet(Smer.values()[i].getVektor2D());
            }
        }
        
        return v;
    }
}
