	var convertAsn1 = function(tCertificate){
		tCertificate.asn1 = convert(tCertificate.certB64);
		return tCertificate; 
	};
			
			
	/*--------------------------- ASN1 ---------------------------*/
			
	var ID   = new Array();
	var NAME = new Array();
	
	ID['BOOLEAN']          = 0x01;
	ID['INTEGER']          = 0x02;
	ID['BITSTRING']        = 0x03;
	ID['OCTETSTRING']      = 0x04;
	ID['NULL']             = 0x05;
	ID['OBJECTIDENTIFIER'] = 0x06;
	ID['ObjectDescripter'] = 0x07;
	ID['UTF8String']       = 0x0c;
	ID['SEQUENCE']         = 0x10;
	ID['SET']              = 0x11;
	ID['NumericString']    = 0x12;
	ID['PrintableString']  = 0x13;
	ID['TeletexString']    = 0x14;
	ID['IA5String']        = 0x16;
	ID['UTCTime']          = 0x17;
	ID['GeneralizedTime']  = 0x18;
	
	var i;
	for ( i in ID ){
	  NAME[ID[i]] = i;
	}
	
	var OID = new Array();
	var valueOID ="2.1.10.0,extension 0.2.262.1.10.1.1,signature 1.2.840.113549.1.1,pkcs-1 1.2.840.113549.1.1.1,rsaEncryption 1.2.840.113549.1.1.4,md5withRSAEncryption 1.2.840.113549.1.1.5,sha1withRSAEncryption 1.2.840.113549.1.1.6,rsaOAEPEncryptionSET 1.2.840.113549.1.7,pkcs-7 1.2.840.113549.1.7.1,data 1.2.840.113549.1.7.2,signedData 1.2.840.113549.1.7.3,envelopedData 1.2.840.113549.1.7.4,signedAndEnvelopedData 1.2.840.113549.1.7.5,digestedData 1.2.840.113549.1.7.6,encryptedData 1.2.840.113549.1.7.7,dataWithAttributes 1.2.840.113549.1.7.8,encryptedPrivateKeyInfo 1.2.840.113549.1.9.22.1,x509Certificate(for.PKCS.#12) 1.2.840.113549.1.9.23.1,x509Crl(for.PKCS.#12) 1.2.840.113549.1.9.3,contentType 1.2.840.113549.1.9.4,messageDigest 1.2.840.113549.1.9.5,signingTime 2.16.840.1.113730.1,cert-extension 2.16.840.1.113730.1.1,netscape-cert-type 2.16.840.1.113730.1.12,netscape-ssl-server-name 2.16.840.1.113730.1.13,netscape-comment 2.16.840.1.113730.1.2,netscape-base-url 2.16.840.1.113730.1.3,netscape-revocation-url 2.16.840.1.113730.1.4,netscape-ca-revocation-url 2.16.840.1.113730.1.7,netscape-cert-renewal-url 2.16.840.1.113730.1.8,netscape-ca-policy-url 2.23.42.0,contentType 2.23.42.1,msgExt 2.23.42.10,national 2.23.42.2,field 2.23.42.2.0,fullName 2.23.42.2.1,givenName 2.23.42.2.10,amount 2.23.42.2.2,familyName 2.23.42.2.3,birthFamilyName 2.23.42.2.4,placeName 2.23.42.2.5,identificationNumber 2.23.42.2.6,month 2.23.42.2.7,date 2.23.42.2.7.11,accountNumber 2.23.42.2.7.12,passPhrase 2.23.42.2.8,address 2.23.42.3,attribute 2.23.42.3.0,cert 2.23.42.3.0.0,rootKeyThumb 2.23.42.3.0.1,additionalPolicy 2.23.42.4,algorithm 2.23.42.5,policy 2.23.42.5.0,root 2.23.42.6,module 2.23.42.7,certExt 2.23.42.7.0,hashedRootKey 2.23.42.7.1,certificateType 2.23.42.7.2,merchantData 2.23.42.7.3,cardCertRequired 2.23.42.7.5,setExtensions 2.23.42.7.6,setQualifier 2.23.42.8,brand 2.23.42.9,vendor 2.23.42.9.22,eLab 2.23.42.9.31,espace-net 2.23.42.9.37,e-COMM 2.5.29.1,authorityKeyIdentifier 2.5.29.10,basicConstraints 2.5.29.11,nameConstraints 2.5.29.12,policyConstraints 2.5.29.13,basicConstraints 2.5.29.14,subjectKeyIdentifier 2.5.29.15,keyUsage 2.5.29.16,privateKeyUsagePeriod 2.5.29.17,subjectAltName 2.5.29.18,issuerAltName 2.5.29.19,basicConstraints 2.5.29.2,keyAttributes 2.5.29.20,cRLNumber 2.5.29.21,cRLReason 2.5.29.22,expirationDate 2.5.29.23,instructionCode 2.5.29.24,invalidityDate 2.5.29.25,cRLDistributionPoints 2.5.29.26,issuingDistributionPoint 2.5.29.27,deltaCRLIndicator 2.5.29.28,issuingDistributionPoint 2.5.29.29,certificateIssuer 2.5.29.3,certificatePolicies 2.5.29.30,nameConstraints 2.5.29.31,cRLDistributionPoints 2.5.29.32,certificatePolicies 2.5.29.33,policyMappings 2.5.29.34,policyConstraints 2.5.29.35,authorityKeyIdentifier 2.5.29.36,policyConstraints 2.5.29.37,extKeyUsage 2.5.29.4,keyUsageRestriction 2.5.29.5,policyMapping 2.5.29.6,subtreesConstraint 2.5.29.7,subjectAltName 2.5.29.8,issuerAltName 2.5.29.9,subjectDirectoryAttributes 2.5.4.0,objectClass 2.5.4.1,aliasedEntryName 2.5.4.10,organizationName 2.5.4.10.1,collectiveOrganizationName 2.5.4.11,organizationalUnitName 2.5.4.11.1,collectiveOrganizationalUnitName 2.5.4.12,title 2.5.4.13,description 2.5.4.14,searchGuide 2.5.4.15,businessCategory 2.5.4.16,postalAddress 2.5.4.16.1,collectivePostalAddress 2.5.4.17,postalCode 2.5.4.17.1,collectivePostalCode 2.5.4.18,postOfficeBox 2.5.4.18.1,collectivePostOfficeBox 2.5.4.19,physicalDeliveryOfficeName 2.5.4.19.1,collectivePhysicalDeliveryOfficeName 2.5.4.2,knowledgeInformation 2.5.4.20,telephoneNumber 2.5.4.20.1,collectiveTelephoneNumber 2.5.4.21,telexNumber 2.5.4.21.1,collectiveTelexNumber 2.5.4.22.1,collectiveTeletexTerminalIdentifier 2.5.4.23,facsimileTelephoneNumber 2.5.4.23.1,collectiveFacsimileTelephoneNumber 2.5.4.25,internationalISDNNumber 2.5.4.25.1,collectiveInternationalISDNNumber 2.5.4.26,registeredAddress 2.5.4.27,destinationIndicator 2.5.4.28,preferredDeliveryMehtod 2.5.4.29,presentationAddress 2.5.4.3,commonName 2.5.4.31,member 2.5.4.32,owner 2.5.4.33,roleOccupant 2.5.4.34,seeAlso 2.5.4.35,userPassword 2.5.4.36,userCertificate 2.5.4.37,caCertificate 2.5.4.38,authorityRevocationList 2.5.4.39,certificateRevocationList 2.5.4.4,surname 2.5.4.40,crossCertificatePair 2.5.4.41,name 2.5.4.42,givenName 2.5.4.43,initials 2.5.4.44,generationQualifier 2.5.4.45,uniqueIdentifier 2.5.4.46,dnQualifier 2.5.4.47,enhancedSearchGuide 2.5.4.48,protocolInformation 2.5.4.49,distinguishedName 2.5.4.5,serialNumber 2.5.4.50,uniqueMember 2.5.4.51,houseIdentifier 2.5.4.52,supportedAlgorithms 2.5.4.53,deltaRevocationList 2.5.4.55,clearance 2.5.4.58,crossCertificatePair 2.5.4.6,countryName 2.5.4.7,localityName 2.5.4.7.1,collectiveLocalityName 2.5.4.8,stateOrProvinceName 2.5.4.8.1,collectiveStateOrProvinceName 2.5.4.9,streetAddress 2.5.4.9.1,collectiveStreetAddress 2.5.6.0,top 2.5.6.1,alias 2.5.6.10,residentialPerson 2.5.6.11,applicationProcess 2.5.6.12,applicationEntity 2.5.6.13,dSA 2.5.6.14,device 2.5.6.15,strongAuthenticationUser 2.5.6.16,certificateAuthority 2.5.6.17,groupOfUniqueNames 2.5.6.2,country 2.5.6.21,pkiUser 2.5.6.22,pkiCA 2.5.6.3,locality 2.5.6.4,organization 2.5.6.5,organizationalUnit 2.5.6.6,person 2.5.6.7,organizationalPerson 2.5.6.8,organizationalRole 2.5.6.9,groupOfNames 2.5.8,X.500-Algorithms 2.5.8.1,X.500-Alg-Encryption 2.5.8.1.1,rsa 2.54.1775.2,hashedRootKey 2.54.1775.3,certificateType 2.54.1775.4,merchantData 2.54.1775.5,cardCertRequired 2.54.1775.7,setQualifier 2.54.1775.99,set-data 1.2.840.10045.4.3.1,ecdsa-with-Sha224 1.2.840.10045.4.3.2,ecdsa-with-Sha256 1.2.840.10045.4.3.3,ecdsa-with-Sha384 1.2.840.10045.4.3.4,ecdsa-with-Sha512 1.2.840.10045.2.1,ecPublicKey 1.2.840.10045.3.1.7,prime256v1 1.2.840.113533.7.65.0,entrustVersInfo 2.23.136.1.1.3,id-icao-cscaMasterListSigningKey 1.3.14.3.2.26,sha1 2.16.840.1.101.3.4.2.4,sha224 2.16.840.1.101.3.4.2.1,sha256 2.16.840.1.101.3.4.2.2,sha384 2.16.840.1.101.3.4.2.3,sha512 1.2.840.113549.1.1.8,id-mgf1 2.5.6.19,cRLDistributionPoint 2.5.8.1.1,rsa 2.5.8.2.1,sqMod_n 2.5.8.3.1,sqMod_nWithRSA 1.3.14.3.2.6,desECB 1.3.14.3.2.7,desCBC 1.3.14.3.2.8,desOFB 1.3.14.3.2.9,desCFB 1.3.14.3.2.10,desMAC 1.3.14.3.2.12,dsa 1.3.14.7.2.1.1,elGamal 1.3.14.7.2.3.2,md2WithElGamal 1.2.840.113549.2.2,md2 1.2.840.113549.2.5,md5 1.2.840.113549.1.1.1,rsaEncryption 1.2.840.113549.1.1.7,rsaOAEP 1.2.840.10040.4.1,dsaX957 1.2.840.10045.2.1,ecPublicKey 1.2.840.10045.2.2,ecPublicKeyRestricted 1.3.132.1.12,ecDH 1.3.132.1.13,ecMQV 1.2.840.10045.3.1.1,P-192 1.3.132.0.33,P-224 1.2.840.10045.3.1.7,P-256 1.3.132.0.34,P-384 1.3.132.0.35,P-521 1.3.132.0.1,K-163 1.3.132.0.15,B-163 1.3.132.0.26,K-233 1.3.132.0.27,B-233 1.3.132.0.16,K-283 1.3.132.0.17,B-283 1.3.132.0.36,K-409 1.3.132.0.37,B-409 1.3.132.0.38,K-571 1.3.132.0.39,B-571 1.3.132.0.9,ansix9p160k1 1.3.132.0.8,ansix9p160r1 1.3.132.0.30,ansix9p160r2 1.3.132.0.31,ansix9p192k1 1.3.132.0.32,ansix9p224k1 1.3.132.0.10,ansix9p256k1 1.3.132.0.2,ansix9t163r1 1.3.132.0.24,ansix9t193r1 1.3.132.0.25,ansix9t193r2 1.3.132.0.3,ansix9t239k1 1.3.36.3.3.2.8.1.1.1,brainpoolP160r1 1.3.36.3.3.2.8.1.1.2,brainpoolP160t1 1.3.36.3.3.2.8.1.1.3,brainpoolP192r1 1.3.36.3.3.2.8.1.1.4,brainpoolP192t1 1.3.36.3.3.2.8.1.1.5,brainpoolP224r1 1.3.36.3.3.2.8.1.1.6,brainpoolP224t1 1.3.36.3.3.2.8.1.1.7,brainpoolP256r1 1.3.36.3.3.2.8.1.1.8,brainpoolP256t1 1.3.36.3.3.2.8.1.1.9,brainpoolP320r1 1.3.36.3.3.2.8.1.1.10,brainpoolP320t1 1.3.36.3.3.2.8.1.1.11,brainpoolP384r1 1.3.36.3.3.2.8.1.1.12,brainpoolP384t1 1.3.36.3.3.2.8.1.1.13,brainpoolP512r1 1.3.36.3.3.2.8.1.1.14,brainpoolP512t1 2.16.840.1.114027.80.2.1,id-PKIXCMP-stdECDHwithX963SHA1 1.2.840.113549.1.1.2,md2WithRSAEncryption 1.2.840.113549.1.1.4,md5WithRSAEncryption 1.2.840.113549.1.1.5,sha1WithRSAEncryption 1.2.840.113549.1.1.14,sha224WithRSAEncryption 1.2.840.113549.1.1.11,sha256WithRSAEncryption 1.2.840.113549.1.1.12,sha384WithRSAEncryption 1.2.840.113549.1.1.13,sha512WithRSAEncryption 1.2.840.113549.1.1.10,id-RSASSA-PSS 1.2.840.10040.4.3,dsa-with-sha1X957 1.2.840.10045.4.1,ecdsa-with-sha1 1.2.840.10045.4.2,ecdsa-with-Recommended 1.2.840.10045.4.3,ecdsa-with-Specified 1.2.840.10045.4.3.1,ecdsa-with-SHA224 1.2.840.10045.4.3.2,ecdsa-with-SHA256 1.2.840.10045.4.3.3,ecdsa-with-SHA384 1.2.840.10045.4.3.4,ecdsa-with-SHA512 1.3.14.7.2.3.1,md2WithRsa 1.3.14.3.2.2,md4WithRSA 1.3.14.3.2.3,md5WithRSA 1.3.14.3.2.4,md4WithRSAEncryption 1.3.14.3.2.29,sha1WithRSASignature 1.3.14.3.2.27,dsa-with-sha1 1.2.840.113533.7.68.0,entrustCAInfo 1.2.840.113533.7.68.16,entrustPwordPolicy 1.2.840.113533.7.68.29,entrustUserRole 1.2.840.113533.7.77.0,entrustRoleMap 1.2.840.113533.7.77.1,entrustPasswordRules 1.2.840.113533.7.77.3,entrustAllowedSymmetricAlgms 1.2.840.113533.7.77.4,entrustAllowedHashAlgms 1.2.840.113533.7.77.5,entrustCSetFlags 1.2.840.113533.7.77.6,entrustMessageOfTheDay 1.2.840.113533.7.77.7,entrustAttrName 1.2.840.113533.7.77.8,entrustApplicationFlags 1.2.840.113533.7.77.9,entrustSignKeyType 1.2.840.113533.7.77.10,entrustEncKeyType 1.2.840.113533.7.77.11,entrustBusCtrlPolOids 1.2.840.113533.7.77.12,entrustBusCtrlFlags 1.2.840.113533.7.77.13,entrustPCertLifetime 1.2.840.113533.7.77.14,entrustDNEncoding 1.2.840.113533.7.77.15,entrustCertConsistencyChecking 1.2.840.113533.7.77.16,entrustUserEncAlgm 1.2.840.113533.7.77.17,entrustCRLGracePeriod 1.2.840.113533.7.77.18,entrustSkipRLChecks 1.2.840.113533.7.77.19,entrustHTTPProxySetting 1.2.840.113533.7.77.20,entrustOfflineProfileUse 1.2.840.113533.7.77.21,entrustAllowServerLogin 1.2.840.113533.7.77.22,entrustEnforceIdentityUse 1.2.840.113533.7.77.23,entrustAllowPKCS12Export 1.2.840.113533.7.77.24,entrustPKCS12ExportMinimumHashCount 1.2.840.113533.7.77.25,entrustClientNKeyType 1.2.840.113533.7.77.26,entrustAllowed3rdPartySymmetricAlgms 1.2.840.113533.7.77.27,entrustPreventManualAppRegistration 1.2.840.113533.7.77.28,entrustPasswordMaxAttempts 1.2.840.113533.7.77.29,entrustPasswordMinTime 1.2.840.113533.7.77.30,entrustPasswordMinSuspend 1.2.840.113533.7.77.31,entrustAllowCAPIExport 1.2.840.113533.7.77.32,entrustICEAdminPolicy 1.2.840.113533.7.77.33,entrustEnableCacheUsage 1.2.840.113533.7.77.34,entrustUserEncAlgm2 1.2.840.113533.7.77.35,entrustSecureDeliveryServiceSMTP 1.2.840.113533.7.77.36,entrustContentScannerServiceSMTP 1.2.840.113533.7.77.37,entrustExpressSearchSourceOrder 1.2.840.113533.7.77.38,entrustCAPIPolicy 1.2.840.113533.7.77.39,entrustSearchbaseSearchOrder 1.2.840.113533.7.77.40,entrustCRLGracePercentage 1.2.840.113533.7.77.49,entrustPublicTokenCerts 1.2.840.113533.7.77.50,entrustProtectKeyTransfer 1.2.840.113533.7.77.57,entrustAllowTokenSpilloverFile 1.2.840.113533.7.77.58,entrustMaximumTokenKeyHistory 1.2.840.113533.7.77.59,entrustSelfRevokePolicy 1.2.840.113533.7.77.60,entrustAllowPSSwitch 1.2.840.113533.7.77.61,entrustManagementClient 1.2.840.113533.7.77.62,entrustForceOriginalCDPolicyCompliance 1.2.840.113533.7.77.63,entrustAllExportable 1.2.840.113533.7.77.64,entrustProtocolSymmetricEncAlgs 1.2.840.113533.7.77.65,entrustProtocolSigningAlgs 1.2.840.113533.7.67.0,entrustUser 1.2.840.113533.7.67.1,entrustCA 1.2.840.113533.7.68.10,attributeCertificate 1.2.840.113533.7.65.0,entrustVersInfo 1.2.840.113533.7.65.1,clearance 1.2.840.113533.7.65.2,noCRL 1.2.840.113533.7.66.0,cast40CBC 1.2.840.113533.7.66.1,cast64CBC 1.2.840.113533.7.66.2,cast64MAC 1.3.6.1.5.5.1.1,SPKM1 1.3.6.1.5.5.1.2,SPKM2 2.16.840.1.113730,netscape 2.16.840.1.113730.1,netscapeCertExtension 2.16.840.1.113730.1.1,netscapeCertType 2.16.840.1.113730.1.2,netscapeBaseUrl 2.16.840.1.113730.1.3,netscapeRevocationUrl 2.16.840.1.113730.1.4,netscapeCaRevocationUrl 2.16.840.1.113730.1.7,netscapeCertRenewalUrl 2.16.840.1.113730.1.8,netscapeCaPolicyUrl 2.16.840.1.113730.1.12,netscapeSslServerName 2.16.840.1.113730.1.13,netscapeComment 1.2.3.4.9999,timeStamp 2.23.42.7.0,hashedRootKey 2.23.42.7.1,setCertificateType 2.16.840.1.114027.10.4,entrustAdminServicesClients 2.16.840.1.114027.10.5,entrustAdminServicesServer 1.3.133.16.840.63.0.2,dhSinglePass-stdDH-sha1kdf-scheme 1.3.132.1.11.0,dhSinglePass-stdDH-sha224kdf-scheme 1.3.132.1.11.1,dhSinglePass-stdDH-sha256kdf-scheme 1.3.132.1.11.2,dhSinglePass-stdDH-sha384kdf-scheme 1.3.132.1.11.3,dhSinglePass-stdDH-sha512kdf-scheme 1.3.133.16.840.63.0.3,dhSinglePass-cofactorDH-sha1kdf-scheme 1.3.132.1.14.0,dhSinglePass-cofactorDH-sha224kdf-scheme 1.3.132.1.14.1,dhSinglePass-cofactorDH-sha256kdf-scheme 1.3.132.1.14.2,dhSinglePass-cofactorDH-sha384kdf-scheme 1.3.132.1.14.3,dhSinglePass-cofactorDH-sha512kdf-scheme 1.3.133.16.840.63.0.16,mqvSinglePass-sha1kdf-scheme 1.3.132.1.15.0,mqvSinglePass-sha224kdf-scheme 1.3.132.1.15.1,mqvSinglePass-sha256kdf-scheme 1.3.132.1.15.2,mqvSinglePass-sha384kdf-scheme 1.3.132.1.15.3,mqvSinglePass-sha512kdf-scheme 0.9.2342.19200300.100.1.1,id_userid 0.9.2342.19200300.100.1.3,id_rfc822Mailbox 0.9.2342.19200300.100.1.25,id_domainComponent 2.16.840.1.101.3.4.1.5,id-aes128-wrap 2.16.840.1.101.3.4.1.8,id-aes128-wrap-pad 2.16.840.1.101.3.4.1.25,id-aes192-wrap 2.16.840.1.101.3.4.1.28,id-aes192-wrap-pad 2.16.840.1.101.3.4.1.45,id-aes256-wrap 2.16.840.1.101.3.4.1.48,id-aes256-wrap-pad 1.2.840.113549.1.9.16.3.6,id-alg-CMS3DESwrap 2.16.840.1.114027.80.2.1,id-PKIXCMP-stdECDHwithX963SHA1 1.2.840.113549.1.1.1,rsaEncryption 1.2.840.113549.1.1.2,md2WithRSAEncryption 1.2.840.113549.1.1.4,md5WithRSAEncryption 1.2.840.113549.1.1.5,sha1WithRSAEncryption 1.2.840.113549.1.1.7,id_RSAES_OAEP 1.2.840.113549.1.1.8,id_mgf1 1.2.840.113549.1.1.9,id_pSpecified 1.2.840.113549.1.1.10,id_RSASSA_PSS 1.2.840.113549.1.9.1,emailAddress 1.2.840.113549.2.2,md2 1.2.840.113549.2.5,md5 1.3.14.3.2.26,id_sha1 1.3.14.3.2.29,sha1WithRSASignature 1.3.14.3.2.27,dsaWithSHA1 1.2.840.10040.4.1,id_dsa 1.2.840.10040.4.3,id_dsa_with_sha1 1.2.840.10046.2.1,dhpublicnumber 1.2.840.113549.3.7,id_des_EDE3_CBC"
	
	var TAB = "                              ";
	var TAB_num = -1;
	
	var Bitstring_hex_limit = 4;
	
	var isEncode = new RegExp("[^0-9a-zA-Z\/=+]", "i");
	var isB64    = new RegExp("[^0-9a-fA-F]", "i");
	
	function convert(src){
		init_oid();
	  var srcValue = src.replace(/[\s\r\n]/g, '');
	
	    if ( srcValue.match(isEncode) ){
	      if ( confirm("Illegal character for Decoding process.\nDo you wish to continue as Encoding process?") ){
	        return encode(srcValue);
	      }
	      else{
	        return;
	      }
	    }
	    return decode(bin2hex(base64decode(srcValue)));
	}
	
	function encode(src){
	  var ans;
	  return ans;
	}
	
	function decode(src)
	{
	  if ( src.length % 2 != 0 )
	  {
	    util.err("Illegal length. Hex string's length must be even.");
	    return "";
	  }
	  else
	  { return readASN1(src); }
	}
	
	function readASN1(data){
	  var point = 0;
	  var ret = "";
	  TAB_num++;
	
	  while ( point < data.length ){
	
	    // Detecting TAG field (Max 1 octet)
	
	    var tag10 = parseInt("0x" + data.substr(point, 2));
	    var isSeq = tag10 & 32;
	    var isContext = tag10 & 128;
	    var tag = tag10 & 31;
	    var tagName = isContext ? "[" + tag + "]" : NAME[tag];
	    if ( tagName == undefined ){
	      tagName = "[" + data.substr(point, 2) +"]";
	    }
	
	    point += 2;
	    
	    // Detecting LENGTH field (Max 2 octets)
	
	    var len = 0;
	    if ( tag != 0x5){  // Ignore NULL
	      if ( parseInt("0x" + data.substr(point, 2)) & 128 ){
	        var lenLength = parseInt("0x" + data.substr(point, 2)) & 127;
	        if ( lenLength > 2 )
	        {
	          util.err("LENGTH field is too long.(at "+point+")\nWe accept up to 2 octets of Length field.");
	          return "";
	        }
	        len = parseInt("0x" + data.substr( point+2, lenLength*2));
	        point += lenLength*2 + 2;  // Special thanks to Mr.(or Ms.) T (Mon, 25 Nov 2002 23:49:29)
	      }
	      else if ( lenLength != 0 ){  // Special thanks to Mr.(or Ms.) T (Mon, 25 Nov 2002 23:49:29)
	        len = parseInt("0x" + data.substr(point,2));
	        point += 2;
	      }
	      
	      if ( len > data.length - point )
	      {
	        util.err("LENGTH is longer than the rest.\n(LENGTH: "+len+", rest: "+data.length+")");
	        return "";
	      }
	    }
	    else{
	      point += 2;
	    }
	
	    // Detecting VALUE
	    
	    var val = "";
	    var tab = TAB.substr(0, TAB_num*3);
	    if ( len ){
	      val = data.substr( point, len*2);
	      point += len*2;
	    }
	
	    ret += tab + tagName + " ";
	    ret += ( isSeq ) ? "{\n" + readASN1(val) + tab + "}" : getValue( isContext ? 4 : tag , val);
	    ret += "\n";
	  };
	  
	  TAB_num -= 1;
	  return ret;
	}
	
	function getValue(tag, data)
	{
	  var ret = "";
	  
	  if (tag == 1)
	  {
	    ret = data ? 'TRUE' : 'FALSE';
	  }
	  else if (tag == 2)
	  {
	     ret = "0x" + data;
	     if ( data.length < 12 // arbitrary, parseInt can handle bigger
	       && data.length > 0  // don't bother if there is nothing
	       && data[0] != "8"   // if the leading bit is a one then this is a negative number and parseInt will give the wrong value
	       && data[0] != "9"
	       && data[0] != "a" && data[0] != "A"
	       && data[0] != "b" && data[0] != "B"
	       && data[0] != "c" && data[0] != "C"
	       && data[0] != "d" && data[0] != "D"
	       && data[0] != "e" && data[0] != "E"
	       && data[0] != "f" && data[0] != "F" )
	     { ret += " (" + parseInt(data,16) + " decimal)"; }
	  }
	  else if (tag == 3){
	    var unUse = parseInt("0x" + data.substr(0, 2));
	    var bits  = data.substr(2);
	    
	    if ( bits.length > Bitstring_hex_limit ){
	      ret = "0x" + bits;
	    }
	    else{
	      ret = parseInt("0x" + bits).toString(2);
	    }
	    ret += " : " + unUse + " unused bit(s)";
	  }
	  else if (tag == 5){
	    ret = "";
	  }
	  else if (tag == 6){
	    var res = new Array();
	    var d0 = parseInt("0x" + data.substr(0, 2));
	    res[0] = Math.floor(d0 / 40);
	    res[1] = d0 - res[0]*40;
	    
	    var stack = new Array();
	    var powNum = 0;
	    var i;
	    for(i=1; i < data.length -2; i=i+2){
	      var token = parseInt("0x" + data.substr(i+1,2));
	      stack.push(token & 127);
	      
	      if ( token & 128 ){
	        powNum++;
	      }
	      else{
	        var j;
	        var sum = 0;
	        for (j in stack){
	          sum += stack[j] * Math.pow(128, powNum);
	          powNum-=1;
	        }
	        res.push( sum );
	        powNum = 0;
	        stack = new Array();
	      }
	    }
	    ret = res.join(".");
	    if ( OID[ret] ) {
	      ret += " (" + OID[ret] + ")";
	    }
	  }
	  else if ( NAME[tag] != undefined && NAME[tag].match(/(Time|String)$/) ) {
	    var k = 0;
	    ret += "'";
	    while ( k < data.length ){
	      ret += String.fromCharCode("0x"+data.substr(k, 2));
	      k += 2;
	    }
	    ret += "'";
	    if (NAME[tag] == "UTF8String")
	    {
	      ret = decodeURIComponent( escape( ret ) );
	    }
	  }
	  else{
	    ret = data;
	  }
	  return ret;
	}
	
	function init_oid(){
	  var lines = new Array();
	  lines = valueOID.split(' ');
	  
	  var i;
	  for ( i in lines ){
	    var item = new Array();
	    item = lines[i].split(/,/);
	    
	    var j;
	    for ( j in item ){
	      item[j] = item[j].replace(/^\s+/);
	      item[j] = item[j].replace(/\s+$/);
	    }
	    
	    
	    if ( item.length < 2 || item[0].match(/^#/) ){
	      continue;
	    }
	    
	    if ( item[0].match(/[^0-9\.\-\s]/) ){
	      OID[ item[1] ] = item[0];
	    }
	    else{
	      OID[ item[0] ] = item[1];
	    }
	  }
	}
	
	function bin2hex(bin){
	  var hex = "";
	  var i = 0;
	  var len = bin.length;
	  
	  while ( i < len ){
	    var h1 = bin.charCodeAt(i++).toString(16);
	    if ( h1.length < 2 ){
	      hex += "0";
	    }
	    hex += h1;
	  }
	
	  return hex;
	}
	
	/* I have copied the routine for decoding BASE64 from 
	   http://www.onicos.com/staff/iz/amuse/javascript/expert/base64.txt */
	
	var base64chr = new Array(
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
	    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
	    -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
	    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
	    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
	    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1);
	    
	function base64decode(str) {
	  var c1, c2, c3, c4;
	  var i, len, out;
	  len = str.length;
	  i = 0;
	  out = "";
	  while(i < len) {
	    /* c1 */
	    do {
	        c1 = base64chr[str.charCodeAt(i++) & 0xff];
	    } while(i < len && c1 == -1);
	    if(c1 == -1){ break; }
	
	    /* c2 */
	    do {
	        c2 = base64chr[str.charCodeAt(i++) & 0xff];
	    } while(i < len && c2 == -1);
	    if(c2 == -1){ break; }
	    out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
	
	    /* c3 */
	    do {
	        c3 = str.charCodeAt(i++) & 0xff;
	        if(c3 == 61) { return out; }
	        c3 = base64chr[c3];
	    } while(i < len && c3 == -1);
	    if(c3 == -1) { break; }
	    out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
	
	    /* c4 */
	    do {
	        c4 = str.charCodeAt(i++) & 0xff;
	        if(c4 == 61) { return out; }
	        c4 = base64chr[c4];
	    } while(i < len && c4 == -1);
	    if(c4 == -1) { break; }
	    out += String.fromCharCode(((c3 & 0x03) << 6) | c4);
	  }
	  return out;
	}