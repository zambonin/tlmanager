INSERT INTO TL_COUNTRIES (CODE_TERRITORY, COUNTRY_NAME) values ('TT', 'Test country');

INSERT INTO TL_ROLES (ID, CODE, NAME, DESCRIPTION) VALUES ( 1, 'SUP', 'Super Administrator', 'All Access! Cannot be deleted');
INSERT INTO TL_ROLES (ID, CODE, NAME, DESCRIPTION) VALUES ( 2, 'MAN', 'Administrator', 'Access to management menu');
INSERT INTO TL_ROLES (ID, CODE, NAME, DESCRIPTION) VALUES ( 6, 'ATH', 'Authenticated', 'Access to the application');

INSERT INTO TL_USERS (ID,ECAS_ID, NAME) values (1,'test', 'Test');
INSERT INTO TL_USER_ROLE (USER_ID, ROLE_ID) VALUES (1,1);

INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The selectable address types', 'ADRTYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The selectable mime types; e.g. application/pdf', 'MIMETYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The language codes; the order defines their appearance in the application user interfaces', 'LANGUAGES');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of TL Community Rule', 'TL_COMMUNITYRULE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The default value for the tag TLType for EU countries', 'TL_STATUS_DETERM_TYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The TSL Tag value', 'TSL_TAG_VALUE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'The default value for the tag TSLType for a TL', 'TL_TYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible prefixes for Service Status', 'SERVICE_STATUS_PREFIX');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible prefixes for TL Type', 'SERVICE_TYPES_PREFIX');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag ServiceTypeIdentifier', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag ServiceStatus', 'SERVICE_STATUS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag previous ServiceStatus (History)', 'SERVICE_PREVIOUS_STATUS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for the qualifiers', 'QUALIFIERS');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for tag PolicyIdentifier', 'IDENTIFIER_QUALIFIER_TYPE');
INSERT INTO TL_PROPERTIES_LIST ( DESCRIPTION, CODE) VALUES( 'A collection of possible values for additionnalServiceInformation qualifier', 'ADDITIONNAL_QUALIFIER');

INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(1, 'mailto:', '', 'ADRTYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(2, 'http://', '', 'ADRTYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(3, 'https://', '', 'ADRTYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(4, 'en', '','LANGUAGES');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(5, 'http://example/Svcstatus/', 				'Example of prefix for Service Status','SERVICE_STATUS_PREFIX');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(6, 'http://example/Svctype/', 				'Example of prefix for Service types','SERVICE_TYPES_PREFIX');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(7, 'http://example/StatusDetn/CC', 			'Example of Status determination approach', 'TL_STATUS_DETERM_TYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(8, 'http://example/schemerules/CC', 			'Example of Community rule', 'TL_COMMUNITYRULE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(9, 'http://example/TSLTag', 					'Example of TSL Tag', 'TSL_TAG_VALUE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(10, 'http://example/TSLType/CCgeneric', 		'Example of TL Type', 'TL_TYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(11, 'http://example/Svctype/CC', 			'Example of Service type', 'SERVICE_TYPES_IDENTIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(12, 'http://example/Svcstatus/status',		'Example of Service Status', 'SERVICE_STATUS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(13, 'http://example/SvcInfoExt/Qualifier', 	'Example of Qualifier', 'QUALIFIERS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(14, 'ExampleID', 							'Example of Qualifier Type', 'IDENTIFIER_QUALIFIER_TYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(15, 'http://example/SvcInfoExt/TestAddQualifier', 'Example of Additionnal qualifier', 'ADDITIONNAL_QUALIFIER');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(16, 'http://example/Svcstatus/test', 		'Example of Service Previous Status', 'SERVICE_PREVIOUS_STATUS');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(17, 'PDF', 'application/pdf', 'MIMETYPE');
INSERT INTO TL_PROPERTIES (PROPERTIES_ID, LABEL, DESCRIPTION, PROPERTIES_LIST_CODE) VALUES(18, 'XML', 'application/vnd.etsi.tsl+xml', 'MIMETYPE');

/* CHECKS */
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_TSL_TAG_PRESENT',					'LEGAL',           'SCHEME_INFORMATION',    'IS_TSL_TAG_PRESENT','ERROR',				'TSL Tag shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_TL_TYPE_PRESENT',					'LEGAL',           'SCHEME_INFORMATION',    'IS_TL_TYPE_PRESENT','ERROR',				'TSL type shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_OPERATOR_NAME_PRESENT',			'LEGAL',           'SCHEME_INFORMATION',    'IS_OPERATOR_NAME_PRESENT','ERROR',			'Operator Name shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_POSTAL_ADDRESS_PRESENT',			'LEGAL',           'SCHEME_INFORMATION',    'IS_POSTAL_ADDRESS_PRESENT','ERROR',		'Postal Address shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_ELECTRONIC_ADDRESS_PRESENT',		'LEGAL', 			'SCHEME_INFORMATION',    'IS_ELECTRONIC_ADDRESS_PRESENT','ERROR',	'Electronic Address shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_SCHEME_NAME_PRESENT',				'LEGAL',   			'SCHEME_INFORMATION',    'IS_SCHEME_NAME_PRESENT','ERROR',			'Scheme Name shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_INFORMATION_URI_PRESENT',			'LEGAL',           'SCHEME_INFORMATION',    'IS_INFORMATION_URI_PRESENT','ERROR',		'Information URI shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_STATUS_DETERMINATION_PRESENT',		'LEGAL',   			'SCHEME_INFORMATION',    'IS_STATUS_DETERMINATION_PRESENT','ERROR',	'Status Determination Approach shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_COMMUNITY_RULES_PRESENT',			'LEGAL',           'SCHEME_INFORMATION',    'IS_COMMUNITY_RULES_PRESENT','ERROR',		'Community Rules shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_ISSUE_DATE_PRESENT',				'LEGAL',           'SCHEME_INFORMATION',    'IS_ISSUE_DATE_PRESENT','ERROR',			'Issue Date shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_NEXT_UPDATE_PRESENT',				'LEGAL',           'SCHEME_INFORMATION',    'IS_NEXT_UPDATE_PRESENT','ERROR',			'Next Update shall be present if at least one service is not "expired"');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_TSL_TAG_CORRECT',					'LEGAL',           'SCHEME_INFORMATION',    'IS_TSL_TAG_CORRECT','ERROR',				'TSL Tag not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_SEQUENCE_NUMBER_GREATER_THAN_ZERO','LEGAL',           'SCHEME_INFORMATION',    'IS_SEQUENCE_NUMBER_GREATER_THAN_ZERO','ERROR',	'Sequence number shall be greater than 0.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_TL_TYPE_CORRECT',					'LEGAL',           'SCHEME_INFORMATION',    'IS_TL_TYPE_CORRECT','ERROR',				'TSL Type not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_ISSUE_DATE_IN_THE_PAST',			'LEGAL',           'SCHEME_INFORMATION',    'IS_ISSUE_DATE_IN_THE_PAST','ERROR',		'Issue Date should be in the past.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_NEXT_UPDATE_IN_THE_FUTURE',		'LEGAL',           'SCHEME_INFORMATION',    'IS_NEXT_UPDATE_IN_THE_FUTURE','ERROR',		'Next Update should be in the future.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE','LEGAL',   'SCHEME_INFORMATION',    'IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE','ERROR',	'The difference between NextUpdate and List Issue Date should not exceed 6 months.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_HISTORICAL_INFORMATION_PERIOD_CORRECT',	'LEGAL',   'SCHEME_INFORMATION',    'IS_HISTORICAL_INFORMATION_PERIOD_CORRECT','ERROR',		'Historical Period should be included between 0 and 65535.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_SCHEME_NAME_START_WITH_COUNTRY_CODE',		'LEGAL',   'SCHEME_INFORMATION',    'IS_SCHEME_NAME_START_WITH_COUNTRY_CODE','ERROR',		'Scheme Name shall be a character string structured as follows: "CC:name_value" where "CC" is the code used in the Scheme territory field.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_STATUS_DETERMINATION_CORRECT',		'LEGAL',           'SCHEME_INFORMATION',    'IS_STATUS_DETERMINATION_CORRECT','ERROR',	'Status Determination Approach not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('SCHEME_INFORMATION.IS_COMMUNITY_RULES_CORRECT',			'LEGAL',           'SCHEME_INFORMATION',    'IS_COMMUNITY_RULES_CORRECT','ERROR',		'Community Rule not found in properties.');

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_LOCATION_PRESENT',                  'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_LOCATION_PRESENT','ERROR',				'TL Location shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_OPERATOR_NAME_PRESENT',          	'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_OPERATOR_NAME_PRESENT','ERROR',			'Operator Name element shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_TERRITORY_PRESENT',          		'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_TERRITORY_PRESENT','ERROR',				'Territory shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_TERRITORY_CORRECT',          		'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_TERRITORY_CORRECT','ERROR',				'Territory not found properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_TL_TYPE_PRESENT',                   'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_TL_TYPE_PRESENT','ERROR',				'TL Type shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_TL_TYPE_CORRECT',                   'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_TL_TYPE_CORRECT','ERROR',               'TL Type not foud in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_DIGITAL_IDENTITIES_PRESENT',		'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_DIGITAL_IDENTITIES_PRESENT','ERROR',	'Digital Identities shall be present and contain at least one element.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_DIGITAL_IDENTITIES_CORRECT',		'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_DIGITAL_IDENTITIES_CORRECT','ERROR',	'Digital Identity shall contain at least one X509Certificate.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_COMMUNITY_RULES_CORRECT',           'LEGAL',   'POINTERS_TO_OTHER_TSL', 	'IS_COMMUNITY_RULES_CORRECT','ERROR',       'Community Rule not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_COMMUNITY_RULES_PRESENT',           'LEGAL',   'POINTERS_TO_OTHER_TSL', 	'IS_COMMUNITY_RULES_PRESENT','ERROR',       'Community Rule shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_MIME_TYPE_PRESENT',               	'LEGAL',	'POINTERS_TO_OTHER_TSL',    'IS_MIME_TYPE_PRESENT','ERROR',				'Mime Type shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POINTERS_TO_OTHER_TSL.IS_MIME_TYPE_CORRECT',                 'LEGAL',    'POINTERS_TO_OTHER_TSL', 	'IS_MIME_TYPE_CORRECT','ERROR',             'Mime Type not found in properties.');

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_TYPE_ID_PRESENT',                 		'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_TYPE_ID_PRESENT','ERROR',             			'Type Identifier shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_STATUS_STARTING_TIME_PRESENT',            'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_STATUS_STARTING_TIME_PRESENT','ERROR',          'Status Starting date & time shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_STATUS_PRESENT',                 			'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_STATUS_PRESENT','ERROR',             			'Status shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_NAME_PRESENT',                 			'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_NAME_PRESENT','ERROR',             				'Service Name shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_DIGITAL_IDENTITIES_PRESENT',              		'LEGAL',    'TSP_SERVICE', 	'IS_DIGITAL_IDENTITIES_PRESENT','ERROR',            		'Digital Identity shall be present except if the Service type is "nothavingPKIid".');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_TYPE_ID_CORRECT',                 		'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_TYPE_ID_CORRECT','ERROR',             			'Type Identifier not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_STATUS_CORRECT',                 			'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_STATUS_CORRECT','ERROR',             			'Status not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_STATUS_STARTING_TIME_ORDER',              'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_STATUS_STARTING_TIME_ORDER','ERROR',            'Current Status starting time shall be after Previous Status starting time.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER',   'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_AND_HISTORY_HAVE_SAME_TYPE_IDENTIFIER','ERROR',	'Type Identifier of the service shall be the same as the Type Identifier of its history.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME',      'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_AND_HISTORY_HAVE_SAME_SUBJECT_NAME','ERROR',    'Subject Name of the service shall be the same as the Subject Name of its history.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI',           'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_AND_HISTORY_HAVE_SAME_X509SKI','ERROR',         'X509SKI of Digital Identity of the Service and its history shall be the same.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_X509CERTIFICATE_ORGANIZATION_MATCH',              'LEGAL',    'TSP_SERVICE', 	'IS_X509CERTIFICATE_ORGANIZATION_MATCH','ERROR',            'Certificate Organization shall match with the Service Provider Name or Trade name.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_QUALIFIER_URI_CORRECT_VALUE',              		'LEGAL',    'TSP_SERVICE', 	'IS_QUALIFIER_URI_CORRECT_VALUE','ERROR',            		'Qualifier URI not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_EXTENSION_ASI_ALLOWED',                 	'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_EXTENSION_ASI_ALLOWED','INFO',             	'Additional Service Information extension may be useful for the following services: CA(PKC), OCSP, CRL, PSES, AdESValidation, AdESGeneration.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED',   		'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED','INFO',		'Additional Service Information extension may be useful for the following services: PSES(Q), QESValidation(Q).');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_QUALIFICATION_EXTENSION_ALLOWED',      			'LEGAL',    'TSP_SERVICE', 	'IS_QUALIFICATION_EXTENSION_ALLOWED','ERROR',    			'Qualification extensions shall not be used for services different than CA(QC).');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_EXPIRED_CERT_REVOC_INFO_EXTENSION_ALLOWED',       'LEGAL',    'TSP_SERVICE', 	'IS_EXPIRED_CERT_REVOC_INFO_EXTENSION_ALLOWED','ERROR',     'Expired Certificate Revocation Date extension shall be present for the following services: CA(PKC), OCSP, CRL, CA(QC), NationalRootCA-QC, Certstatus/OCSP/QC, Certstatus/CRL/QC.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE.IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC',      	'LEGAL',    'TSP_SERVICE', 	'IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC','ERROR',    	'TSP Definition URI shall be present for a NationalRootCA-QC service');

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE_PROVIDER.IS_TSP_NAME_PRESENT',           'LEGAL',    'TSP_SERVICE_PROVIDER', 	'IS_TSP_NAME_PRESENT','ERROR',        	 	'TSP Name shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE_PROVIDER.IS_TSP_TRADE_NAME_PRESENT',     'LEGAL',    'TSP_SERVICE_PROVIDER', 	'IS_TSP_TRADE_NAME_PRESENT','ERROR',       	'TSP Trade Name shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE_PROVIDER.IS_POSTAL_ADDRESS_PRESENT',     'LEGAL',    'TSP_SERVICE_PROVIDER', 	'IS_POSTAL_ADDRESS_PRESENT','ERROR',        'Postal address shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE_PROVIDER.IS_ELECTRONIC_ADDRESS_PRESENT', 'LEGAL',    'TSP_SERVICE_PROVIDER', 	'IS_ELECTRONIC_ADDRESS_PRESENT','ERROR',	'Electronic address shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('TSP_SERVICE_PROVIDER.IS_INFORMATION_URI_PRESENT',    'LEGAL',    'TSP_SERVICE_PROVIDER', 	'IS_INFORMATION_URI_PRESENT','ERROR',       'Information URI shall be present.');

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('LANGUAGE_FIELDS.IS_ATTRIBUTE_LANG_PRESENT',   	'LEGAL',    'LANGUAGE_FIELDS', 	'IS_ATTRIBUTE_LANG_PRESENT','ERROR',   	'Language attribute shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('LANGUAGE_FIELDS.IS_ATTRIBUTE_LANG_LOWERCASE',	'LEGAL',    'LANGUAGE_FIELDS', 	'IS_ATTRIBUTE_LANG_LOWERCASE','ERROR',	'Language attribute shall be in lowercase.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('LANGUAGE_FIELDS.IS_ATTRIBUTE_LANG_ALLOWED',   	'LEGAL',    'LANGUAGE_FIELDS', 	'IS_ATTRIBUTE_LANG_ALLOWED','ERROR',    'Language attribute not found in properties.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('LANGUAGE_FIELDS.IS_LIST_CONTAIN_LANG_EN',    	'LEGAL',    'LANGUAGE_FIELDS', 	'IS_LIST_CONTAIN_LANG_EN','ERROR',      'List should contain at least "en" language.');

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('URI_FIELDS.IS_URIS_VALID',   	'LEGAL',    'URI_FIELDS', 	'IS_URIS_VALID','WARNING',   		'URI is unvalid.');
/*INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('URI_FIELDS.IS_URIS_ACCESSIBLE',	'LEGAL',    'URI_FIELDS', 	'IS_URIS_ACCESSIBLE','WARNING',		'URI is unaccessible.');*/

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POSTAL_ADDRESSES.IS_POSTAL_ADDRESSES_STREET_NOT_EMPTY',	'LEGAL',    'POSTAL_ADDRESSES', 	'IS_POSTAL_ADDRESSES_STREET_NOT_EMPTY','ERROR',		'Postal address street shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POSTAL_ADDRESSES.IS_POSTAL_ADDRESSES_LOCALITY_NOT_EMPTY','LEGAL',    'POSTAL_ADDRESSES', 	'IS_POSTAL_ADDRESSES_LOCALITY_NOT_EMPTY','ERROR',	'Postal address locality shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POSTAL_ADDRESSES.IS_POSTAL_ADDRESSES_COUNTRY_NOT_EMPTY',	'LEGAL',    'POSTAL_ADDRESSES', 	'IS_POSTAL_ADDRESSES_COUNTRY_NOT_EMPTY','ERROR',	'Postal address country shall be present.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('POSTAL_ADDRESSES.IS_POSTAL_ADDRESSES_COUNTRY_ALLOWED',	'LEGAL',    'POSTAL_ADDRESSES', 	'IS_POSTAL_ADDRESSES_COUNTRY_ALLOWED','ERROR',		'Postal address country not found in properties.');

INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('DIGITAL_IDENTITY.IS_VALID_CERTIFICATE',	'LEGAL',    'DIGITAL_IDENTITY', 	'IS_VALID_CERTIFICATE','ERROR',		'The X509Certificate element MUST contain a valid Base64 certificate.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('DIGITAL_IDENTITY.IS_SUBJECT_NAME_MATCH',	'LEGAL',    'DIGITAL_IDENTITY', 	'IS_SUBJECT_NAME_MATCH','ERROR',	'The X509SubjectName element MUST match the SubjectName of the X509Certificate.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('DIGITAL_IDENTITY.IS_X509SKI_MATCH',		'LEGAL',    'DIGITAL_IDENTITY', 	'IS_X509SKI_MATCH','ERROR',			'The X509SKI element MUST match the X509SKI of the X509Certificate.');
INSERT INTO TL_CHECKS (CHECK_ID, IMPACT, TARGET, NAME, PRIORITY, DESCRIPTION) VALUES ('DIGITAL_IDENTITY.IS_CERTIFICATE_EXPIRED','LEGAL',    'DIGITAL_IDENTITY', 	'IS_CERTIFICATE_EXPIRED','ERROR',	'The X509Certificate has expired.');