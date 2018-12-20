/** ****** Modal add User ******* */
function modalAddUserController($scope, $modalInstance, showModal) {

    initMessages($scope);

    $scope.ecasId = "";

    $scope.ok = function() {
        if ($scope.ecasId == "") {
            showModal.information($scope.userManagementController_fillFields);
        } else {
            var userForm = {
                ecasId : $scope.ecasId,
            };
            $modalInstance.close(userForm);
        }
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };
};