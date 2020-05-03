<#-- @ftlvariable name="title" type="String" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${title!"JSlide"}</title>
    <meta charset='utf-8'>
    <#list _context.scripts as script>
        <script src='${script}'></script>
    </#list>
    <#list _context.styles as style>
        <link rel="stylesheet" href="${style}">
    </#list>
</head>

<body>
<div id="root" class="root">
    <#list _children as child>
        <#if child.isElement()>
            ${child.content?no_esc}
        <#elseif child.content?has_content>
            <p>${child.content}</p>
        </#if>
    </#list>
    <#list _context.globalElements as re>
        ${re?no_esc}
    </#list>
</div>
</body>
</html>