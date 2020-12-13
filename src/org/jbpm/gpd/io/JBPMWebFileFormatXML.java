/*
 * Created on 04.01.2004
 * Export of JBPM Fileformat
 */
package org.jbpm.gpd.io;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.DefaultGpdCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.exception.InvalidModelException;
import org.jbpm.gpd.model.ActivityStateVO;
import org.jbpm.gpd.model.FieldVO;
import org.jbpm.gpd.model.FormatterVO;
import org.jbpm.gpd.model.ParameterVO;
import org.jbpm.gpd.model.StartStateVO;
import org.jbpm.gpd.model.StateVO;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;

public class JBPMWebFileFormatXML extends AbstractJBPMFileFormatXML {
	private StringBuffer out=null;
	private List exportedCellList=null;
	private LinkedList joinCellStack=null;
	private String graphName="graph.jpg";
        private double scale=2.0;

	public JBPMWebFileFormatXML() { super(); }
        /**
	 * @Constructor of the JBPMWebFileFormat class
	 * @param name of the associated graphic
	 */
        public JBPMWebFileFormatXML(String name) {
            super();
            this.graphName = name;
        }
        
        /**
	 * @see org.jbpm.gpd.io.GraphModelFileFormat#getFileExtension()
	 */
	public String getFileExtension() {
		return "xml";
	}

	/**
	 * Writes the graph as JBPM file
	 * @see org.jbpm.gpd.io.GraphModelFileFormat#write(java.net.URL, java.util.Hashtable, org.jgraph.JGraph, org.jgraph.graph.GraphModel)
	 */
	public void write(
		URL file,
		Hashtable properties,
		GpdGraph gpGraph,
		GraphModel graphModel)
		throws Exception {
		
		init(gpGraph);

		OutputStream out = null;
		out = new FileOutputStream(file.getFile());
		out = new BufferedOutputStream(out);
		out.write(toString(gpGraph,graphName).getBytes());
		out.flush();
		out.close();

	}

	public String toString(JGraph graph) throws InvalidModelException {
            return toString(graph,"graph.jpg");
        }
	public String toString(JGraph graph, String graphName) throws InvalidModelException {
		//init
		scale=graph.getScale();
                out=new StringBuffer();
		exportedCellList=new LinkedList();
		joinCellStack=new LinkedList();
		
		out.append("<?xml version=\"1.0\"?>\n");
		out.append("<!DOCTYPE web-interface PUBLIC \n");
		out.append("  \"jbpm/webinterface_1_0\"\n");
		out.append("  \"http://jbpm.org/dtd/webinterface_1_0.dtd\">\n\n");

                Dimension d = graph.getCellBounds(graph.getRoots()).getBounds().getSize();
                d.setSize( scale*d.width+10 , scale*d.height+10 );
                // scale of graphic, graph.getScale(); 10 points of margin
                if (graphName == null || graphName.length() < 1)
		out.append("<web-interface>\n");
		out.append("  <!-- =================================== -->\n");
		out.append("  <!-- == PROCESS DEFINITION PROPERTIES == -->\n");
		out.append("  <!-- =================================== -->\n");
		out.append("  <image name=\"web/"+graphName+"\" mime-type=\"image/jpg\" width=\""+d.width+"\" height=\""+d.height+"\" />\n");
		out.append("  <!-- ================ -->\n");
		out.append("  <!-- == ACTIVITIES == -->\n");
		out.append("  <!-- ================ -->\n");
		int max = getGraphModel().getRootCount();
		for (int i = 0; i < max; i++) {
			Object cell = getGraphModel().getRootAt(i);
			if (cell instanceof StartCell){
				writeStartInfo((StartCell)cell);
			} else if (cell instanceof ActivityCell){
				writeActivityInfo((ActivityCell)cell);
			}
		}
		out.append("</web-interface>\n");
		return out.toString();
	}

	/**
	 * @param cell
	 */
	private void writeActivityInfo(ActivityCell cell) {
		ActivityStateVO model = cell.getModel();
		List fieldList = model.getFieldList();
		writeActivityStartInfo(cell, model.getName(), fieldList);
	}

	private void writeActivityStartInfo(
            Object cell, String name, List fieldList) {
                Rectangle2D offset = graph.getCellBounds(graph.getRoots());
                Rectangle2D cellBound = graph.getCellBounds(cell);

                out.append("  <activity-state name = \""+name+"\">"+"\n");
		out.append("		<image-coordinates>\n");
		out.append("			<x1>"+getNumber(cellBound.getMinX()-offset.getX())+"</x1>\n");
		out.append("			<y1>"+getNumber(cellBound.getMinY()-offset.getY())+"</y1>\n");
		out.append("			<x2>"+getNumber(cellBound.getMaxX()-offset.getX(), 10)+"</x2>\n");
		out.append("			<y2>"+getNumber(cellBound.getMaxY()-offset.getY(), 10)+"</y2>\n");
		out.append("		</image-coordinates>\n");

                writeFields(fieldList);
		out.append("  </activity-state>\n");
	}

	/**
	 * @param d the double number
         * @return a string that rapresent the scaled integer number
	 */
	private String getNumber(double d) { return getNumber(d, 0); }
	/**
	 * @param d the double number
         * @param increase the value to add at 'd' number
	 * @return a string that rapresent the scaled integer number
	 */
	private String getNumber(double d, int increase) {
		String number=new Double( scale * d + increase ).toString(); 
                //scale of graphic graph.getScale(); increase points around the shape
		return number.substring(0,number.indexOf("."));
	}

	private void writeFields(List fieldList) {
		Iterator it=fieldList.iterator();
		while (it.hasNext()){
			FieldVO field= (FieldVO)it.next();
			if (field.getFormatter()!=null){
				out.append("		<field attribute = \""+field.getAttribute()+ "\">\n");
				out.append("		  <name>"+field.getName()+"</name>\n");
				out.append("		  <description>"+field.getDescription()+"</description>\n");
				out.append("		  <htmlformatter class=\"" +field.getFormatter().getFormaterclass()+"\">\n");
				Iterator parameterIt = field.getFormatter().getParameterList().iterator();
				while(parameterIt.hasNext()){
					ParameterVO parameter = (ParameterVO)parameterIt.next();
					out.append("      <parameter name=\""+parameter.getName()+"\" >"+parameter.getValue()+"</parameter>\n");
				}
				out.append("		  </htmlformatter>\n");
				out.append("		</field>\n" );
			}
		}
	}

	/**
	 * @param cell
	 */
	private void writeStartInfo(StartCell cell) {
		StartStateVO model = cell.getModel();
		List fieldList = model.getFieldList();
		writeActivityStartInfo(cell, model.getName(), fieldList);
	}

	/**
	/**
	 * @see org.jbpm.gpd.io.GraphModelFileFormat#read(java.net.URL, java.util.Hashtable, org.jgraph.JGraph)
	 */
	public GraphModel read(URL file, Hashtable properties, GpdGraph gpGraph)
		throws Exception {
			SAXBuilder builder = new SAXBuilder();
			builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document doc = builder.build(file);

			init(gpGraph);
			createActivityState(gpGraph, doc);
			
			return graph.getModel();
	}

	private void createActivityState(GpdGraph gpGraph, Document doc)
		throws JDOMException, InvalidModelException {
		XPath activityPath = XPath.newInstance("/web-interface//activity-state");
		List activityStateList = activityPath.selectNodes(doc);
		Iterator it=activityStateList.iterator();
		while (it.hasNext()){
			Element activityState = (Element) it.next();
			createActivityStateDetail(activityState);
		}
	}


	private void createActivityStateDetail(Element activityState) throws InvalidModelException {
		String name=activityState.getAttribute("name").getValue();
		if (getCellByName(name) instanceof ActivityCell){
			ActivityCell cell=(ActivityCell)getCellByName(name);
			if (cell==null){
				cell=new ActivityCell();
				cell.getModel().setName(activityState.getAttribute("name").getValue());
				insert(new Point(10, 10), cell,new Dimension(150,25));
			}
		}
		DefaultGpdCell tempcell = getCellByName(name);
		StateVO model=null;
		if (tempcell instanceof ActivityCell){
			model = ((ActivityCell)tempcell).getModel();
		} else if (tempcell instanceof StartCell){
			model = ((StartCell)tempcell).getModel();
		} 
		createFields(model,activityState);
	}

	/**
	 * @param model
	 * @param activityState
	 */
	private void createFields(StateVO model, Element activityState) {
		List resultList=model.getFieldList();
		List fieldList=activityState.getChildren("field");
		Iterator it = fieldList.iterator();
		while (it.hasNext()){
			Element filed=(Element) it.next();
			String attribute=filed.getAttribute("attribute").getValue();
			FieldVO fieldvo = new FieldVO();
			fieldvo.setAttribute(attribute);
			
			if (resultList.indexOf(fieldvo)!=-1){
				fieldvo=(FieldVO)resultList.get(resultList.indexOf(fieldvo));
			} else {
				resultList.add(fieldvo);
			}

			Element nameElement = filed.getChild("name");
			if (nameElement!=null){
				fieldvo.setName(nameElement.getValue());
			} else if (filed.getAttribute("name")!=null){
				fieldvo.setName(filed.getAttribute("name").getValue());
			}
			
			Element descriptionElement = filed.getChild("description");
			if (descriptionElement!=null){
				fieldvo.setDescription(descriptionElement.getValue());
			}

			Element htmlformatterElement = filed.getChild("htmlformatter");
			if (htmlformatterElement!=null){
				FormatterVO formatter=new FormatterVO();
				formatter.setFormaterclass(htmlformatterElement.getAttributeValue("class"));
				fieldvo.setFormatter(formatter);
				
				formatter.setParameterList(createParameters(htmlformatterElement));
			}
		}
	}

}
