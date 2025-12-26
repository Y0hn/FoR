import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
/**
 * 
 * 
 * @author y0hn
 * @version v0.1
 */
public class OtacanaGrafika extends JLabel {
    private Image obraz; 
    private double uhol;

    /**
     * 
     */
    public OtacanaGrafika(String cesta) {
        ImageIcon zobrazenie = new ImageIcon(cesta);
        this.obraz = zobrazenie.getImage();
        super.setIcon(zobrazenie);
        
        this.uhol = 0;
    }

    public void setUhol(double uhol) {
        this.uhol = uhol;
    } 
    
    @Override
    protected void paintComponent(Graphics grafika) {
        Graphics2D g = (Graphics2D)grafika.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int x = w / 2;
        int y = h / 2;

        g.rotate(this.uhol, x, y);
        g.drawImage(this.obraz, 0, 0, w, h, this);

        g.dispose();
    }
}
