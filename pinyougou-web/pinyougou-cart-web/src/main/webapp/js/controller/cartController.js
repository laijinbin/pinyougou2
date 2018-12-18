app.controller('cartController',function ($scope,$controller,baseService) {
    $controller('baseController',{$scope:$scope});

    $scope.carts={};
    $scope.findCart=function () {
      baseService.sendGet("/cart/findCart").then(function (value) {
          $scope.carts=value.data;
        $scope.totalEntity ={totalNum:0,totalMoney:0.00};
          for (var i=0;i<value.data.length;i++){
              var cart=value.data[i];
              for (var j=0;j<cart.orderItems.length;j++){
                  var orderItem=cart.orderItems[j];
                  $scope.totalEntity.totalNum+=orderItem.num;
                  $scope.totalEntity.totalMoney+=orderItem.totalFee;
              }
          }
      });
    };

    $scope.addCart=function (itemId,num) {
        baseService.sendGet("/cart/addCart?itemId="+itemId+"&num="+num)
            .then(function (value) {
                if (value.data){
                    $scope.findCart();
                }else {
                    alert("失败，再来一次");
                }

            });
    };
});