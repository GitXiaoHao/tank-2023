package top.yh.music;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class MusicPlay extends Thread {
    public int flag = 1;
    SourceDataLine dataLine = null;
    private String music;

    public static void main(String[] args) {
        new MusicPlay("top/yh/music//war1.wav").start();
    }

    public MusicPlay(String music) {
        this.music = music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public void playMusic() {
        AudioInputStream audio = null;
        try {
            audio =
                    AudioSystem.getAudioInputStream(Objects.requireNonNull(MusicPlay.class.getClassLoader().getResource("top/yh/music//" + music)));
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        assert audio != null;
        AudioFormat format = audio.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
        try {
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        dataLine.start();
        int count = 0;
        byte[] buffer = new byte[1024];
        while (true) {
            try {
                if (!(flag == 1 && (count = audio.read(buffer, 0, buffer.length)) != -1)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (count > 0) {
                dataLine.write(buffer, 0, count);
            }
        }
        dataLine.drain();
        dataLine.close();
    }

    public void stopMusic() {
        flag = 0;
        dataLine.drain();
        dataLine.close();
    }

    @Override
    public void run() {
        playMusic();
    }

}


