/**
 * Extensiondir : display service extension (+ change/checks)
 * Type : html tag
 * Parameter :
 ** extension - object : extension
 ** listcheck/listchange - array[object]: list used to display changes/checks
 ** edit - boolean (true or undefined) : used to apply different css style between browser & edition
 ** tl - object : trustedlist used to apply different css style
 */
digitTslWeb.directive('extensiondir',['extensionFactory', function(extensionFactory){
	return {
		restrict: 'EA',
        replace: true,
        transclude: true,
        scope : {
        	extension : '=',
        	listcheck : '=',
        	listchange : '=',
        	edit : '=',
        	tl : '='
        },
		templateUrl : 'serviceExtension',
		link : function($scope,element,attrs){
			initMessages($scope);

			$scope.extension.label = extensionFactory.extensionLabel($scope.extension);
			$scope.extension.show = false;

			/*------- DOM MANAGEMENT --------*/
			$scope.isOpen = function(obj){
				if(($scope.edit==undefined) && (obj=="1")){
					return "isOpen";
				}else{
					return "";
				};
			};

			$scope.tableVisibility = function(table){
				return false;
			};

			$scope.iconTableVisibility = function(table){
				if((table==undefined) || (table==null) || (table.length==0)){
					return true;
				}else{
					return false;
				};
			};

			$scope.carretIsOpen = function(){
				if($scope.edit==undefined){
					return "white";
				}else{
					return "";
				};
			};

			$scope.iconObjVisibility = function(obj){
				if((obj==undefined) || (obj==null) ){
					return true;
				}else{
					return false;
				};
			};

		}
	}
}]);
