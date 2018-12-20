digitTslWeb.controller('systemController', [ '$scope', '$modal', 'httpFactory', 'showModal', 'appConstant', function($scope, $modal, httpFactory, showModal, appConstant) {

    initMessages($scope);

    $scope.callRules = function() {
        httpFactory.get("/api/jobs/rulesValidation", appConstant.systemController.rulesFailure).then(function() {
        });
    };

    $scope.callSignature = function() {
        httpFactory.get("/api/jobs/signatureValidation", appConstant.systemController.signatureFailure).then(function() {
        });
    };

    $scope.callRetention = function() {
        httpFactory.get("/api/jobs/retentionPolicy", appConstant.systemController.retentionFailure).then(function() {
        });
    };

    $scope.callSignatureAlert = function() {
        httpFactory.get("/api/jobs/signatureAlert", appConstant.systemController.signatureAlertFailure).then(function() {
        });
    };

    $scope.cacheCountries = function() {
        httpFactory.get("/api/cache/countries", appConstant.systemController.cacheCountriesFailure).then(function() {
            showModal.information(appConstant.systemController.cacheCountriesSuccess);
        });
    };

    $scope.cacheProperties = function() {
        httpFactory.get("/api/cache/properties", appConstant.systemController.cachePropertiesFailure).then(function() {
            showModal.information(appConstant.systemController.cachePropertiesSuccess);
        });
    };

    $scope.cacheChecks = function() {
        httpFactory.get("/api/cache/check", appConstant.systemController.cacheChecksFailure).then(function() {
            showModal.information(appConstant.systemController.cacheChecksSuccess);
        });
    };

} ]);
