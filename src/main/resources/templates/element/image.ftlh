<#-- @ftlvariable name="_model" type="cn.xdean.webppt.core.render.element.ImageRender.ImageModel" -->
<#assign common = _model.common/>
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
            <#if common.width??>width="${common.width}"</#if>
            <#if common.height??>height="${common.height}"</#if>
            <#if common.style??>style="${common.style}"</#if>
            <#if common.classes??>class="${common.classes}"</#if>
            <#if common.attributes??>${common.attributes?no_esc}</#if>
    >
</div>