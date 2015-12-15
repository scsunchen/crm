/**
 * Add typeahaead to  <i>input</i> HTML component. 
 * 
 * @param {type} displayProperty JSON property key that will be used for setting 
 * text of <i>input</i> component
 * @param {type} The URL remote data should be loaded from.
 * @param {limit} The max number of suggestions to be displayed. Defaults to 5.
 * @author Bobic Dragan 
 */
jQuery.fn.autocomplete = function (displayProperty, url, limit ) {
    limit = (typeof limit !== 'undefined') ? limit : 5;
    var element = $(this[0]);
    element.typeahead({
        hint: false,
        highlight: true
    }, {
        limit : limit,
        display: displayProperty,
        source: new Bloodhound({
            datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: url,
                wildcard: '%QUERY'
            }
        })
    });
};

