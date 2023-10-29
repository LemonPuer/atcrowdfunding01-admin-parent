<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script type="text/javascript" src="crowd/my-role.js" charset="UTF-8"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css"/>
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
    $(function () {
        window.pageNum = 1;
        window.pageSize = 6;
        window.keyword = "";
        generatePage();

        //查询按键绑定单机响应函数
        $("#searchBtn").click(function () {
            //1.获取关键字数据赋值给全局变量
            window.keyword = $("#keywordInput").val();
            //调用分页函数
            generatePage();
        });

        //点击新增按钮打开模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });

        //模态框中的保存按钮绑定单击响应函数
        $("#saveRoleBtn").click(function () {
            //1.获取文本框中的角色名称；
            // #addModal表示找到整个模态框；空格表示在后代元素中继续查找
            //[name=roleName]表示匹配name属性等于roleName的元素
            var roleName = $.trim($("#addModal [name=roleName]").val());
            //2.发送ajax请求
            $.ajax({
                url: "role/save.json",
                type: "post",
                data: {"name": roleName},
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功!");
                        //定位最后一页
                        window.pageNum = 9999999;
                        //重新加载分页数据
                        generatePage();
                    } else if (result = "FAILED") {
                        layer.msg("操作失败!" + response.message);
                    }
                },
                error: function (response) {
                    layer.msg(response.status + "" + response.statusText);
                }
            });

            //关闭模态框
            $("#addModal").modal("hide");
            //清理模态框
            $("#addModal [name=roleName]").val("");
        });

        //为所有行的铅笔绑定单击响应参数
        //使用传统的时间绑定方式只能在第一个分页有效；
        //我们这里使用jQuery对象的on()函数
        //首先找到所有“动态生成”元素所附着的静态元素
        //参数是事件类型；要绑定时间的元素选择器；响应函数
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            $("#editModal").modal("show");
            //获取当前行的角色名称（数据回显）
            var roleName = $(this).parent().prev().text();
            //获取当前角色的id;为了方便,存放在全局变量中
            window.roleId = this.id;
            //设置模态框中的文本框
            $("#editModal [name=roleName]").val(roleName);
        });

        //为更新模态框中的按钮绑定单击响应函数
        $("#updateRoleBtn").click(function () {
            //1.获取文本框中的名称
            var name = $("#editModal [name=roleName]").val();
            //2.发送ajax请求
            $.ajax({
                url: "role/update.json",
                type: "post",
                data: {
                    "id": window.roleId,
                    "name": name
                },
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功!");
                        //重新加载分页数据
                        generatePage();
                    } else if (result = "FAILED") {
                        layer.msg("操作失败!" + response.message);
                    }
                },
                error: function (response) {
                    layer.msg(response.status + "" + response.statusText);
                }
            });
            //关闭模态框
            $("#editModal").modal("hide");
        });

        //点击确认模态框中的删除按钮执行删除
        $("#removeRoleBtn").click(function () {
            var requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                url: "role/delete.json",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功!");
                        //重新加载分页数据
                        generatePage();
                    } else if (result = "FAILED") {
                        layer.msg("操作失败!" + response.message);
                    }
                },
                error: function (response) {
                    layer.msg(response.status + "" + response.statusText);
                }
            });
            $("#confirmModal").modal("hide");
        });

        //单条数据删除
        $("#rolePageBody").on("click", ".removeBtn", function () {
            var roleName = $(this).parent().prev().text();
            var roleArray = [{
                id: this.id,
                name: roleName
            }];
            showConfirmModal(roleArray);
        });

        //给全选框绑定单击响应函数
        $("#summaryBox").click(function () {
            //获取当前状态
            var currentStatus = this.checked;
            //选择当前页面所有显示的数据
            $(".itemBox").prop("checked", currentStatus);
        });

        //取消全选
        $("#rolePageBody").on("click", ".itemBox", function () {
            var checkedNum = $(".itemBox:checked").length;
            var num = $(".itemBox").length;
            $("#summaryBox").prop("checked", checkedNum === num);
        });

        //批量删除绑定单击响应函数
        $("#batchRemoveBtn").click(function () {
            var roleArray = [];
            //遍历当前选中的多选框
            $(".itemBox:checked").each(function () {
                var roleName = $(this).parent().next().text();
                roleArray.push({
                    id: this.id,
                    name: roleName
                });
            });
            //检查roleArray的长度
            if (roleArray.length == 0) {
                layer.msg("请至少选择一个角色!");
                return;
            }
            showConfirmModal(roleArray);
            //在删除整页的情况下修改全选框
            $("#summaryBox").prop("checked", false);
        });

        //分配权限绑定单击响应函数
        $("#rolePageBody").on("click", ".checkBtn", function () {
            window.roleId = this.id;
            // 打开模态框
            $("#assignModal").modal("show");
            // 在模态框中装载树 Auth 的形结构数据
            fillAuthTree();
        });

        //分配按钮绑定单击响应函数
        $("#assignBtn").click(function () {
            // ①收集树形结构的各个节点中被勾选的节点
            // [1]声明一个专门的数组存放 id
            var authIdArray = [];
            // [2]获取 zTreeObj 对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // [3]获取全部被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();
            // [4]遍历 checkedNodes
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }
            // ②发送请求执行分配
            //服务器接受data若是数组，接收时得加[]
            var requestBody = {
                "authIdArray": authIdArray, // 为了服务器端 handler 方法能够统一使用 List<Integer>方式接收数据，roleId 也存入数组
                "roleId": [window.roleId]
            };
            requestBody = JSON.stringify(requestBody);
            $.ajax({
                "url": "assign/do/role/assign/auth.json",
                "type": "post",
                "data": requestBody,
                "contentType": "application/json;charset=UTF-8",
                "dataType": "json",
                "success": function (response) {
                    var result = response.result;
                    if (result == "SUCCESS") {
                        layer.msg("操作成功！");
                    }
                    if (result == "FAILED") {
                        layer.msg("操作失败！" + response.message);
                    }
                },
                "error": function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#assignModal").modal("hide");
        });
    });
</script>

<body>

<%@ include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;"><i
                            class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/modal-role-add.jsp" %>
        <%@include file="/WEB-INF/modal-role-edit.jsp" %>
        <%@include file="/WEB-INF/modal-role-confirm.jsp" %>
        <%@include file="/WEB-INF/modal-role-assign-auth.jsp" %>
</body>
</html>