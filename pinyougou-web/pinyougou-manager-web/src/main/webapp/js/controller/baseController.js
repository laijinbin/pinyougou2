app.controller('baseController',function($scope){
    $scope.ids = [];
    $scope.paginationConf = {
        currentPage: 1,//当前页
        totalItems: 0,//总记录数
        itemsPerPage: 10, // 每页显示的记录数
        perPageOptions: [10, 20, 30], // 页码下拉列表框
        onChange: function () { // 改变事件
            $scope.reload(); //重新加载
        }
    };
    $scope.reload = function () {
        /** 切换页码 */
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    };

    $scope.updateSelection= function ($event,id) {
        if ($event.target.checked) {
            $scope.ids.push(id);
        } else {
            var num = $scope.ids.indexOf(id);
            $scope.ids.splice(num, 1);
        }
    };
});
