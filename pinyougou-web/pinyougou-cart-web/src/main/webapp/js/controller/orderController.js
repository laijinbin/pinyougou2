app.controller('orderController', function ($scope, $controller, baseService) {
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

});