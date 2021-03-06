package org.pm4knime.node.visualization;

import javax.swing.JPanel;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * This is an example implementation of the node factory of the
 * "LogVisualization" node.
 *
 * @author Kefang Ding
 */
public class LogVisualizationNodeFactory 
        extends NodeFactory<LogVisualizationNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public LogVisualizationNodeModel createNodeModel() {
		// Create and return a new node model.
        return new LogVisualizationNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
		// The number of views the node should have, in this cases there is none.
        return 2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<LogVisualizationNodeModel> createNodeView(final int viewIndex,
            final LogVisualizationNodeModel nodeModel) {
		// We return null as this example node does not provide a view. Also see "getNrNodeViews()".
    	JPanel[] viewPanels = new JPanel[getNrNodeViews()];
    	for(int i= 0 ; i< getNrNodeViews(); i++ ) {
    		viewPanels[i] = new JPanel();
    		viewPanels[i].setName("Log View "+ i);
    	}
		return new LogVisualizationNodeView(viewIndex, nodeModel, viewPanels);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
		// Indication whether the node has a dialog or not.
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
		// This example node has a dialog, hence we create and return it here. Also see "hasDialog()".
        return null;
    }

}

