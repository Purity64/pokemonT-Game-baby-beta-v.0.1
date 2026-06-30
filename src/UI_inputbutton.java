import java.awt.*;
import java.util.HashMap;

public class UI_inputbutton {
    private static Font arial_20 = new Font("Arial", Font.BOLD, 20);
    private static Font inputFont = new Font("Arial", Font.PLAIN, 24);

    public static HashMap<String, String> hsm = new HashMap<>();
    // เก็บว่าช่องไหนกำลังถูกพิมพ์อยู่ (Focus)
    public static String activeId = "";

    public  static  void resetinput(){
        hsm.clear();
        activeId = "";
    }

    public static void CreaterInputButtin(Graphics2D g2, int x, int y, int width, int height, String label, String id) {
        if (!hsm.containsKey(id)) {
            hsm.put(id, "");
        }

        g2.setColor(Color.BLACK);
        g2.setFont(arial_20);
        g2.drawString(label, x, y - 10);

        boolean isActive = id.equals(activeId);

        if (isActive) {
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x, y, width, height, 10, 10);
            g2.setColor(new Color(0, 120, 215));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width, height, 10, 10);
        } else {
            g2.setColor( Color.white);
            g2.fillRoundRect(x, y, width, height, 10, 10);
        }

        // 4. วาดข้อความ
        g2.setColor(Color.BLACK);
        g2.setFont(inputFont);

        String currentText = hsm.get(id);
        // ถ้า Active ให้วาด Cursor กระพริบ
        if (isActive && (System.currentTimeMillis() / 500) % 2 == 0) {
            currentText += "|";
        }

        g2.drawString(currentText, x + 15, y + 30);
    }
}