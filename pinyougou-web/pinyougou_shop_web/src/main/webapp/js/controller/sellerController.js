/** 定义控制器层 */
app.controller('sellerController', function ($scope, $http,$controller, $location, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/seller/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.seller.id) {
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/seller/" + url, $scope.seller)
            .then(function (response) {
                if (response.data) {
                    alert("申请入驻成功");
                    location.href = "/shoplogin.html";
                } else {
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };
    $scope.findSeller = function () {
        baseService.sendGet("/seller/findSeller?sellerId=" + $location.search().sellerId)
            .then(function (value) {
                $scope.sellerInfo = value.data;
            });
    };
    $scope.updateSeller = function () {
        baseService.sendPost("/seller/updateSeller", $scope.sellerInfo)
            .then(function (value) {
                if (value.data) {
                    alert("修改成功");
                    $scope.sellerInfo = {};
                } else {
                    alert("修改失败，再来一次");
                }
            });
    };
    $scope.alertPwd = function () {
        if ($scope.password.oldPwd=='') {
            alert("原密码不能为空");
            return;
        }
        else if ($scope.password.newPwd=='') {
            alert("新密码不能为空");
            return;
        } else if ($scope.confirmPassword=='') {
            alert("请再次输入新密码");
            return;
        }else if ($scope.password.newPwd!=$scope.confirmPassword){
            alert("两次输入密码不一致，请重新输入，谢谢");
            $scope.password.newPwd='';
            $scope.confirmPassword='';
            return;
        }else {
            $http.post("/seller/alertPwd?sellerId="+$location.search().sellerId,{
               "newPwd": $scope.password.newPwd,
                "oldPwd":$scope.password.oldPwd})
                .then(function (value) {
                   alert(value.data.msg);
                   if (value.data.flag=="true"){
                       location.href="/logout";
                   }
                });
        }

    };
});