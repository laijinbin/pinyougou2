app.controller("indexController", function ($scope,$controller, baseService) {
    $controller('baseController',{$scope:$scope});
    $scope.findUserName=function () {
        baseService.sendGet("/user/findUserName")
            .then(function (value) {
                $scope.map=value.data;
            });
    };
});
