/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager non-EU
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 * 
 * This file is part of the "DIGIT-TSL - Trusted List Manager non-EU" project.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package eu.europa.ec.joinup.tsl.business.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.EnvironmentalProfileFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopConfParser;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.pdfbox.io.IOUtils;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.apache.xmlgraphics.io.TempResourceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import eu.europa.ec.joinup.tsl.business.dto.CheckDTO;
import eu.europa.ec.joinup.tsl.business.dto.CheckResultDTO;
import eu.europa.ec.joinup.tsl.business.dto.TLDifference;
import eu.europa.ec.joinup.tsl.business.dto.TLSignature;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFCheck;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFChecksChanges;
import eu.europa.ec.joinup.tsl.business.dto.pdf.PDFInfoTL;
import eu.europa.ec.joinup.tsl.business.dto.tl.TL;
import eu.europa.ec.joinup.tsl.model.enums.Tag;

/**
 * PDF report generator
 */
@Service
public class PDFReportService {

    private static ResourceBundle bundle = ResourceBundle.getBundle("messages");

    @Autowired
    private TLService tlService;
    
    @Autowired
    private CheckService checkService;

    private FopFactory fopFactory;
    private FOUserAgent foUserAgent;
    private Templates templateOrderedCheckResult;

    @PostConstruct
    public void init() throws Exception {

        ResourceResolver rr = ResourceResolverFactory.createTempAwareResourceResolver(getTempResourceResolver(), getResourceResolver());
        FopFactoryBuilder builder = new FopConfParser(new ClassPathResource("/fop/fop.conf.xml").getInputStream(),
                EnvironmentalProfileFactory.createRestrictedIO(new ClassPathResource("/fop/").getURI(), rr)).getFopFactoryBuilder();
        builder.setAccessibility(true);

        fopFactory = builder.build();

        foUserAgent = fopFactory.newFOUserAgent();
        foUserAgent.setCreator("TL Manager");
        foUserAgent.setAccessibility(true);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        InputStream simpleIS = new ClassPathResource("/xslt/TLStatusReport.xslt").getInputStream();
        templateOrderedCheckResult = transformerFactory.newTemplates(new StreamSource(simpleIS));
        IOUtils.closeQuietly(simpleIS);
    }

    /**
     * Generate TL PDF report
     *
     * @param tl
     * @param os
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void generateTLReport(TL tl, OutputStream os) throws Exception {
        foUserAgent.getRendererOptions().put("pdf-a-mode", "PDF");
        PDFChecksChanges toSerial = new PDFChecksChanges();
        String countryName = "";
        countryName += bundle.getString("pdf.draft");
        countryName += " " + tl.getSchemeInformation().getTerritory() + " (Sn" + tl.getSchemeInformation().getSequenceNumber() + ")";
        toSerial.setCountryName(countryName);

        TLSignature currentSignature = tlService.getSignatureInfo(tl.getTlId());
        PDFInfoTL infoTl = new PDFInfoTL(tl, currentSignature);
        toSerial.setCurrentTL(infoTl);
        toSerial.setChecks(checkService.getTLChecksResult(tl.getTlId()));
        
        XStream stream = new XStream();
        stream.setMode(XStream.NO_REFERENCES);
        stream.processAnnotations(PDFChecksChanges.class);
        stream.processAnnotations(PDFCheck.class);
        stream.alias("check", CheckDTO.class);
        stream.alias("tag", Tag.class);
        stream.alias("change", TLDifference.class);
        stream.alias("result", CheckResultDTO.class);
        String xml = stream.toXML(toSerial);

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, os);
        Result res = new SAXResult(fop.getDefaultHandler());
        Transformer transformer = templateOrderedCheckResult.newTransformer();
        transformer.transform(new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8"))), res);

    }

    private ResourceResolver getResourceResolver() {
        return new ResourceResolver() {

            @Override
            public Resource getResource(URI uri) throws IOException {
                return new Resource(new ClassPathResource("/fop/" + uri.toString()).getInputStream());
            }

            @Override
            public OutputStream getOutputStream(URI uri) throws IOException {
                return new FileOutputStream(new File(uri));
            }
        };
    }

    private TempResourceResolver getTempResourceResolver() {
        return new TempResourceResolver() {

            private final Map<String, ByteArrayOutputStream> tempBaos = Collections.synchronizedMap(new HashMap<String, ByteArrayOutputStream>());

            @Override
            public Resource getResource(String id) throws IOException {
                if (!tempBaos.containsKey(id)) {
                    throw new IllegalArgumentException("Resource with id = " + id + " does not exist");
                }
                return new Resource(new ByteArrayInputStream(tempBaos.remove(id).toByteArray()));
            }

            @Override
            public OutputStream getOutputStream(String id) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                tempBaos.put(id, out);
                return out;
            }
        };
    }
}
