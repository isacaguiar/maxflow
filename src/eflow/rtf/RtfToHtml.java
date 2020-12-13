/*
 * Created on 12/04/2005
 *
 */
package eflow.rtf;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.rtf.*;
import javax.swing.text.html.*;
import java.io.*;


public class RtfToHtml {
	
	
	
	/**
	 * método original
	 * @param txt
	 * @return
	 */
	public static String convert(String txt) {
		RTFEditorKit rtf_edit = new RTFEditorKit();
		JTextPane jtp_rtf = new JTextPane();
		JTextPane jtp_html = new JTextPane();
		StyleContext rtf_context = new StyleContext();
		DefaultStyledDocument rtf_doc = new DefaultStyledDocument(rtf_context);
		jtp_rtf.setEditorKit(rtf_edit);
		jtp_rtf.setContentType("text/rtf");
		jtp_html.setContentType("text/html");
		try {
			rtf_edit.read(new StringReader(txt),rtf_doc,0);
			jtp_rtf.setDocument(rtf_doc);
			jtp_html.setText(rtf_doc.getText(0,rtf_doc.getLength()));
			HTMLDocument html_doc = null;
			for (int i = 0; i < rtf_doc.getLength(); i++) {
				AttributeSet a = rtf_doc.getCharacterElement(i).getAttributes();
				AttributeSet p = rtf_doc.getParagraphElement(i).getAttributes();
				String s = jtp_rtf.getText(i, 1);
				jtp_html.select(i, i+1);
				jtp_html.replaceSelection(s);
				html_doc = (HTMLDocument)jtp_html.getDocument();
				html_doc.putProperty("","");
				html_doc.setCharacterAttributes(i,1,a,false);
				MutableAttributeSet attr = new SimpleAttributeSet(p);
				html_doc.setParagraphAttributes(i,1,attr,false);
			}
			StringWriter writer = new StringWriter();
			HTMLEditorKit html_edit = new HTMLEditorKit();
			html_edit.write(writer,html_doc,0,html_doc.getLength());
			return writer.toString();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return txt;
	}
	
	
	
	
	
	
	public static String convertRtfToHtml(String txt) {
		RTFEditorKit rtf_edit = new RTFEditorKit();
		JTextPane jtp_rtf = new JTextPane();
		JTextPane jtp_html = new JTextPane();
		StyleContext rtf_context = new StyleContext();
		DefaultStyledDocument rtf_doc = new DefaultStyledDocument(rtf_context);
		jtp_rtf.setEditorKit(rtf_edit);
		jtp_rtf.setContentType("text/rtf");
		jtp_html.setContentType("text/html");
		try {
			rtf_edit.read(new StringReader(txt), rtf_doc, 0);
			jtp_rtf.setDocument(rtf_doc);
			jtp_html.setText(rtf_doc.getText(0,rtf_doc.getLength()));
			HTMLDocument html_doc = null;
//			/System.out.println(rtf_doc.getLength());
			for (int i = 0; i < rtf_doc.getLength(); i++) {
				AttributeSet a = rtf_doc.getCharacterElement(i).getAttributes();
				AttributeSet p = rtf_doc.getParagraphElement(i).getAttributes();
				String s = jtp_rtf.getText(i, 1);
				jtp_html.select(i, i+1);
				jtp_html.replaceSelection(s.replaceAll("\n", "§"));
				html_doc = (HTMLDocument)jtp_html.getDocument();
				html_doc.putProperty("","");
				html_doc.setCharacterAttributes(i,1,a,false);
				MutableAttributeSet attr = new SimpleAttributeSet(p);
				//html_doc.setParagraphAttributes(i,1,attr,false);
			}
			StringWriter writer = new StringWriter();
			HTMLEditorKit html_edit = new HTMLEditorKit();
			html_edit.write(writer,html_doc,0,html_doc.getLength());
			return writer.toString().replaceAll("&#167;", "<br/>");
		}catch (Exception ex) {
			//ex.printStackTrace();
		}
		return txt;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println(RtfToHtml.convertRtfToHtml("{\\rtf1\\ansi\n" +
"{\\fonttbl\\f0\\fnil Monospaced;\\f1\\fnil Microsoft Sans Serif;\\f2\\fnil Arial;}\n" +
"\\margl1701\\margr1701\\margt1417\\margb1417\\widowctrl\n" +
"\\li0\\ri0\\fi0\\ql\\f2\\fs28\\i0\\b\\ul0\\cf0 linha21\\par\n" +
"linha2\\par\n" +
"\\f1\\fs22\\b0\\par\n" +
"\\par\n" +
"\\li0\\ri0\\fi0\\ul0\\par\n" +
"}"
				));
	}
	
}