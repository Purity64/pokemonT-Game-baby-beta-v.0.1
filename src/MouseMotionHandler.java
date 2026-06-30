import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler implements MouseMotionListener {
    GamePanel gp;
    public MouseMotionHandler(GamePanel gp) { this.gp = gp; }

    @Override
    public void mouseMoved(MouseEvent e) {
        // อัปเดตพิกัดเมาส์เข้าตัวแปรใน GamePanel
        gp.mouseX = e.getX();

        gp.mouseY = e.getY();
        System.out.print(gp.mouseX);
        System.out.print(gp.mouseY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {}
}