package org.pm4knime.node.logmanipulation.classify;


import java.util.List;

import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.portobject.XLogPortObject;
import org.pm4knime.portobject.XLogPortObjectSpec;
import org.pm4knime.util.XLogSpecUtil;
import org.pm4knime.util.defaultnode.DefaultNodeModel;
import org.processmining.log.utils.TraceVariantByClassifier;
import org.processmining.log.utils.XUtils;
import org.processmining.logenhancement.view.LogViewVisualizer;

import com.google.common.collect.ImmutableListMultimap;

/**
 * This is the model implementation of RandomClassifier.
 * RandomClassifier classifies the event log randomly, and assigns labels to the trace
 *
 * @author Kefang Ding
 */
public class RandomClassifierNodeModel extends DefaultNodeModel {
	
    public static final String CFG_KEY_CONFIG = "Classify config";

    ClassifyConfig m_config = new ClassifyConfig(CFG_KEY_CONFIG);
	/**
     * Constructor for the node model.
     */
    protected RandomClassifierNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
    	super(new PortType[] { XLogPortObject.TYPE },
				new PortType[] { XLogPortObject.TYPE });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

        // TODO: assign the labels to event log according to the overlap rate and positive rate
    	if(! (inData[0] instanceof XLogPortObject))
    		throw new InvalidSettingsException("Input is not a valid Event Log!");
    	
    	XLogPortObject logPortObject = (XLogPortObject) inData[0];
    	XLog log = logPortObject.getLog();
    	if(log.isEmpty()) {
    		throw new InvalidSettingsException("This event log is empty, reset a new event log");
    	}
    	checkCanceled(exec);
    	// get the event classes
    	XEventClasses eventClasses = LogViewVisualizer.createEventClasses(log);
    	// change the current distribution to integer map 
    	
    	
    	ImmutableListMultimap<TraceVariantByClassifier, XTrace> variantsMap = 
    			XUtils.getVariantsByClassifier(log, eventClasses);
    	
    	
    	for(TraceVariantByClassifier variant : variantsMap.keySet()) {
    		checkCanceled(exec);
    		List<XTrace> traceList = variantsMap.get(variant);
    		ClassifyUtil.addLabelWithPercent(traceList, m_config.getValueMap(), m_config.getLabelName());
    	}

    	// check if it uses the same traces
    	XAttributeLiteral attr = new XAttributeLiteralImpl(m_config.getLabelName(), "");
    	log.getGlobalTraceAttributes().add(attr);
    	
    	// here spec needs changes, because the new added values here
    	logPortObject.setSpec(XLogSpecUtil.extractSpec(log));
    	checkCanceled(exec);
		return  new PortObject[]{logPortObject};
    }

   

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	
        // TODO: check if the inport object is XLOgPortObject
    	if(! (inSpecs[0] instanceof XLogPortObjectSpec))
    		throw new InvalidSettingsException("Input is not a valid Event Log!");
    	// check if there are some data available to use 
    	if(m_config.getLabelName().isEmpty() || m_config.getValueMap().isEmpty()) {
    		throw new InvalidSettingsException("Please give valid classification distribution!");
    	}
        return new PortObjectSpec[]{new XLogPortObjectSpec()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    	m_config.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
       m_config.loadValidatedSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    	m_config.loadValidatedSettingsFrom(settings);
    }
    
}

