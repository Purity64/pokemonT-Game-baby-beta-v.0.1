import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class MouseLocation {
    public int mouseX, mouseY;
    GamePanel gp;

    public MouseLocation(GamePanel gp) {
        this.gp = gp;

        // ลงทะเบียนการดักจับเมาส์ให้กับ GamePanel
        gp.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();

            }
        });
    }
}