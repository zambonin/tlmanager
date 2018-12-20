/*******************************************************************************
 * DIGIT-TSL - Trusted List Manager
 * Copyright (C) 2018 European Commission, provided under the CEF E-Signature programme
 *  
 * This file is part of the "DIGIT-TSL - Trusted List Manager" project.
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
package eu.europa.ec.joinup.tsl.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import eu.europa.esig.dss.client.http.proxy.ProxyConfig;
import eu.europa.esig.dss.client.http.proxy.ProxyProperties;

@Configuration
@PropertySource("classpath:proxy.properties")
public class ProxyConfiguration {

    @Value("${proxy.http.enabled}")
    private boolean httpEnabled;
    @Value("${proxy.http.host}")
    private String httpHost;
    @Value("${proxy.http.port}")
    private int httpPort;
    @Value("${proxy.http.user}")
    private String httpUser;
    @Value("${proxy.http.password}")
    private String httpPassword;
    @Value("${proxy.http.exclude}")
    private String httpExcludedHosts;

    @Value("${proxy.https.enabled}")
    private boolean httpsEnabled;
    @Value("${proxy.https.host}")
    private String httpsHost;
    @Value("${proxy.https.port}")
    private int httpsPort;
    @Value("${proxy.https.user}")
    private String httpsUser;
    @Value("${proxy.https.password}")
    private String httpsPassword;
    @Value("${proxy.https.exclude}")
    private String httpsExcludedHosts;

    @Bean
    public ProxyConfig proxyConfig() {
        if (!httpEnabled && !httpsEnabled) {
            return null;
        }
        ProxyConfig config = new ProxyConfig();
        if (httpEnabled) {
            ProxyProperties httpProperties = new ProxyProperties();
            httpProperties.setHost(httpHost);
            httpProperties.setPort(httpPort);
            httpProperties.setUser(httpUser);
            httpProperties.setPassword(httpPassword);
            httpProperties.setExcludedHosts(httpExcludedHosts);
            config.setHttpProperties(httpProperties);
        }
        if (httpsEnabled) {
            ProxyProperties httpsProperties = new ProxyProperties();
            httpsProperties.setHost(httpsHost);
            httpsProperties.setPort(httpsPort);
            httpsProperties.setUser(httpsUser);
            httpsProperties.setPassword(httpsPassword);
            httpsProperties.setExcludedHosts(httpsExcludedHosts);
            config.setHttpsProperties(httpsProperties);
        }
        return config;
    }

}
