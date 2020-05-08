<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.element.HomePageRender.HomePageModel" -->
<div class="homepage">
    <h1 class="homepage-title">${_model.title!"Define @title as Slide title"}</h1>
    <#if _model.subtitle??>
        <h2 class="homepage-subtitle">${_model.subtitle}</h2>
    </#if>
    <#if _model.date??>
        <h2 class="homepage-date">${_model.date}</h2>
    </#if>
    <#if _model.author??>
        <div class="homepage-author">
            ${_model.author}
        </div>
    </#if>
</div>