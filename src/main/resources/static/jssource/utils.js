String.prototype.replaceAll = function (search, replace) {
    return this.split(search).join(replace);
};

function numValidDouble(input) {
    $(input).keydown(function (event) {
        // Разрешаем: backspace, delete, tab, escape , "."
        if (event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 ||
            event.keyCode == 110 || event.keyCode == 190 ||
            // Разрешаем: Ctrl+A
            (event.keyCode == 65 && event.ctrlKey === true) ||
            // Разрешаем: home, end, влево, вправо
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            // Ничего не делаем

            return;
        } else {
            // Обеждаемся, что это цифра, и останавливаем событие keypress
            if ((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105)) {
                event.preventDefault();
            }
        }
    });

};

function numValid(input) {
    $(input).keydown(function (event) {
        // Разрешаем: backspace, delete, tab, escape ,
        if (event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 27 ||
            // Разрешаем: Ctrl+A
            (event.keyCode == 65 && event.ctrlKey === true) ||
            // Разрешаем: home, end, влево, вправо
            (event.keyCode >= 35 && event.keyCode <= 39)) {
            // Ничего не делаем
            return;
        } else {
            // Обеждаемся, что это цифра, и останавливаем событие keypress
            if ((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105)) {
                event.preventDefault();
            }
        }
    });
};

function encodeRequestReservedSymbols(strToValidation) {
    return strToValidation.replaceAll("%", "%25")
        .replaceAll("+", "%2B")
        .replaceAll("&", "%26")
        .trim();
}

function arrowActiveItemHandling(e, htmlItemList, searchInputField) {
    var searchListLiElFirst = htmlItemList.children('.list-group-item:first');
    var searchListLiElLast = htmlItemList.children('.list-group-item:last');
    var searchListLiElActive = htmlItemList.children('.active:first');
    var activeLiIsFirst = searchListLiElFirst.hasClass("active");
    var activeLiIsLast = searchListLiElLast.hasClass("active");

    if (searchListLiElActive.length !== 0) {

        if (e.keyCode == 38 && activeLiIsFirst ||
            e.keyCode == 40 && activeLiIsLast) {

            if (e.keyCode == 38 && activeLiIsFirst) {
                searchListLiElFirst.removeClass("active");
                searchListLiElLast.addClass("active");
                searchListLiElLast.focus();
                searchInputField.focus();
            }

            if (e.keyCode == 40 && activeLiIsLast) {
                searchListLiElLast.removeClass("active");
                searchListLiElFirst.addClass("active");
                searchListLiElFirst.focus();
                searchInputField.focus();
            }

        } else {

            if (e.keyCode == 40) {
                var nextActiveLi = searchListLiElActive.next();
                nextActiveLi.addClass("active");
                nextActiveLi.focus();
                searchInputField.focus();
                searchListLiElActive.removeClass("active");

            }

            if (e.keyCode == 38) {
                var prevActiveLi = searchListLiElActive.prev();
                prevActiveLi.addClass("active");
                prevActiveLi.focus();
                searchInputField.focus();
                searchListLiElActive.removeClass("active");

            }

        }

    } else {
        searchListLiElFirst.addClass("active");
    }

}


//KeyupEvent that provide dropdown search list for products
//input must have ul tag with id-attr = #searchProductResult
$(document).on('keyup', '.product-dropdown-search-input', function (e) {
    var searchList = $(this).siblings("#searchProductResult");
    searchList.html('');
    var searchField = $(this).val();

    if (searchField.length > 1) {


        $.getJSON(location.origin + "/rest/products/?nonFullProductName=" + searchField, function (data) {
            $.each(data, function (key, value) {
                searchList.append('<li class="list-group-item product-search-editor-res-item" tabindex ="' + key + '" id="orderLineItem" data-prodid = "' + value.id + '"><div class="row"' +
                    '><div class="col-4"><img src="' + value.main_image + '" height="60" width="80" class="img-thumbnail"></div>' +
                    '<div class="col-8"> <p id="prodNamePar" style="overflow: hidden; text-overflow: ellipsis;">' + value.name + '</p> </div>' +
                    '</div></li>');


            });

        });
    }

});
