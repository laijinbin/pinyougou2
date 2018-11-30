/** 定义首页控制器层 */
app.controller("indexController", function($scope, baseService){
    $scope.findContentByCategoryId=function (id) {
        baseService.sendGet("/index/findContentByCategoryId?categoryId="+id)
            .then(function (value) {
                $scope.contentList=value.data;
            });
    };


});