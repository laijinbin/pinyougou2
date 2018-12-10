/** 定义订单控制器 */
app.controller("seckillOrderController", function ($scope, $timeout,$controller,
                                                   $interval, $location, baseService) {

    /** 指定继承seckillGoodsController */
    $controller("seckillGoodsController", {$scope: $scope});
    var id = $location.search().id;
    $scope.findOne = function () {
        baseService.sendGet("/seckill/findOne?id=" + id)
            .then(function (value) {
                $scope.goods = value.data;
                $scope.downcount($scope.goods.endTime);
            });
    };

    $scope.downcount = function (endTime) {
        //相差的毫秒数
        var milliSeconds = new Date(endTime).getTime() - new Date().getTime();
        //计算相差的秒数
        var seconds = Math.floor(milliSeconds / 1000);
        if (seconds > 0) {
            var minutes = Math.floor(seconds / 60);
            var hours = Math.floor(minutes / 60);
            var day = Math.floor(hours / 24);
            var timeStr = new Array();
            if (day>0){
                timeStr.push(day+"天");
            }
            if (hours>0){
                timeStr.push((hours-day*24)+":");
            }
            if (minutes>0){
                timeStr.push((minutes-hours*60)+":");

            }
            if (seconds>0){
                timeStr.push(seconds-minutes*60);
            }
            $scope.timeStr2 = timeStr.join("");
            //开启定时器
          var promise= $timeout(function () {
                $scope.downcount(endTime);

            },1000);
        }else {
            $scope.timeStr2="秒杀结束";
            $timeout.cancel(promise);
        }
    };
    $scope.submitOrder=function () {
        if($scope.loginName){
            baseService.sendGet("/order/submitOrder?id="+id)
                .then(function (value) {
                    if (value.data){
                        alert("抢购成功");
                        location.href="/order/pay.html"
                    }else {
                        alert("抢购失败，再来一次");
                    }
                });
        }else {
            alert("请先登录");
            location.href="http://sso.pinyougou.com/?service="+$scope.redirectUrl;
        }

    };



    /** 生成微信支付二维码 */
    $scope.genPayCode = function () {
        baseService.sendGet("/order/genPayCode").then(function (response) {
            if (response.data) {
                /** 获取金额 */
                $scope.money = (response.data.totalFee / 100).toFixed(2);
                /** 获取订单号 */
                $scope.outTradeNo = response.data.outTradeNo;
                /** 生成二维码 */
                var qr = new QRious({
                    element: document.getElementById('qrious'),
                    size: 250,
                    level: 'H',
                    value: response.data.codeUrl
                });


                /**
                 * 开启定时器
                 * 第一个参数：回调函数
                 * 第二个参数：时间毫秒数 3秒
                 * 第三个参数：执行的次数 100次
                 * */
                var timer = $interval(function () {
                    /** 发送请求，查询支付状态 */
                    baseService.sendGet("/order/queryPayStatus?outTradeNo="
                        + $scope.outTradeNo)
                        .then(function (response) {
                            if (response.data.status == 1) {// 支付成功
                                /** 取消定时器 */
                                $interval.cancel(timer);
                                location.href = "/order/paysuccess.html?money="
                                    + $scope.money;
                            }
                            if (response.data.status == 3) { // 支付失败
                                /** 取消定时器 */
                                $interval.cancel(timer);
                                location.href = "/order/payfail.html";
                            }
                        });
                }, 3000, 100);

                /** 100次(5分钟)成功后需要调用的函数 */
                timer.then(function () {
                    $scope.codStr = "微信支付二维码已失效！";
                });
            }
            ;
        });
    };

    /** 获取金额 */
    $scope.getMoney = function () {
        return $location.search().money;
    };
});