<#-- @ftlvariable name="_model" type="cn.xdean.webppt.core.render.element.CodeRender.CodeModel" -->
<#assign common = _model.common/>
<div
        id="element-code-${_model.id}"
        class="element-code<#if common.classes??> ${common.classes}</#if><#if _model.resize> resize</#if>"
        style="<#if common.width??>width:${common.width};</#if>
                height:${common.height!"300px"};
        <#if common.style??>${common.style}</#if>"
        <#if common.attributes??>${common.attributes?no_esc}</#if>
>
    Code Loading...
</div>

<script>
    function resolveCodeContent${_model.id}() {
        <#switch _model.type>
        <#case "TEXT">
        return String.raw`${_model.content?no_esc}`;
        <#break />
        <#case "URL">
        return fetch("../resource/?path=${_model.content}")
            .then(res => {
                if (res.ok) {
                    return res.text();
                } else {
                    throw res.status + ": " + res.statusText;
                }
            })
            .catch(e => {
                return "Fail to fetch source code (${_model.content}), caused by:\n" + e.toString();
            });
        <#break />
        </#switch>
    }

    async function initCode${_model.id}() {
        let content = await resolveCodeContent${_model.id}();
        let element = document.getElementById("element-code-${_model.id}");
        element.innerText = "";
        let codeMirror = CodeMirror(element, {
            value: content,
            lineNumbers: true,
            theme: '${_model.theme}',
            scrollbarStyle: "overlay",
            play: ${_model.play?string},
            <#if _model.readonly>
            readOnly: true,
            </#if>
            <#if _model.language??>
            mode: "${_model.language.mime}",
            <#else >
            mode: null,
            </#if>
        });
    }

    initCode${_model.id}();
</script>