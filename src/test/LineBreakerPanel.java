package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LineBreakerPanel extends JPanel {
  String text = "This is a long string of Java 2D text "
      + "that can wrap futher to new rows when the "
      + "user reduces the width of this window!!! ";

  AttributedString attribString;

  AttributedCharacterIterator attribCharIterator;

  public LineBreakerPanel() {
    setBackground(Color.white);
    setSize(350, 400);

    attribString = new AttributedString(text);
    attribString.addAttribute(TextAttribute.FOREGROUND, Color.blue, 0, text
        .length()); // Start and end indexes.
    Font font = new Font("sanserif", Font.ITALIC, 20);
    attribString.addAttribute(TextAttribute.FONT, font, 0, text.length());
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    attribCharIterator = attribString.getIterator();

    FontRenderContext frc = new FontRenderContext(null, false, false);
    LineBreakMeasurer lbm = new LineBreakMeasurer(attribCharIterator, frc);

    int x = 10, y = 20; // Left and top margins
    int w = getWidth(), h = getHeight(); // Window dimensions

    float wrappingWidth = w - 15;

    while (lbm.getPosition() < text.length()) {
      TextLayout layout = lbm.nextLayout(wrappingWidth);
      y += layout.getAscent();
      layout.draw(g2, x, y);
      y += layout.getDescent() + layout.getLeading();
    }
  }

  public static void main(String arg[]) {
    JFrame frame = new JFrame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.getContentPane().add("Center", new LineBreakerPanel());
    frame.pack();
    frame.setSize(new Dimension(350, 400));
    frame.setVisible(true);
  }
}