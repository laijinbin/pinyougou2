/** 定义控制器层 */
app.controller('contentController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});
    $scope.status = ["无效","有效"];

    /** 查询条件对象 */
    $scope.searchEntity = {};
    $scope.contentCategoryList=[];
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/content/findByPage", page,
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
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/content/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                    alert("操作成功");
                    $scope.reload();
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
    $scope.delete= function(){
        if ($scope.ids.length > 0){
            baseService.delete("/content/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        alert("删除成功");
                        $scope.ids = [];
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };
    $scope.findAllContentCategory=function () {
      baseService.sendGet("/contentCategory/findAllContentCategory")
          .then(function (value) {
              $scope.contentCategoryList=value.data;
          });
    };

    $scope.uploadFile=function () {
        baseService.uploadFile().then(function (value) {
            if(value.data.status == 200){
                /** 设置图片访问地址 */
                $scope.entity.pic = value.data.url;
            }else{
                alert("上传失败！");
            }

        });
    };

    $scope.checkStatus=function ($event) {
        $scope.entity.status=$event.target.checked?'1':'0';
    };
});