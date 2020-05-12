<#-- @ftlvariable name="_model" type="cn.xdean.webppt.core.render.element.PageRender.PageModel" -->
<div class="page">
    <div class="page-content">
        <#list _children as child>
            ${child?no_esc}
        </#list>
    </div>
    <div class="page-footer">
        ${_model.page + 1} / ${_model.pageCount}
    </div>
</div>