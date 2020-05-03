<page class="page">
    <#list _children as child>
        <#if child.isElement()>
            ${child.content?no_esc}
        <#else >
            <p>${child.content}</p>
        </#if>
    </#list>
</page>