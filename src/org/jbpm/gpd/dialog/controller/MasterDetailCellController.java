package org.jbpm.gpd.dialog.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.jbpm.gpd.Configuration;
import org.jbpm.gpd.ProcessDesigner;
import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.ActivityDecisionCell;
import org.jbpm.gpd.cell.DecisionCell;
import org.jbpm.gpd.cell.EndCell;
import org.jbpm.gpd.cell.ForkCell;
import org.jbpm.gpd.cell.JoinCell;
import org.jbpm.gpd.cell.StartCell;
import org.jbpm.gpd.cell.SubProcessCell;
import org.jbpm.gpd.cell.Transition;
import org.jbpm.gpd.dialog.panel.AbstractPropertyPanel;
import org.jbpm.gpd.dialog.panel.ActivityPropertyPanel;
import org.jbpm.gpd.dialog.panel.DecisionPropertyPanel;
import org.jbpm.gpd.dialog.panel.EndPropertyPanel;
import org.jbpm.gpd.dialog.panel.NamePanel;
import org.jbpm.gpd.dialog.panel.StartStatePropertyPanel;
import org.jbpm.gpd.dialog.panel.SubProcessPropertyPanel;
import org.jbpm.gpd.dialog.panel.TransitionPropertyPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.ActionPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.AssignmentPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.AttributePanel;
import org.jbpm.gpd.dialog.panel.detailpanel.DecisionPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.FieldPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.FormatterPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.ParameterPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.ProcessPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.StatePanel;
import org.jbpm.gpd.dialog.panel.detailpanel.SubProcessPanel;
import org.jbpm.gpd.dialog.panel.detailpanel.TransitionPanel;
import org.jbpm.gpd.dialog.valuedialog.AccessDialog;
import org.jbpm.gpd.dialog.valuedialog.ClassInspectorDialog;
import org.jbpm.gpd.dialog.valuedialog.EventDialog;
import org.jbpm.gpd.dialog.valuedialog.OnExceptionDialog;
import org.jbpm.gpd.dialog.valuedialog.TypeDialog;
import org.jbpm.gpd.dialog.valuedialog.editor.EditorDialog;
import org.jbpm.gpd.model.ActionVO;
import org.jbpm.gpd.model.ActivityStateVO;
import org.jbpm.gpd.model.AssignmentVO;
import org.jbpm.gpd.model.AttributeVO;
import org.jbpm.gpd.model.DecisionVO;
import org.jbpm.gpd.model.FieldVO;
import org.jbpm.gpd.model.FormatterVO;
import org.jbpm.gpd.model.ParameterVO;
import org.jbpm.gpd.model.ProcessDefinition;
import org.jbpm.gpd.model.StartStateVO;
import org.jbpm.gpd.model.StateVO;
import org.jbpm.gpd.model.SubProcessVO;
import org.jbpm.gpd.model.TransitionVO;
import org.jbpm.gpd.renderer.ActivityRenderer;

import eflow.gpd.action.ImportEflowAction;
import eflow.model.definition.impl.ProcessTypeImpl;
import eflow.model.organisation.impl.RoleImpl;
import eflow.rtf.RtfEditorFactory;

public class MasterDetailCellController extends AbstractController implements
		TreeSelectionListener, MouseListener, ActionListener {

	public final String ACTIONS_NODE = "actions";

	public final String FIELDS_NODE = "fields";

	private JPanel view;

	private DefaultMutableTreeNode lastSelection = null;

	private ProcessDesigner processDesigner;


	/**
	 * @see org.jbpm.gpd.dialog.controller.Controller#getView()
	 */
	public JPanel getView() {
		if (view == null) {
			if ((getModel() instanceof ActivityCell)
					|| (getModel() instanceof ActivityDecisionCell)) {
				view = new ActivityPropertyPanel(this);
			} else if (getModel() instanceof StartCell) {
				view = new StartStatePropertyPanel(this);
			} else if (getModel() instanceof Transition) {
				view = new TransitionPropertyPanel(this);
			} else if (getModel() instanceof DecisionCell) {
				view = new DecisionPropertyPanel(this);
			} else if (getModel() instanceof EndCell) {
				view = new NamePanel(this);
				//view = new EndPropertyPanel(this);
			} else if (getModel() instanceof ForkCell) {
				view = new NamePanel(this);
			} else if (getModel() instanceof JoinCell) {
				view = new NamePanel(this);
			} else if (getModel() instanceof SubProcessCell) {
				view = new SubProcessPropertyPanel(this);
			} else {
				view = new JPanel();
			}
		}
		return view;
	}

	public void setModel(Object cell) {
		super.setModel(cell);
		view = null;
	}

	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		if (getView() instanceof AbstractPropertyPanel) {
			AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) view.propertytree
					.getLastSelectedPathComponent();

			if (node != null && node.getUserObject() != null) {
				Object model = node.getUserObject();
				activateDetailPanel(view, model);
				nodeChanged(node);
				lastSelection = node;
			}
		}
	}

	public void activateDetailPanel(AbstractPropertyPanel view, Object model) {
		if (model instanceof AttributeVO) {
			AttributeVO attribute = (AttributeVO) model;
			// create detailpanel
			AttributePanel subPanel = new AttributePanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(attribute.getName());
			subPanel.typeTextField.setText(attribute.getType());
			subPanel.initialValueTextField.setText(attribute.getInitialValue());
			subPanel.serializerTextField.setText(attribute.getSerializer());

			view.setDetail(subPanel);
		} else if (model instanceof SubProcessVO) {
			SubProcessVO subProcess = (SubProcessVO) model;
			// create detailpanel
			SubProcessPanel subPanel = new SubProcessPanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(subProcess.getName());
			subPanel.descriptionSubProcessTextField.setText(subProcess
					.getDescription());
			subPanel.subProcessTextField.setText(subProcess.getSubProcess());
			subPanel.orderTextField.setText(subProcess.getOrder() + "");
			view.setDetail(subPanel);
		} else if (model instanceof StartStateVO) {
			StartStateVO startstate = (StartStateVO) model;
			// create detailpanel
			StatePanel subPanel = new StatePanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(startstate.getName());
			subPanel.descriptionTextField.setText(startstate.getDescription());
			subPanel.roleTextField.setText(startstate.getRole());
			view.setDetail(subPanel);
		} else if (model instanceof ActivityStateVO) {
			ActivityStateVO startstate = (ActivityStateVO) model;
			// create detailpanel
			StatePanel subPanel = new StatePanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(startstate.getName());
			subPanel.descriptionTextField.setText(startstate.getDescription());
			subPanel.roleTextField.setText(startstate.getRole());
			subPanel.orderTextField.setText(String.valueOf(startstate
					.getOrder()));
			subPanel.ncTextField.setText(String.valueOf(startstate.getNc()));

			FieldVO field = new FieldVO();
			field.setAttribute("@time");
			try {
				List fieldList = startstate.getFieldList();
				field = (FieldVO) fieldList.get(fieldList.indexOf(field));
				subPanel.timeTextField.setText(field.getDescription());
			} catch (Exception e) {
			}

			/*
			 * pegar a lista de roles do servidor
			 */
			try {
				List roleList = ImportEflowAction.roleList;
				if (roleList != null) {
					Iterator it = roleList.iterator();
					Vector roles = new Vector();

					while (it.hasNext()) {
						RoleImpl role = (RoleImpl) it.next();
						roles.add(role);
						subPanel.roleList.addItem(role);
					}
					try {
						Long idRole = idRole = new Long(startstate.getRole());
						RoleImpl r = new RoleImpl();
						r.setId(idRole);
						subPanel.roleList.setSelectedItem(roles.get(roles
								.indexOf(r)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// para aparecer o nome da role antiga, mesmo que nao tenha
					// se conectado ao servidor
					Vector roles = new Vector();
					RoleImpl role = new RoleImpl();
					role.setId(new Long(startstate.getRole()));
					role.setName(startstate.getRoleName());
					roles.add(role);
					subPanel.roleList.addItem(role);
					subPanel.roleList.setSelectedItem(roles.get(roles
							.indexOf(role)));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			view.setDetail(subPanel);
		} else if (model instanceof ActionVO) {
			ActionVO action = (ActionVO) model;
			// create detailpanel
			ActionPanel subPanel = new ActionPanel(this);
			// copy values to panel
			subPanel.eventTextField.setText(action.getEvent());
			subPanel.handlerTextField.setText(action.getHandler());
			subPanel.onExceptionTextField.setText(action.getOnException());

			view.setDetail(subPanel);
		} else if (model instanceof ParameterVO) {
			ParameterVO parameter = (ParameterVO) model;
			// create detailpanel
			ParameterPanel subPanel = new ParameterPanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(parameter.getName());
			subPanel.valueTextField.setText(parameter.getValue());

			view.setDetail(subPanel);
		} else if (model instanceof FieldVO) {
			FieldVO parameter = (FieldVO) model;
			// create detailpanel
			FieldPanel subPanel = new FieldPanel(this);
			// copy values to panel
			subPanel.accessTextField.setText(parameter.getAccess());
			subPanel.attributeTextField.setText(parameter.getAttribute());
			subPanel.descriptionTextField.setText(parameter.getDescription());
			subPanel.nameTextField.setText(parameter.getName());

			view.setDetail(subPanel);
		} else if (model instanceof TransitionVO) {
			TransitionVO transition = (TransitionVO) model;
			// create detailpanel
			TransitionPanel subPanel = new TransitionPanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(transition.getName());

			view.setDetail(subPanel);
		} else if (model instanceof DecisionVO) {
			DecisionVO decision = (DecisionVO) model;
			// create detailpanel
			DecisionPanel subPanel = new DecisionPanel(this);
			// copy values to panel
			subPanel.nameTextField.setText(decision.getName());
			subPanel.handlerTextField.setText(decision.getHandler());

			view.setDetail(subPanel);
		} else if (model instanceof AssignmentVO) {
			AssignmentVO assignment = (AssignmentVO) model;
			// create detailpanel
			AssignmentPanel subPanel = new AssignmentPanel(this);
			// copy values to panel
			subPanel.assignmentTextField.setText(assignment.getHandler());

			view.setDetail(subPanel);
		} else if (model instanceof ProcessDefinition) {
			ProcessDefinition definition = (ProcessDefinition) model;
			// create detailpanel
			ProcessPanel subPanel = new ProcessPanel(this);
			
			// copy values to panel
			//System.out.println("Casa");
			subPanel.descriptionTextField.setText(definition.getDescription());
			subPanel.nameTextField.setText(definition.getName());

			AttributeVO attribute = new AttributeVO();
			attribute.setName("@objective");
			try {
				List attributeList = definition.getAttributeList();
				attribute = (AttributeVO) attributeList.get(attributeList
						.indexOf(attribute));
				subPanel.objectiveTextField
						.setText(attribute.getInitialValue());
			} catch (Exception e) {
			}

			attribute = new AttributeVO();
			attribute.setName("@application");
			try {
				List attributeList = definition.getAttributeList();
				attribute = (AttributeVO) attributeList.get(attributeList
						.indexOf(attribute));
				subPanel.applicationTextField.setText(attribute
						.getInitialValue());
			} catch (Exception e) {
			}

			attribute = new AttributeVO();
			attribute.setName("@code");
			try {
				List attributeList = definition.getAttributeList();
				attribute = (AttributeVO) attributeList.get(attributeList
						.indexOf(attribute));
				subPanel.codeTextField.setText(attribute.getInitialValue());
			} catch (Exception e) {
			}

			
			//pegar a lista de roles do servidor
			
			Vector processTypeVector = new Vector();
			try {
				List processTypeList = ImportEflowAction.processTypeList;
				Iterator it = processTypeList.iterator();

				while (it.hasNext()) {
					ProcessTypeImpl processType = (ProcessTypeImpl) it.next();
					processTypeVector.add(processType);
					subPanel.processTypeList.addItem(processType);
				}
			} catch (Exception e) {
			}

			attribute = new AttributeVO();
			attribute.setName("@processtype");
			try {
				List attributeList = definition.getAttributeList();
				attribute = (AttributeVO) attributeList.get(attributeList
						.indexOf(attribute));
				subPanel.processTypeTextField.setText(attribute
						.getInitialValue());

				Long id = new Long(attribute.getInitialValue());
				ProcessTypeImpl p = new ProcessTypeImpl();
				p.setId(id);
				subPanel.processTypeList.setSelectedItem(processTypeVector
						.get(processTypeVector.indexOf(p)));
			} catch (Exception e) {
			}
			
			view.setDetail(subPanel);
			
		} else if (model instanceof FormatterVO) {
			FormatterVO formatter = (FormatterVO) model;
			// create detailpanel
			FormatterPanel subPanel = new FormatterPanel(this);
			// copy values to panel
			subPanel.formatterclassTextField.setText(formatter
					.getFormaterclass());

			view.setDetail(subPanel);
		}
	}

	// ------------------------------------
	// save Changes begin
	// ------------------------------------
	public void saveTransitionDataButton_actionPerformed(ActionEvent e) {
		if (lastSelection.getUserObject() instanceof TransitionVO) {
			AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
			TransitionVO selection = (TransitionVO) lastSelection
					.getUserObject();
			TransitionPanel detailPanel = ((TransitionPanel) view.getDetail());
			selection.setName(detailPanel.nameTextField.getText());
		}
	}
	
	/**
	 * Marcador
	 * @param e
	 */
	public void saveStateDataButton_actionPerformed(ActionEvent e) {
		
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		if (lastSelection.getUserObject() instanceof ActivityStateVO) {
			ActivityStateVO selection = (ActivityStateVO) lastSelection
					.getUserObject();
			StatePanel detailPanel = ((StatePanel) view.getDetail());
			selection.setName(detailPanel.nameTextField.getText());
			selection
			.setDescription(detailPanel.descriptionTextField.getText());

			// s� permitir modificar a role se tiver conectado no eflow
			if (ImportEflowAction.CONECTADO) {
				selection.setRole(detailPanel.roleTextField.getText());
				if (selection.getRole().equals("")
						|| detailPanel.roleList.getItemCount() == 0) {
					selection.setRole("0");
				} else {
					try {
						// atributo usado para manter o roleName
						RoleImpl r = new RoleImpl();
						r.setId(new Long(selection.getRole()));
						List roleList = ImportEflowAction.roleList;
						String roleView = "";
						RoleImpl b = (RoleImpl) roleList.get(roleList
								.indexOf(r));
						roleView = b.getName();
						selection.setRoleName(roleView);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			selection.setTime(new Long(detailPanel.timeTextField.getText())
					.longValue());
			selection.setOrder(Integer.parseInt(detailPanel.orderTextField
					.getText()));
			selection.setNc(detailPanel.ncTextField.getText());
		}
		if (lastSelection.getUserObject() instanceof StartStateVO) {
			StartStateVO selection = (StartStateVO) lastSelection
					.getUserObject();
			StatePanel detailPanel = ((StatePanel) view.getDetail());
			selection.setName(detailPanel.nameTextField.getText());
			selection
					.setDescription(detailPanel.descriptionTextField.getText());
			selection.setRole(detailPanel.roleTextField.getText());
		}
		
		if (lastSelection.getUserObject() instanceof SubProcessVO) {
			this.saveSubProcessDataButton_actionPerformed(e);
		}
			
	}
	
	/**
	 * M�todo que dispara a a��o para salvar as propriedades do SubProcesso
	 * @param e
	 */
	public void saveSubProcessDataButton_actionPerformed(ActionEvent e) {
		
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		if (lastSelection.getUserObject() instanceof SubProcessVO) {
			SubProcessVO selection = (SubProcessVO) lastSelection
					.getUserObject();
			SubProcessPanel detailPanel = ((SubProcessPanel) view.getDetail());
			selection.setName(detailPanel.nameTextField.getText());
			selection.setDescription(detailPanel.descriptionSubProcessTextField
					.getText());
			selection.setSubProcess(detailPanel.subProcessTextField.getText());
			selection.setOrder(Integer.parseInt(detailPanel.orderTextField
					.getText()));
		}
	}
	
	/**
	 * M�todo que dispara a a��o para salvar as propriedades do Processo. 
	 * @param e
	 */
	public void saveProcessDataButton_actionPerformed(ActionEvent e) {
		
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ProcessPanel detailPanel = ((ProcessPanel) view.getDetail());
		ProcessDefinition definition = (ProcessDefinition) lastSelection
				.getUserObject();
		definition.setDescription(detailPanel.descriptionTextField.getText());
		definition.setName(detailPanel.nameTextField.getText());

		AttributeVO attribute = new AttributeVO();
		attribute.setName("@objective");
		attribute.setInitialValue(detailPanel.objectiveTextField.getText());
		definition.addAttribute(attribute);

		attribute = new AttributeVO();
		attribute.setName("@application");
		attribute.setInitialValue(detailPanel.applicationTextField.getText());
		definition.addAttribute(attribute);

		if (ImportEflowAction.CONECTADO) {
			attribute = new AttributeVO();
			attribute.setName("@processtype");
			attribute.setInitialValue(detailPanel.processTypeTextField
					.getText());
			if (attribute.getInitialValue().equals("")
					|| detailPanel.processTypeList.getItemCount() == 0) {
				attribute.setInitialValue("0");
			}
			definition.addAttribute(attribute);
		}

		attribute = new AttributeVO();
		attribute.setName("@code");
		attribute.setInitialValue(detailPanel.codeTextField.getText());
		definition.addAttribute(attribute);
	}

	public void saveParameterDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ParameterVO selection = (ParameterVO) lastSelection.getUserObject();
		ParameterPanel detailPanel = ((ParameterPanel) view.getDetail());
		selection.setName(detailPanel.nameTextField.getText());
		selection.setValue(detailPanel.valueTextField.getText());
	}

	public void saveFormatterDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		FormatterVO formatter = (FormatterVO) lastSelection.getUserObject();
		FormatterPanel detailPanel = ((FormatterPanel) view.getDetail());
		formatter.setFormaterclass(detailPanel.formatterclassTextField
				.getText());
	}

	public void saveFieldDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		FieldVO selection = (FieldVO) lastSelection.getUserObject();
		FieldPanel detailPanel = ((FieldPanel) view.getDetail());
		selection.setAccess(detailPanel.accessTextField.getText());
		selection.setAttribute(detailPanel.attributeTextField.getText());
		selection.setDescription(detailPanel.descriptionTextField.getText());
		selection.setName(detailPanel.nameTextField.getText());
	}

	public void saveDesicionDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		DecisionVO decision = (DecisionVO) lastSelection.getUserObject();
		DecisionPanel detailPanel = ((DecisionPanel) view.getDetail());
		decision.setName(detailPanel.nameTextField.getText());
		decision.setHandler(detailPanel.handlerTextField.getText());
	}

	public void saveAttributeDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		AttributeVO selection = (AttributeVO) lastSelection.getUserObject();
		AttributePanel detailPanel = ((AttributePanel) view.getDetail());
		selection.setName(detailPanel.nameTextField.getText());
		selection.setType(detailPanel.typeTextField.getText());
		selection.setInitialValue(detailPanel.initialValueTextField.getText());
		selection.setSerializer(detailPanel.serializerTextField.getText());
	}

	public void saveAssignmentDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		AssignmentVO assignment = (AssignmentVO) lastSelection.getUserObject();
		AssignmentPanel detailPanel = ((AssignmentPanel) view.getDetail());
		assignment.setHandler(detailPanel.assignmentTextField.getText());
	}

	public void saveActionDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ActionVO selection = (ActionVO) lastSelection.getUserObject();
		ActionPanel detailPanel = ((ActionPanel) view.getDetail());
		selection.setEvent(detailPanel.eventTextField.getText());
		selection.setHandler(detailPanel.handlerTextField.getText());
		selection.setOnException(detailPanel.onExceptionTextField.getText());
	}
	
	/**
	 * M�todo que dispara a a��o que salva dados dos paineis b�sicos.
	 * @param e
	 */
	public void saveNamePanelDataButton_actionPerformed(ActionEvent e) {
		Object model = getModel();
		String name = ((NamePanel) getView()).nameTextField.getText();
		if (model instanceof EndCell) {
			((EndCell) model).getModel().setName(name);
		} else if (model instanceof ForkCell) {
			((ForkCell) model).getModel().setName(name);
		} else if (model instanceof JoinCell) {
			((JoinCell) model).getModel().setName(name);
		}
	}

	public void objectiveDataButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ProcessDefinition definition = (ProcessDefinition) lastSelection
				.getUserObject();
		ProcessPanel detailPanel = ((ProcessPanel) view.getDetail());

		// Stylepad stylepad = new Stylepad(detailPanel.objectiveTextField);
		// stylepad.open("OBJETIVO");
		RtfEditorFactory.getInstance().createEditor("Objetivo",
				detailPanel.objectiveTextField, this);
	}

	public void descriptionDataButton_actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(null, "EM DESENVOLVIMENTO");
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ProcessDefinition definition = (ProcessDefinition) lastSelection
				.getUserObject();
		ProcessPanel detailPanel = ((ProcessPanel) view.getDetail());

		// Stylepad stylepad = new Stylepad(detailPanel.descriptionTextField);
		// stylepad.open("DESCRI��O");
		RtfEditorFactory.getInstance().createEditor("Defini��es",
				detailPanel.descriptionTextField, this);
	}

	public void applicationDataButton_actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(null, "EM DESENVOLVIMENTO");
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ProcessDefinition definition = (ProcessDefinition) lastSelection
				.getUserObject();
		ProcessPanel detailPanel = ((ProcessPanel) view.getDetail());

		// Stylepad stylepad = new Stylepad(detailPanel.applicationTextField);
		// stylepad.open("APLICA��O");
		RtfEditorFactory.getInstance().createEditor("Aplica��o",
				detailPanel.applicationTextField, this);
	}

	public void descriptionSubProcessDataButton_actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(null, "EM DESENVOLVIMENTO");
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		SubProcessVO subProcess = (SubProcessVO) lastSelection.getUserObject();
		SubProcessPanel detailPanel = ((SubProcessPanel) view.getDetail());

		// Stylepad stylepad = new Stylepad(detailPanel.applicationTextField);
		// stylepad.open("APLICA��O");
		RtfEditorFactory.getInstance().createEditor("Descri��o",
				detailPanel.descriptionSubProcessTextField, this);
	}

	public void descriptionStateDataButton_actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(null, "EM DESENVOLVIMENTO");
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		StateVO state = (StateVO) lastSelection.getUserObject();
		StatePanel detailPanel = ((StatePanel) view.getDetail());

		// Stylepad stylepad = new Stylepad(detailPanel.descriptionTextField);
		// stylepad.open("DESCRI��O");
		RtfEditorFactory.getInstance().createEditor(
				"Descri��o Atividade " + state.getName(),
				detailPanel.descriptionTextField, this);
	}

	/**
	 * m�todo que salva a informa��o do painel que est� ativo no momento
	 * 
	 */
	public void saveCurrent() {
		// exception causada quando o painel � de outro tipo
		try {
			this.saveStateDataButton_actionPerformed(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.saveProcessDataButton_actionPerformed(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// novo
		try {
			this.saveSubProcessDataButton_actionPerformed(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	// ------------------------------------
	// save Changes end
	// ------------------------------------

	/**
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent evt) {
		if (evt.getModifiers() == MouseEvent.BUTTON3_MASK) {
			if (getView() instanceof AbstractPropertyPanel) {
				((AbstractPropertyPanel) getView()).showPopMenu(evt
						.getComponent(), evt.getX(), evt.getY());
			}
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((AbstractPropertyPanel) getView()).propertytree
				.getLastSelectedPathComponent();
		
		if (AbstractPropertyPanel.ATTRIBUTE_DELETE.equals(e.getActionCommand())) {
			AttributeVO attribute = (AttributeVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent());

			ProcessDefinition processDefinition = (ProcessDefinition) parent
					.getUserObject();
			processDefinition.getAttributeList().remove(attribute);

			removeSelectedNode();
		} else if (AbstractPropertyPanel.ATTRIBUTE_CREATE.equals(e
				.getActionCommand())) {
			ProcessDefinition process = (ProcessDefinition) node
					.getUserObject();
			AttributeVO attribute = new AttributeVO();
			process.getAttributeList().add(attribute);

			addToTree(node, new DefaultMutableTreeNode(attribute));
		} else if (AbstractPropertyPanel.FIELD_CREATE.equals(e
				.getActionCommand())) {
			FieldVO fild = new FieldVO();
			if (node.getUserObject() instanceof StartStateVO) {
				StartStateVO startState = (StartStateVO) node.getUserObject();
				startState.getFieldList().add(fild);
			} else if (node.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) node
						.getUserObject();
				activityState.getFieldList().add(fild);
			}

			DefaultMutableTreeNode fieldsnode = findNode(FIELDS_NODE, node);
			addToTree(fieldsnode, new DefaultMutableTreeNode(fild));
		} else if (AbstractPropertyPanel.FIELD_DELETE.equals(e
				.getActionCommand())) {
			FieldVO field = (FieldVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent());
			if (((DefaultMutableTreeNode) parent.getParent()).getUserObject() instanceof StartStateVO) {
				StartStateVO startState = (StartStateVO) ((DefaultMutableTreeNode) parent
						.getParent()).getUserObject();
				startState.getFieldList().remove(field);
			} else if (((DefaultMutableTreeNode) parent.getParent())
					.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activity = (ActivityStateVO) ((DefaultMutableTreeNode) parent
						.getParent()).getUserObject();
				activity.getFieldList().remove(field);
			}
			removeSelectedNode();
		} else if (AbstractPropertyPanel.ACTION_CREATE.equals(e
				.getActionCommand())) {
			ActionVO action = new ActionVO();
			if (node.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) node
						.getUserObject();
				activityState.getActionList().add(action);
			} else if (node.getUserObject() instanceof TransitionVO) {
				TransitionVO trasition = (TransitionVO) node.getUserObject();
				trasition.getActionList().add(action);
			} else if (node.getUserObject() instanceof StartStateVO) {
				StartStateVO startstate = (StartStateVO) node.getUserObject();
				startstate.getActionList().add(action);
			} else if (node.getUserObject() instanceof ProcessDefinition) {
				ProcessDefinition processdefinition = (ProcessDefinition) node
						.getUserObject();
				processdefinition.getActionList().add(action);
			}

			DefaultMutableTreeNode actionnode = findNode(ACTIONS_NODE, node);
			addToTree(actionnode, new DefaultMutableTreeNode(action));
		} else if (AbstractPropertyPanel.ACTION_DELETE.equals(e
				.getActionCommand())) {
			ActionVO action = (ActionVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent().getParent());
			if (parent.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) parent
						.getUserObject();
				activityState.getActionList().remove(action);
			} else if (parent.getUserObject() instanceof TransitionVO) {
				TransitionVO transition = (TransitionVO) parent.getUserObject();
				transition.getActionList().remove(action);
			} else if (parent.getUserObject() instanceof StartStateVO) {
				StartStateVO startstate = (StartStateVO) parent.getUserObject();
				startstate.getActionList().remove(action);
			}

			removeSelectedNode();
		} else if (AbstractPropertyPanel.PARAMETER_CREATE.equals(e
				.getActionCommand())) {
			ParameterVO parameter = new ParameterVO();
			if (node.getUserObject() instanceof ActionVO) {
				ActionVO action = (ActionVO) node.getUserObject();
				action.getParameterList().add(parameter);
			} else if (node.getUserObject() instanceof AssignmentVO) {
				AssignmentVO assignment = (AssignmentVO) node.getUserObject();
				assignment.getParameterList().add(parameter);
			} else if (node.getUserObject() instanceof DecisionVO) {
				DecisionVO decision = (DecisionVO) node.getUserObject();
				decision.getParameterList().add(parameter);
			} else if (node.getUserObject() instanceof FormatterVO) {
				FormatterVO formatter = (FormatterVO) node.getUserObject();
				formatter.getParameterList().add(parameter);
			}

			addToTree(node, new DefaultMutableTreeNode(parameter));
		} else if (AbstractPropertyPanel.PARAMETER_DELETE.equals(e
				.getActionCommand())) {
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent());
			if (parent.getUserObject() instanceof DecisionVO) {
				DecisionVO decision = (DecisionVO) parent.getUserObject();
				ParameterVO parameter = (ParameterVO) node.getUserObject();
				decision.getParameterList().remove(parameter);
			} else if (parent.getUserObject() instanceof ActionVO) {
				ActionVO action = (ActionVO) parent.getUserObject();
				ParameterVO parameter = (ParameterVO) node.getUserObject();
				action.getParameterList().remove(parameter);
			} else if (parent.getUserObject() instanceof AssignmentVO) {
				AssignmentVO assignment = (AssignmentVO) parent.getUserObject();
				ParameterVO parameter = (ParameterVO) node.getUserObject();
				assignment.getParameterList().remove(parameter);
			}

			removeSelectedNode();
		} else if (AbstractPropertyPanel.ASSIGNMENT_CREATE.equals(e
				.getActionCommand())) {
			AssignmentVO assignment = new AssignmentVO();
			ActivityStateVO activityState = (ActivityStateVO) node
					.getUserObject();
			activityState.setAssignment(assignment);

			addToTree(node, new DefaultMutableTreeNode(assignment));
		} else if (AbstractPropertyPanel.ASSIGNMENT_DELETE.equals(e
				.getActionCommand())) {
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent());
			ActivityStateVO activity = (ActivityStateVO) parent.getUserObject();
			activity.setAssignment(null);

			removeSelectedNode();
		} else if (AbstractPropertyPanel.MOVE_UP_ACTION.equals(e
				.getActionCommand())) {
			ActionVO action = (ActionVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent().getParent());
			if (parent.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) parent
						.getUserObject();
				moveUpObject(activityState.getActionList(), action);
			} else if (parent.getUserObject() instanceof TransitionVO) {
				TransitionVO transition = (TransitionVO) parent.getUserObject();
				moveUpObject(transition.getActionList(), action);
			} else if (parent.getUserObject() instanceof StartStateVO) {
				StartStateVO startstate = (StartStateVO) parent.getUserObject();
				moveUpObject(startstate.getActionList(), action);
			}

			moveUpNode(node);
		} else if (AbstractPropertyPanel.MOVE_DOWN_ACTION.equals(e
				.getActionCommand())) {
			ActionVO action = (ActionVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent().getParent());
			if (parent.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) parent
						.getUserObject();
				moveDownObject(activityState.getActionList(), action);
			} else if (parent.getUserObject() instanceof TransitionVO) {
				TransitionVO transition = (TransitionVO) parent.getUserObject();
				moveDownObject(transition.getActionList(), action);
			} else if (parent.getUserObject() instanceof StartStateVO) {
				StartStateVO startstate = (StartStateVO) parent.getUserObject();
				moveDownObject(startstate.getActionList(), action);
			}
			moveDownNode(node);
		} else if (AbstractPropertyPanel.MOVE_UP_FIELD.equals(e
				.getActionCommand())) {
			FieldVO field = (FieldVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent().getParent());
			if (parent.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) parent
						.getUserObject();
				moveUpObject(activityState.getFieldList(), field);
			} else if (parent.getUserObject() instanceof StartStateVO) {
				StartStateVO startstate = (StartStateVO) parent.getUserObject();
				moveUpObject(startstate.getFieldList(), field);
			}
			moveUpNode(node);
		} else if (AbstractPropertyPanel.MOVE_DOWN_FIELD.equals(e
				.getActionCommand())) {
			FieldVO field = (FieldVO) node.getUserObject();
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent().getParent());
			if (parent.getUserObject() instanceof ActivityStateVO) {
				ActivityStateVO activityState = (ActivityStateVO) parent
						.getUserObject();
				moveDownObject(activityState.getFieldList(), field);
			} else if (parent.getUserObject() instanceof StartStateVO) {
				StartStateVO startstate = (StartStateVO) parent.getUserObject();
				moveDownObject(startstate.getFieldList(), field);
			}
			moveDownNode(node);
		} else if (AbstractPropertyPanel.FORMATER_CREATE.equals(e
				.getActionCommand())) {
			FieldVO field = (FieldVO) node.getUserObject();
			FormatterVO formatter = new FormatterVO();
			field.setFormatter(formatter);
			addToTree(node, new DefaultMutableTreeNode(formatter));
		} else if (AbstractPropertyPanel.FORMATER_DELETE.equals(e
				.getActionCommand())) {
			DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) node
					.getParent());
			FieldVO field = (FieldVO) parent.getUserObject();
			field.setFormatter(null);

			removeSelectedNode();
		}
	}

	/**
	 * @param list
	 * @param action
	 */
	private void moveDownObject(List list, Object action) {
		int pos = list.indexOf(action);
		if (pos + 1 < list.size()) {
			list.remove(pos);
			list.add(pos + 1, action);
		}
	}

	/**
	 * @param list
	 * @param action
	 */
	private void moveUpObject(List list, Object action) {
		int pos = list.indexOf(action);
		if (pos != 0) {
			list.remove(pos);
			list.add(pos - 1, action);
		}
	}

	/**
	 * @param node
	 */
	private void moveDownNode(DefaultMutableTreeNode node) {
		JTree jtree = ((AbstractPropertyPanel) getView()).propertytree;
		MutableTreeNode parent = (MutableTreeNode) node.getParent();
		int index = parent.getIndex(node);
		if (index + 1 < node.getParent().getChildCount()) {
			DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, parent, index + 1);
		}
	}

	/**
	 * @param node
	 */
	private void moveUpNode(DefaultMutableTreeNode node) {
		JTree jtree = ((AbstractPropertyPanel) getView()).propertytree;
		MutableTreeNode parent = (MutableTreeNode) node.getParent();
		int index = parent.getIndex(node);
		if (index != 0) {
			DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();
			model.removeNodeFromParent(node);
			model.insertNodeInto(node, parent, index - 1);
		}
	}

	/**
	 * @param ACTION_NODE
	 * @param node
	 * @return
	 */
	private DefaultMutableTreeNode findNode(String nodeName,
			DefaultMutableTreeNode node) {
		int nodeCount = node.getChildCount();
		for (int i = 0; i < nodeCount; i++) {
			DefaultMutableTreeNode child = ((DefaultMutableTreeNode) node
					.getChildAt(i));
			if (child.getUserObject().equals(nodeName)) {
				return child;
			}
		}
		return null;
	}

	private void removeSelectedNode() {
		JTree jtree = ((AbstractPropertyPanel) getView()).propertytree;
		TreePath[] paths = jtree.getSelectionPaths();
		DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();
		for (int i = 0; i < paths.length; i++) {
			MutableTreeNode node = (MutableTreeNode) paths[i]
					.getLastPathComponent();
			// do not remove the root node
			if (node != model.getRoot())
				model.removeNodeFromParent(node);
		}
	}

	private void addToTree(DefaultMutableTreeNode parent,
			DefaultMutableTreeNode node) {
		JTree jtree = ((AbstractPropertyPanel) getView()).propertytree;
		int index = parent.getChildCount();
		DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();
		model.insertNodeInto(node, parent, index);
	}

	private void nodeChanged(DefaultMutableTreeNode node) {
		JTree jtree = ((AbstractPropertyPanel) getView()).propertytree;
		DefaultTreeModel model = (DefaultTreeModel) jtree.getModel();
		model.nodeChanged(node);
	}

	// -----------------------------
	// all Action functions
	// -----------------------------
	public void accessButton_actionPerformed(ActionEvent e) {
		AccessDialog dialog = new AccessDialog();
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
			FieldPanel fieldPanel = (FieldPanel) view.getDetail();
			fieldPanel.accessTextField.setText(dialog.getSelectedValue());
		}
	}

	public void eventButton_actionPerformed(ActionEvent e) {
		EventDialog dialog = new EventDialog();
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
			ActionPanel actionPanel = (ActionPanel) view.getDetail();
			actionPanel.eventTextField.setText(dialog.getSelectedValue());
		}
	}

	public void onExcepitonButton_actionPerformed(ActionEvent e) {
		OnExceptionDialog dialog = new OnExceptionDialog();
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
			ActionPanel actionPanel = (ActionPanel) view.getDetail();
			actionPanel.onExceptionTextField.setText(dialog.getSelectedValue());
		}
	}

	public void valueButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ParameterPanel actionPanel = (ParameterPanel) view.getDetail();
		EditorDialog dialog = new EditorDialog(actionPanel.valueTextField
				.getText());
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			actionPanel.valueTextField.setText(dialog.getSelectedValue());
		}
	}

	public void typeButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		AttributePanel typePanel = (AttributePanel) view.getDetail();
		TypeDialog dialog = new TypeDialog();
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			typePanel.typeTextField.setText(dialog.getSelectedValue());
		}
	}

	public void actionhandlerButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		ActionPanel actionPanel = (ActionPanel) view.getDetail();
		ClassInspectorDialog dialog = new ClassInspectorDialog(
				Configuration.ACTION_PACKAGE);
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			actionPanel.handlerTextField.setText(dialog.getSelectedValue());
		}
	}

	public void formatterhandlerButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		FormatterPanel formatterPanel = (FormatterPanel) view.getDetail();
		ClassInspectorDialog dialog = new ClassInspectorDialog(
				Configuration.FORMATTER_PACKAGE);
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			formatterPanel.formatterclassTextField.setText(dialog
					.getSelectedValue());
		}
	}

	public void assignmenthandlerButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		AssignmentPanel assignmentPanel = (AssignmentPanel) view.getDetail();
		ClassInspectorDialog dialog = new ClassInspectorDialog(
				Configuration.ASSIGNMENT_PACKAGE);
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			assignmentPanel.assignmentTextField.setText(dialog
					.getSelectedValue());
		}
	}

	public void decisionhandlerButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		DecisionPanel decisionPanel = (DecisionPanel) view.getDetail();
		ClassInspectorDialog dialog = new ClassInspectorDialog(
				Configuration.DECISION_PACKAGE);
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			decisionPanel.handlerTextField.setText(dialog.getSelectedValue());
		}
	}

	public void serializerhandlerButton_actionPerformed(ActionEvent e) {
		AbstractPropertyPanel view = (AbstractPropertyPanel) getView();
		AttributePanel attributePanel = (AttributePanel) view.getDetail();
		ClassInspectorDialog dialog = new ClassInspectorDialog(
				Configuration.SERIALIZER_PACKAGE);
		dialog.show();
		if (dialog.getSelectedValue() != null) {
			attributePanel.serializerTextField.setText(dialog
					.getSelectedValue());
		}
	}

	/**
	 * @param node
	 */
	public void setLastSelection(DefaultMutableTreeNode node) {
		lastSelection = node;
	}

}
