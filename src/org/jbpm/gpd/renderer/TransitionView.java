package org.jbpm.gpd.renderer;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.jbpm.gpd.cell.ActivityCell;
import org.jbpm.gpd.cell.Transition;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellView;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.PortView;
import org.jgraph.graph.VertexView;

/**
 * An EdgeView that shows a Connection between tables
 *
 * @version $Revision: 1.1 $
 * @author Brian Sidharta
 */

public class TransitionView extends EdgeView 
{
    /**
     * Constructs an edge view for the specified model object.
     *
     * @param cell reference to the model object
     */
    public TransitionView( Object cell ) 
    {
        super(cell);
        update();
        Transition sub = (Transition) cell;
		sub.setView(this);
    }

    /**
     * Returns the cached points for this edge.
     */
    public Point2D getPoint(int index) 
    {
    	// 0=source 1=target
		Point2D centerPoint=null;
		CellView activityview=null;
		Transition transition = ((Transition)cell);
		//GraphModel jGraphModel = graph.getModel();
		Point2D point = super.getPoint( index );
		Object sourceCell = super.source;
		Object targetCell = super.target;
		if (sourceCell instanceof ActivityCell && index==0 && points.get(index) instanceof PortView){
			centerPoint = simpleGetPoint(index);
			PortView pview=(PortView)points.get(index);
			activityview = pview.getParentView();
			if (centerPoint.getY()>point.getY()){
				//point.y=centerPoint.y;
				if (point.getX()<centerPoint.getX()){
					point.setLocation(centerPoint.getX()-(activityview.getBounds().getWidth()/2), point.getY());
				}else {
					point.setLocation(centerPoint.getX()+(activityview.getBounds().getWidth()/2), point.getY());
				}
			}
		} else if (targetCell instanceof ActivityCell && index==1  && points.get(index) instanceof PortView){
			centerPoint = simpleGetPoint(index);
			PortView pview=(PortView)points.get(index);
			activityview = pview.getParentView();
			if (centerPoint.getY()<point.getY()){
				//point.y=centerPoint.y;
				if (point.getX()<centerPoint.getX()){
					point.setLocation(centerPoint.getX()-(activityview.getBounds().getWidth()/2), point.getY());
				}else {
					point.setLocation(centerPoint.getX()+(activityview.getBounds().getWidth()/2), point.getY());
				}
			}
		} 
		return (point);
    }
	/**
	 * Lightweight version of getPoint that isn't recursive but doesn't
	 * return points that should be drawn
	 */
	private Point2D simpleGetPoint( int index )
	{
		Object obj = points.get(index);
		if (obj instanceof Point)
			return (Point) obj;
		else if (obj instanceof PortView) {
			VertexView vertex = (VertexView) ((CellView) obj).getParentView();
			return vertex.getCenterPoint();
		}
		return null;
        
	}
    
    
    
    
    
    /**
     * Returns a cell handle for the view.
     */
    public CellHandle getHandle(GraphContext context) {
        return new PointEdgeHandle(this, context);
    }
    
  
 
}
