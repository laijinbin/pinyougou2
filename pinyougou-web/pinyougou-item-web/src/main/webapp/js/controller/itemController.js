app.controller('itemController',function ($scope){
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
        alert($scope.sku.id);
    };


 });