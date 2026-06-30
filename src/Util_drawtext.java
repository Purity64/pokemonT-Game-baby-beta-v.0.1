import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
public class Util_drawtext {
    public static void drawWrappedText(Graphics2D g2, String text, int x, int y, int width) {
        if (text == null || text.isEmpty()) return;

        AttributedString as = new AttributedString(text);
        as.addAttribute(TextAttribute.FONT, g2.getFont());

        LineBreakMeasurer measurer = new LineBreakMeasurer(as.getIterator(), g2.getFontRenderContext());
        float wrappingWidth = (float) width;
        float currentY = (float) y;

        while (measurer.getPosition() < text.length()) {
            TextLayout layout = measurer.nextLayout(wrappingWidth);
            currentY += layout.getAscent();
            layout.draw(g2, (float) x, currentY);
            currentY += layout.getDescent() + layout.getLeading();
        }
    }
}
