package org.jbpm.gpd.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.ExceptionHandler;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.io.GraphModelFileFormat;
import org.jbpm.gpd.io.JBPMFileFormatXML;
import org.jbpm.gpd.io.SimpleFileFilter;

import eflow.gpd.io.EflowFileFormatXML;

public class ImportJBPMAction extends AbstractAction{
	/** The short descriptive name = "Print Preview  ..." */
	public static final String NAME_VALUE = "importar processdefinition.xml ...";

	/** The mnemonic keycode = KeyEvent.VK_W */
	public static final int MNEMONIC_CODE = KeyEvent.VK_X;

	/** The long description text of this action */
	 public static final String LONG_DESCRIPTION_VALUE = 
		 "Import to JBPM processdefinition.xml";

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "Import to JBPM processdefinition.xml";

	private GpdGraph graph;
	
	public ImportJBPMAction( GpdGraph graph )
	{
		super( NAME_VALUE );
        
		// Setup action parameters
		putValue( MNEMONIC_KEY, new Integer( MNEMONIC_CODE ));
		putValue( LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE );
		putValue( SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE );

		this.graph = graph;
		setEnabled( true );
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser jc = new JFileChooser();
		jc.setDialogTitle("Import File");
		jc.addChoosableFileFilter(new SimpleFileFilter("xml","\" *.xml \" Process Definition File"));
                String lastFile=(String)Configuration.getInstance().getProperty(Configuration.LAST_SELECTED_PROCESSEXPORT_FILE);
		if (lastFile!=null){
			jc.setSelectedFile(new File(lastFile));
		}
		int result = jc.showOpenDialog(graph);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jc.getSelectedFile();
			try {
				GraphModelFileFormat graphModelFileFormat =
					new EflowFileFormatXML();

				Hashtable props = new Hashtable();
				graphModelFileFormat.read(
					file.toURL(),
					props,
					graph);
			} catch (Exception e2) {
				ExceptionHandler.getInstance().handleException(e2);
			}
			Configuration.getInstance().setProperty(Configuration.LAST_SELECTED_PROCESSEXPORT_FILE,file.toString());
		}
	}
}
