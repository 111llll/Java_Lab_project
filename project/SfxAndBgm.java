import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SfxAndBgm {
    private static Clip bgmClip;
        private static final String[] bgmPaths = {
        "SfxAndBgm/Bgm1.wav",
        "SfxAndBgm/Bgm2.wav",
        "SfxAndBgm/Bgm3.wav"
    };
    private static int currentBgmIndex = 0;
    private static boolean isManuallyStopped = false;
    // 撥放放置成功音效
    public static void playPlace() {
        playSound("SfxAndBgm/Place.wav", false);
    }

    // 撥放遊戲結束音效
    public static void playEnd() {
        playSound("SfxAndBgm/End.wav", false);
    }

    // 撥放背景音樂（循環）
    public static void playBgm() {
        stopBgm(); // 確保不會重複播放
        isManuallyStopped = false;
        playNextBgm();
    }

    // 停止背景音樂
    public static void stopBgm() {
        isManuallyStopped = true;
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }

    // 通用音效播放方法
    private static Clip playSound(String path, boolean loop) {
        try {
            File soundFile = new File(path);
            if (!soundFile.exists()) {
                System.err.println("找不到音效檔案: " + path);
                return null;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
            return clip;

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void playNextBgm() {
        try {
            File file = new File(bgmPaths[currentBgmIndex]);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            bgmClip.start();

            // 撥放結束時自動切下一首
            bgmClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (!isManuallyStopped){
                        bgmClip.close();
                        currentBgmIndex = (currentBgmIndex + 1) % bgmPaths.length;
                        playNextBgm(); // 撥放下一首
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} 
