/**
 * Modal Change Controller
 */
function modalChangeDirectiveController($scope, $modalInstance, list, label, information) {

    initMessages($scope);
    $scope.label = label;

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss();
    };

    /** Initialization * */
    var initOption = function() {
        var firstColumnLabel = "";
        var secondColumnLabel = "";
        var firstColumnLabel = $scope.tComparedValue;
        var secondColumnLabel = $scope.tCurrentValue;

        information

        var dirList = [];
        for (var i = 0; i < list.length; i++) {
            var obj = new Object();
            var hrTemp = explodeLocation(list[i].hrLocation);
            if (hrTemp) {
                obj.hrLocation = hrTemp;
            }
            ;
            obj.publishedValue = list[i].publishedValue;
            obj.currentValue = list[i].currentValue;
            dirList.push(obj);
        }
        ;
        // Case : no HrLocation
        if (dirList.length > 0 && dirList[0].hrLocation) {
            var head = [ {
                label : $scope.tLocation,
                size : "500px;"
            }, {
                label : firstColumnLabel,
                size : "385px;"
            }, {
                label : secondColumnLabel,
                size : "385px;"
            } ];
            dirList.sort(dynamicSortMultiple("hrLocation"));
        } else {
            var head = [ {
                label : firstColumnLabel,
                size : "635px;"
            }, {
                label : secondColumnLabel,
                size : "635px;"
            } ];
            dirList.sort(dynamicSortMultiple("publishedValue"));
        }
        ;
        $scope.tableOptions = {
            nbItemPerPage : 7,
            listObj : dirList,
            thead : head,
            displayHeading : true
        };
    };
    initOption();

};