app.controller("itemCatController", function ($scope,$controller, baseService) {
    $controller('baseController', {$scope: $scope});
    $scope.grade = 1;
    $scope.parentId=0;
    $scope.findItemCatByParentId=function (parentId) {
        $scope.parentId=parentId;
        baseService.sendGet("/itemCat/findItemCatByParentId","parentId="+parentId)
            .then(function (value) {
                $scope.itemCatList = value.data;
            });
    };
    $scope.selectList=function (entity,grade) {
        $scope.grade = grade;
        if (grade==1){
            $scope.itemCat_1=null;
            $scope.itemCat_2=null;
        }
        if (grade==2){
            $scope.itemCat_1=entity;
            $scope.itemCat_2=null;
        }
        if (grade==3){
            $scope.itemCat_2=entity;
        }
        $scope.findItemCatByParentId(entity.id);
    };
    $scope.saveOrUpdate=function () {
        if($scope.grade==1){
            $scope.entity.parentId=0;
        }
        if($scope.grade==2){
            $scope.entity.parentId=$scope.itemCat_1.id;
        }
        if($scope.grade==3){
            $scope.entity.parentId=$scope.itemCat_2.id;
        }
      var url="save";
      if ($scope.entity.id){
          url="update";
      }
      baseService.sendPost("/itemCat/"+url,$scope.entity)
          .then(function (value) {
              if (value.data){
                  alert("操作成功");
                  $scope.findItemCatByParentId($scope.entity.parentId);
              }
          })
    };
    $scope.findTypeTemplateList=function () {
        baseService.sendGet("/typeTemplate/findTypeTemplateList")
            .then(function (value) {
                $scope.typeTemplateList={data:value.data};
            });
    };

    $scope.show=function (entity) {
      $scope.entity=JSON.parse(JSON.stringify(entity));
    };
    $scope.delete=function (grade) {
       baseService.sendGet("/itemCat/delete?grade="+grade+"&ids="+$scope.ids)
            .then(function (value) {
                if (value.data){
                    alert("删除成功");
                    $scope.ids=[];
                    $scope.findItemCatByParentId($scope.parentId);
                }else {
                    alert("删除失败");
                }
            });
    }



});