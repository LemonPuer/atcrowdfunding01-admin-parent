function fillAuthTree() {
    // 1.发送 Ajax 请求查询 Auth 数据
    var ajaxReturn = $.ajax({
        "url": "assgin/get/all/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status != 200) {
        layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： " + ajaxReturn.status + " 说 明 是 ：" + ajaxReturn.statusText);
        return;
    }
    // 2.从响应结果中获取 Auth 的 JSON 数据
    // 从服务器端查询到的 list 不需要组装成树形结构，这里我们交给 zTree 去组装
    var authList = ajaxReturn.responseJSON.data;
    // 3.准备对 zTree 进行设置的 JSON 对象
    var setting = {
        "data": {
            "simpleData": {
                // 开启简单 JSON 功能
                "enable": true, // 使用 categoryId 属性关联父节点，不用默认的 pId 了
                "pIdKey": "categoryId"
            }, "key": {
                // 使用 title 属性显示节点名称，不用默认的 name 作为属性名了
                "name": "title"
            }
        }, "check": {
            "enable": true
        }
    };
    // 4.生成树形结构
    // <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    // 获取 zTreeObj 对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 调用 zTreeObj 对象的方法，把节点展开
    zTreeObj.expandAll(true);
    // 5.查询已分配的 Auth 的 id 组成的数组
    ajaxReturn = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status != 200) {
        layer.msg(" 请 求 处 理 出 错 ！ 响 应 状 态 码 是 ： " + ajaxReturn.status + " 说 明 是 ：" + ajaxReturn.statusText);
        return;
    }
    // 从响应结果中获取 authIdArray
    var authIdArray = ajaxReturn.responseJSON.data;
    // 6.根据 authIdArray 把树形结构中对应的节点勾选上
    // ①遍历 authIdArray
    for (var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        // ②根据 id 查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        // ③将 treeNode 设置为被勾选
        // checked 设置为 true 表示节点勾选
        var checked = true;
        // checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
        // 执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}

function showConfirmModal(roleArray) {
    //打开模态框
    $("#confirmModal").modal("show");
    //清除旧数据
    $("#roleNameDiv").empty();
    //声明全局变量存储角色id
    window.roleIdArray = [];
    for (var i = 0; i < roleArray.length; i++) {
        $("#roleNameDiv").append("<h5 style='color: darkred'>" + roleArray[i].name + "</h5>");
        window.roleIdArray.push(roleArray[i].id);
    }
}

function generatePage() {
    //1.获取分页数据
    var pageInfo = getPageInfoRemote();
    //2.填充表格
    fillTableBody(pageInfo);
}

//远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {
    //调用$.ajax()函数发送请求并接受$.ajax()函数的返回值
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,//同步调用；防止数据还未接受就执行下面代码
        "dataType": "json"
    });
    // console.log(ajaxResult);
    var statusCode = ajaxResult.status;
    //如果响应状态码不是200，说明出现服务器返回意外情况，显示提示消息
    if (statusCode !== 200) {
        layer.msg("失败！响应状态码=" + statusCode + "说明信息=" + ajaxResult.statusText);
        return null;
    }
    var resultEntity = ajaxResult.responseJSON;
    var result = resultEntity.result;
    if (result === "FAILED") {
        //这里是后端代码的异常信息
        layer.msg(resultEntity.message);
        return null;
    }
    return resultEntity.data;
}

//填充表格
function fillTableBody(pageInfo) {
    //清除表格中的旧数据
    $("#rolePageBody").empty();
    //在没有搜索结果时不显示页码导航条
    $("#Pagination").empty();
    //判断pageInfo是否有效
    if (pageInfo == null || pageInfo == undefined
        || pageInfo.list == null || pageInfo.list.lang == 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>" +
            "抱歉！没有查询到您搜索的数据!</td></tr>");
        return;
    }
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkBoxTd = "<td><input id='" + role.id + "' class='itemBox' type='checkbox'></td>";
        var nameTd = "<td>" + role.name + "</td>";
        var checkBtn = "<button id='" + role.id + "' type='button' class='btn btn-success btn-xs checkBtn'><i " +
            "class='glyphicon glyphicon-check'></i></button>";
        var pencilBtn = "<button id='" + role.id + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i " +
            "class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + role.id + "' type='button' class='btn btn-danger btn-xs removeBtn'><i " +
            "class='glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        var tr = "<tr>" + numberTd + checkBoxTd + nameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }
    //生成分页导航条
    generateNavigator(pageInfo);
}

function generateNavigator(pageInfo) {
    //获取总记录数
    var totalRecord = pageInfo.total;
    //声明相关属性
    var properties = {
        num_edge_entries: 3,								// 边缘页数
        num_display_entries: 3,								// 主体页数
        callback: paginationCallback,						// 指定用户点击“翻页”的按钮时跳转页面的回调函数
        items_per_page: pageInfo.pageSize,	// 每页要显示的数据的数量
        current_page: pageInfo.pageNum - 1,	// Pagination内部使用pageIndex来管理页码，pageIndex从0开始，pageNum从1开始，所以要减一
        prev_text: "上一页",									// 上一页按钮上显示的文本
        next_text: "下一页"									// 下一页按钮上显示的文本
    };

    //调用pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}

function paginationCallback(pageIndex, jQuery) {
    //修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;
    //调用分页函数；这里不是同步调用（点页码才调用），所以不会出现死循环
    generatePage();
    //取消页码超链接的默认行为
    return false;
}