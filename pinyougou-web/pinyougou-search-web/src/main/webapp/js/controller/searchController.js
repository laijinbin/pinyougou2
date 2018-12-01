/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope,$sce, baseService) {

    $scope.searchParam = {};
    $scope.resultMap={};
    $scope.search=function () {
      baseService.sendPost("/Search",$scope.searchParam)
          .then(function (value) {
              $scope.resultMap = value.data;
          });
    };
    
    $scope.trustHtml=function (value) {
      return $sce.trustAsHtml(value);
    };

   
});
