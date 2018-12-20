INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('EU', 'European Union');
INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('BE', 'Belgium');
INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('DE', 'Germany');
INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('BG', 'Bulgaria');
INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('LU', 'Luxembourg');
INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('AT', 'Austria');

INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (1,'test', 'Test');
INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (2,'adm', 'Admin');
INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (100,'testAth', 'testAth');

INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (200,'ath1', 'ath1');
INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (201,'ath2', 'ath2');
INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (202,'ath3', 'ath3');

INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (300,'athNot1', 'athNot1');
INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (301,'athNot2', 'athNot2');

INSERT INTO TL_ROLES (ID, CODE, NAME, DESCRIPTION) VALUES ( 1, 'SUP', 'Super Administrator', 'All Access! Cannot be deleted');
INSERT INTO TL_ROLES (ID, CODE, NAME, DESCRIPTION) VALUES ( 2, 'MAN', 'Administrator', 'Access to management menu!');
INSERT INTO TL_ROLES (ID, CODE, NAME, DESCRIPTION) VALUES ( 6, 'ATH', 'Authenticated', 'Access to the application');

INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (1,1);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (2,2);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (3,3);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (4,4);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (100,6);

INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (200,6);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (201,6);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (202,6);

INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (300,6);
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (301,6);

INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The selectable address types', 'ADRTYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The selectable mime types; e.g. application/pdf', 'MIMETYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The language codes; the order defines their appearance in the application user interfaces', 'LANGUAGES');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of TL Community Rule', 'TL_COMMUNITYRULE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The default value for the tag TLType for EU countries', 'TL_STATUS_DETERM_TYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The TSL Tag value', 'TSL_TAG_VALUE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The TSL Version Identifier value', 'TSL_VERSION_IDENTIFIER_VALUE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The default value for the tag TSLType for a TL', 'TL_TYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag ServiceTypeIdentifier', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag ServiceStatus', 'SERVICE_STATUS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag previous ServiceStatus (History)', 'SERVICE_PREVIOUS_STATUS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for the qualifiers', 'QUALIFIERS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag PolicyIdentifier', 'IDENTIFIER_QUALIFIER_TYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for additionnalServiceInformation qualifier', 'ADDITIONNAL_QUALIFIER');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The maximal value of HistoricalInformationPeriod', 'HISTORICAL_INFO_PERIOD');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(1, 'mailto:', '', 'ADRTYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(2, 'http://', '', 'ADRTYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(3, 'https://', '', 'ADRTYPE');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(11, 'en', '','LANGUAGES');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(12, 'fr', '','LANGUAGES');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(21, 'http://test/Svcstatus/', 'Test prefix for Service Status','SERVICE_STATUS_PREFIX');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(22, 'http://test/Svctype/', 'Test prefix for TL Types','SERVICE_TYPES_PREFIX');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(31, 'http://test/StatusDetn/CC', 'Status determination approach example', 'TL_STATUS_DETERM_TYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(32, 'http://test/schemerules/CC', 'Community rule example', 'TL_COMMUNITYRULE');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(41, 'http://test/TSLTag', '', 'TSL_TAG_VALUE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(42, 'http://test/TSLType/CCgeneric', 'TL Type example', 'TL_TYPE');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(97, 'http://test/TrstSvc/Svctype/CA/PKC', 'Certificate generation service creating and signing end-entity non-qualified certificates', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(98, 'http://test/TrstSvc/Svctype/CA/QC', 'Certificate generation service creating and signing qualified certificates based on the identity and other attributes verified by the relevant registration services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(99, 'http://test/TrstSvc/Svctype/TSA', 'Time-stamping generation service creating and signing time-stamp tokens', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(100, 'http://test/TrstSvc/Svctype/TSA/QTST', 'Time-stamping generation service creating and signing qualified time-stamp tokens', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(101, 'http://test/TrstSvc/Svctype/TSA/TSS-QC', 'Time-stamping generation service, operated as part of services from a TSP issuing qualified certificates, creating and signing time-stamp tokens used to support QES and AdESQC', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(102, 'http://test/TrstSvc/Svctype/TSA/TSS-AdESQCandQES', 'Time-stamping generation service creating and signing time-stamp tokens used to support QES and AdESQC', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(103, 'http://test/TrstSvc/Svctype/Certstatus/OCSP', 'Certificate validity status services issuing Online Certificate Status Protocol (OCSP) signed responses', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(104, 'http://test/TrstSvc/Svctype/Certstatus/OCSP/QC', 'Certificate validity status services issuing Online Certificate Status Protocol (OCSP) signed responses and being part of a service from a TSP issuing qualified certificates', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(105, 'http://test/TrstSvc/Svctype/Certstatus/CRL', 'Certificate validity status services issuing Certificate Revocation Lists (CRLs)', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(106, 'http://test/TrstSvc/Svctype/Certstatus/CRL/QC', 'Certificate validity status services issuing Certificate Revocation Lists (CRLs) and being part of a service from a TSP issuing qualified certificates', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(107, 'http://test/TrstSvc/Svctype/RA', 'Registration services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(108, 'http://test/TrstSvc/Svctype/RA/nothavingPKIid', 'Registration services that cannot be identified by a PKI-based public key', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(109, 'http://test/TrstSvc/Svctype/ACA', 'Certificate generation service creating and signing attribute certificates', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(110, 'http://test/TrstSvc/Svctype/SignaturePolicyAuthority', 'Service responsible for issuing, publishing or maintenance of signature policies', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(111, 'http://test/TrstSvc/Svctype/NationalRootCA-QC', 'National root-CA services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(112, 'http://test/TrstSvc/Svctype/Archiv', 'Archival services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(113, 'http://test/TrstSvc/Svctype/EDS', 'Electronic delivery services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(114, 'http://test/TrstSvc/Svctype/EDS/Q', 'Electronic delivery services providing qualified electronic deliveries', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(115, 'http://test/TrstSvc/Svctype/PSES', 'Preservation service for electronic signatures', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(116, 'http://test/TrstSvc/Svctype/PSES/Q', 'Preservation service for qualified electronic signatures', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(117, 'http://test/TrstSvc/Svctype/IdV', 'Identity verification services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(118, 'http://test/TrstSvc/Svctype/KEscrow', 'Key escrow services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(119, 'http://test/TrstSvc/Svctype/PPwd', 'Services issuing PIN- or password-based identity credentials', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(120, 'http://test/TrstSvd/Svctype/TLIssuer', 'Trusted list issuing services', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(121, 'http://test/TrstSvc/Svctype/unspecified', 'A trust service of an unspecified type', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(122, 'http://test/TrstSvc/Svctype/EDS/REM/Q', 'A qualified electronic registered mail delivery service providing qualified electronic registered mail deliveries in accordance with the applicable national legislation in the territory identified by the TL Scheme territory (see clause 5.3.10) or with Regulation (EU) No 910/2014 [i.10] whichever is in force at the time of provision.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(123, 'http://test/TrstSvc/Svctype/QESValidation/Q', 'A qualified validation service for qualified electronic signatures and/or qualified electronic seals in accordance with the applicable national legislation in the territory identified by the TL Scheme territory (see clause 5.3.10) or with Regulation (EU) No 910/2014 [i.10] whichever is in force at the time of provision.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(124, 'http://test/TrstSvc/Svctype/EDS/REM', 'A Registered Electronic Mail delivery service, not qualified.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(125, 'http://test/TrstSvc/Svctype/AdESValidation', 'A not qualified validation service for advanced electronic signatures and/or advanced electronic seals.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(126, 'http://test/TrstSvc/Svctype/AdESGeneration', 'A not qualified generation service for advanced electronic signatures and/or advanced electronic seals.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(127, 'http://test/TrstSvc/Svctype/Archiv/nothavingPKIid', 'An Archival service that cannot be identified by a specific PKI-based public key.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(128, 'http://test/TrstSvc/Svctype/IdV/nothavingPKIid', 'An Identity verification service that cannot be identified by a specific PKI-based public key.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(129, 'http://test/TrstSvc/Svctype/KEscrow/nothavingPKIid', 'A Key escrow service that cannot be identified by a specific PKI-based public key.', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(130, 'http://test/TrstSvc/Svctype/PPwd/nothavingPKIid', 'Issuer of PIN- or password-based identity credentials that cannot be identified by a specific PKI-based public key.', 'SERVICE_TYPES_IDENTIFIERS');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(51, 'http://test/Svcstatus/status', 'Test status', 'SERVICE_STATUS');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(61, 'http://test/SvcInfoExt/Qualifier', '', 'QUALIFIERS');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(71, 'TestID', 'Test qualifier', 'IDENTIFIER_QUALIFIER_TYPE');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(81, 'http://test/SvcInfoExt/TestAddQualifier', 'Test additionnal qualifier', 'ADDITIONNAL_QUALIFIER');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(91, 'http://test/Svcstatus/test', 'Test previous status', 'SERVICE_PREVIOUS_STATUS');