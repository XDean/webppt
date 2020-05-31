<#-- @ftlvariable name="_model" type="cn.xdean.webppt.core.render.element.layout.BoxRender.BoxModel" -->
<#assign common = _model.common/>
<div class="element-box ${_model.horizontal?then("horizontal", "vertical")}
h-${_model.align.horizontal.name()?lower_case}
v-${_model.align.vertical.name()?lower_case}
<#if common.classes??>${common.classes}</#if>"

     style="<#if common.width??>width:${common.width};</#if>
     <#if common.width??>height:${common.height};</#if>
     <#if common.style??>${common.style}</#if>"

        <#if common.attributes??>${common.attributes?no_esc}</#if>
>
    <#list _children as child>
        ${child?no_esc}
    </#list>
</div>