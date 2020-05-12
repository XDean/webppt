<#-- @ftlvariable name="_model" type="cn.xdean.webppt.core.render.text.PlainTextRender.PlainTextModel" -->
<#list _model.paragraphs as paragraph>
    <p>
        <#list paragraph as line>
            ${line?no_esc}<br/>
        </#list>
    </p>
</#list>