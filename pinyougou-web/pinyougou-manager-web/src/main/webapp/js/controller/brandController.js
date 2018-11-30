app.controller("brandController", function ($scope,$controller, baseService) {
    $controller('baseController',{$scope:$scope});
    $scope.findAll = function () {
        $http.get("/brand/findAll").then(function (value) {
            $scope.brandList = value.data;
        }, function (reason) {
            alert("加载数据失败")
        });
    };
    $scope.searchEntity = {};

    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.entity.id) {
            url = "update";
        }
        baseService.sendPost("/brand/" + url, $scope.entity).then(function (value) {
            if (value.data) {
                $scope.reload();
            } else {
                alert("添加数据失败");
            }
        });
    };
    $scope.show = function (entity) {
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };


    $scope.search = function (page, rows) {
        baseService.findByPage("/brand/findByPage", page, rows, $scope.searchEntity)
            .then(function (value) {
                $scope.brandList = value.data.list;
                $scope.paginationConf.totalItems = value.data.total;
            });
    };

    $scope.delete = function () {
        if ($scope.ids.length != 0) {
            baseService.delete("/brand/delete", $scope.ids)
                .then(function (value) {
                    if (value.data) {
                        $scope.ids = [];
                        alert("删除成功");
                        $scope.reload();
                    } else {
                        alert("删除失败，再来一次");
                    }
                });
        } else {
            alert("请选择要删除的品牌")
        }
    };
});