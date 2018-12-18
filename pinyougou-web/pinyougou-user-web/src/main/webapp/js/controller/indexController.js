app.controller("indexController", function($scope,$interval,$filter,$location, baseService) {
    /** 定义获取登录用户名方法 */

    $scope.showName = function(){
        $scope.redirectUrl=window.encodeURIComponent(location.href);
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };
    $scope.findUser=function () {
        baseService.sendGet("/user/findUser")
            .then(function (value) {
                $scope.user=value.data;
                $scope.user.address = JSON.parse(value.data.address);
                $scope.user.birthday=$filter("date")(value.data.birthday,"yyyy-MM-dd HH:mm:ss");
            });
    };
    $scope.paginationConf = {
        currentPage: 1,//当前页
        totalItems: 0,//总记录数
        itemsPerPage: 5, // 每页显示的记录数
        perPageOptions: [5, 10, 15], // 页码下拉列表框
        onChange: function () { // 改变事件
            $scope.reload(); //重新加载
        }
    };
    $scope.reload = function () {
        /** 切换页码 */
        $scope.search($scope.paginationConf.currentPage,
            $scope.paginationConf.itemsPerPage);
    };
    $scope.genPayCode=function () {
        baseService.sendGet("/order/genPayCode?money="+$location.search().payMoney+"&orderId="+$location.search().OrderId).then(function (value) {
            $scope.money=(value.data.totalFee/100).toFixed(2);
            /** 获取订单交易号 */
            $scope.outTradeNo= value.data.outTradeNo;
            document.getElementById("qrious").src = "/barcode?url=" + value.data.codeUrl;
            /**
             * 开启定时器
             * 第一个参数：调用的函数
             * 第二个参数：时间毫秒数(3000 毫秒也就是 3 秒) *
             * 第三个参数：调用的总次数(60 次)
             * * */
            var timer=$interval(function () {
                baseService.sendGet("/order/queryPayStatus?outTradeNo=" + $scope.outTradeNo+"&money="+$location.search().payMoney)
                    .then(function (value) {
                        if(value.data.status == 1){// 支付成功
                            /** 取消定时器 */
                            $interval.cancel(timer);
                            location.href = "/order/paysuccess.html?money="
                                + $location.search().payMoney;
                        }
                        if(value.data.status == 3 ){// 支付失败
                            /** 取消定时器 */
                            $interval.cancel(timer);
                            location.href = "/order/payfail.html";
                        }
                    });

            },3000,60);
            /** 执行 60 次(3 分钟)之后需要回调的函数 */
            timer.then(function () {
                alert("微信支付失败");
            });

        });
        $scope.getMoney = function(){
            return $location.search().money;
        };
    };
});
