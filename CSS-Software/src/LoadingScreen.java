import javax.swing.*;
import java.awt.*;

public class LoadingScreen {
    public LoadingScreen() {
        this.init();
    }

    public void init() {
        // frame
        JFrame frame = new JFrame();
        // icon for windows
        frame.setIconImage(new ImageIcon("CSS-Software/img/icon.png").getImage());
        JLabel loading = new JLabel();
        loading.setIcon(new ImageIcon("CSS-Software/img/loading.png"));
        frame.add(loading);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
//        centerWindow(frame);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }
        frame.dispose();
//        new GUI("Chum Shum Shop");
    }

    public void centerWindow(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
    public static void main(String[] args) {
        new LoadingScreen();
    }
}