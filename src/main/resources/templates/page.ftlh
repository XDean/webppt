<#-- @ftlvariable name="_model" type="cn.xdean.jslide.service.render.provider.PageRender.PageModel" -->
<div class="page">
    <div class="page-content">
        <#list _children as child>
            <#if child.isElement()>
                ${child.content?no_esc}
            <#else >
                <p>${child.content}</p>
            </#if>
        </#list>
    </div>
    <div class="page-footer">
        ${_model.page + 1} / ${_model.pageCount}
    </div>
</div>