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

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import eu.europa.ec.joinup.tsl.business.dto.Properties;
import eu.europa.ec.joinup.tsl.business.repository.CountryRepository;
import eu.europa.ec.joinup.tsl.model.DBCountries;

@Service
@Transactional(value = TxType.REQUIRED)
public class CountryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryService.class);
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private CacheService cacheService;

    public List<DBCountries> getAll() {
        return countryRepository.findAllByOrderByCountryName();
    }

    /**
     * Get Country by Territory Code Error : territory null; Error : country doesn't exist;
     *
     * @return DBCountries
     */
    public DBCountries getCountryByTerritory(String territory) {
        if (territory == null) {
            LOGGER.error("Code territory is null");
            return null;
        }
        DBCountries country = countryRepository.findOne(territory);
        if (country == null) {
            LOGGER.error("Country with territory code '" + territory + "' doesn't exist");
        }
        return country;
    }

    /**
     * Get "COUNTRYCODENAME" application properties and store in cache
     */
    @Cacheable(value = "countryCache", key = "#root.methodName")
    public List<Properties> getPropertiesCountry() {
        List<Properties> propList = new ArrayList<>();
        List<DBCountries> dbCountryList = getAll();
        for (DBCountries dbCountry : dbCountryList) {
            Properties myProp = new Properties("COUNTRYCODENAME", dbCountry);
            propList.add(myProp);
        }
        return propList;
    }

    /**
     * Get list of country code from database
     */
    public List<String> getAllCountryCode() {
        List<String> countryCodes = new ArrayList<>();
        for (DBCountries country : getAll()) {
            countryCodes.add(country.getCodeTerritory());
        }
        return countryCodes;
    }

    /**
     * Check if give @countryCode exist
     *
     * @param countryCode
     */
    @Cacheable(value = "countryCache", key = "#countryCode")
    public boolean isExist(String countryCode) {
        if (StringUtils.isNotBlank(countryCode)) {
            return countryRepository.exists(countryCode);
        }
        return false;
    }

    /**
     * Add new "COUNTRYCODENAME" application property
     *
     * @param country
     */
    public Properties add(DBCountries country) {
        DBCountries c = countryRepository.save(country);
        Properties myProp = new Properties("COUNTRYCODENAME", c);
        return myProp;
    }

    /**
     * Delete country from properties
     *
     * @param countryCode
     */
    public void delete(String countryCode) {
        countryRepository.delete(countryCode);
        cacheService.evictCountryCache();
    }

}
