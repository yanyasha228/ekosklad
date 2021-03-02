$(function () {

    $(document).on('show.bs.modal', '#addResourceModal', function (event) {
    });

    $(document).on('click', '#deleteResourceButton', function (ev) {

        var resId = $(this).data('res-id');

        var dFs= {
            id : resId
        };

        $.ajax({
            url: location.origin + "/rest/supp_res/delete",
            dataType: 'json',
            type: 'DELETE',
            data: dFs
        }).done(function (d) {
            $('#addUserModal').modal('hide');
            location.reload();
        }).fail(function () {
            $('#addUserModal').modal('hide');
            location.reload();
        });

    });

    $(document).on('show.bs.modal', '#deleteResourceModal', function (event) {

        var referrerItem = $(event.relatedTarget);
        var modal = $(this);

        var resId = referrerItem.data('res-id');
        var resName = referrerItem.data('res-name');


        modal.find('#deleteResourceModalNameLabel').text(resName);
        modal.find('#deleteResourceButton').attr('data-res-id', resId);


    });


    $(document).on('click', '#addResourceLink', function (ev) {

        var resourceName = $('#inputResourceName').val();

        var resourceLink = $('#inputResourceLink').val();

        var dFs= {
            name : resourceName,
            link : resourceLink
        };

        $.ajax({
            url: location.origin + "/rest/supp_res/add",
            dataType: 'json',
            type: 'POST',
            data: dFs
        }).done(function (d) {
            $('#addUserModal').modal('hide');
            location.reload();
        }).fail(function () {
            $('#addUserModal').modal('hide');
            location.reload();
        });

    });

});