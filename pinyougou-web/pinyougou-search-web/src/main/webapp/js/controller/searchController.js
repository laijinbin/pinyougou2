/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope,$sce,$location,$controller,baseService) {

    $controller("baseController", {$scope:$scope});
    $scope.searchParam = {keywords : '', category : '',
        brand : '', price : '', spec : {},page:1,rows:20,sortField : '', sort : ''};

    $scope.resultMap={};
    $scope.search=function () {
      baseService.sendPost("/Search",$scope.searchParam)
          .then(function (value) {
              $scope.resultMap = value.data;
              $scope.keyword=$scope.searchParam.keywords;
          });
      $scope.initPageNum();
    };
    
    $scope.trustHtml=function (value) {
      return $sce.trustAsHtml(value);
    };

    $scope.addSearchItem=function (name,value) {
      if (name=='category' || name=='brand' || name=='price') {
          $scope.searchParam[name]=value;
      }else {
          $scope.searchParam.spec[name]=value;
      }
        $scope.search();
    };

    $scope.removeSearchItem=function (name) {
        if (name=='category' || name=='brand' || name=='price') {
            $scope.searchParam[name]="";
        }else {
           delete $scope.searchParam.spec[name];
        }
        $scope.search();
    };

    $scope.initPageNum=function () {
      $scope.pageNums=[];
      var totalPage=$scope.resultMap.totalPages;//总页数
        var firstPage=1;//开始页码
        var lastPage=totalPage;//结束页码
        /** 前面有点 */
        $scope.firstDot = true;
        /** 后面有点 */
        $scope.lastDot = true;


        //如果开始总页数大于5
        if (totalPage>5){
            //如果当前页码位置在前面
            if ($scope.searchParam.page<=3){
                lastPage=5;
                $scope.firstDot=false;
            }else if($scope.searchParam.page>=totalPage-3){
                firstPage=totalPage-4;
                $scope.lastDot=false;
            }else {
                firstPage=$scope.searchParam.page-2;
                lastPage=$scope.searchParam.page+2;
            }
        }else {
            $scope.firstDot = false; // 前面没点
            $scope.lastDot = false; // 后面没点
        }
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }

    };
    $scope.pageSearch = function(page){
        page = parseInt(page);
        /** 页码验证 */
        if (page >= 1 && page <= $scope.resultMap.totalPages
            && page != $scope.searchParam.page){
            $scope.searchParam.page = page;
            $scope.search();
        }
    };

    $scope.sortSearch = function(sortField, sort){
        $scope.searchParam.sortField = sortField;
        $scope.searchParam.sort = sort;
        $scope.search();
    };

    $scope.getkeywords=function () {
        $scope.searchParam.keywords = $location.search().keywords;
        $scope.search();
    };
});
