digitTslWeb.controller('draftController',[ '$scope','$http','$modal','$timeout','$window','$parse','httpFactory','$window','showModal','$location',
                                   		function($scope,$http, $modal,$timeout,$window,$parse,httpFactory,$window,showModal,$location) {

		    $scope.getApplicationProperties();
		    $scope.loadingDrafts = $scope.draftController_loading;
		    $scope.country = countryCodeNameProperties;
		    $scope.territory = "";

		    /**
			 * Loading Status update Process
			 */
			var loadingStatusProcess = function(){
				$scope.loadingDrafts = $scope.clone_create;
				setTimeout(function () {
			        $scope.$apply(function () {
			        	$scope.loadingDrafts=$scope.clone_check;
			        });
			    }, 2000);
				setTimeout(function () {
			        $scope.$apply(function () {
			        	$scope.loadingDrafts=$scope.clone_uri;
			        });
			    }, 12000);
			};

		    /**
			 * Delete a Draft
			 */
		    $scope.deleteDraft = function(draft) {
		    	$scope.loadingDrafts = $scope.draftController_deleteProgress;
		    	$scope.loadDraft = false;

		    	var tlDraftDelete = {
                        tlId : draft.id,
                        cookie : $scope.draftStoreId,
                        rejected : false
                };

		    	deleteDraft(tlDraftDelete,draft);
			  };

			  var deleteDraft = function(tlDraftDelete,draft){
			      httpFactory.post("/api/draft/delete",tlDraftDelete,$scope.draftController_draftDeleteFailure).then(function(data) {
                      $scope.drafts.splice( $scope.drafts.indexOf(draft), 1 );
                  })
                  .finally(function (){
                      $scope.loadingDrafts="";
                      $scope.loadDraft = true;
                  });
			  };

			  /**
				 * Upload file on the server
				 */
			  $scope.uploadFile = function(myFile,fileInput){
				  var file = myFile;
				  $scope.loadDraft = false;
				  $scope.loadingDrafts = $scope.draftController_loadingImport;
			      if($scope.testFile(file)){
					var fd = new FormData();
					fd.append('file', file);
					$http.post('/tl-manager-non-eu/fileUpload/'+$scope.draftStoreId , fd, {
					    transformRequest: angular.identity,
					    headers: {'Content-Type': undefined}
					})
					.success(function(data, status, headers, config) {
						if(serviceResponseHandler(data)){
							$scope.drafts.push(data.content);
							$window.location.href = '/tl-manager-non-eu/tl/'+data.content.id;
						}else{
							showModal.httpStatusHandler(data.responseStatus,data.errorMessage);
							$scope.loadingDrafts = "";
							$scope.loadDraft = true;
						};
					})
					.error(function(data, status, headers, config) {
					    showModal.httpStatusHandler(status,$scope.draft_importError);
						$scope.loadingDrafts = "";
						$scope.loadDraft = true;
					});
			      }else{
			    	$scope.loadingDrafts = "";
					$scope.loadDraft = true;
			      };
			      // Clean fileinput
			      fileInput[0].value = "";
			    };
			    
			  /**
				 * Create an empty draft
				 */
			  $scope.createEmptyDraft = function(territory){
				  $scope.loadDraft = false;
				  $scope.loadingDrafts = $scope.draftController_loadingEmptyDraft;
				  httpFactory.post("/api/draft/create/"+$scope.draftStoreId, territory,$scope.draftController_creatingEmptyDraftFailure).then(function(result) {
							$scope.drafts.push(result);
							$window.location.href = '/tl-manager-non-eu/tl/'+result.id;
						}, function(){
							$scope.loadingDrafts="";
							$scope.territory="";
							$scope.loadDraft = true;
						});
			    };

			  /**
				 * Duplicate a draft
				 */
			  $scope.duplicate = function(origDraft){
				  $scope.loadDraft = false;
				  $scope.loadingDrafts = $scope.draftController_loadingDuplicate;
				  httpFactory.post("/api/draft/duplicate", origDraft, $scope.draftController_duplicateFailure)
						.then(function(result) {
							$scope.drafts.push(result);
							$scope.loadingDrafts="";
							$scope.loadDraft = true;
						}, function(){
							$scope.loadingDrafts="";
							$scope.loadDraft = true;
						});
			    };
			    
			  /**
				 * Check type of file
				 */
			  $scope.testFile = function(myFile){
		        var file = myFile;
		        angular.element('#uploadFile').val='';
		        if(file.type!="text/xml"){
		            showModal.information($scope.draftController_xmlFile);
				    return false;
		        };
		        return true;
			  };
			  
			  /**
				 * Rename a draft
				 */
			  $scope.rename = function(draft){
				  var value = draft.name;  
				  var template =
	    				'<div class="panel panel-primary">'+
	    					'<div class="panel-heading">'+
	    						'<div style="width: 97% !important; display: inline-block;">'+
	    							'<span>{{myTLInfo.name}} ({{myTLInfo.sequenceNumber}}) : {{tRename}}</span>'+
	    						'</div>'+
	    						'<span class="fa fa-times cursor-pointer" ng-click="cancel()"></span>'+
	    						'<span ng-click="ok()" style="margin-left: 5px;" class="fa fa-check cursor-pointer" ></span>'+
	    					'</div>'+
	    					'<div class="panel-body">'+'<input class="col-sm-9" ng-model="value" style="margin-left:5px;" />'+
					'</div></div>';
	                var modalInstance = $modal.open({
						template : template,
						controller : modalTLInfoEdit,
						windowClass: 'modal-fit',
						backdrop : 'static',
						resolve : {
							myValue : function() {
								return value;
							},
							myTLInfo : function() {
								return draft;
							}
						}
					});

	                modalInstance.result.then(function(myValue) {
	                	var tlDraftRename = {
    						tlId : draft.id,
                            cookie : $scope.draftStoreId,
    						newName : myValue
	    				};
	                	httpFactory.put('/api/draft/rename/',tlDraftRename,$scope.draftController_renameFailure)
	                		.then(function(data) {
	                			draft.name=myValue;
	                		});
	                });
	            };
			  
}]);