<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.element.ImageRender.ImageModel" -->
<div class="image">
    <#switch _model.type>
        <#case "URL">
            <img
                    src="${_model.content}"
                    <#if _model.width??>width="${_model.width}"</#if>
                    <#if _model.height??>height="${_model.height}"</#if>
                    <#if _model.style??>style="${_model.style}"</#if>
                    <#if _model.alt??>alt="${_model.alt}"</#if>
                    <#if _model.attributes??>${_model.attributes?no_esc}</#if>
            >
            <#break >
        <#case "BASE64">
            <#break >
    </#switch>
</div>