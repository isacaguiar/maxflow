/*
 * Created on 04.01.2004
 * Export of JBPM Fileformat
 */
package eflow.gpd.io;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.GpdMarqueeHandler;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.DefaultGpdCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.cell.Transition;
import org.jbpm.gpd.exception.InvalidModelException;
import org.jbpm.gpd.io.AbstractJBPMFileFormatXML;
import org.jbpm.gpd.model.ActivityStateVO;
import org.jbpm.gpd.model.FieldVO;
import org.jbpm.gpd.model.FormatterVO;
import org.jbpm.gpd.model.ParameterVO;
import org.jbpm.gpd.model.StartStateVO;
import org.jbpm.gpd.model.StateVO;
import org.jbpm.gpd.model.TransitionVO;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;

public class EflowWebFileFormatXML extends AbstractJBPMFileFormatXML {
    private StringBuffer out=null;
    private List exportedCellList=null;
    private LinkedList joinCellStack=null;
    private String graphName="graph.jpg";
    private double scale=2.0;
    private String dir = null;
    
    public EflowWebFileFormatXML() { super(); }
    /**
     * @Constructor of the JBPMWebFileFormat class
     * @param name of the associated graphic
     */
    public EflowWebFileFormatXML(String name) {
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
        //calculando o diretorio
        dir = file.getFile().substring(0, file.getFile().indexOf("forms.xml")); 
        
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
        
        out.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
        
        Dimension d = graph.getCellBounds(graph.getRoots()).getBounds().getSize();
        d.setSize( scale*d.width+10 , scale*d.height+10 );
        // scale of graphic, graph.getScale(); 10 points of margin
        //?????????
        //if (graphName == null || graphName.length() < 1)
        out.append("<forms>\n");
        out.append("  <!-- =================================== -->\n");
        out.append("  <!-- == EFLOW WEB FORMS == -->\n");
        out.append("  <!-- =================================== -->\n");
        
        int max = getGraphModel().getRootCount();
        for (int i = 0; i < max; i++) {
            Object cell = getGraphModel().getRootAt(i);
            if (cell instanceof StartCell){
                writeStartInfo((StartCell)cell);
            } else if (cell instanceof ActivityCell){
                writeActivityInfo((ActivityCell)cell);
            }
        }
        out.append("</forms>\n");
        return out.toString();
    }
    
    /**
     * @param cell
     */
    private void writeActivityInfo(ActivityCell cell) {
        ActivityStateVO model = cell.getModel();
        List fieldList = model.getFieldList();
        writeActivityInfo(cell, model.getName(), fieldList);
    }
    
    private void writeActivityStartInfo(
            Object cell, String name, List fieldList, String form) {
        Rectangle2D offset = graph.getCellBounds(graph.getRoots());
        Rectangle2D cellBound = graph.getCellBounds(cell);
        
        out.append("  <form> \n");
        out.append("  	<file>"+form+"</file> \n");
        out.append("  	<state-name>"+name+"</state-name> \n");
        out.append("  	<task-name></task-name> \n");
        out.append("  	<variable name=\"message\" /> \n");
        out.append("  	<variable name=\"observation\" /> \n");
//      writeFields(fieldList);
        writeTransitionsInfo(findTransitionBySource((DefaultGpdCell)cell));
        out.append("  </form>\n");
    }
    
    
    /**
     * @param list
     */
    private void writeTransitionsInfo(List transitionList) {
        Iterator it = transitionList.iterator();
        while(it.hasNext()){
            Transition transition=(Transition)it.next();
            TransitionVO transitionVo=transition.getModel();
            if(transitionVo.getName()!=null) {
                out.append("           <submitbutton value=\""+transitionVo.getName()+"\" transition-name=\""+transitionVo.getName()+"\" /> \n");
            }
        }
    }
    
    /**
     * @param list
     */
    private StringBuffer generateTransitionsFormInfo(List transitionList) {
        Iterator it = transitionList.iterator();
        StringBuffer ret = new StringBuffer();
        boolean valid = false;
        while(it.hasNext()){
            Transition transition=(Transition)it.next();
            TransitionVO transitionVo=transition.getModel();
            if(transitionVo.getName()!=null) {
                valid = true;
                ret.append("<input type=\"submit\" name=\"submitbutton\" value=\""+transitionVo.getName()+"\" /> \n");
            }
        }
        if(valid)
            return ret;
        else 
            return null;
    }
    
    
    
    private void writeActivityStartInfo(
            Object cell, String name, List fieldList) {
        //String form = "forms/"+name+".form";
        String form = "forms/" + ((DefaultGpdCell)cell).getId() + ".form";
        this.writeActivityStartInfo(cell, name, fieldList, form);
        
        try {
            writeStartForm(cell, name, fieldList, form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeActivityInfo(
            Object cell, String name, List fieldList) {
        String form = "forms/" + ((DefaultGpdCell)cell).getId() + ".form";
        this.writeActivityStartInfo(cell, name, fieldList, form);
        
        try {
            writeForm(cell, name, fieldList, form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * @param cell
     * @param name
     * @param fieldList
     * @param form
     * @throws IOException 
     */
    private void writeStartForm(Object cell, String name, List fieldList, String form) throws IOException {
        StringBuffer transitionsGenerate = this.generateTransitionsFormInfo(findTransitionBySource((DefaultGpdCell) cell));
        String transitions = "";
        if(transitionsGenerate==null) {
            transitions = "<input type=\"submit\" name=\"submitbutton\" value=\"Ok\" /> \n";
        }
        else {
            transitions = transitionsGenerate.toString();
        }            
        
        String ret = ""+
        "<table cellspacing=\"0\"> \n"+
        "<tr> \n"+
        "<td valign=\"top\"> \n"+
        "<table cellspacing=\"0\" bgcolor=\"#eeeeee\" style=\"border-style:solid;border-width:1px;border-color:black;\"> \n"+
        "<tr> \n"+
        "<th nowrap=\"true\" colspan=\"2\"> \n"+
        "<h3>Início do processo</h3> \n"+
        "<hr> \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr title=\"Message\"> \n"+
        "<td align=\"right\"> \n"+
        "Mensagem: \n"+
        "</td> \n"+
        "<td> \n"+
        "<textarea name=\"message\" cols=\"25\" rows=\"4\"></textarea> \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr title=\"Observation\"> \n"+
        "<td align=\"right\"> \n"+
        "Observação: \n"+
        "</td> \n"+
        "<td> \n"+
        "<textarea name=\"observation\" cols=\"25\" rows=\"4\"/></textarea> \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr> \n"+
        "<td nowrap=\"true\" align=\"right\" colspan=\"2\"> \n"+
        "<hr> \n"+
        transitions+
        "</td> \n"+
        "</tr> \n"+
        "</table> \n"+
        "</td> \n"+
        "<td valign=\"top\"> \n"+
        generateImage(cell) +
        //"${image:forms/process.jpg} \n"+
        "</td> \n"+
        "</tr> \n"+
        "</table> \n";
        
        OutputStream out = null;
        out = new FileOutputStream(dir+form);
        out = new BufferedOutputStream(out);
        out.write(ret.getBytes());
        out.flush();
        out.close();
    }
    
    /**
     * @param cell
     * @param name
     * @param fieldList
     * @param form
     * @throws IOException 
     */
    private void writeForm(Object cell, String name, List fieldList, String form) throws IOException {
        StringBuffer transitionsGenerate = this.generateTransitionsFormInfo(findTransitionBySource((DefaultGpdCell) cell));
        String transitions = "";
        
        if(transitionsGenerate==null) {
        	transitionsGenerate = new StringBuffer();
            transitionsGenerate.append("<input type=\"submit\" name=\"submitbutton\" value=\"Ok\" /> \n");
            if(cell instanceof ActivityCell) {
            	ActivityCell activity = (ActivityCell) cell;
            	String nc = activity.getModel().getNc();
            	if(nc!=null && !nc.equals("")) {
            		transitionsGenerate.append("<a target=\"_blank\" href=\"nc.do?nc=" + nc + "\" >" + nc + "</a> \n");
            	}
            }        
        }
        transitions = transitionsGenerate.toString();

        String ret = ""+
        "<table cellspacing=\"0\"> \n"+
        "<tr> \n"+
        "<td valign=\"top\"> \n"+
        "<table cellspacing=\"0\" bgcolor=\"#eeeeee\" style=\"border-style:solid;border-width:1px;border-color:black;\"> \n"+
        "<tr> \n"+
        "<th nowrap=\"true\" colspan=\"2\"> \n"+
        "<h3>${title}</h3> \n"+
        "<hr> \n"+
        "</td> \n"+
        "</tr> \n"+
        
        "<tr> \n"+
        "<th nowrap=\"true\" colspan=\"2\"> \n"+
        "Data Início: ${start} \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr> \n"+
        "<th nowrap=\"true\" colspan=\"2\"> \n"+
        "Data Limite: ${time} \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr> \n"+
        "<th nowrap=\"true\" colspan=\"2\"> \n"+
        "Mensagem: <textarea cols=\"25\" rows=\"3\" readonly>${message}</textarea> \n"+
        "</td> \n"+
        "</tr> \n"+
/*        "<tr> \n"+
        "<th nowrap=\"true\" colspan=\"2\"> \n"+
        "Observação: <textarea cols=\"25\" rows=\"3\" readonly>${observation}</textarea> \n"+
        "</td>  \n"+
        "</tr> \n"+
*/        
        "<tr title=\"Message\"> \n"+
        "<td align=\"right\"> \n"+
        "Mensagem: \n"+
        "</td> \n"+
        "<td> \n"+
        "<textarea name=\"message\" cols=\"25\" rows=\"4\"></textarea> \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr title=\"Observation\"> \n"+
        "<td align=\"right\"> \n"+
        "Observação: \n"+
        "</td> \n"+
        "<td> \n"+
        "<textarea name=\"observation\" cols=\"25\" rows=\"4\"/></textarea> \n"+
        "</td> \n"+
        "</tr> \n"+
        "<tr> \n"+
        "<td nowrap=\"true\" align=\"right\" colspan=\"2\"> \n"+
        "<hr> \n"+
        transitions +
        "</td> \n"+
        "</tr> \n"+
        "</table> \n"+
        "</td> \n"+
        "<td valign=\"top\"> \n"+
        generateImage(cell) +
        //"${image:forms/process.jpg} \n"+
        "</td> \n"+
        "</tr> \n"+
        "</table> \n";
        
        OutputStream out = null;
        out = new FileOutputStream(dir+form);
        out = new BufferedOutputStream(out);
        out.write(ret.getBytes());
        out.flush();
        out.close();
    }
    
    /**
     * @return
     */
    private String generateImage(Object cell) {
        Rectangle2D offset = graph.getCellBounds(graph.getRoots());
        Rectangle2D cellBound = graph.getCellBounds(cell);
        
        int imageWidth = graph.getWidth();
        int imageHeight = graph.getHeight();
        
        String x1 = getNumber(cellBound.getMinX()-offset.getX());
        String y1 = getNumber(cellBound.getMinY()-offset.getY());
        String x2 = getNumber(cellBound.getMaxX()-offset.getX(), 10);
        String y2 = getNumber(cellBound.getMaxY()-offset.getY(), 10);

        String ret = "${image:forms/process.gif box:"+imageWidth+","+imageHeight+","+x1+","+y1+","+x2+","+y2+"} \n";
        
        return ret;
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
     */
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
