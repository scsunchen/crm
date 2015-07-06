/**
 * Created by NikolaB on 6/7/2015.
 */
$(document).ready(function () {
        $('[data-toggle="offcanvas"]').click(function () {
            $('.row-offcanvas-left').toggleClass('active')
        });
    },
    function MM_showHideLayers() {
        $('#divId').dialog({modal: true});
    });

$('#itemDesc').typeahead({
    hint: false,
    highlight: true,
    minLength: 1,
    limit: 1000
}, {
    display: 'description',
    source: new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '${pageContext.request.contextPath}/device/read-item/%QUERY',
            wildcard: '%QUERY'
        }
    })
});
$('#itemDesc').bind('typeahead:selected', function (obj, datum, name) {
    $('#itemDescHidden').val(datum['code']);
});
