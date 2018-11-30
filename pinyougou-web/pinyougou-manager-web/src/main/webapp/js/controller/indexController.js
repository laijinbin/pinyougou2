app.controller("indexController", function ($scope,$controller, baseService) {
    $controller('baseController',{$scope:$scope});
    $scope.findUserName=function () {
        baseService.sendGet("/login/findUserName")
            .then(function (value) {
                $scope.map=value.data;
            });
    };
});
