package org.jbpm.gpd.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.ExceptionHandler;
import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.io.GpdConverter;
import org.jbpm.gpd.io.SimpleFileFilter;

import eflow.gpd.io.gif.GIFEncoder;

public class ExportJpegAction extends AbstractAction{
	public static final String NAME_VALUE = "Salvar como JPEG ...";

	public static final int MNEMONIC_CODE = KeyEvent.VK_S;

	/** The long description text of this action */
	 public static final String LONG_DESCRIPTION_VALUE = 
		 "Salvar como JPEG";

	/** Short description for tool-tip */
	public static final String SHORT_DESCRIPTION_VALUE = "Salvar como JPEG";
	
	public final Icon SMALL_ICON_VALUE = new ImageIcon(getClass()
			.getClassLoader().getResource("gif/image.gif"));

	private GpdGraph graph;
	
	public ExportJpegAction( GpdGraph graph )
	{
		super( NAME_VALUE );
        
		// Setup action parameters
		putValue( MNEMONIC_KEY, new Integer( MNEMONIC_CODE ));
		putValue( LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE );
		putValue( SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE );
		putValue(SMALL_ICON, SMALL_ICON_VALUE);

		this.graph = graph;
		setEnabled( true );
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser jc = new JFileChooser();
		jc.setDialogTitle("Export File JPEG");
        jc.addChoosableFileFilter(new SimpleFileFilter("jpg","\" *.jpg \" Image Files"));
                String lastFile=(String)Configuration.getInstance().getProperty(Configuration.LAST_SELECTED_JPEG_FILE);
		if (lastFile!=null){
			jc.setSelectedFile(new File(lastFile));
		}
		int result = jc.showSaveDialog(graph);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jc.getSelectedFile();
                        if ( file.getName().indexOf('.') < 0 )
                          file = new File(file+".jpg");
			export(file);
			Configuration.getInstance().setProperty(Configuration.LAST_SELECTED_JPEG_FILE,file.toString());
		}
		
	}

        protected void export(File file)
        {
                try {
                        BufferedImage img = GpdConverter.toImage(graph);
                        //ImageIO.write(img, "jpeg".toLowerCase(),file);
                        //ImageIO.write(img, "gif".toLowerCase(),file);

                        
                        OutputStream out = null;
                        out = new FileOutputStream(file);
                        out = new BufferedOutputStream(out);
                        GIFEncoder gif = new GIFEncoder(img);
                        gif.write(out);
                        
                } catch (Exception e2) {
                        ExceptionHandler.getInstance().handleException(e2);
                }
        }
}
