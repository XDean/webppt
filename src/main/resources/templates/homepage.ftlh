<div class="homepage">
    <h1 class="homepage-title">${title!"Define @title as Slide title"}</h1>
    <#if subtitle??>
        <h2 class="homepage-subtitle">${subtitle}</h2>
    </#if>
    <#if date??>
        <h2 class="homepage-date">${date}</h2>
    </#if>
    <#if author??>
        <div class="homepage-author">
            ${author}
        </div>
    </#if>
</div>