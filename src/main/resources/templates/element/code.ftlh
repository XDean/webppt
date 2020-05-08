<#-- @ftlvariable name="_model" type="cn.xdean.jslide.core.render.element.CodeRender.CodeModel" -->
<div class="element-code" id="element-code-${_model.id}">

</div>

<script>
    let content = String.raw`${_model.content?no_esc}`;
    let element = document.getElementById("element-code-${_model.id}");
    CodeMirror(element, {
        value: content,
        lineNumbers: true,
        activeLines: true,
        theme: 'idea',
    });
</script>