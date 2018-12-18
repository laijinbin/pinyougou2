app.controller('itemController',function ($scope,$http,$controller){

    $controller('baseController',{$scope:$scope});
    $scope.specItems = {};

    $scope.addNum=function (num) {
      $scope.num= $scope.num+num;
        if($scope.num < 1){
            $scope.num = 1;
        }
    };
    
    $scope.isSelected=function (attributeName,item) {
        return $scope.specItems[attributeName] == item;
    };
    $scope.selectSpec = function(attributeName, item){
        $scope.specItems[attributeName] = item;
        $scope.searchSku();
    };
    $scope.loadSku=function () {
        $scope.sku = itemList[0];
        /** 获取SKU商品选择的选项规格 */
        $scope.specItems = JSON.parse($scope.sku.spec);

    };
    $scope.searchSku=function () {
      for (var i=0;i<itemList.length;i++){
          if (itemList[i].spec==JSON.stringify($scope.specItems)){
              $scope.sku=itemList[i];
              return;
          }
      }
    };

    $scope.addToCart=function () {
        // 发送异步请求(自已的域名: http://item.pinyougou.com)
        $http.get("http://cart.pinyougou.com/cart/addCart?itemId="
            + $scope.sku.id + "&num=" + $scope.num, {withCredentials:true}).then(function(response){
            // 获取响应数据
            if (response.data){
                // 跳转到购物车系统
                location.href = "http://cart.pinyougou.com";
            }
        },function (response) {
            alert("加入购物车失败！");
        });
    };


 });