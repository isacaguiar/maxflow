package org.jbpm.gpd.action;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.io.SimpleFileFilter;
import org.jbpm.gpd.tools.classinspector.ServiceInspector;

import eflow.util.DirUtil;

public class ExportJBPMFile extends AbstractAction {
	/** The short descriptive name = "Print Preview ..." */
	public static final String NAME_VALUE = "exportar arquivo par ...";

	/** The mnemonic keycode = KeyEvent.VK_W */
	public static final int MNEMONIC_CODE = KeyEvent.VK_X;

	/** The long description text of this action */
	public static final String LONG_DESCRIPTION_VALUE = "Export to JBPM par";

	/** The icon for the toolbar */
	public final Icon SMALL_ICON_VALUE = new ImageIcon(getClass()
			.getClassLoader().getResource("gif/export.gif"));

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "Export to JBPM par";

	private GpdGraph graph;

	public ExportJBPMFile(GpdGraph graph) {
		super(NAME_VALUE);

		// Setup action parameters
		putValue(MNEMONIC_KEY, new Integer(MNEMONIC_CODE));
		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);

		this.graph = graph;
		setEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			graph.setPortsVisible(false);
			JFileChooser jc = new JFileChooser();
			jc.setDialogTitle("Export File .par");
			jc.addChoosableFileFilter(new SimpleFileFilter("par",
					"\" *.par \" JBPM Files"));
			// String
			// lastFile=(String)Configuration.getInstance().getProperty(Configuration.LAST_SELECTED_PROCESSEXPORT_FILE);
			String lastFile = (String) Configuration.getInstance().getProperty(
					Configuration.LAST_SELECTED_GPD_FILE);
			lastFile = lastFile.substring(0, lastFile.lastIndexOf('.')) + ".par";
			if (lastFile != null) {
				jc.setSelectedFile(new File(lastFile));
			}
			int result = jc.showSaveDialog(graph);
			if (result == JFileChooser.APPROVE_OPTION
					&& jc.getSelectedFile() != null) {
				File file = jc.getSelectedFile();
				if (file.getName().indexOf('.') < 0)
					file = new File(file + ".par");

				
				//TODO
				final File f = file;
				Thread t = new Thread() { 
					public void run() {
						doExport(f);
					}
				};
				t.setPriority(Thread.NORM_PRIORITY);
				//t.start();
				t.run();
				//doExport(file);
				

				Configuration.getInstance().setProperty(
						Configuration.LAST_SELECTED_PROCESSEXPORT_FILE,
						file.toString());
				
				SaveAction.save(new File((String) Configuration.getInstance().getProperty(
						Configuration.LAST_SELECTED_GPD_FILE)), graph);
				
				JOptionPane.showMessageDialog(this.graph, "Arquivo PAR gerado!");
			}
		} catch (HeadlessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			graph.setPortsVisible(true);
		}
	}

	public void export(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ZipOutputStream zos = new ZipOutputStream(fos);
			if (file.getName().indexOf('.') > 0)
				file = new File(file.getPath().substring(0,
						file.getPath().lastIndexOf('.')));
			exportDir(zos, file, null);
			zos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exportDir(ZipOutputStream zos, File file, String dir) {
		try {
			byte[] buffer = new byte[4096];
			if (file.isDirectory()) {
				File[] files = (file).listFiles();
				for (int i = 0; i < files.length; i++) {
					String directory = dir;
					if (files[i].isDirectory())
						directory = (dir == null) ? files[i].getName() : dir
								+ File.separator + files[i].getName();
					exportDir(zos, files[i], directory);
				}
			} else {
				String fileName = (dir == null) ? file.getName() : dir
						+ File.separator + file.getName();
				ZipEntry ze = new ZipEntry(fileName);
				zos.putNextEntry(ze);
				FileInputStream fis = new FileInputStream(file);
				int sz;
				while ((sz = fis.read(buffer, 0, buffer.length)) > -1)
					zos.write(buffer, 0, sz);
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doExport(File f) {
		File tmp = new File(System.getProperty("java.io.tmpdir")
				+ File.separator + "eflow");
		
		DirUtil.deleteDir(tmp);
		
		File file = new File(System.getProperty("java.io.tmpdir")
				+ File.separator + "eflow" + File.separator
				+ f.getPath().substring(f.getPath().lastIndexOf(File.separator)));
		
		File dir = new File(System.getProperty("java.io.tmpdir")
				+ File.separator + "eflow" + File.separator
				+ file.getPath().substring(file.getPath().lastIndexOf(File.separator), 
						file.getPath().lastIndexOf('.')));
		
		
		File dirFinal = new File(file.getPath().substring(0,
				file.getPath().lastIndexOf('.')));
		// if ( dir.isDirectory() ) dir.delete();
		File dirWeb = new File(dir + File.separator + "forms");
		if (!dirWeb.exists())
			dirWeb.mkdirs();

		ExportJpegAction jpegAction = new ExportJpegAction(graph);
		// jpegAction.export(new
		// File(dirWeb+File.separator+dir.getName()+".jpg"));
		jpegAction.export(new File(dirWeb + File.separator + "process"
				+ ".gif"));
		
		ExportJBPMWebAction jbpmWeb = new ExportJBPMWebAction(graph);
		jbpmWeb.export(new File(dir + File.separator + "forms.xml"), dir
				.getName()
				+ ".jpg");

		/**
		 * Problema: lentidão em arquivos grandes
		 */ 
		ExportJBPMAction jbpmAction = new ExportJBPMAction(graph);
		jbpmAction.export(new File(dir + File.separator
				+ "processdefinition.xml"));
		
		Iterator it = jbpmAction.getClasses(graph).iterator();
		while (it.hasNext())
			ServiceInspector.getServiceInspector().getTheClass(
					(String) it.next(),
					dir.toString().concat(File.separator + "class"));
		
		export(file);
		//apagando o arquivo anterior
		f.delete();
		//criando o novo arquivo no local selecionado
		file.renameTo(f);
	}
}
