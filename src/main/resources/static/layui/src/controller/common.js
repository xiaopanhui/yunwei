layui.define(function(exports){
  var $ = layui.$
  ,layer = layui.layer
  ,laytpl = layui.laytpl
  ,setter = layui.setter
  ,view = layui.view
  ,admin = layui.admin

  admin.events.logout = function(){
    admin.req({
      url: '/layui/src/controller/logout.js'
      ,type: 'get'
      ,data: {}
      ,done: function(res){
        window.localStorage.removeItem("user")
        admin.exit();
      }
    });
  };
  exports('common', {});
});