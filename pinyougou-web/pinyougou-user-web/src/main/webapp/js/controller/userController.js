/** 定义控制器层 */
app.controller('userController', function($scope, baseService){

    $scope.save=function () {
      if ($scope.password!=$scope.password){
          alert("2次输入的密码不一致");
          return;
      }
      baseService.sendPost("/user/save?smsCode=" + $scope.smsCode,$scope.user)
          .then(function (value) {
              if (value.data){
                  alert("注册成功");
                  $scope.user={};
                  $scope.password="";
                  $scope.smsCode="";
              }else {
                  alert("注册失败");
              }
          });
    };
    $scope.sendCode=function () {
        if ($scope.user.phone){

      baseService.sendGet("/user/sendCode?phone="
          + $scope.user.phone)
          .then(function (value) {
              alert(value.data ? "发送成功！" : "发送失败！");
          });

        }else {
            alert("请输入手机号码");
        }
    };

});