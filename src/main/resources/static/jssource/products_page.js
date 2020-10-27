$(function () {

    $(document).on('show.bs.modal', '#addGroupModal', function (event) {
    });



    $(document).on('click', '#addGroupLink', function (ev) {

        var groupId = $('#inputGroupId').val();
        var dFs= {
            id : groupId
        };

        $.ajax({
            url: location.origin + "/rest/groups/add",
            dataType: 'json',
            type: 'POST',
            data: dFs
        }).done(function (d) {
            $('#addGroupModal').modal('hide');
            location.reload();
        }).fail(function () {
            $('#addGroupModal').modal('hide');
            location.reload();
        });

    });

    $(document).on('show.bs.modal', '#addProductModal', function (event) {
    });



    $(document).on('click', '#addProductLink', function (ev) {

        var productId = $('#inputProductId').val();
        var dFs= {
            id : productId
        };

        $.ajax({
            url: location.origin + "/rest/products/add",
            dataType: 'json',
            type: 'POST',
            data: dFs
        }).done(function (d) {
            $('#addProductModal').modal('hide');
            location.reload();
        }).fail(function () {
            $('#addProductModal').modal('hide');
            location.reload();
        });

    });

    $(document).on('click', '.product-search-res-item', function (e) {
        var prodId = $(this).data('prodid');
        window.location = (location.origin + "/admin/products/editProduct?id=" + prodId);
    });

    $(document).mouseup(function (e) {
        var searcResOrdList = $("#searchProductResult");
        if (searcResOrdList.has(e.target).length === 0) {
            searcResOrdList.empty();
        }
    });

    $(document).on('click', '#orderLineItem', function (e) {

        var prodEditLink = location.origin +"/products/" + $(this).data('prodid') + "/edit";
        window.open(prodEditLink, "_top");

    });


    $(document).on('keydown', '#productNameSearchInput', function (e) {

        var searchList = $(this).siblings("#searchProductsList");

        var searchField = $(this).val();

        searchField = encodeRequestReservedSymbols(searchField);

        if (e.keyCode == 13) {
            e.preventDefault();
            var activeItem = searchList.children('.active:first');
            if(activeItem.length <= 0) {

                $('#searchInputForm').submit();
                return;
            } else {
                var prodEditLink = location.origin + "/products/" + activeItem.data('prodid') + "/edit";
                window.open(prodEditLink, "_top");
                return;
            }

        }

        if (e.keyCode == 40 || e.keyCode == 38) {
            arrowActiveItemHandling(e, searchList, $(this));

        } else {
            searchList.html('');
//////Validirovat searchSend !!!!!!!!!!!!!!!!
            if (searchField.length > 1) {
                $.getJSON(location.origin + "/rest/products/?nonFullProductName=" + searchField, function (data) {
                    $.each(data, function (key, value) {
                        var prodStatus = "text-danger";
                        if(value.validationStatus === true) prodStatus="test-success";

                        searchList.append('<li class="list-group-item product-search-editor-res-item" tabindex ="' + key + '" id="orderLineItem" data-prodid = "' + value.id + '"><div class="row"' +
                            '><div class="col-4"><img src="' + value.main_image + '" height="60" width="80" class="img-thumbnail"></div>' +
                            '<div class="col-1"><span class="status ' + prodStatus +
                            '">&bull;</span></div>'+
                            '<div class="col-7"> <p id="prodNamePar" style="overflow: hidden; text-overflow: ellipsis;">' + value.name + '</p> </div>' +
                            '</div></li>');


                    });

                });
            }
        }

    });

});



