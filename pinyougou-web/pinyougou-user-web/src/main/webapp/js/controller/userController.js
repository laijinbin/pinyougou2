/** 定义控制器层 */
app.controller('userController', function ($scope, $controller, $http, $location, baseService) {
    $controller('indexController', {$scope: $scope});
    $scope.address = {};
    $scope.user={};
    $scope.newPhone='';
    $scope.save = function () {
        if ($scope.password != $scope.password) {
            alert("2次输入的密码不一致");
            return;
        }
        baseService.sendPost("/user/save?smsCode=" + $scope.smsCode, $scope.user)
            .then(function (value) {
                if (value.data) {
                    alert("注册成功");
                    $scope.user = {};
                    $scope.password = "";
                    $scope.smsCode = "";
                } else {
                    alert("注册失败");
                }
            });
    };
    $scope.sendCode = function () {
        if ($scope.newPhone){
            alert($scope.newPhone);
            $scope.user.phone=$scope.newPhone;
        }
        if ($scope.user.phone) {
            baseService.sendGet("/user/sendCode?phone="
                + $scope.user.phone)
                .then(function (value) {
                    alert(value.data ? "发送成功！" : "发送失败！");
                });

        } else {
            alert("请输入手机号码");
        }
    };
    $scope.findAddress = function () {
        baseService.sendGet("/address/findAddress")
            .then(function (value) {
                $scope.addressList = value.data;

            });
    };
    $scope.deleteAddress = function (address) {
        if (address.isDefault == 1) {
            alert("默认地址不能删除。");
        } else {
            baseService.sendGet("/address/deleteAddress?id=" + address.id)
                .then(function (value) {
                    if (value.data) {
                        alert("删除成功");
                        $scope.findAddress();
                    }
                });
        }
    };
    $scope.saveOrUpdate = function () {
        var url = "save";
        if ($scope.address.id) {
            url = "update";
        }
        baseService.sendPost("/address/" + url, $scope.address)
            .then(function (value) {
                if (value.data) {
                    alert("操作成功");
                    $scope.findAddress();
                }
            });
    };
    $scope.show = function (address) {
        $scope.address = JSON.parse(JSON.stringify(address));
    }
    $scope.setDefault = function (id) {
        baseService.sendGet("/address/setDefault?id=" + id)
            .then(function (value) {
                if (value.data) {
                    alert("当前地址已设置为默认地址");
                    $scope.findAddress();
                } else {
                    alert("操作失误，再来一次");
                }
            });
    };

    $scope.savePwd = function () {
        if ($scope.password.oldPwd == '') {
            alert("原密码不能为空");
            return;
        } else if ($scope.password.newPwd == '') {
            alert("新密码不能为空");
            return;
        } else if ($scope.pwd == '') {
            alert("请再次输入新密码");
            return;
        } else if ($scope.password.newPwd != $scope.pwd) {
            alert("两次新密码不一致，请重新输入");
            return;
        } else {
            $http.post("/user/savePwd", {
                "newPwd": $scope.password.newPwd,
                "oldPwd": $scope.password.oldPwd
            })
                .then(function (value) {
                    alert(value.data.msg);
                    if (value.data.flag == "true") {
                        location.href = "http://sso.pinyougou.com/logout?service=" + $scope.redirectUrl;
                    }
                });
        }
    };
    $scope.findUserById = function () {
        baseService.sendGet("/user/findUserById")
            .then(function (value) {
                $scope.user = value.data;
                $scope.user.phone=value.data.phone;
                $scope.phone = value.data.phone.substr(0, 3) + "****" + value.data.phone.substr(7);
            });
    };
    $scope.getCode = function () {
        document.getElementById("code").src = "/checkCode?id=" + new Date().getTime();
    };

    $scope.goNewPhone = function (step) {
        if ($scope.newPhone){
            alert($scope.newPhone);
            $scope.user.phone=$scope.newPhone;
        }
        $http.post("/user/goNewPhone",
            {"phone": $scope.user.phone, "code": $scope.code, "msgcode": $scope.msgcode,"step":step})
            .then(function (value) {
                if (value.data.flag) {
                    $scope.code = '';
                    $scope.phone='';
                    $scope.msgcode = '';
                    if (step == '1') {
                        location.href = "/home-setting-address-phone.html";
                    }
                    if (step == '2') {
                        location.href = "/home-setting-address-complete.html";
                    }
                } else {
                    alert(value.data.msg);
                }
            });
    };

});