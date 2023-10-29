<%--
  Created by IntelliJ IDEA.
  User: 16958
  Date: 2023/6/8
  Time: 13:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="/WEB-INF/include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        $("#toRightBtn").click(function () {
            // select 是标签选择器
            // :eq(0)表示选择页面上的第一个
            // :eq(1)表示选择页面上的第二个
            // “>”表示选择子元素
            // :selected 表示选择“被选中的”option
            // appendTo()能够将 jQuery 对象追加到指定的位置
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        });
        $("#toLeftBtn").click(function () {
            // select 是标签选择器
            // :eq(0)表示选择页面上的第一个
            // :eq(1)表示选择页面上的第二个
            // “>”表示选择子元素
            // :selected 表示选择“被选中的”option
            // appendTo()能够将 jQuery 对象追加到指定的位置
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        });
        $("#submitBtn").click(function (){
            //只有被选中的才会提交，所以提交前全选已分配的部分
            $("select:eq(1)>option").prop("selected","selected");
        });
    });
</script>
<body>
<%@ include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.html" method="post" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}"/>
                        <input type="hidden" name="pageNum" value="${param.pageNum}"/>
                        <input type="hidden" name="keyWord" value="${param.keyWord}"/>
                        <div class="form-group">
                            <label>未分配角色列表<br>
                                <select class="form-control" multiple size="10" style="width:100px;overflow-y:auto;">
                                    <c:forEach items="${requestScope.unAssigned}" var="role">
                                        <option value="${role.id}">${role.name}</option>
                                    </c:forEach>
                                </select>
                            </label>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label>已分配角色列表<br>
                                <select name="roleIdList" class="form-control" multiple size="10"
                                        style="width:100px;overflow-y:auto;">
                                    <c:forEach items="${requestScope.assigned}" var="role">
                                        <option value="${role.id}">${role.name}</option>
                                    </c:forEach>
                                </select>
                            </label>
                        </div>
                        <br>
                        <div class="form-group">
                            <button id="submitBtn" type="submit" class="btn btn-primary"
                                    style="float: right;">保存
                            </button>
                        </div>
                        <div class="form-group">
                            <button type="button" class="btn btn-primary"
                                    onclick="location.replace('admin/get/page.html?adminId=${param.adminId}&pageNum=${param.pageNum}&keyWord=${param.keyWord}')"
                                    style="float: bottom;">返回
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
