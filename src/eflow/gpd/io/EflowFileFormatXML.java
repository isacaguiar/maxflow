/*
 * Created on 04.01.2004
 * Export of JBPM Fileformat
 */
package eflow.gpd.io;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jbpm.gpd.GpdGraph;
import org.jbpm.gpd.GpdMarqueeHandler;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.DecisionCell;
import org.jbpm.gpd.cell.DefaultGpdCell;
import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.cell.SubProcessCell;
import org.jbpm.gpd.cell.Transition;
import org.jbpm.gpd.exception.InvalidModelException;
import org.jbpm.gpd.io.AbstractJBPMFileFormatXML;
import org.jbpm.gpd.model.ActionVO;
import org.jbpm.gpd.model.ActivityStateVO;
import org.jbpm.gpd.model.AssignmentVO;
import org.jbpm.gpd.model.AttributeVO;
import org.jbpm.gpd.model.DecisionVO;
import org.jbpm.gpd.model.FieldVO;
import org.jbpm.gpd.model.ForkJoinVO;
import org.jbpm.gpd.model.ParameterVO;
import org.jbpm.gpd.model.ProcessDefinition;
import org.jbpm.gpd.model.StartStateVO;
import org.jbpm.gpd.model.SubProcessVO;
import org.jbpm.gpd.model.TransitionVO;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import eflow.delegation.rules.RulesID;
import eflow.rtf.RtfToHtml;

public class EflowFileFormatXML extends AbstractJBPMFileFormatXML {
	private StringBuffer out = null;

	private List exportedCellList = null;

	private List exportedCellClassList = null;

	private LinkedList joinCellStack = null;

	private LinkedList joinCellClassStack = null;

	private List visitedTransitionList = new ArrayList();

	private List listRole = new ArrayList();

	/**
	 * @see org.jbpm.gpd.io.GraphModelFileFormat#getFileExtension()
	 */
	public String getFileExtension() {
		return "xml";
	}

	private List getActionClasses(List cellList, List result) {
		Iterator startIt = cellList.iterator();
		while (startIt.hasNext()) {
			ActionVO action = (ActionVO) startIt.next();
			if (action.getHandler() != null)
				result.add(action.getHandler());
			// if(action.getOnException()!=null)
			// result.add(action.getOnException());
		}
		return result;
	}

	private List getTransitionClasses(List transitionList, List result)
			throws InvalidModelException {
		Iterator it = transitionList.iterator();
		while (it.hasNext()) {
			Transition transition = (Transition) it.next();
			getActionClasses(transition.getModel().getActionList(), result);
		}
		return result;
	}

	public List getClasses(GpdGraph gpGraph) throws InvalidModelException {
		init(gpGraph);
		List result = new LinkedList();
		exportedCellClassList = new LinkedList();
		joinCellClassStack = new LinkedList();

		// StartCell startCell = writeStartEndModelInfo();
		StartCell startCell = null;
		int max = getGraphModel().getRootCount();
		// System.out.println("\nCells count: "+max);
		for (int i = 0; i < max && startCell == null; i++) {
			Object cell = getGraphModel().getRootAt(i);
			if (cell instanceof StartCell)
				startCell = (StartCell) cell;
		}
		List actionList = startCell.getModel().getActionList();
		getActionClasses(actionList, result);
		List transition = findTransitionBySource(startCell);
		if (!transition.isEmpty()) {
			getTransitionClasses(transition, result);
			getClassesBlock(transition, result);
		}
		return result;
	}

	private List getClassesBlock(List transitionList, List result)
			throws InvalidModelException {
		Iterator it = transitionList.iterator();
		while (it.hasNext()) {
			Transition transition = (Transition) it.next();
			Object cellObject = getGraphModel().getParent(
					getGraphModel().getTarget(transition));
			if (!exportedCellClassList.contains(cellObject)) {
				exportedCellClassList.add(cellObject);
				List transit = new LinkedList();
				if (cellObject instanceof ActivityCell) {
					ActivityStateVO activityModel = ((ActivityCell) cellObject)
							.getModel();
					AssignmentVO assignment = activityModel.getAssignment();
					if (assignment != null)
						result.add(assignment.getHandler());
					// writeActionInfo(activityModel.getActionList());
					getActionClasses(activityModel.getActionList(), result);
					transit = findTransitionBySource((ActivityCell) cellObject);
				} else if (cellObject instanceof DecisionCell) {
					DecisionVO decisionModel = ((DecisionCell) cellObject)
							.getModel();
					if (decisionModel.getHandler() != null)
						result.add(decisionModel.getHandler());
					transit = findTransitionBySource((DecisionCell) cellObject);
				} else if (cellObject instanceof ForkCell) {
					transit = findTransitionBySource((ForkCell) cellObject);
					// OLD JoinCell
					// joinCell=(JoinCell)joinCellClassStack.getFirst();
					// OLD joinCellClassStack.remove(joinCell);
				} else if (cellObject instanceof JoinCell) {
					transit = findTransitionBySource((JoinCell) cellObject);
					// OLD if (!joinCellClassStack.contains(cellObject))
					// joinCellClassStack.add(cellObject);
				} else if (cellObject instanceof SubProcessCell) {
					SubProcessVO activityModel = ((SubProcessCell) cellObject)
							.getModel();
					getActionClasses(activityModel.getActionList(), result);
					transit = findTransitionBySource((SubProcessCell) cellObject);
				}
				if (!transit.isEmpty()) {
					getTransitionClasses(transit, result);
					getClassesBlock(transit, result);
				}
			}
		}
		return result;
	}

	/**
	 * Writes the graph as JBPM file
	 * 
	 * @see org.jbpm.gpd.io.GraphModelFileFormat#write(java.net.URL,
	 *      java.util.Hashtable, org.jgraph.JGraph, org.jgraph.graph.GraphModel)
	 */
	public void write(URL file, Hashtable properties, GpdGraph gpGraph,
			GraphModel graphModel) throws Exception {

		init(gpGraph);
		OutputStream out = null;
		out = new FileOutputStream(file.getFile());
		out = new BufferedOutputStream(out);
		out.write(toString(gpGraph).getBytes());
		out.flush();
		out.close();

	}

	public String toString(JGraph graph) throws InvalidModelException {
		// init
		out = new StringBuffer();
		exportedCellList = new LinkedList();
		joinCellStack = new LinkedList();

		out.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
		out.append("<!DOCTYPE process-definition PUBLIC \n");
		out.append("  \"-//jBpm/jBpm Mapping DTD 2.0//EN\"\n");
		out.append("  \"http://jbpm.org/dtd/processdefinition-2.0.dtd\">\n");

		StartCell startCell = getStartCell();
		List transition = findTransitionBySource(startCell);

		StartStateVO startModel = startCell.getModel();
		ProcessDefinition processDefinition = startModel.getProcessDefinition();

		out.append("<process-definition name=\"" + processDefinition.getName()
				+ "\">\n");
		// PROCESS DEFINITION PROPERTIES
		out.append("  <!-- =================================== -->\n");
		out.append("  <!-- == PROCESS DEFINITION PROPERTIES == -->\n");
		out.append("  <!-- =================================== -->\n");
		// out.append(" <name>"+processDefiniton.getName()+"</name>\n");
		out.append("  <description><![CDATA["
				+ RtfToHtml
						.convertRtfToHtml(processDefinition.getDescription())
				+ "]]></description>\n");
		out.append("  <swimlane name=\"requester\" />\n");

		// atributos da metalingagem escritos como swimlane
		writeVariableDef(processDefinition);

		// swimlanes
		this.visitedTransitionList = new ArrayList();
		writeSwimlanesByTransitions(transition);
		this.exportedCellList.clear();

		// start-state
		writeStartModelInfo();
		out.append("  <!-- =========== -->\n");
		out.append("  <!-- == NODES == -->\n");
		out.append("  <!-- =========== -->\n");
		// other nodes
		this.visitedTransitionList = new ArrayList();
		writeObjectsByTransitions(transition);

		writeActionInfo(startCell.getModel().getProcessDefinition()
				.getActionList());

		// end-state
		writeEndModelInfo();

		// processtype
		writeProcessTypeInfo(processDefinition);

		out.append("</process-definition>\n");
		return out.toString();
	}

	/**
	 * @param processDefiniton
	 */
	private void writeProcessTypeInfo(ProcessDefinition processDefinition) {
		try {
			List list = processDefinition.getAttributeList();
			AttributeVO attribute = new AttributeVO();
			attribute.setName("@processtype");
			attribute = (AttributeVO) list.get(list.indexOf(attribute));
			out.append("  <processtype id=\"" + attribute.getInitialValue()
					+ "\" />\n\n");
		} catch (Exception e) {
			out.append("  <processtype id=\"0\" />\n\n");
		}
	}

	/**
	 * @param transition
	 * @throws InvalidModelException
	 */
	private void writeSwimlanesByTransitions(List transitionList)
			throws InvalidModelException {
		Iterator it = transitionList.iterator();
		while (it.hasNext()) {
			Transition transition = (Transition) it.next();
			if (this.visitedTransitionList.contains(transition)) {
				continue;
			} else {
				this.visitedTransitionList.add(transition);
			}
			try {
				writeSwimlaneByTransition((Transition) transition);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param transition
	 * @param model
	 */
	private void writeSwimlaneByTransition(Transition transition)
			throws InvalidModelException {
		Object cellObject = getGraphModel().getParent(
				getGraphModel().getTarget(transition));
		if (!exportedCellList.contains(cellObject)) {
			exportedCellList.add(cellObject);

			if (cellObject instanceof ActivityCell) {
				writeSwimlane((ActivityCell) cellObject);
			}
			// indo para as proximas transições
			List transitionList = findTransitionBySource((DefaultGpdCell) cellObject);
			writeSwimlanesByTransitions(transitionList);
			/*
			 * else if (cellObject instanceof DecisionCell) { //TODO
			 * writeDecision((DecisionCell)cellObject); } else if (cellObject
			 * instanceof ForkCell) {
			 * writeConcurrentBlock((ForkCell)cellObject); } else if (cellObject
			 * instanceof JoinCell) { if (!joinCellStack.contains(cellObject)){
			 * joinCellStack.add(cellObject); } }
			 */
		}
	}

	/**
	 * @param cell
	 * @throws InvalidModelException
	 */
	private void writeSwimlane(ActivityCell cell) throws InvalidModelException {
		//String pattern = "@role.";
		ActivityStateVO activityModel = cell.getModel();
		if (activityModel.getRole() != null
				&& !activityModel.getRole().equals("")
				&& !listRole.contains(activityModel.getName())) {
				//&& !listRole.contains(activityModel.getRole())) {
			
			//listRole agora armazena a atividade que já tem a swimlane
			//na verdade este recurso é dispensável 
			//listRole.add(activityModel.getRole());
			listRole.add(activityModel.getName());
			//out.append("  <swimlane name=\"" + pattern + activityModel.getRole() + "\"> \n");
			out.append("  <swimlane name=\"" + RulesID.generate(RulesID.ROLE, activityModel.getName()) + "\"> \n");

			out.append("    <description>" + activityModel.getRole()
					+ "</description> \n");
			out
					.append("    <delegation class=\"eflow.delegation.ActorAssignmentHandler\">"
							+ activityModel.getRole() + "</delegation> \n");
			out.append("  </swimlane> \n");
		}
		// List transition=findTransitionBySource(cell);
		// writeSwimlanesByTransitions(transition);
	}

	/**
	 * @param transition
	 * @param model
	 */
	private void writeObjectsByTransitions(List transitionList)
			throws InvalidModelException {
		Iterator it = transitionList.iterator();
		while (it.hasNext()) {
			Transition transition = (Transition) it.next();
			if (this.visitedTransitionList.contains(transition)) {
				continue;
			} else {
				this.visitedTransitionList.add(transition);
				writeObjectByTransition((Transition) transition);
			}

		}
	}

	/**
	 * @param transition
	 * @param model
	 */
	private void writeObjectByTransition(Transition transition)
			throws InvalidModelException {
		Object cellObject = getGraphModel().getParent(
				getGraphModel().getTarget(transition));
		if (!exportedCellList.contains(cellObject)) {
			exportedCellList.add(cellObject);
			if (cellObject instanceof SubProcessCell) {
				// OK
				writeSubProcess((SubProcessCell) cellObject, false);
			} else if (cellObject instanceof ActivityCell) {
				// OK
				writeActivityState((ActivityCell) cellObject);
			} else if (cellObject instanceof DecisionCell) {
				// TODO
				writeDecision((DecisionCell) cellObject);
			} else if (cellObject instanceof ForkCell) {
				// OK
				writeConcurrentBlock((ForkCell) cellObject);
			} else if (cellObject instanceof JoinCell) {
				if (!joinCellStack.contains(cellObject)) {
					joinCellStack.add(cellObject);
				}
			}
		}
	}

	/**
	 * @param cell
	 */
	private void writeConcurrentBlock(ForkCell cell)
			throws InvalidModelException {
		ForkJoinVO model = cell.getModel();
		out.append("  <!-- ====================== -->\n");
		out.append("  <!-- == CONCURRENT BLOCK == -->\n");
		out.append("  <!-- ====================== -->\n");
		// out.append(" <concurrent-block>\n");
		out.append("  <fork name=\"" + model.getName() + "\">\n");
		List transition = findTransitionBySource(cell);
		if (transition.isEmpty()) {
			throw new InvalidModelException("fork " + model.getName()
					+ " has no transitions!");
		}
		writeTransitonInfo(transition);
		out.append("  </fork>\n");
		StringBuffer cache = out;
		out = new StringBuffer();
		writeObjectsByTransitions(transition);
		// join
		if (joinCellStack.isEmpty()) {
			throw new InvalidModelException("can't find join!");
		}

		JoinCell joinCell = (JoinCell) joinCellStack.getFirst();
		joinCellStack.remove(joinCell);
		ForkJoinVO joinModel = joinCell.getModel();

		StringBuffer innerBlocks = out;
		out = cache;

		out.append("  <join name=\"" + joinModel.getName() + "\">\n");
		transition = findTransitionBySource(joinCell);
		if (transition.isEmpty()) {
			throw new InvalidModelException("join " + model.getName()
					+ " has no transitions!");
		}
		writeTransitonInfo(transition);
		out.append("  </join>\n");
		// cache.append(out);
		cache.append(innerBlocks);
		out = cache;
		// out.append(" </concurrent-block>\n");
		writeObjectsByTransitions(transition);
	}

	/**
	 * @param cell
	 */
	private void writeDecision(DecisionCell cell) throws InvalidModelException {
		DecisionVO model = cell.getModel();

		// out.append(" <decision name=\""+model.getName()+"\"
		// handler=\""+model.getHandler()+"\">\n");
		out.append("  <decision name=\"" + model.getName() + "\" >\n");
		/*
		 * Iterator parameterIt = model.getParameterList().iterator();
		 * while(parameterIt.hasNext()){ ParameterVO parameter =
		 * (ParameterVO)parameterIt.next(); out.append(" <parameter
		 * name=\""+parameter.getName()+"\" >"+parameter.getValue()+"</parameter>\n"); }
		 */
		out.append("  <delegation class=\"" + model.getHandler() + "\" />\n");
		List transition = findTransitionBySource(cell);
		if (transition.isEmpty()) {
			throw new InvalidModelException("Decision " + model.getName()
					+ " has no transitions!");
		}
		writeTransitonInfo(transition);
		out.append("  </decision>\n");
		writeObjectsByTransitions(transition);
	}

	/**
	 * @param cell
	 */
	private void writeActivityState(ActivityCell cell)
			throws InvalidModelException {
		ActivityStateVO activityModel = cell.getModel();

		String time = "0";
		FieldVO field = new FieldVO();
		field.setAttribute("@time");
		try {
			List fieldList = activityModel.getFieldList();
			field = (FieldVO) fieldList.get(fieldList.indexOf(field));
			time = field.getDescription();
		} catch (Exception e) {
		}

		out.append("  <state name=\"" + activityModel.getName() + "\" time=\""
				+ time + "\"  order=\"" + activityModel.getOrder() + "\">\n");
		out.append("    <description><![CDATA["
				+ RtfToHtml.convertRtfToHtml(activityModel.getDescription())
				+ "]]></description>\n");

		/*
		 * AssignmentVO assignment = activityModel.getAssignment(); if
		 * (assignment!=null){ out.append(" <assignment
		 * handler=\""+assignment.getHandler()+"\">\n"); Iterator parameterIt =
		 * assignment.getParameterList().iterator();
		 * 
		 * while(parameterIt.hasNext()){ ParameterVO parameter =
		 * (ParameterVO)parameterIt.next(); out.append(" <parameter
		 * name=\""+parameter.getName()+"\" >"+parameter.getValue()+"</parameter>\n"); }
		 * 
		 * out.append(" </assignment>\n"); }
		 */
		// out.append(" <role>"+activityModel.getRole()+"</role>\n");
		// out.append(" <assignment swimlane=\""+activityModel.getRole()+"\"
		// />\n");
		out.append("    " + generateAssignment(activityModel));

		// writeFieldInfo(activityModel.getFieldList());
		// writeActionInfo(activityModel.getActionList());
		List transition = findTransitionBySource(cell);
		if (transition.isEmpty()) {
			throw new InvalidModelException("ActivityState "
					+ activityModel.getName() + " has no transitions!");
		}
		writeTransitonInfo(transition);
		out.append("  </state>\n\n");
		writeObjectsByTransitions(transition);
	}

	private void writeSubProcess(SubProcessCell cell, boolean nc)
			throws InvalidModelException {
		SubProcessVO subProcessModel = cell.getModel();

		out.append("  <process-state name=\"" + subProcessModel.getName()
				+ "\"" + " order=\"" + subProcessModel.getOrder() + "\">\n");
		out.append("    <description><![CDATA["
				+ RtfToHtml.convertRtfToHtml(subProcessModel.getDescription())
				+ "]]></description>\n");

		out
				.append("    <delegation class=\"org.jbpm.delegation.processinvocation.DefaultProcessInvocationHandler\">"
						+ "<![CDATA[<document><sub-process-name>"
						+ subProcessModel.getSubProcess()
						+ "</sub-process-name></document>]]>"
						+ "</delegation>\n");

		// writeFieldInfo(activityModel.getFieldList());
		// writeActionInfo(activityModel.getActionList());
		List transition = new ArrayList();
		nc = false;
		if (!nc) {
			transition = findTransitionBySource(cell);
			if (transition.isEmpty()) {
				throw new InvalidModelException("SubProcess "
						+ subProcessModel.getName() + " has no transitions!");
			}
			writeTransitonInfo(transition);
		} else {
			// transition to end
			EndCell endCell = getEndCell();
			out.append("    <transition to=\"" + endCell.getModel().getName()
					+ "\"></transition>\n");
		}
		out.append("  </process-state>\n\n");
		writeObjectsByTransitions(transition);
	}

	/**
	 * @param activityModel
	 * @return
	 */
	private String generateAssignment(ActivityStateVO activityModel) {
		//String pattern = "@role.";
		if (activityModel.getRole() != null
				&& !activityModel.getRole().equals("")) {
			//return "<assignment swimlane=\"" + pattern + activityModel.getRole() + "\" />\n";
			return "<assignment swimlane=\"" + RulesID.generate(RulesID.ROLE, activityModel.getName()) + "\" />\n";
		} else {
			return "<assignment swimlane=\"requester\" />\n";
		}
	}

	private StartCell getStartCell() throws InvalidModelException {
		int max = getGraphModel().getRootCount();
		StartCell startCell = null;
		for (int i = 0; i < max; i++) {
			Object cell = getGraphModel().getRootAt(i);
			if (cell instanceof StartCell) {
				startCell = (StartCell) cell;
			}
		}
		if (startCell == null) {
			throw new InvalidModelException("Can’t find startstate!");
		}
		return startCell;
	}

	private StartCell writeStartModelInfo() throws InvalidModelException {
		StartCell startCell = getStartCell();
		StartStateVO startModel = startCell.getModel();
		ProcessDefinition processDefiniton = startModel.getProcessDefinition();

		/*
		 * out.append("<process-definition
		 * name=\""+processDefiniton.getName()+"\">\n"); // PROCESS DEFINITION
		 * PROPERTIES out.append(" <!-- ===================================
		 * -->\n"); out.append(" <!-- == PROCESS DEFINITION PROPERTIES ==
		 * -->\n"); out.append(" <!-- ===================================
		 * -->\n"); //out.append(" <name>"+processDefiniton.getName()+"</name>\n");
		 * out.append(" <description>"+processDefiniton.getDescription()+"</description>\n");
		 * out.append(" <swimlane name=\"requester\" />\n");
		 */
		// out.append("
		// <responsible>"+processDefiniton.getResponsible()+"</responsible>\n\n");
		String swimlane = "requester";
		if (startModel.getRole() != null && !startModel.getRole().equals("")) {
			//swimlane = "@role." + startModel.getRole();
			swimlane = RulesID.generate(RulesID.ROLE, startModel.getName());
		}
		out.append("  <!-- ====================== -->\n");
		out.append("  <!-- ===== STARTSTATE ===== -->\n");
		out.append("  <!-- ====================== -->\n");
		out.append("  <start-state name=\"" + startModel.getName()
				+ "\" swimlane=\"" + swimlane + "\">\n");
		out.append("    <description>" + startModel.getDescription()
				+ "</description>\n");
		// out.append(" <role>"+startModel.getRole()+"</role>\n");
		// writeFieldInfo(startModel.getFieldList());
		// writeActionInfo(startModel.getActionList());

		List transition = findTransitionBySource(startCell);
		if (transition.isEmpty()) {
			throw new InvalidModelException("No transition from startstate!");
		}
		writeTransitonInfo(transition);

		out.append("  </start-state>\n\n");

		// out.append(" <end-state name=\""+endCell.getModel().getName()+"\"
		// />\n\n");

		/*
		 * out.append(" <!-- ================ -->\n"); out.append(" <!-- ==
		 * ATTRIBUTES == -->\n"); out.append(" <!-- ================ -->\n");
		 * writeVariableDef(processDefiniton);
		 */
		return startCell;
	}

	private EndCell writeEndModelInfo() throws InvalidModelException {
		int max = getGraphModel().getRootCount();
		EndCell endCell = null;
		for (int i = 0; i < max; i++) {
			Object cell = getGraphModel().getRootAt(i);
			if (cell instanceof EndCell) {
				endCell = (EndCell) cell;
			}
		}
		out.append("  <!-- ====================== -->\n");
		out.append("  <!-- ======= ENDSTATE ===== -->\n");
		out.append("  <!-- ====================== -->\n");
		out.append("  <end-state name=\"" + endCell.getModel().getName()
				+ "\" />\n\n");
		return endCell;
	}

	/**
	 * @param list
	 */
	private void writeFieldInfo(List list) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			FieldVO field = (FieldVO) it.next();
			out.append("    <field attribute=\"" + field.getAttribute()
					+ "\" access=\"" + field.getAccess().trim() + "\" />\n");
		}

	}


	private void writeTransitonInfo(List transitionList)
			throws InvalidModelException {

		Iterator it = transitionList.iterator();
		while (it.hasNext()) {
			Transition transition = (Transition) it.next();
			Object targetObject = getGraphModel().getParent(
					getGraphModel().getTarget(transition));
			String targetName = getCellName(targetObject);
			if (targetName == null) {
				throw new InvalidModelException(
						"transition need a target of type activitystate, decision, end, join or fork!");
			}

			TransitionVO transitionVo = transition.getModel();
			String name = transitionVo.getName();
			if (name == null || name.length() == 0) {
				out.append("    <transition to=\"" + targetName + "\">\n");
			} else {
				out.append("    <transition name=\"" + transitionVo.getName()
						+ "\" to=\"" + targetName + "\">\n");
			}

			writeActionInfo(transition.getModel().getActionList());
			out.append("    </transition>\n");
		}
	}

	private void writeActionInfo(List actionList) {
		Iterator actionIt = actionList.iterator();
		while (actionIt.hasNext()) {
			ActionVO action = (ActionVO) actionIt.next();
			out.append("      <action event=\"" + action.getEvent()
					+ "\" handler=\"" + action.getHandler()
					+ "\" on-exception=\"" + action.getOnException() + "\">\n");
			Iterator parameterIt = action.getParameterList().iterator();
			while (parameterIt.hasNext()) {
				ParameterVO parameter = (ParameterVO) parameterIt.next();
				out.append("        <parameter name=\"" + parameter.getName()
						+ "\">" + parameter.getValue() + "</parameter>\n");
			}
			out.append("      </action>\n");
		}
	}

	/**
	 * @param processDefiniton
	 */
	private void writeVariableDef(ProcessDefinition processDefiniton) {
		Iterator it = processDefiniton.getAttributeList().iterator();
		while (it.hasNext()) {
			AttributeVO attribute = (AttributeVO) it.next();
			// gravando no formato HTML
			if (attribute.getName().equals("@objective")
					|| attribute.getName().equals("@application")) {
				out.append("  <swimlane name=\"" + attribute.getName()
						+ "\"> \n");
				out.append("    <description><![CDATA["
						+ RtfToHtml.convertRtfToHtml(attribute
								.getInitialValue()) + "]]></description> \n");
				out.append("  </swimlane> \n");
			} else if (attribute.getName().equals("@code")) {
				out.append("  <swimlane name=\"" + attribute.getName()
						+ "\"> \n");
				out.append("    <description><![CDATA["
						+ attribute.getInitialValue() + "]]></description> \n");
				out.append("  </swimlane> \n");
			}
		}
	}

	/**
	 * @param processDefiniton
	 *            private void writeFieldDef(ProcessDefinition processDefiniton) {
	 *            int max = getGraphModel().getRootCount(); for ( int i=0 ; i<max ;
	 *            i++ ) { Object cell = getGraphModel().getRootAt(i); if ( cell
	 *            instanceof ActivityCell ) { Iterator it =
	 *            ((ActivityCell)cell).getModel().getFieldList().iterator();
	 *            while (it.hasNext()){ FieldVO field = (FieldVO)it.next();
	 *            out.append(" <attribute name=\""+field.getName()+"\" "); if
	 *            (field.getType()!=null && attribute.getType().length()>0){
	 *            out.append(" type=\""+attribute.getType()+"\" "); } if
	 *            (attribute.getSerializer()!=null &&
	 *            attribute.getSerializer().length()>0){ out.append("
	 *            serializer=\""+attribute.getSerializer()+"\" "); } if
	 *            (attribute.getInitialValue()!=null &&
	 *            attribute.getInitialValue().length()>0){ out.append("
	 *            initial-value=\""+attribute.getInitialValue()+"\" "); }
	 *            out.append("/>\n"); } } } }
	 */

	/**
	 * @see org.jbpm.gpd.io.GraphModelFileFormat#read(java.net.URL,
	 *      java.util.Hashtable, org.jgraph.JGraph)
	 */
	public GraphModel read(URL file, Hashtable properties, GpdGraph gpGraph)
			throws Exception {
		SAXBuilder builder = new SAXBuilder();
		builder
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);
		Document doc = builder.build(file);

		init(gpGraph);

		createStartEnd(gpGraph, doc);
		createActivityState(gpGraph, doc);
		createForkJoin(gpGraph, doc);
		createDecision(gpGraph, doc);
		createTransitions(gpGraph, doc);
		return graph.getModel();
	}

	/**
	 * @param gpGraph
	 * @param doc
	 */
	private void createDecision(GpdGraph gpGraph, Document doc)
			throws JDOMException {
		XPath forkPath = XPath.newInstance("/process-definition//decision");
		List forkList = forkPath.selectNodes(doc);
		Iterator it = forkList.iterator();
		while (it.hasNext()) {
			Element decision = (Element) it.next();
			DecisionCell cell = new DecisionCell();
			DecisionVO model = cell.getModel();
			model.setName(decision.getAttribute("name").getValue());
			Attribute handler = decision.getAttribute("handler");
			if (handler != null) {
				model.setHandler(handler.getValue());
			}
			model.setParameterList(createParameters(decision));

			insert(new Point(10, 10), cell, new Dimension(25, 25));
		}

	}

	/**
	 * @param gpGraph
	 * @param doc
	 */
	private void createTransitions(GpdGraph gpGraph, Document doc)
			throws JDOMException, InvalidModelException {
		XPath transitionPath = XPath
				.newInstance("/process-definition//transition");
		List transitionList = transitionPath.selectNodes(doc);
		Iterator it = transitionList.iterator();
		while (it.hasNext()) {
			Element transition = (Element) it.next();
			String to = transition.getAttribute("to").getValue();
			String from = ((Element) transition.getParent()).getAttribute(
					"name").getValue();
			createTransition(from, to, transition);
		}

	}

	/**
	 * @param from
	 * @param to
	 */
	private void createTransition(String from, String to, Element transition)
			throws InvalidModelException {
		DefaultGpdCell fromCell = getCellByName(from);
		DefaultGpdCell toCell = getCellByName(to);
		((GpdMarqueeHandler) graph.getMarqueeHandler()).connect((Port) fromCell
				.getChildAt(0), (Port) toCell.getChildAt(0));
		Transition transiton = (Transition) findConnection(fromCell, toCell);
		TransitionVO model = new TransitionVO();
		transiton.setModel(model);
		createTransitonDetail(model, transition);
	}

	/**
	 * @param model
	 * @param transition
	 */
	private void createTransitonDetail(TransitionVO model, Element transition) {
		String name = transition.getAttributeValue("name");
		model.setName(name);
		model.setActionList(createActions(transition));
	}

	/**
	 * @param gpGraph
	 * @param doc
	 */
	private void createForkJoin(GpdGraph gpGraph, Document doc)
			throws JDOMException {
		XPath forkPath = XPath.newInstance("/process-definition//fork");
		List forkList = forkPath.selectNodes(doc);
		Iterator it = forkList.iterator();
		if (it.hasNext()) {
			Element fork = (Element) it.next();
			ForkCell cell = new ForkCell();
			cell.getModel().setName(fork.getAttribute("name").getValue());
			insert(new Point(10, 10), cell, new Dimension(150, 25));
		}

		XPath joinPath = XPath.newInstance("/process-definition//join");
		List joinList = joinPath.selectNodes(doc);
		it = joinList.iterator();
		if (it.hasNext()) {
			Element end = (Element) it.next();
			JoinCell cell = new JoinCell();
			cell.getModel().setName(end.getAttribute("name").getValue());
			insert(new Point(10, 10), cell, new Dimension(150, 25));
		}
	}

	/**
	 * @param gpGraph
	 * @param doc
	 */
	private void createStartEnd(GpdGraph gpGraph, Document doc)
			throws JDOMException {
		XPath startstatePath = XPath
				.newInstance("/process-definition/start-state");
		List activityStateList = startstatePath.selectNodes(doc);
		Iterator it = activityStateList.iterator();
		if (it.hasNext()) {
			Element startState = (Element) it.next();
			StartCell cell = new StartCell();
			StartStateVO model = cell.getModel();
			createStartState(model, startState);
			createProcessDefinition(model.getProcessDefinition(), doc);
			insert(new Point(10, 10), cell, null);
		}

		XPath endPath = XPath.newInstance("/process-definition/end-state");
		List endList = endPath.selectNodes(doc);
		it = endList.iterator();
		if (it.hasNext()) {
			Element endState = (Element) it.next();
			EndCell cell = new EndCell();
			cell.getModel().setName(endState.getAttribute("name").getValue());
			insert(new Point(10, 10), cell, null);
		}
	}

	/**
	 * @param model
	 * @param doc
	 */
	private void createProcessDefinition(ProcessDefinition definition,
			Document doc) throws JDOMException {
		XPath processdefinitonPath = XPath.newInstance("/process-definition");
		List processdefinitonList = processdefinitonPath.selectNodes(doc);
		Iterator it = processdefinitonList.iterator();
		if (it.hasNext()) {
			Element processdefinition = (Element) it.next();
			Element name = processdefinition.getChild("name");
			if (name != null) {
				definition.setName(name.getValue());
			}
			Element description = processdefinition.getChild("description");
			if (description != null) {
				definition.setDescription(description.getValue());
			}
			Element responsible = processdefinition.getChild("responsible");
			if (responsible != null) {
				definition.setResponsible(responsible.getValue());
			}
			definition.setAttributeList(createAttributes(processdefinition));
		}
	}

	/**
	 * @param processdefinition
	 */
	private List createAttributes(Element processdefinition) {
		List resultList = new LinkedList();
		List attributelist = processdefinition.getChildren("attribute");
		Iterator it = attributelist.iterator();
		while (it.hasNext()) {
			Element attribute = (Element) it.next();
			createAttributeDetail(attribute, resultList);
		}
		return resultList;
	}

	/**
	 * @param attribute
	 * @param resultList
	 */
	private void createAttributeDetail(Element attribute, List resultList) {
		AttributeVO attributeVo = new AttributeVO();
		Attribute name = attribute.getAttribute("name");
		if (name != null) {
			attributeVo.setName(name.getValue());
		}
		Attribute type = attribute.getAttribute("type");
		if (type != null) {
			attributeVo.setType(type.getValue());
		}
		Attribute serializer = attribute.getAttribute("serializer");
		if (serializer != null) {
			attributeVo.setSerializer(serializer.getValue());
		}
		Attribute initialvalue = attribute.getAttribute("initial-value");
		if (initialvalue != null) {
			attributeVo.setType(initialvalue.getValue());
		}
		resultList.add(attributeVo);
	}

	/**
	 * @param model
	 * @param startState
	 */
	private void createStartState(StartStateVO model, Element startState) {
		model.setName(startState.getAttribute("name").getValue());
		Element description = startState.getChild("description");
		if (description != null) {
			model.setDescription(description.getValue());
		}

		Element role = startState.getChild("role");
		if (role != null) {
			model.setRole(role.getValue());
		}

	}

	/**
	 * @param gpGraph
	 * @param doc
	 */
	private void createActivityState(GpdGraph gpGraph, Document doc)
			throws JDOMException, InvalidModelException {
		XPath activityPath = XPath
				.newInstance("/process-definition//activity-state");
		List activityStateList = activityPath.selectNodes(doc);
		Iterator it = activityStateList.iterator();
		while (it.hasNext()) {
			Element activityState = (Element) it.next();
			createActivityStateDetail(activityState);
		}
	}

	private void createActivityStateDetail(Element activityState)
			throws InvalidModelException {
		String name = activityState.getAttribute("name").getValue();
		ActivityCell cell = (ActivityCell) getCellByName(name);
		if (cell == null) {
			cell = new ActivityCell();
			cell.getModel().setName(
					activityState.getAttribute("name").getValue());
			insert(new Point(10, 10), cell, new Dimension(150, 25));
		}
		ActivityStateVO model = cell.getModel();

		Element description = activityState.getChild("description");
		if (description != null) {
			model.setDescription(description.getValue());
		}

		Element role = activityState.getChild("role");
		if (role != null) {
			model.setRole(role.getValue());
		}

		Element assignment = activityState.getChild("assignment");
		if (assignment != null) {
			model.setAssignment(createAssignmentDetail(assignment));
		}

		model.setFieldList(createFields(activityState));
		model.setActionList(createActions(activityState));
	}

	/**
	 * @param action
	 */
	private List createActions(Element action) {
		List resultList = new LinkedList();
		List actionlist = action.getChildren("action");
		Iterator it = actionlist.iterator();
		while (it.hasNext()) {
			Element acition = (Element) it.next();
			createActionDetail(acition, resultList);
		}
		return resultList;
	}

	/**
	 * @param acition
	 * @param resultList
	 */
	private void createActionDetail(Element action, List resultList) {
		ActionVO actionVo = new ActionVO();
		Attribute event = action.getAttribute("event");
		if (event != null) {
			actionVo.setEvent(event.getValue());
		}

		Attribute handler = action.getAttribute("handler");
		if (handler != null) {
			actionVo.setHandler(handler.getValue());
		}

		Attribute onexception = action.getAttribute("on-exception");
		if (onexception != null) {
			actionVo.setOnException(onexception.getValue());
		}
		actionVo.setParameterList(createParameters(action));
		resultList.add(actionVo);
	}

	/**
	 * @param field
	 */
	private List createFields(Element action) {
		List resultList = new LinkedList();
		List fieldlist = action.getChildren("field");
		Iterator it = fieldlist.iterator();
		while (it.hasNext()) {
			Element field = (Element) it.next();
			createFieldDetail(field, resultList);
		}
		return resultList;

	}

	/**
	 * @param acition
	 * @param resultList
	 */
	private void createFieldDetail(Element field, List resultList) {
		FieldVO fieldVo = new FieldVO();
		Attribute attribute = field.getAttribute("attribute");
		fieldVo.setAttribute(attribute.getValue());
		Attribute access = field.getAttribute("access");
		fieldVo.setAccess(access.getValue());

		resultList.add(fieldVo);
	}

	/**
	 * @param assignment
	 */
	private AssignmentVO createAssignmentDetail(Element assignment) {
		AssignmentVO assignmentVo = new AssignmentVO();
		Attribute handler = assignment.getAttribute("handler");
		assignmentVo.setHandler(handler.getValue());
		assignmentVo.setParameterList(createParameters(assignment));

		return assignmentVo;
	}

}
