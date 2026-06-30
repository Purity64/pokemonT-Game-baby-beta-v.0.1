public class MouseOver {
    public static boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return (mx >= x && mx <= x + width && my >= y && my <= y + height);
    }
}
