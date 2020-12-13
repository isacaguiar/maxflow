package eflow.gpd.action;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.ExceptionHandler;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.action.ExportJBPMFile;
import org.jbpm.gpd.action.SaveAction;
import org.jbpm.gpd.io.DefaultGraphModelFileFormatXML;
import org.jbpm.gpd.io.GraphModelFileFormat;
import org.jbpm.gpd.io.SimpleFileFilter;

public class SaveExportAction extends AbstractAction {
	/** The short descriptive name = "Print Preview  ..." */
	public static final String NAME_VALUE = "Exportar ...";

	/** The mnemonic keycode = KeyEvent.VK_W */
	public static final int MNEMONIC_CODE = KeyEvent.VK_S;

	/** The long description text of this action */
	public static final String LONG_DESCRIPTION_VALUE = "Save and Export GPD Workflow";

	/** The icon for the toolbar */
	public final Icon SMALL_ICON_VALUE = new ImageIcon(getClass()
			.getClassLoader().getResource("gif/export.gif"));

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "Save and Export GPD Workflow";

	private GpdGraph graph;

	public SaveExportAction(GpdGraph graph) {
		super(NAME_VALUE);

		// Setup action parameters
		putValue(MNEMONIC_KEY, new Integer(MNEMONIC_CODE));
		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);

		this.graph = graph;
		setEnabled(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			graph.setPortsVisible(false);
			JFileChooser jc = new JFileChooser();
			jc.setDialogTitle("Save File");
			jc.addChoosableFileFilter(new SimpleFileFilter("gpd",
					"\" *.gpd \" Graphical Process Designer File"));
			String lastFile = (String) Configuration.getInstance().getProperty(
					Configuration.LAST_SELECTED_GPD_FILE);
			if (lastFile != null) {
				jc.setSelectedFile(new File(lastFile));
			}
			int result = jc.showSaveDialog(graph);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = jc.getSelectedFile();
				
				boolean salva = false;
				if(file.exists()) {
					int op = JOptionPane.showConfirmDialog(this.graph, "Arquivo já existente. Sobrescrever?");
					if(op==JOptionPane.YES_OPTION) {
						salva = true;
					}
					else {
						salva = false;
					}
				}
				else {
					salva = true;
				}
				if(salva) {
					SaveAction save = new SaveAction(this.graph);
					save.save(file, graph);
					
					ExportJBPMFile export = new ExportJBPMFile(this.graph);
					String lastFileExport = (String) Configuration.getInstance().getProperty(
							Configuration.LAST_SELECTED_GPD_FILE);
					lastFileExport = lastFileExport.substring(0, lastFileExport.lastIndexOf('.')) + ".par";
					export.doExport(new File(lastFileExport));
					JOptionPane.showMessageDialog(this.graph, "Arquivo salvo e arquivo PAR gerado!");
				}				
			}
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			graph.setPortsVisible(true);	
		}		
	}
	
	
	
}
