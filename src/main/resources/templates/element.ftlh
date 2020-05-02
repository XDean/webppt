<#macro page elem>
    <page>
        ${elem.name}
<#--        <#list elem.children as child>-->
<#--            <@.vars["${child.name}"] elem=child/>-->
<#--        </#list>-->
    </page>
</#macro>