app.controller("specificationController", function ($scope,$controller, baseService) {
    $controller('baseController',{$scope:$scope});
    $scope.searchEntity = {};

    $scope.search = function (page, rows) {
        baseService.findByPage("/specification/findByPage", page, rows, $scope.searchEntity)
            .then(function (value) {
                $scope.specificationList = value.data.list;
                $scope.paginationConf.totalItems = value.data.total;
            });
    };

    $scope.addRow=function () {
        $scope.entity.specificationOptionList.push({});
    };
    $scope.deleteRow=function (index) {
        $scope.entity.specificationOptionList.splice(index,1);
    };
    $scope.saveOrupdate=function () {
        url="save";
        if( $scope.entity.id){
            url="update";
        }
        baseService.sendPost("/specification/"+url,$scope.entity)
            .then(function (value) {
                if (value.data){
                    $scope.reload();
                }else {
                    alert("添加失败，再来一次");
                }
            });
    };
    $scope.show=function (entity) {
        $scope.entity=JSON.parse(JSON.stringify(entity));
        baseService.sendGet("/specificationOption/findById?id="+entity.id)
            .then(function (value) {
                $scope.entity.specificationOptionList=value.data;
            });
    };
    $scope.delete=function () {
        baseService.delete("/specification/delete",$scope.ids)
            .then(function (value) {
                    if (value.data){
                        $scope.ids = [];
                        alert("删除成功");
                        $scope.reload();
                    }else {
                        alert("删除失败，再来一次");
                    }
            });
    };
});