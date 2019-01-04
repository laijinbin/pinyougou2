app.controller('cartController', function ($scope, $controller, baseService) {
    $controller('baseController', {$scope: $scope});
    $scope.totalEntity = {totalNum: 0, totalMoney: 0.00};
    $scope.carts = {};
    var checkAllNum = new Array();
    $scope.sellerCheck = [];
    $scope.checkAll = [];
    $scope.goodIds = [];
    var num = new Array();
    $scope.sellerNum = 0;
    $scope.ckAll = false;
    var arry = new Array();
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function (value) {
            $scope.carts = value.data;
            for (var i = 0; i < value.data.length; i++) {
                var cart = value.data[i];
                num[i] = 0;
                cart.isCheck = false;
                arry[i] = new Array();
                for (var j = 0; j < cart.orderItems.length; j++) {
                    var orderItem = cart.orderItems[j];
                    orderItem.isCheck = false;
                    num[i]++;
                    // $scope.totalEntity.totalNum+=orderItem.num;
                    // $scope.totalEntity.totalMoney+=orderItem.totalFee;
                }
            }
        });
    };

    $scope.addCart = function (itemId, num) {
        baseService.sendGet("/cart/addCart?itemId=" + itemId + "&num=" + num)
            .then(function (value) {
                if (value.data) {
                    $scope.findCart();
                } else {
                    alert("失败，再来一次");
                }

            });
    };
//全选
    $scope.allChecked = function ($event) {
        $scope.ckAll = $event.target.checked;
        for (var i = 0; i < $scope.carts.length; i++) {
            var cart = $scope.carts[i];
            cart.isCheck = $scope.ckAll;
            if (cart.isCheck) {
                checkAllNum.push(cart.sellerName);
            } else {
                var cartindex = checkAllNum[i].indexOf(cart.sellerName);
                checkAllNum.splice(cartindex, 1);
            }
            for (var j = 0; j < cart.orderItems.length; j++) {
                cart.orderItems[j].isCheck = $scope.ckAll;
                if ($event.target.checked) {
                    if (arry[i].indexOf(cart.orderItems[j].itemId) == -1) {
                        arry[i].push(cart.orderItems[j].itemId);
                    }
                } else {
                    var x = arry[i].indexOf(cart.orderItems[j].itemId);
                    if (x > -1) {
                        arry[i].splice(x, 1);
                    }
                }
            }
        }
        $scope.jieSuan();

    };
    //店铺全选
    $scope.someCheck = function ($event, cart, index) {
        cart.isCheck = $event.target.checked;
        if ($event.target.checked) {
            checkAllNum.push(cart.sellerName);
            for (var i = 0; i < cart.orderItems.length; i++) {
                if (arry[index].indexOf(cart.orderItems[i].itemId) == -1) {
                    arry[index].push(cart.orderItems[i].itemId);
                }
            }
        } else {
            var cartindex = checkAllNum[index].indexOf(cart.sellerName);
            checkAllNum.splice(cartindex, 1);
            for (var i = 0; i < cart.orderItems.length; i++) {
                var x = arry[index].indexOf(cart.orderItems[i].itemId);
                if (x > -1) {
                    arry[index].splice(x, 1);
                }
            }
        }
        for (var i = 0; i < cart.orderItems.length; i++) {
            cart.orderItems[i].isCheck = $event.target.checked;
        }
        $scope.ckAll = checkAllNum.length == $scope.carts.length;
        $scope.jieSuan();

    };
    //每一个商品的
    $scope.isCheck = function ($event, cart, orderItem, outIndex) {
        orderItem.isCheck = $event.target.checked;
        if ($event.target.checked) {
            arry[outIndex].push(orderItem.itemId);
        } else {
            var indexOf = arry[outIndex].indexOf(orderItem.itemId);
            arry[outIndex].splice(indexOf, 1);
            if (checkAllNum[outIndex]) {
                var cartindex = checkAllNum[outIndex].indexOf(cart.sellerName);
                checkAllNum.splice(cartindex, 1);
            }
        }
        cart.isCheck = num[outIndex] == arry[outIndex].length;
        if (cart.isCheck) {
            checkAllNum.push(cart.sellerName);
        }
        $scope.ckAll = checkAllNum.length == $scope.carts.length;
        $scope.jieSuan();

    };

    //结算
    $scope.jieSuan=function () {
        $scope.totalEntity.totalNum=0;
        $scope.totalEntity.totalMoney=0;
        for (var i = 0; i < $scope.carts.length; i++) {
            var cart = $scope.carts[i];
            for (var j = 0; j < cart.orderItems.length; j++) {
                var orderItem = cart.orderItems[j];
                if (orderItem.isCheck){
                    $scope.totalEntity.totalNum+=orderItem.num;
                    $scope.totalEntity.totalMoney+=orderItem.totalFee;
                }

            }
        }
    };

});