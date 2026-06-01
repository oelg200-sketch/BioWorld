package simulador;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Reproductor {
    private Clip clip;

    public void encenderMusica(String nombreArchivo) {
        detenerMusica(); // Apaga la anterior antes de encender la nueva

        try {
            File archivo = new File(nombreArchivo);
            AudioInputStream stream = AudioSystem.getAudioInputStream(archivo);
            clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Bucle infinito
        } catch (Exception e) {
            System.out.println("Error de audio con " + nombreArchivo + ": " + e.getMessage());
        }
    }

    public void detenerMusica() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
