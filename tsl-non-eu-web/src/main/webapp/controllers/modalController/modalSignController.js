/** ******* Modal to Sign a TrustedList *********** */
function modalSignController($scope, $modalInstance, myTLInfo, myTl, $modal, httpFactory, nexuFactory, showModal, accessRightFactory, appConstant) {
	initMessages($scope);

	$scope.myTLInfo = myTLInfo;
	$scope.myTl = myTl;
	$scope.signatureProccessStatus = "";

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};

	var runNexu = function() {
		$scope.signatureProccessStatus = appConstant.nexuLoading.checkVersion;
		nexuFactory.info().then(function() {
			$scope.signatureProccessStatus = appConstant.nexuLoading.getSmart;
			nexuFactory.certificates($scope.myTLInfo.id).then(function(certObj) {
				$scope.signatureProccessStatus = appConstant.nexuLoading.getTbs;
				httpFactory.post('/api/signature/nexU/getTbs/', certObj, appConstant.nexuLoading.sigError).then(function(nexuSignObj) {
					$scope.signatureProccessStatus = appConstant.nexuLoading.finalize;
					nexuFactory.sign(nexuSignObj, certObj).then(function(signObj) {
						httpFactory.post('/api/signature/nexU/sign/', signObj, appConstant.nexuLoading.sigError).then(function(signature) {
							$modalInstance.close(signature);
						}, function() {
							$modalInstance.close();
						});
					}, function() {
						$modalInstance.close();
					});
				}, function() {
					$modalInstance.close();
				});
			}, function() {
				$modalInstance.close();
			});
		}, function() {
			$modalInstance.close();
		});
	};
	runNexu();

};