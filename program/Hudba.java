import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * Trieda sluziaca na prehravanie Hudby
 * 
 * @author y0hn
 * @version v0.2
 */
public class Hudba {
    private Clip clipHudby;
    private boolean spustene;

    /**
     * Vytvori prehravac pre spustenie Hudobneho suboru 
     * @param subor
     * @param opakovanie ak PRAVDA opakuje sa
     */
    public Hudba(String subor, boolean opakovanie) {
        try (InputStream is =
            Hudba.class.getResourceAsStream(Hra.CESTA_K_SUBOROM + subor);
            BufferedInputStream bis = new BufferedInputStream(is);
            AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {

            this.clipHudby = AudioSystem.getClip();
            this.clipHudby.open(ais);

            if (opakovanie) {
                this.clipHudby.loop(Clip.LOOP_CONTINUOUSLY);
            }            

            this.clipHudby.stop();            
            this.spustene = false;
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
            this.spustene = true;
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
