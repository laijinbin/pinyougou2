app.controller("goodsController", function ($scope,$controller, baseService) {
    $controller('baseController', {$scope: $scope});
    $scope.status = ['未审核','已审核','审核未通过','关闭'];
    /** 定义搜索对象 */
    $scope.searchEntity = {};

    $scope.search = function (page, rows) {
        baseService.findByPage("/goods/findByPage", page, rows, $scope.searchEntity)
            .then(function (value) {
                $scope.dataList = value.data.list;
                $scope.paginationConf.totalItems = value.data.total;
            });
    };

    $scope.checkPass=function (status) {
        if ($scope.ids.length>0){

        baseService.sendGet("/goods/checkPass?ids="+$scope.ids+"&" +
            "status="+status)
            .then(function (value) {
                if (value.data){
               alert("操作成功");
                $scope.ids=[];
                $scope.reload();
                }else {
                    alert("操作出错，再来一次");
                }
            });
        }else {
            alert("请选择要操作的商品");
        }
    };

    $scope.delete=function () {
        if ($scope.ids.length>0){
            baseService.sendGet("/goods/deleteGoods?ids="+$scope.ids)
                .then(function (value) {
                   if (value.data){
                       alert("操作成功");
                       $scope.ids=[];
                       $scope.reload();
                   }else {
                       alert("操作失败");
                   }
                });
        }else {
            alert("请选择你要删除的商品");
        }
    };

});