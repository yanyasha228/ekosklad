$(function () {
    $(document).on('click', '#checkValid', function (ev) {
        var urlField = $('#urlForValidating');
        var cssQueryField = $('#cssQueryForValidating');
        var validPriceField = $('#validPrice');
        var validPriceFieldH = $('#validPriceH');



        var urlF = urlField.val();
        var cssQueryF = cssQueryField.val();

        var dFs= {
            url : urlF,
            cssQuery : cssQueryF
        };


        $.ajax({
            url: location.origin + "/rest/check",
            dataType: 'json',
            type: 'POST',
            data: dFs
        }).done(function (d) {
            if($.isEmptyObject(d)){
                validPriceField.text("NoN");
                validPriceFieldH.attr('style' , 'color: red');
                urlField.attr('class', 'form-control is-invalid');
                cssQueryField.attr('class', 'form-control is-invalid');
            }else {
                validPriceField.text(d.price);
                validPriceFieldH.attr('style' , 'color: green');
                urlField.attr('class', 'form-control is-valid');
                cssQueryField.attr('class', 'form-control is-valid');
            }
        }).fail(function () {
            validPriceField.text("NoN");
            validPriceFieldH.attr('style' , 'color: red');
            urlField.attr('class', 'form-control is-invalid');
            cssQueryField.attr('class', 'form-control is-invalid');
        });

    });
});
