<#import "element.ftlh" as elem/>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>${slide.getValue("title")}</title>
    <meta charset='utf-8'>
</head>

<body>
<slide>
    <#list slide.children as child>
        ${service.render(child)?no_esc}
    </#list>
</slide>
</body>
</html>