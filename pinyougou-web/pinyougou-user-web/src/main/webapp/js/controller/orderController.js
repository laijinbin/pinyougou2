app.controller('orderController',function ($scope,$controller,baseService) {
    $controller('indexController',{$scope:$scope});
    $scope.orderList={};
    $scope.searchEntity={};
    $scope.str=['无操作','等待买家付款','买家已付款'];
    $scope.findAllOrder=function () {
      baseService.sendGet("/order/findAllOrder")
          .then(function (value) {
              $scope.orderList=value.data;
          });
    };

    $scope.search = function (page, rows) {
        baseService.findByPage("/order/findByPage", page, rows, $scope.searchEntity)
            .then(function (value) {
                $scope.orderList = value.data.list;
                $scope.paginationConf.totalItems = value.data.total;
            });
    };


});