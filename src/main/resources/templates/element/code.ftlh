<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.element.CodeRender.CodeModel" -->
<#assign common = _model.common/>
<div
        id="element-code-${_model.id}"
        class="element-code<#if common.classes??> ${common.classes}</#if>"
        style="<#if common.width??>width:${common.width};</#if>
        <#if common.height??>height:${common.height};</#if>
        <#if common.style??>${common.style}</#if>"
        <#if common.attributes??>${common.attributes?no_esc}</#if>
>

</div>

<script>
    let content = String.raw`${_model.content?no_esc}`;
    let element = document.getElementById("element-code-${_model.id}");
    let codeMirror = CodeMirror(element, {
        value: content,
        lineNumbers: true,
        activeLines: true,
        theme: '${_model.theme}',
        scrollbarStyle: "overlay"
    });
</script>