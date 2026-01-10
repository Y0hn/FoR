import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * Grafika s mosznostou rotacie okolo stredu obrazu
 * 
 * @author y0hn
 * @version v0.2
 */
public class OtacanaGrafika extends JLabel {
    private transient Image obraz; 
    private final String cesta;
    private double uhol;

    /**
     * Vytvori grafiku Obrazu na ceste s moznostou otacania
     * @param cesta
     */
    public OtacanaGrafika(String cesta) {
        ImageIcon zobrazenie = new ImageIcon(OtacanaGrafika.class.getResource(Hra.CESTA_K_SUBOROM + cesta));
        this.obraz = zobrazenie.getImage();
        super.setIcon(zobrazenie);
        
        this.cesta = cesta;
        this.uhol = 0;
    }
    /**
     * Nastavi uhol otocenia obrazu
     * @param uhol 0 -> smeruje do prava
     */
    public void setUhol(double uhol) {
        this.uhol = uhol;
    }
    /**
     * Nacita novu grafiku podla predom stanovenej cesty
     */
    public void obnovObraz() {
        this.obraz = new ImageIcon(this.cesta).getImage();
    }
    
    @Override
    protected void paintComponent(Graphics grafika) {
        Graphics2D g = (Graphics2D)grafika.create();

        // lepsi vizual
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ziska rozmery obrazu
        int w = getWidth();
        int h = getHeight();

        // ziska stred obrazu
        int x = w / 2;
        int y = h / 2;

        // otoci okolo stredu
        g.rotate(this.uhol, x, y);
        g.drawImage(this.obraz, 0, 0, w, h, this);

        // zbavi sa grafiky (uvolni pamat)
        g.dispose();
    }
}
