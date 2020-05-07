<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.element.ImageRender.ImageModel" -->
<div class="image">
    <img
            <#switch _model.type>
                <#case "URL">
                    src="${_model.content}"
                    <#break >
                <#case "BASE64">
                    src="data:image/jpeg;base64,${_model.content}"
                    <#break >
            </#switch>
            <#if _model.width??>width="${_model.width}"</#if>
            <#if _model.height??>height="${_model.height}"</#if>
            <#if _model.style??>style="${_model.style}"</#if>
            <#if _model.alt??>alt="${_model.alt}"</#if>
            <#if _model.attributes??>${_model.attributes?no_esc}</#if>
    >
</div>