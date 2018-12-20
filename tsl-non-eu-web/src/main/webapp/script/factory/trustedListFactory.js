/**
 * TrustedListFactory : interface between browserController & httpFactory
 */
digitTslWeb.factory('trustedListFactory', [ '$q', 'httpFactory', 'appConstant', 'showModal', '$window', function($q, httpFactory, appConstant, showModal, $window) {
    return {

        /**
         * Get TrustedList By ID
         */
        getTl : function(tlCookie) {
            var value = tlCookie.tlId;
            if (!isNaN(value) && function(x) {
                return (x | 0) === x;
            }(parseFloat(value))) {
                return httpFactory.put('/api/tl/', tlCookie, appConstant.trustedListFactory.getTlError);
            } else {
            	showModal.information(appConstant.trustedListFactory.tlIdIncorrect);
            }
        },

        /**
         * Get Signature Information By ID
         */
        getSignatureInfo : function(tlCookie) {
            return httpFactory.post('/api/tl/signatureInfo', tlCookie, appConstant.trustedListFactory.getSignatureError);
        },

        /**
         * Get Checks
         */
        getChecks : function(tlCookie, checkToRun) {
            var deferred = $q.defer();
            if (checkToRun) {
	            httpFactory.post('/api/checks/errors', tlCookie, appConstant.trustedListFactory.getCheckError).then(function(checks) {
	                deferred.resolve(checks);
	            }, function() {
	                deferred.reject(-1);
	            });
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        },
        
        getDrafts : function(draftStoreId) {
        	return httpFactory.get('/api/list/tlDraft/' + draftStoreId, appConstant.trustedListFactory.getAllDraftsError)
        },
        
        getChangesDraft : function(currId, compId) {
            return httpFactory.get('/api/changes/draft/' + currId + '/' + compId, appConstant.trustedListFactory.getChangesDraftError);
        },

        getLastEdit : function(tlId) {
            return httpFactory.get('/api/tl/edtDate/' + tlId, appConstant.trustedListFactory.getLastEditedError);
        },

        conflictTl : function(tl) {
            return httpFactory.put('/api/tl/conflict/', tl, appConstant.trustedListFactory.conflictTlFailure);
        },

        switchCheckToRun : function(tlCookie) {
            return httpFactory.put('/api/tl/switchCheckToRun', tlCookie, appConstant.trustedListFactory.switchCheckToRunError);
        },

        runAllRules : function(tlCookie) {
            return httpFactory.put('/api/checks/cleanAndRunAllRules', tlCookie, appConstant.trustedListFactory.getAllCheckError);
        },

    };

} ]);
