package eu.europa.ec.joinup.tsl.business.util;

import static org.junit.Assert.assertNotNull;

import javax.xml.transform.Transformer;

import org.junit.Test;

import eu.europa.esig.dss.DomUtils;

public class DomUtilsTest {

    @Test
    public void getSecureTransformer() {
        
        // Unit test which fails if xalan or xercesImpl is still present
        Transformer transformer = DomUtils.getSecureTransformer();
        assertNotNull(transformer);
    }
    
}
