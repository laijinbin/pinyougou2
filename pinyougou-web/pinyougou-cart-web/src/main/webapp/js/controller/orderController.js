app.controller('orderController', function ($scope,$location, $controller,$interval, baseService) {
    $controller('baseController', {$scope: $scope});
    $controller('cartController', {$scope: $scope});
    $scope.order = {paymentType: '1'};
    $scope.findAddressByUser = function () {
        baseService.sendGet("/order/findAddressByUser")
            .then(function (response) {
                $scope.addressList = response.data;
                for (var i = 0; i < response.data.length; i++) {
                    if (response.data[i].isDefault == 1) {
                        $scope.address = response.data[i];
                    }
                }
            });
    };
    $scope.isSelectedAddress = function (item) {
        if ($scope.address == item) {
            return true;
        }
        else
            return false;

    };
    $scope.selectAddress = function (item) {
        $scope.address = item;
    };
    $scope.selectPayType = function (payType) {
        $scope.order.paymentType = payType;

    };

    $scope.saveOrder = function () {
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiver = $scope.address.contact;
        baseService.sendPost("/order/save", $scope.order)
            .then(function (value) {
                if (value.data) {
                    if ($scope.order.paymentType == 1) {
                        location.href = "/order/pay.html";
                    } else {
                        location.href = "/order/paysuccess.html";
                    }
                }else {
                    alert("订单提交失败，别买了");
                }
            });
    };
    $scope.genPayCode=function () {
        baseService.sendGet("/order/genPayCode").then(function (value) {
            $scope.money=(value.data.totalFee/100).toFixed(2);
            /** 获取订单交易号 */
            $scope.outTradeNo= value.data.outTradeNo;
            var qr = new QRious({
                element : document.getElementById('qrious'),
                size : 250,
                level : 'H',
                value : value.data.codeUrl
            });
            /**
             * 开启定时器
             * 第一个参数：调用的函数
             * 第二个参数：时间毫秒数(3000 毫秒也就是 3 秒) *
             * 第三个参数：调用的总次数(60 次)
             * * */
            var timer=$interval(function () {
                baseService.sendGet("/order/queryPayStatus?outTradeNo=" + $scope.outTradeNo)
                    .then(function (value) {
                        if(value.data.status == 1){// 支付成功
                            /** 取消定时器 */
                            $interval.cancel(timer);
                            location.href = "/order/paysuccess.html?money="
                                + $scope.money;
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
    };
    //用来页面之间传参用的
    $scope.getMoney = function(){
        return $location.search().money;
    };
});