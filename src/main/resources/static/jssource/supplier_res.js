$(function (){
    $(document).on('click', '.btn-add', function (e) {
        e.preventDefault();

        $("#searchProductResult").empty();


        var controlForm = $(this).parents('.controls-1:first'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).prependTo(controlForm);

        newEntry.find('input').val('');
        newEntry.find('input').attr("class", "form-control is-invalid");

        controlForm.find('.entry:not(:last) .btn-add')
            .removeClass('btn-add').addClass('btn-remove')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="glyphicon glyphicon-minus"></span>');
    }).on('click', '.btn-remove', function (e) {
        var entryToRemove = $(this).parents('.entry:first');
        entryToRemove.remove();
        e.preventDefault();
        return false;
    });

    // $('#orderForm').on('keyup keypress', function (e) {
    //     var keyCode = e.keyCode || e.which;
    //     if (keyCode === 13) {
    //         e.preventDefault();
    //         return false;
    //     }
    // });

    //
    // $('#orderForm').submit(function (e) {
    //     e.preventDefault();
    //     if (validateSubmit()) {
    //         this.submit();
    //     }
    //
    // });
});