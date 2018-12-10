/** 定义秒杀商品控制器 */
app.controller("seckillGoodsController", function($scope,$controller,$location,$timeout,baseService){

    /** 指定继承cartController */
    $controller("baseController", {$scope:$scope});

    $scope.findSeckillGoods=function () {
      baseService.sendGet("/seckill/findSeckillGoods")
          .then(function (value) {
              $scope.seckillGoods=value.data;
          }) ;
    };

    
});