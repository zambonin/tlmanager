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
package eu.europa.ec.joinup.tsl.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import eu.europa.esig.dss.client.crl.OnlineCRLSource;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.dss.client.http.commons.CommonsDataLoader;
import eu.europa.esig.dss.client.http.commons.OCSPDataLoader;
import eu.europa.esig.dss.client.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.x509.crl.CRLSource;
import eu.europa.esig.dss.x509.ocsp.OCSPSource;

@Configuration
@ComponentScan(basePackages = { "eu.europa.ec.joinup.tsl.business" })
@PropertySource(value = "classpath:test.properties")
public class ServiceTestConfig {

    @Value("${connection.timeout}")
    private int connectionTimeout;

    @Bean
    public DataLoader dataLoader() {
        CommonsDataLoader dataLoader = new CommonsDataLoader();
        dataLoader.setTimeoutConnection(connectionTimeout);
        return dataLoader;
    }

    @Bean
    public OCSPDataLoader ocspDataLoader() {
        OCSPDataLoader ocspDataLoader = new OCSPDataLoader();
        ocspDataLoader.setTimeoutConnection(connectionTimeout);
        return ocspDataLoader;
    }

    @Bean
    public OCSPSource ocspSource() {
        OnlineOCSPSource ocspSource = new OnlineOCSPSource();
        ocspSource.setDataLoader(ocspDataLoader());
        return ocspSource;
    }

    @Bean
    public CRLSource crlSource() {
        OnlineCRLSource crlSource = new OnlineCRLSource();
        crlSource.setDataLoader(dataLoader());
        return crlSource;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
