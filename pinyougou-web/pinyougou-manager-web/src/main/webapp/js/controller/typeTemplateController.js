app.controller("typeTemplateController", function ($scope,$controller, baseService) {
    $controller('baseController', {$scope: $scope});
    $scope.searchEntity = {};
    $scope.search = function (page, rows) {
        baseService.findByPage("/typeTemplate/findByPage", page, rows, $scope.searchEntity)
            .then(function (value) {
                $scope.typeTemplateList = value.data.list;
                $scope.paginationConf.totalItems = value.data.total;
            });
    };
    $scope.findBrandAndSpecification=function () {
      baseService.sendGet("/brand/findIdAndName").then(function (value) {
          $scope.brandList={data:value.data};
      });
        baseService.sendGet("/specification/findIdAndName").then(function (value) {
            $scope.specificationList={data:value.data};
        });
    };
    $scope.addRow =function () {
        $scope.entity.customAttributeItems.push({});
    };
    $scope.deleteRow=function (index) {
        $scope.entity.customAttributeItems.splice(index,1);
    };
    $scope.show=function (entity) {
        $scope.entity = JSON.parse(JSON.stringify(entity));
        $scope.entity.brandIds = JSON.parse(entity.brandIds);
        /** 转换规格列表 */
        $scope.entity.specIds = JSON.parse(entity.specIds);
        /** 转换扩展属性 */
        $scope.entity.customAttributeItems = JSON
            .parse(entity.customAttributeItems);
    };

    $scope.saveOrUpdate=function () {
      var url="save";
      if ($scope.entity.id){
          url="update";
      }
      baseService.sendPost("/typeTemplate/"+url,$scope.entity)
          .then(function (value) {
              if (value.data){
                  alert("添加成功");
                  $scope.reload();
              }else {
                  alert("操作失败，再来一次");
              }
          });
    };

    $scope.delete = function () {
        if ($scope.ids.length != 0) {
            baseService.delete("/typeTemplate/delete", $scope.ids)
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