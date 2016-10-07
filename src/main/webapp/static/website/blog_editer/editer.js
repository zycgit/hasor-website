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
    $('.ui.dropdown').dropdown();
    $('.ui.checkbox').checkbox();
    $('.ui.sticky').sticky({
        context: '#attrForms'
    });
    //
});