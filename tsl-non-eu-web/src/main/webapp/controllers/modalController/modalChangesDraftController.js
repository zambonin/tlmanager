/** ****** TrustedList : Pop-up with archived prod TL to enable/disable changes ******* */
function modalChangesDraftController($scope, $modalInstance, $filter, myTl, drafts, trustedListFactory, httpFactory, showModal, appConstant) {

    initMessages($scope);
    $scope.myTl = myTl;
    $scope.load_change = false;
    $scope.drafts = drafts;
    
    var head = [ 
        {label : $scope.tName,                  	size : "160px"},
        {label : $scope.schemeTerritory,        	size : "120px"},
        {label : $scope.signatureInformationMenu,	size : "220px"},
        {label : $scope.tlBrowser_issueDate,    	size : "200px"},
        {label : $scope.tlBrowser_expiryDate,   	size : "200px"},
        {label : "",                            	size : "20px"}
    ];

    $scope.ok = function(index) {
        var message = $filter('replaceIn')($scope.confirm_changes_comparator,'%TL%',(drafts[index].name+' (Sn'+drafts[index].sequenceNumber+') ')); 
        showModal.confirmation(message, appConstant.modalTitle.confirmation).then(function() {
            $modalInstance.close(drafts[index]);
        });
    };

    $scope.cancel = function() {
        $modalInstance.dismiss();
    };

    var initList = function() {
        var dirList = [];
        for (var i = 0; i < drafts.length; i++) {
            if(drafts[i].id != myTl.id) {
            	var obj = new Object();
            	obj.name = drafts[i].name + " (" + drafts[i].sequenceNumber + ")";
            	obj.territory = drafts[i].countryName;
            	obj.signature = drafts[i].sigStatus;
            	obj.issueDate = $filter('date')(drafts[i].issueDate, "yyyy-MM-dd");
                obj.nextUpdateDate = $filter('date')(drafts[i].nextUpdateDate, "yyyy-MM-dd");
            	obj.manage = {
            			html : "<a class='fa fa-clone uri' tooltip='"+$scope.tCompare_tls+"' tooltip-placement='bottom' ></a>",
            			action : "ok(" + i + ")"
            	};
            	dirList.push(obj);
            }
        }

        $scope.tableOptions = {
            nbItemPerPage : 10,
            listObj : dirList,
            thead : head,
            displayHeading : true
        };
        $scope.load_change = true;
    }
    initList();

};