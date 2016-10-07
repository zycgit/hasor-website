var testEditor;
/* 实现编辑器最小高度的功能,使用Timeout方式,实时计算 */
function echoHeight(exitFull) {
    //
    var watchHeight = 270;
    var height = testEditor.codeEditor.display.sizer.clientHeight;
    if (height < watchHeight && testEditor.settings.autoHeight) {
        $("#blogEditor").height(watchHeight + 35);
        testEditor.settings.autoHeight = false;
        testEditor.cm.setSize("100%", "100%");
    } else if (exitFull || ( height > watchHeight && !testEditor.settings.autoHeight)) {
        testEditor.settings.autoHeight = true;
        $('#blogEditor').css('height', '');
        testEditor.cm.setSize("100%", "100%");
        testEditor.resize();
    }
    window.setTimeout(function () {
        echoHeight();
    }, 100);
}

/*编辑器初始化*/
function initEditor(markdownData) {
    testEditor = editormd("blogEditor", {
        width: "100%",
        height: "100%",
        path: '//files.hasor.net/static/javascript/editor.md/1.5.0/lib/',
        markdown: markdownData,
        toolbarAutoFixed: true,         // 工具栏锁定在上方
        toolbarIcons: function () {
            return [
                "bold", "italic", "del", "|",
                "h1", "h2", "h3", "|",
                "list-ul", "list-ol", "hr", "quote", "link", "reference-link", "image", "|",
                "code", "code-block", "table", "html-entities", "||",
                "watch"//, "fullscreen"
            ]
        },
        autoHeight: true,               // 自动高
        autoCloseBrackets: false,       // 
        fontSize: "14px",               // 
        saveHTMLToTextarea: true,       // 保存 HTML 到 Textarea
        searchReplace: true,
        watch: false,                   // 关闭实时预览
        imageUpload: true,
        imageFormats: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL: ctx_path + "/uploader/upload_to_temp.do?csrfToken=" + $("#csrfToken").val(),
        placeholder: "开始你的Blog之路吧!",
        onload: function () {
            echoHeight();
        },
        onfullscreenExit: function () {
            echoHeight(true);
        },
        onfullscreen: function () {
            //echoHeight(true);
        }
    });
}

function dropdownShow(target) {
    $(target).children('.ui.dropdown').focus();
}

function checkContentType(target) {
    var val = $(target).closest('.ui.form').form('get value', 'contentType');
    if ('2' == val) {
        $('#contentType_ext_info').show();
    } else {
        $('#contentType_ext_info').hide();
    }
}

$(function () {
    initEditor("");
    $('.ui.dropdown').dropdown();
    $('.ui.checkbox').checkbox();
    $('.ui.sticky').sticky({
        context: '#attrForms'
    });
    //
});