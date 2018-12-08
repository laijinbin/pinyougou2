/** 定义首页控制器层 */
app.controller("indexController", function($scope,$controller, baseService){
    $controller('baseController',{$scope:$scope});
    $scope.findContentByCategoryId=function (id) {
        baseService.sendGet("/index/findContentByCategoryId?categoryId="+id)
            .then(function (value) {
                $scope.contentList=value.data;
            });
    };

    $scope.search=function () {
        var keyword = $scope.keywords ? $scope.keywords : "";
        location.href="http://search.pinyougou.com?keywords=" + keyword;
    };


});