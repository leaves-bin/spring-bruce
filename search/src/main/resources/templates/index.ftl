<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Search</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/dist/css/layui.css" media="all">
    <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>
<body>
<div class="layui-form">
    <form class="layui-form" action="/">
        <div class="layui-form-item"><label class="layui-form-label">关键词</label>
            <div class="layui-input-block">
                <input type="text" name="keyword" lay-verify="title" autocomplete="off" value="${keyword}" placeholder="请输入关键词" class="layui-input" style="display: inline-block;width: 80%;">
                <button type="submit" class="layui-btn layui-btn-primary">搜索</button>
            </div>
        </div>
    </form>
    <table class="layui-table">
        <colgroup>
            <col width="200">
            <col width="500">
            <col>
        </colgroup>
        <thead>
        <tr>
            <th>标题</th>
            <th>摘要</th>
            <th>内容</th>
        </tr>
        </thead>
        <tbody>
        <#if list?? && (list?size > 0)>
            <#list list as item>
                <tr>
                    <td>${item.title}(${item.score})</td>
                    <td>${item.summary}</td>
                    <td>${item.html}</td>
                </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>

<script src="/dist/layui.js" charset="utf-8"></script>
<script>

</script>

</body>
</html>