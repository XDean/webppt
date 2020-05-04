<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.text.PlainTextRender.PlainTextModel" -->
<#list _model.paragraphs as paragraph>
    <p>
        <#list paragraph as line>
            ${line}<br/>
        </#list>
    </p>
</#list>