import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

/**
 * Drzi informacie o hracovi vo Svete
 * 
 * @author y0hn 
 * @version v0.2
 */
public class Hrac {
    private Telo telo;
    /**
     * Vytvori hraca vo svete
     * @param svet Svet v ktorom hrac zacina hru 
     */
    public Hrac() {
        Vektor2D pozicia = Displej.getRozmer().getVelkost().skalarnySucin(0.5);
        this.telo = new Telo(10, pozicia, Vektor2D.zero(), 5, 20);
    }
    /**
     * Vrati Telo Hraca
     * @return reprazentacia Hraca vo Svete
     */
    public Telo getTelo() {
        return this.telo;
    }
    public void tik() {
        //this.telo.pohybVSmere();
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
            /*public void keyReleased(KeyEvent e) {
                koniecVstupu(e.getKeyCode());
            }*/
        };
        okno.addKeyListener(ka);
    }

    private void vstup(int vstup) {
        this.telo.pohybVektor(this.keygetVektor2D(vstup));
    }
    /*private void koniecVstupu(int vstup) {
        telo.pohybVektor(keygetVektor2D(vstup).skalarnySucin(-1));
    }*/
    private Vektor2D keygetVektor2D(int klaves) {   
        Vektor2D v = Vektor2D.zero();     
        switch (klaves) {
            case 87: // W
                v = Vektor2D.dole();
                break;
            case 65: // A
                v = Vektor2D.lavo();
                break;
            case 83: // S
                v = Vektor2D.hore();
                break;
            case 68: // D
                v = Vektor2D.pravo();
                break;

            case 38: // sipka Hore
                v = Vektor2D.dole();
                break;
            case 37: // sipka Lavo
                v = Vektor2D.lavo();
                break;
            case 40: // sipka Dole
                v = Vektor2D.hore();
                break;
            case 39: // sipka Pravo
                v = Vektor2D.pravo();
                break;

            default:
                break;
        }
        return v;
    }
}
