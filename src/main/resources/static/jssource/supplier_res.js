$(function () {

    String.prototype.replaceAll = function (search, replace) {
        return this.split(search).join(replace);
    };

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

    $('#orderForm').on('keyup keypress', function (e) {
        var keyCode = e.keyCode || e.which;
        if (keyCode === 13) {
            e.preventDefault();
            return false;
        }
    });


    $('#orderForm').submit(function (e) {
        e.preventDefault();
        if (validateSubmit()) {
            this.submit();
        }

    });

    function validateSubmit() {

        var formIsValid = true;

        var entryDomItems = $('#productLines').find('.entry');

        var entryDomDepItems = $('#depProductLines').find('.entry');

        entryDomItems.each(function (i, item) {
            if ($(item).find('#productLineProductName:first').hasClass("is-invalid") ||
                $(item).find('#productLineProductQua:first').hasClass("is-invalid")) {

                formIsValid = false;

            }

        });


        entryDomDepItems.each(function (i, item) {
            if ($(item).find('#productLineProductName:first').hasClass("is-invalid") ||
                $(item).find('#productLineProductQua:first').hasClass("is-invalid")) {

                formIsValid = false;

            }

        });

        return formIsValid;

    }

    $(document).on('focusout', '#inputContainsString', function (e) {

        var searchField = $(this).val();
        var input = $(this);

        if (searchField.replaceAll(" ", "") !== "") {
            input.attr("class", "form-control is-valid");
        } else input.attr("class", "form-control is-invalid");
    });

    $(document).on('change', '#selectQueryType', function (e) {
        var queryType = $(this).val();
        var queryInput = $(this).closest("div.form-row").find("input[id='xPathInput']");

        if (queryType === "X_PATH") {
            if (!queryInput.val().endsWith("/text()")) queryInput.val(queryInput.val() + "/text()");
        } else if (queryType === "CSS_QUERY") {
            if (queryInput.val().endsWith("/text()")) queryInput.val(queryInput.val().replaceAll("/text()", ""));
        }

    });

    $(document).on('focusout', '#xPathInput', function (e) {

        var searchField = $(this).val();
        var input = $(this);
        var type = $(this).closest("div.form-row").find("select[id='selectQueryType']").val();
        if (type === "X_PATH") {
            if (!searchField.endsWith("/text()")) input.val(searchField + "/text()");
        }

        if (searchField.replaceAll(" ", "") !== "") {
            input.attr("class", "form-control is-valid");
        } else input.attr("class", "form-control is-invalid");
    });


});