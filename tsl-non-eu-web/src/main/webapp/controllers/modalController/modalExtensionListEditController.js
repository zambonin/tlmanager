/** ****** TrustedList : Electronic Address Edition ******* */
function modalExtensionListEditController($scope, $modalInstance, tabs, myTLInfo,$modal,tabsetFactory) {
	initMessages($scope);

	$scope.myTabs = tabs;
	$scope.myTLInfo = angular.copy(myTLInfo);
	
	/**
	 * Validate
	 */
	$scope.ok = function() {
		$modalInstance.close($scope.myTabs);
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
	
	$scope.addTable = function(table) {
		var obj = new Object();
		obj.id = Math.floor(Math.random() * 100 + 1);
		table.push(obj);
		$scope.activeTab(table.length-1);
	};

	$scope.deleteTable = function(table,index) {
		table.splice(index, 1);
	};
	
	/**
	 * TabsetFocus (Factory)
	 */
	$scope.activeTab = function(index){
		$scope.myTabs = tabsetFactory.activeTab(index,$scope.myTabs);
	};
	$scope.activeTab(myTLInfo.index);
};