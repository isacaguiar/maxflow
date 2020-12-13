package eflow.rtf;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import org.jbpm.gpd.dialog.controller.MasterDetailCellController;

public class RtfEditorFactory {

	private static final RtfEditorFactory rtfEditorFactory = new RtfEditorFactory();

	private Map listEditor = new HashMap();
	
	
	private RtfEditorFactory() {

	}

	public static RtfEditorFactory getInstance() {
		return rtfEditorFactory;
	}

	public Stylepad createEditor(String title, JTextArea textArea, MasterDetailCellController controller) {
		Stylepad stylepad = null;
		if(this.listEditor.containsKey(textArea)) {
			stylepad = (Stylepad) this.listEditor.get(textArea);
			if(!stylepad.isShowing()) {
				stylepad.open();
			}
			else {
				//setar o foco
				stylepad.transferFocus();
			}
		}
		else {
			stylepad = new Stylepad(title, textArea, controller);
			stylepad.open();
			this.listEditor.put(textArea, stylepad);
		}
		return stylepad;
	}

}
