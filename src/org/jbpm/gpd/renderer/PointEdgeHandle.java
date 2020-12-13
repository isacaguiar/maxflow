/*
 * Created on 17/03/2005
 *
 */
package org.jbpm.gpd.renderer;

import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;


//Defines a EdgeHandle that uses the Shift-Button (Instead of the Right
// Mouse Button, which is Default) to add/remove point to/from an edge.
public class PointEdgeHandle extends EdgeView.EdgeHandle {

    /**
     * @param edge
     * @param ctx
     */
    public PointEdgeHandle(EdgeView edge, GraphContext ctx) {
        super(edge, ctx);
    }

    // Override Superclass Method
    public boolean isAddPointEvent(MouseEvent event) {
        // Points are Added using Shift-Click
        return event.isShiftDown();
    }

    // Override Superclass Method
    public boolean isRemovePointEvent(MouseEvent event) {
        // Points are Removed using Shift-Click
        return event.isShiftDown();
    }

}