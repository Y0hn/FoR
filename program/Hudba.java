import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * Trieda sluziaca na prehravanie Hudby
 * 
 * @author y0hn
 * @version v0.1
 */
public class Hudba {
    private Clip clipHudby;
    private boolean opakovanie;
    private boolean spustene;

    /**
     * Vytvori Prehravac pre Prehravanie Hudobneho suboru 
     * @param cestaSuboru
     */
    public Hudba(String cestaSuboru, boolean opakovanie) {
        try {
            File subor = new File(cestaSuboru);
            AudioInputStream ais = AudioSystem.getAudioInputStream(subor);

            this.clipHudby = AudioSystem.getClip();
            this.clipHudby.open(ais);

            if (opakovanie) {
                this.clipHudby.loop(Clip.LOOP_CONTINUOUSLY);
            }            
            this.clipHudby.stop();

            this.spustene = false;
            this.opakovanie = opakovanie;

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    /**
     * Zacne alebo pokracuje v prehravani,
     * iba ak je zastavene
     */
    public void prehraj() {
        if (!this.spustene) {
            this.clipHudby.start();

            this.spustene = this.opakovanie;
        }
    }

    /**
     * Zastavi prehravanie,
     * iba ak je spustene
     */
    public void zastav() {
        if (this.spustene) {
            this.clipHudby.stop();
            this.spustene = false;
        }
    }
}
