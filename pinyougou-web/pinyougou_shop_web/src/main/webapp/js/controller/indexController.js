app.controller('indexController', function($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    $scope.findLoginName=function () {
        baseService.sendGet("/login/findLoginName")
            .then(function (value) {
               $scope.loginUser=value.data;
            });
    };
});