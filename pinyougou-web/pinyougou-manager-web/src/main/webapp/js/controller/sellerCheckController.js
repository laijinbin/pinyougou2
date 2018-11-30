app.controller("sellerCheckController", function ($scope,$controller, baseService) {
    $controller('baseController', {$scope: $scope});

    $scope.search=function (page,rows) {
        baseService.findByPage("/seller/findByPage",page,rows,$scope.searchEntity)
            .then(function (value) {
                $scope.sellecCheckList=value.data.list;
                $scope.paginationConf.totalItems = value.data.total;
            });
    };
    $scope.show = function (entity) {
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };
    $scope.updateStatus=function (sellerId,status) {
        baseService.sendGet("/seller/updateStatus?sellerId="+sellerId+"&status="+status)
            .then(function (value) {
                if (value.data){
                    alert("操作成功");
                    $scope.reload();
                }else {
                    alert("操作失败");
                }
            });
    };

});