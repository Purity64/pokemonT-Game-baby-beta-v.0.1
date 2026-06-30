import java.awt.*;

public class UI_AlertSystem {
    // สร้าง Font ไว้ในนี้เลย เพื่อให้คลาสอื่นไม่ต้องส่งมาให้


    public static void drawMessageDialog(Graphics2D g2, GamePanel gp, String title, String message) {
        // 1. วาดพื้นหลังมืด
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        int w = 400, h = 200;
        int x = (gp.getWidth() - w) / 2;
        int y = (gp.getHeight() - h) / 2;

        // 2. วาดตัวกล่อง
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, w, h, 20, 20);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, w, h, 20, 20); // เพิ่มเส้นขอบกล่องให้ดูคมชัด

        // 3. วาดข้อความ Title
        g2.setFont(FontConfig.thaiFont30);
        g2.drawString(title, x + 30, y + 50);

        // 4. วาดข้อความ Message
        g2.setFont(FontConfig.thaiFont25);
        // กรณีข้อความยาวเกินไป อาจจะตัดบรรทัดหรือจำกัดตัวอักษร
        g2.drawString(message, x + 30, y + 100);

        // 5. วาดปุ่ม OK
        // หมายเหตุ: คุณต้องมั่นใจว่าเมธอด clickbutton ใน UiCreateroom เป็น static ด้วยถึงจะเรียกแบบนี้ได้
        // หรือถ้าให้ดี ควรเขียนวิธีวาดปุ่มง่ายๆ ไว้ในนี้เลย
        int btnX = x + 150;
        int btnY = y + 140;
        int btnW = 100;
        int btnH = 40;

        // เช็ค Hover สำหรับปุ่ม OK ใน Alert
        if (gp.mouseX >= btnX && gp.mouseX <= btnX + btnW && gp.mouseY >= btnY && gp.mouseY <= btnY + btnH) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
        }

        g2.fillRoundRect(btnX, btnY, btnW, btnH, 10, 10);
        g2.setColor(Color.BLACK);

        // วาดตัวอักษร OK ให้กลางปุ่ม
        FontMetrics fm = g2.getFontMetrics(FontConfig.thaiFont25);
        int textX = btnX + (btnW - fm.stringWidth("OK")) / 2;
        int textY = btnY + (btnH / 2) + (fm.getAscent() / 2) - 2;
        g2.drawString("OK", textX, textY);
    }
}