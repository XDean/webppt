<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.element.RootRender.RootModel" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${_model.title!"JSlide"}</title>
    <meta charset='utf-8'>
    <#list _model.context.scripts as script>
        <script src='${script}'></script>
    </#list>
    <#list _model.context.styles as style>
        <link rel="stylesheet" href="${style}">
    </#list>
</head>

<body>
<div id="root" class="root">
    <#list _children as child>
        ${child?no_esc}
    </#list>
    <#list _model.context.globalElements as re>
        ${re?no_esc}
    </#list>
</div>
</body>
</html>