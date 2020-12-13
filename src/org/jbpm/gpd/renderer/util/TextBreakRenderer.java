package org.jbpm.gpd.renderer.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

public class TextBreakRenderer {
	
	/**
	 * 
	 * @param g2: o objeto que recebe o draw
	 * @param wrappingWidth: a largura do texto apresentada em cada linha
	 * @param title: o texto
	 * @param height: altura do componente
	 * @param width: largura do componente
	 * @param min: cordenada Y do inicio do texto 
	 */	
	public static void draw(Graphics2D g2, float wrappingWidth, String title, int height, int width, int min) {
		AttributedString attribString = new AttributedString(title);
		attribString.addAttribute(TextAttribute.FOREGROUND, Color.BLACK, 0,
				title.length()); // Start and end indexes.
		Font font = new Font("Verdana", Font.PLAIN, 10);
		attribString.addAttribute(TextAttribute.FONT, font, 0, title.length());
		AttributedCharacterIterator attribCharIterator = attribString
				.getIterator();

		FontRenderContext frc = new FontRenderContext(null, false, false);
		LineBreakMeasurer lbm = new LineBreakMeasurer(attribCharIterator, frc);

		int linhas = 0;
		float space = 0;
		while (lbm.getPosition() < title.length()) {
			TextLayout layout = lbm.nextLayout(wrappingWidth);
			linhas++;
			space = layout.getAscent();
		}
		//o y começa de..
		float y = (height - linhas*space)/2;
		if(y<min) y=min;
		
		lbm.setPosition(0);
		while (lbm.getPosition() < title.length()) {
			TextLayout layout = lbm.nextLayout(wrappingWidth);
			y += layout.getAscent();
			float x = (width - layout.getVisibleAdvance()) / 2;
			layout.draw(g2, x, y);
			//height+=y;
			y += layout.getDescent() + layout.getLeading();			
		}
	}

}
