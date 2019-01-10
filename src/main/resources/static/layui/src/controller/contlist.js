layui.define(['table', 'form'], function (exports) {
    var $ = layui.$,
        admin = layui.admin,
        view = layui.view,
        table = layui.table,
        form = layui.form,
        router = layui.router(),
        search = router.search

    //公共模块
    var show_message = function (msg ,type, table_render, index) {
        layer.msg(msg, {
            offset: '15px',
            icon: type,
            time: 1000
        }, function() {
            layer.close(index);
            table_render.reload()
        });
    }

    var headers = function () {
        var res = {
            'headers':{
                "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
            }
        }
        return res
    }

    var resolve_res = function () {
        var res = arguments[0]
        var table_render = arguments[1]
        var option = arguments[2]
        var index = arguments[3]
        var obj = ""
            if (arguments.length==5){
                obj = arguments[4]
            }
            if(res.status==200&&res.data.status==0){

                show_message(option+"成功", 1, table_render, index)
                if (obj != ""){
                    obj.del()
                }
            }
            else if(res.status==200&&res.data.status==500){
                show_message("登录状态失效", 5, table_render, index)
            }
            else if(res.status == 200 && res.data.status == 403) {
                show_message("权限不足", 5, table_render, index)
            }
            else{
                show_message("系统错误", 2, table_render, index)
            }
    }

    //**********用户管理**********
    var user_active = {
        flushkey: function() {
            table.reload('LAY-user-user-list',{where:{userName:""}});
        },
        add: function(othis) {
            admin.popup({
                title: '添加用户',
                area: ['550px', '450px'],
                id: 'LAY-popup-user-add',
                success: function(layero, index) {
                    view(this.id).render('user/user_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/user', field, headers)
                                .then(res => {
                                    resolve_res(res, user_render, "添加用户", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-user-user-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.user_opt').on('click', function() {
        var type = $(this).data('type');
        user_active[type] ? user_active[type].call(this) : '';
    });

    var user_render =table.render({
        elem: '#LAY-user-user-list'
        ,url: '/user'
        ,request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        }
        ,parseData: function(res){
            return {
                "code": res.status,
                "msg": res.msg,
                "count": res.total,
                "data": res.data
            };
        },
        done: function(res){
            if(res.code==403){
                layer.msg("权限不足", {
                    offset: '15px',
                    icon: 2,
                    time: 1000
                }, function() {
                });
            }
        },
        cols: [[
            {field: 'userName', title: '用户名', width:200}
            ,{field: 'role', title: '角色', width:200, sort: true}
            ,{field: 'createdAt', title: '创建日期', width:300, sort: true, templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"}
            ,{field: 'description', title: '描述', minWidth: 100}
            ,{title: '操作', width: 150, align: 'center', fixed: 'right', toolbar: '#table-user-list'}
        ]],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });
    //监听工具条
    table.on('tool(LAY-user-user-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此用户？', function (index) {
                axios.delete('/user/' + data.userId)
                    .then(res => {
                        resolve_res(res, user_render, "删除用户", index, obj)
                    })
                    .catch(err => {
                        alert(JSON.stringify(err))
                    });
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改用户',
                area: ['550px', '450px'],
                id: 'LAY-popup-user-edit',
                success: function (layero, index) {
                    view(this.id).render('user/user_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');
                        switch (data.role) {
                            case 'USER':
                                $("input[name=role][title='USER']").attr("checked", true);
                                break;
                            case 'READONLY':
                                $("input[name=role][title='READONLY']").attr("checked", true);
                        }
                        form.render('radio')
                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/user', field, headers)
                            .then(res => {
                                resolve_res(res, user_render, "修改用户", index)
                            })
                            .catch(err => {
                                alert(err)
                            })
                            return false
                            layui.table.reload('LAY-user-user-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    });

    //**********文件管理**********
    var file_active = {
        flushkey: function() {
            table.reload('LAY-file-file-list',{where:{keyword:""}});
        },
        add: function(othis) {
            admin.popup({
                title: '添加文件',
                area: ['550px', '350px'],
                id: 'LAY-popup-file-add',
                success: function(layero, index) {
                    view(this.id).render('file/file_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }

                            var field = data.field; //获取提交的字段
                            axios.post('/file', field ,headers)
                                .then(res => {
                                    resolve_res(res, file_render, "添加文件", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-file-file-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.file_opt').on('click', function() {
        var type = $(this).data('type');
        file_active[type] ? file_active[type].call(this) : '';
    });

    var file_render = table.render({
        elem: '#LAY-file-file-list',
        url: '/file' ,//模拟接口，
        request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        },

         parseData: function (res) { //res 即为原始返回的数据
            return {
                "code": res.status, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.total, //解析数据长度
                "data": res.data //解析数据列表
            };
        },
        done: function (res) {
            if (res.code == 403) {
                view.exit()
            }
        },
        cols: [
            [{
                field: 'fileName',
                width: 250,
                title: '文件名'
            }, {
                field: 'createdAt',
                width: 250,
                title: '创建日期',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }, {
                field: 'description',
                title: '描述',
                minWidth: 100
            }, {
                title: '操作',
                width: 200,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-file-list'
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });

    //监听工具条
    table.on('tool(LAY-file-file-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此文件？', function (index) {
                var headers = {
                    'headers': {
                        "Authorization": JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                    }
                }
                axios.delete('/file/' + data.fileId, headers)
                    .then(res => {
                        resolve_res(res, file_render, "删除文件", index, obj)
                    })
                    .catch(err => {
                        console.log(err);
                    });

                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改文件',
                area: ['550px', '350px'],
                id: 'LAY-popup-file-edit',
                success: function (layero, index) {
                    view(this.id).render('file/file_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function (data) {
                            var field = data.field;
                            var headers = {
                                'headers': {
                                    "Authorization": JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/file', field, headers)
                                .then(res => {
                                    resolve_res(res, file_render, "修改文件", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                        });
                    });
                }
            });
        }
        else if(obj.event=="view"){
            location.hash = search.redirect ? decodeURIComponent(search.redirect) : '/file/file_view/fileId='+data.fileId+"/fileName="+data.fileName;
        }
    });

    //服务列表
    var service_active = {
        add: function(othis) {
            admin.popup({
                title: '添加服务',
                area: ['550px', '550px'],
                id: 'LAY-popup-service-add',
                success: function(layero, index) {
                    view(this.id).render('service/service_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(service-layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/service', field, headers)
                                .then(res => {
                                    resolve_res(res, service_render, "添加服务", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-service-service-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.service_opt').on('click', function() {
        var type = $(this).data('type');
        service_active[type] ? service_active[type].call(this) : '';
    });

    var service_render= table.render({
        elem: '#LAY-service-service-list',
        url: '/service', //模拟接口
        request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        }
        , parseData: function (res) { //res 即为原始返回的数据
            return {
                "code": res.status, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.total, //解析数据长度
                "data": res.data //解析数据列表
            };
        },
        done: function (res) {
            if (res.code == 0) {
                data = res.data
                var tag_list = document.getElementsByClassName("s_status")
                for (var i in data){
                    if(data[i].status){
                        tag_list[i].title = "停止程序"
                        tag_list[i].innerHTML = '<i class="layui-icon layui-icon-pause"></i>'
                    }else{
                        tag_list[i].title = "启动程序"
                        tag_list[i].innerHTML = '<i class="layui-icon layui-icon-play"></i>'
                    }
                }
            }
            if (res.code == 403) {
                view.exit()
            }
        },
        cols: [
            [{
                field: 'serviceName',
                width: 250,
                title: '服务名'
            }, {
                field: 'createdAt',
                width: 250,
                title: '创建日期',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }, {
                field: 'description',
                title: '描述',
                minWidth: 100
            }, {
                title: '操作',
                width: 200,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-service-list'
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });

    //监听工具条
    table.on('tool(LAY-service-service-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此服务？', function (index) {
                var headers = {
                    'headers':{
                        "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                    }
                }
                axios.delete('/service/' + data.serviceId,headers)
                    .then(res => {
                        resolve_res(res, service_render, "删除服务", index, obj)
                    })
                    .catch(err => {
                       alert(err)
                    });
                 obj.del();
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改服务',
                area: ['550px', '550px'],
                id: 'LAY-popup-service-edit',
                success: function (layero, index) {
                    view(this.id).render('service/service_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');
                        setTimeout(function () {
                            switch (data.type) {
                                case 'JAR':
                                    $("input[name=type][title='Java 程序']").attr("checked", true);
                                    break;
                                case 'EXE':
                                    $("input[name=type][title='可执行程序']").attr("checked", true);
                                    break;
                                case 'PY2':
                                    $("input[name=type][title='Python2 脚本']").attr("checked", true);
                                    break;
                                case 'PY3':
                                    $("input[name=type][title='Python3 脚本']").attr("checked", true);
                                    break;
                                case 'SHELL':
                                    $("input[name=type][title='shell 脚本']").attr("checked", true);
                                    break;
                                case 'OTHER':
                                    $("input[name=type][title='其他服务']").attr("checked", true);
                            }
                            form.render('select')
                            form.render('radio')
                        }, 120)

                        //监听提交
                        form.on('submit(service-layuiadmin-app-form-submit)', function (data) {

                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/service', field, headers)
                                .then(res => {
                                    resolve_res(res, service_render, "修改服务", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-service-service-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        } else if (obj.event === 'start'){
            var headers = {
                'headers': {
                    "Authorization": JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                }
            }
            var event = "start"
            if (data.status){
                event = 'stop'
            } else{
                event = "start"
            }
            var show_msg = function(msg, type) {
                layer.msg(msg, {
                    offset: '15px',
                    icon: type,
                    time: 1000
                }, function() {
                });
            }
            axios.get('/service/'+event+'/'+data.serviceId, headers)
                .then(res => {
                    var status = res.data.status
                    if(status == 201){
                        show_msg("文件不存在", 2)
                    }
                    if(status == 0){
                        show_msg('启动成功', 1)
                        var index = parseInt(obj.tr.selector.slice(-3,-2))
                        var tag_list = document.getElementsByClassName("s_status")
                        tag_list[index].title = "停止程序"
                        tag_list[index].innerHTML = '<i class="layui-icon layui-icon-pause"></i>'
                    }
                    if(status == 500){
                        show_msg("系统错误", 2)
                    }
                })
                .catch(err => {
                })
            layui.table.reload('LAY-service-service-list');
        }
    });

    //**********日志管理**********
    var log_active = {
        flushkey: function() {
            table.reload('LAY-log-log-list',{where:{keyword:""}});
        },
        del: function(othis) {
            admin.popup({
                title: '日志清理',
                area: ['550px', '300px'],
                id: 'LAY-popup-log-del',
                success: function(layero, index) {
                    view(this.id).render('log/log_del').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/log/logdel', field, headers)
                                .then(res => {
                                    resolve_res(res, log_render, "日志清除", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        },
        add: function(othis) {
            admin.popup({
                title: '添加日志',
                area: ['550px', '550px'],
                id: 'LAY-popup-log-add',
                success: function(layero, index) {
                    view(this.id).render('log/log_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');
                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/log', field, headers)
                                .then(res => {
                                    resolve_res(res, log_render, "添加日志", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-log-log-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.log_opt').on('click', function() {
        var type = $(this).data('type');
        log_active[type] ? log_active[type].call(this) : '';
    });

    var log_render= table.render({
        elem: '#LAY-log-log-list',
        url: '/log',
        request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        },
        parseData: function(res){
            return {
                "code": res.status,
                "msg": res.msg,
                "count": res.total,
                "data": res.data
            };
        },
        done: function(res){
            if(res.code==403){
                layer.msg("权限不足", {
                    offset: '15px',
                    icon: 2,
                    time: 1000
                }, function() {
                });
            }
        },
        cols: [
            [{
                field: 'name',
                width: 200,
                title: '服务名'
            }, {
                field: 'createdAt',
                width: 200,
                title: '创建日期',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }, {
                field: 'description',
                title: '描述',
                minWidth: 100
            }, {
                field: 'logPath',
                title: '日志路径',
                minWidth: 100
            }, {
                title: '操作',
                width: 240,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-log-list'
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });

    //监听工具条
    table.on('tool(LAY-log-log-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此日志？', function (index) {
                var headers = {
                    'headers':{
                        "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                    }
                }
                axios.delete('/log/' + data.logId, headers)
                    .then(res => {
                        resolve_res(res, log_render, "删除日志", index)
                    })
                    .catch(err => {
                        alert(err)
                    });
                obj.del();
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改日志',
                area: ['550px', '550px'],
                id: 'LAY-popup-log-edit',
                success: function (layero, index) {
                    view(this.id).render('log/log_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');
                        setTimeout(function () {
                            $('#dbId').val(data.dbId)
                            form.render('select')
                        }, 70)

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/log', field, headers)
                                .then(res => {
                                    resolve_res(res, log_render, "修改日志", index)
                                })
                               .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-log-log-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        } else if (obj.event === 'item') {
            admin.popup({
                title: '配置日志字段',
                area: ['810px', '550px'],
                id: 'LAY-popup-log-edit',
                success: function (layero, index) {
                    view(this.id).render('log/log_item', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(onsub)', function (d) {
                            var field = d.field; //获取提交的字段
                            var length = Object.keys(field).pop().substring(4)
                            list = []
                            for(var i = 0; i<=length; i++){
                                item = {}
                                var key = document.getElementById('key'+i).value
                                var name = document.getElementById('name'+i).value
                                var type = document.getElementById('type'+i).value
                                var show = document.getElementById('show'+i).checked
                                item['key'] = key
                                item['name'] = name
                                item['type'] = type
                                item['show'] = show
                                list.push(item)
                            }
                            axios.post('/log/fields/'+data.logId, list, headers())
                                .then(res => {
                                    resolve_res(res, log_render, "配置日志字段", index)
                                })
                                .catch(err => {
                                    console.log(JSON.stringify(err))
                                })
                            layui.table.reload('LAY-log-log-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        } else if (obj.event == "view") {
            location.hash = search.redirect ? decodeURIComponent(search.redirect) : '/log/log_view/logId='+data.logId+"/logName="+data.name;
        }
    });

    //**********数据库管理**********
    var db_active = {
        flushkey: function() {
            table.reload('LAY-dbinfo-db-list',{where:{keyword:""}});
        },
        add: function(othis) {
            admin.popup({
                title: '添加数据库',
                area: ['550px', '550px'],
                id: 'LAY-popup-db-add',
                success: function(layero, index) {
                    view(this.id).render('dbinfo/db_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/db', field, headers)
                                .then(res => {
                                    resolve_res(res, db_render, "添加数据库", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-dbinfo-db-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.db_opt').on('click', function() {
        var type = $(this).data('type');
        db_active[type] ? db_active[type].call(this) : '';
    });

    var db_render =table.render({
        elem: '#LAY-dbinfo-db-list'
        ,url: '/db'
        ,request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        }
        ,parseData: function(res){
            return {
                "code": res.status,
                "msg": res.msg,
                "count": res.total,
                "data": res.data
            };
        },
        done: function(res){
            if(res.code==403){
                layer.msg("权限不足", {
                    offset: '15px',
                    icon: 2,
                    time: 1000
                }, function() {
                });
            }
        },
        cols: [
            [{
                field: 'name',
                width: 250,
                title: '数据库名'
            }, {
                field: 'createdAt',
                width: 250,
                title: '创建日期',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }, {
                field: 'description',
                title: '描述',
                minWidth: 100
            }, {
                title: '操作',
                width: 150,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-dbinfo-list'
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '数据加载出现异常！'
    });

    //监听工具条
    table.on('tool(LAY-dbinfo-db-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此数据库？', function (index) {
                var headers = {
                    'headers':{
                        "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                     }
                }
                axios.delete('/db/' + data.dbId, headers)
                    .then(res => {
                        resolve_res(res, db_render, "删除数据库", index, obj)
                    })
                    .catch(err => {
                        alert(JSON.stringify(err))
                    });
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改数据库',
                area: ['550px', '550px'],
                id: 'LAY-popup-db-edit',
                success: function (layero, index) {
                    view(this.id).render('dbinfo/db_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');
                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/db', field, headers)
                            .then(res => {
                                resolve_res(res, db_render, "修改数据库", index)
                            })
                            .catch(err => {
                                alert(err)
                            })
                            return false
                            layui.table.reload('LAY-dbinfo-db-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    });

    //**********配置管理**********
    var config_active = {
        flushkey: function() {
            table.reload('LAY-configinfo-config-list',{where:{keyword:''}});
        },
        add: function(othis) {
            admin.popup({
                title: '添加配置',
                area: ['550px', '450px'],
                id: 'LAY-popup-config-add',
                success: function(layero, index) {
                    view(this.id).render('configinfo/config_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/config', field, headers)
                                .then(res => {
                                    resolve_res(res, config_render, "添加配置", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-configinfo-config-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.config_opt').on('click', function() {
        var type = $(this).data('type');
        config_active[type] ? config_active[type].call(this) : '';
    });

    var config_render = table.render({
        elem: '#LAY-configinfo-config-list',
        url: '/config',
        request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        },
        parseData: function(res){
            return {
                 "code": res.status,
                 "msg": res.msg,
                 "count": res.total,
                 "data": res.data
            };
        },
        done: function(res){
            if(res.code==403){
                layer.msg("权限不足", {
                    offset: '15px',
                    icon: 2,
                    time: 1000
                }, function() {
                });
            }
        },
        cols: [
            [{
                field: 'name',
                width: 200,
                title: '配置名'
            }, {
                field: 'tableName',
                width: 200,
                title: '表名'
            }, {
                field: 'createdAt',
                width: 200,
                title: '创建日期',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }, {
                field: 'description',
                title: '描述',
                minWidth: 100
            }, {
                title: '操作',
                width: 240,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-configinfo-list'
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });

    //监听工具条
    table.on('tool(LAY-configinfo-config-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此配置？', function (index) {
                var headers = {
                    'headers':{
                        "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                     }
                }
                axios.delete('/config/' + data.configId, headers)
                    .then(res => {
                        resolve_res(res, config_render, "删除配置", index, obj)
                    })
                    .catch(err => {
                        alert(JSON.stringify(err))
                    });
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改配置',
                area: ['550px', '450px'],
                id: 'LAY-popup-config-edit',
                success: function (layero, index) {
                    view(this.id).render('configinfo/config_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');
                        setTimeout(function () {
                            $('#dbId').val(data.dbId)
                            form.render('select')
                        }, 70)

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/config', field, headers)
                            .then(res => {
                                resolve_res(res, config_render, "修改配置", index)
                            }).catch(err => {
                                alert(err)
                            })
                            return false
                            layui.table.reload('LAY-configinfo-config-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        } else if (obj.event === 'item') {
            admin.popup({
                title: '配置字段',
                area: ['750px', '550px'],
                id: 'LAY-popup-config-item',
                success: function (layero, index) {
                    view(this.id).render('configinfo/config_item', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');

                        //监听提交
                        form.on('submit(onsub)', function (d) {
                            var field = d.field; //获取提交的字段
                            var list = []
                            var i = 0
                            var item = {}
                            for (var it in field){
                                switch(i){
                                    case 0:
                                        item['key'] = field[it];
                                        break;
                                    case 1:
                                        item['name'] = field[it];
                                        break;
                                    case 2:
                                        item['type'] = field[it];
                                        break;
                                }
                                i++
                                if (i==3){
                                    item['show'] = false;
                                    var base = {};
                                    base['key'] = item.key;
                                    base['name'] = item.name;
                                    base['type'] = item.type;
                                    base['show'] = item.show;
                                    list.push(base);
                                    i = 0
                                }
                            }
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                           axios.post('/config/fields/' + data.configId, list, headers)
                               .then(res => {
                                   resolve_res(res, config_render, "配置字段", index)
                               })
                               .catch(err => {
                                   alert(err)
                               })
                            return false
                            layui.table.reload('LAY-configinfo-config-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        } else if (obj.event == "view") {
            location.hash = search.redirect ? decodeURIComponent(search.redirect) : '/configinfo/config_view/configId='+data.configId+"/configName="+data.name;
        }
    });

    //**********定时任务**********
    var schedule_active = {
        flushkey: function() {
            table.reload('LAY-schedule-schedule-list',{where:{keyword:""}});
        },
        add: function(othis) {
            admin.popup({
                title: '添加定时任务',
                area: ['550px', '600px'],
                id: 'LAY-popup-schedule-add',
                success: function(layero, index) {
                    view(this.id).render('schedule/schedule_form').done(function() {
                        form.render(null, 'layuiadmin-app-form-list');


                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function(data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.post('/schedule', field, headers)
                                .then(res => {
                                    resolve_res(res, schedule_render, "添加定时任务", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-schedule-schedule-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        }
    };

    $('.layui-btn.layuiadmin-btn-list.schedule_opt').on('click', function() {
        var type = $(this).data('type');
        schedule_active[type] ? schedule_active[type].call(this) : '';
    });
    var schedule_render = table.render({
        elem: '#LAY-schedule-schedule-list',
        url: '/schedule',
        parseData: function(res){
            return {
                 "code": res.status,
                 "msg": res.msg,
                 "count": res.total,
                 "data": res.data
            };
        },
        done: function(res){
            if(res.code==403){
                layer.msg("权限不足", {
                    offset: '15px',
                    icon: 2,
                    time: 1000
                }, function() {
                });
            }
        },
        cols: [
            [{
                field: 'scheduleName',
                width: 250,
                title: '任务名'
            }, {
                field: 'timerType',
                width: 250,
                title: '类型'
            }, {
                field: 'createdAt',
                width: 250,
                title: '创建日期',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }, {
                field: 'description',
                title: '描述',
                minWidth: 100
            }, {
                title: '操作',
                width: 200,
                align: 'center',
                fixed: 'right',
                toolbar: '#table-schedule-list'
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });

    //监听工具条
    table.on('tool(LAY-schedule-schedule-list)', function (obj) {
        var data = obj.data;
        if (obj.event === 'del') {
            layer.confirm('确定删除此任务？', function (index) {
                var headers = {
                    'headers':{
                        "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                     }
                }
                axios.delete('/schedule/' + data.scheduleId, headers)
                    .then(res => {
                        resolve_res(res, schedule_render, "删除定时任务", index, obj)
                    })
                    .catch(err => {
                        alert(JSON.stringify(err))
                    });
                layer.close(index);
            });
        } else if (obj.event === 'edit') {
            admin.popup({
                title: '修改定时任务',
                area: ['550px', '600px'],
                id: 'LAY-popup-schedule-edit',
                success: function (layero, index) {
                    view(this.id).render('schedule/schedule_form', data).done(function () {
                        form.render(null, 'layuiadmin-app-form-list');
                        setTimeout(function () {
                            $('#dbId').val(data.extra)
                            form.render('select')
                        }, 70)

                        //监听提交
                        form.on('submit(layuiadmin-app-form-submit)', function (data) {
                            var field = data.field; //获取提交的字段
                            var headers = {
                                'headers':{
                                    "Authorization":JSON.parse(window.localStorage.getItem("layuiAdmin")).token
                                }
                            }
                            axios.patch('/schedule', field, headers)
                                .then(res => {
                                    resolve_res(res, schedule_render, "修改定时任务", index)
                                })
                                .catch(err => {
                                    alert(err)
                                })
                            return false
                            layui.table.reload('LAY-schedule-schedule-list'); //重载表格
                            layer.close(index); //执行关闭
                        });
                    });
                }
            });
        } else if (obj.event == "view") {
            location.hash = search.redirect ? decodeURIComponent(search.redirect) : '/schedule/schedule_view/scheduleId='+data.scheduleId+"/scheduleName="+data.scheduleName;
        }
    });

    //********定时任务日志********
    var scheduleview_active = {
        flushkey: function() {
            table.reload('LAY-schedule-schedule-view',{where:{dateTime:clear}});
        }
    };
    $('.layui-btn.layuiadmin-btn-list.scheduleview_opt').on('click', function() {
        var type = $(this).data('type');
        scheduleview_active[type] ? scheduleview_active[type].call(this) : '';
    });
    table.render({
        elem: '#LAY-schedule-schedule-view',
        url: '/schedule/' + search.scheduleId + '/log',
        request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        },
        parseData: function (res) {
            return {
                "code": res.status,
                "msg": res.msg,
                "count": res.total,
                "data": res.data
            };
        },
        cols: [
            [{
                field: 'execute',
                title: '执行命令',
                minWidth: 100
            }, {
                field: 'status',
                width: 250,
                title: '状态'
            }, {
                field: 'log',
                title: '输出日志',
                minWidth: 100
            }, {
                field: 'createdAt',
                width: 250,
                title: '执行时间',
                sort: true,
                templet: "<div>{{layui.util.toDateString(d.createdAt, 'yyyy-MM-dd HH:mm:ss')}}</div>"
            }]
        ],
        page: true,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: '对不起，加载出现异常！'
    });
    exports('contlist', {})
});
