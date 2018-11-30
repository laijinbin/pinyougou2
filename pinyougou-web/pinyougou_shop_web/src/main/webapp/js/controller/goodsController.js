/** 定义控制器层 */
app.controller('goodsController', function($scope, $controller, baseService){
    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});
    $scope.status = ['未审核','已审核','审核未通过','关闭'];
    /** 查询条件对象 */
    $scope.searchEntity = {};
    $scope.specId=[];
    $scope.specList=[];
    $scope.specList.options={};
    $scope.specList2=[];
    // $scope.goods={goodsDesc:{itemImages: []}};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/goods/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.list;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        $scope.goods.goodsDesc.introduction=editor.html();
        var url = "save";
        /** 发送post请求 */
        baseService.sendPost("/goods/" + url, $scope.goods)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                   alert("添加成成功");
                    editor.html('');
                    $scope.goods= [];
                    $scope.specList=[];
                }else{
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/goods/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };

    $scope.findItemCatList=function (parentId,name) {
        baseService.sendGet("/goods/findItemCatList?parentId="+parentId)
            .then(function (value) {
                $scope[name]=value.data;
            });
    };
    $scope.findBrandListByTypeTemplateId=function (typeTemplateId) {
        baseService.sendGet("/goods/findBrandList?id="+typeTemplateId)
            .then(function (value) {
                $scope.brandList=JSON.parse(value.data.brandIds);
                $scope.goods.goodsDesc.customAttributeItems=JSON.parse(value.data.customAttributeItems);
                $scope.specList=JSON.parse(value.data.specIds);
                if ($scope.goods.goodsDesc.specificationItems){
                    $scope.findspecificationItemsOptionList();
                }
            });

        $scope.findspecificationItemsOptionList=function () {
            for (var i=0;i<$scope.specList.length;i++){
                $scope.specId.push($scope.specList[i].id);
            }
            baseService.sendGet("/goods/findspecificationItemsOption?specId="+$scope.specId)
                .then(function (value) {
                    for (var i=0;i<$scope.specList.length;i++){
                        for (var j=0;j<value.data.length;j++) {
                            if ($scope.specList[i].id == value.data[j].specId){
                                $scope.specList2.push(value.data[j]);
                            }
                        }
                        $scope.specList[i].options=$scope.specList2;
                        $scope.specList2=[];
                    }
                });
        };
    };

    $scope.$watch('goods.category1Id',function (newValue,oldValue) {
        if (newValue){
        $scope.findItemCatList(newValue,'ItemCatList2');
        }else {
            $scope.ItemCatList2=[];
        }
    });
    $scope.$watch('goods.category2Id',function (newValue,oldValue) {
        if (newValue){
            $scope.findItemCatList(newValue,'ItemCatList3');
        }else {
            $scope.ItemCatList3=[];
        }
    });
    $scope.$watch('goods.category3Id',function (newValue,oldValue) {
        if (newValue){
            for (var i=0;i<$scope.ItemCatList3.length;i++){
                $scope.ItemCat=$scope.ItemCatList3[i];
                if ($scope.ItemCat.id==newValue){
                    $scope.goods.typeTemplateId = $scope.ItemCat.typeId;
                    break;
                }
            }
        }else {
            $scope.goods.typeTemplateId=[];
        }
    });
    $scope.$watch('goods.typeTemplateId',function (newValue,oldValue) {
        if (newValue){
            $scope.findBrandListByTypeTemplateId($scope.goods.typeTemplateId);

        }else {
            $scope.brandList=[];
        }
    });

    $scope.uploadFile=function () {
      baseService.uploadFile().then(function (value) {
          if(value.data.status == 200){
              $scope.picEntity.url = value.data.url;
          }else {
              alert("上传失败");
          }
      });
    };
    $scope.addPic=function () {
        $scope.goods.goodsDesc.itemImages.push($scope.picEntity);
    };
    $scope.remove=function (index) {
        alert(index);
        $scope.goods.goodsDesc.itemImages.splice(index,1);
    };

    $scope.updateSpec=function ($event,name,value) {
        var obj=$scope.searchJsonByKey($scope.goods.goodsDesc.specificationItems,'attributeName', name);
        if (obj){
            if($event.target.checked){
                obj.attributeValue.push(value);
            }else {
                obj.attributeValue.splice(obj.attributeValue.indexOf(value),1);
            }
            if (obj.attributeValue.length==0){
                $scope.goods.goodsDesc.specificationItems.splice($scope.goods.goodsDesc.specificationItems
                    .indexOf(obj),1);
            }

        }else {
            $scope.goods.goodsDesc.specificationItems.push({"attributeValue":[value],"attributeName":name});
        }
    };
    $scope.createItems=function () {
        // $scope.goods.items = [{spec:{}, price:0, num:9999,status:'0', isDefault:'0'}];
        $scope.goods.items = [{spec:{}, price:0, num:9999, status:'0', isDefault:'0' }];
        var specItem=$scope.goods.goodsDesc.specificationItems;
        for (var i=0;i<specItem.length;i++){
            $scope.goods.items=$scope.swapItems($scope.goods.items,specItem[i].attributeName,specItem[i].attributeValue);
        }
    };

    $scope.swapItems=function (items,attributeName,attributeValue) {
        var newItems=new Array();
        for (var i=0;i<items.length;i++){
            var item=items[i];
            for (var j=0;j<attributeValue.length;j++){
                var newItem=JSON.parse(JSON.stringify(item));
                newItem.spec[attributeName]=attributeValue[j];
                newItems.push(newItem);
            }
        }
        return newItems;
    };
    //应该是 要给增加属性吧
    $scope.searchJsonByKey=function (jsonArr,key,value) {
        for (var i=0;i<jsonArr.length;i++){
            if (jsonArr[i][key]==value){
                return jsonArr[i];
            }
        }
    };


    $scope.updateMarketable=function (marketable) {
        if ($scope.ids.length>0){
            baseService.sendGet("/goods/updateMarketable?" +
                "marketable="+marketable+"&ids="+$scope.ids)
                .then(function (value) {
                   if (value.data){
                       alert("商品上下架成功");
                       $scope.ids=[];
                       $scope.reload();
                   }else {
                       alert("操作失败");
                   }
                });
        }else {
            alert("请选择要操作的对象");
        }
    };


});