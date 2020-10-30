$(function () {

    $(document).on('show.bs.modal', '#addParamModal', function (event) {
    });


    $(document).on('click', '#deleteParamButton', function (ev) {

        var paramId = $(this).data('param-id');

        var dFs= {
            id : paramId
        };

        $.ajax({
            url: location.origin + "/rest/settings/rozetka/params/delete",
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

    $(document).on('show.bs.modal', '#deleteParamModal', function (event) {

        var referrerItem = $(event.relatedTarget);
        var modal = $(this);

        var paramId = referrerItem.data('param-id');
        var paramName = referrerItem.data('param-name');


        modal.find('#deleteParamModalNameLabel').text(paramName);
        modal.find('#deleteParamButton').attr('data-param-id', paramId);


    });


    $(document).on('click', '#addParamLink', function (ev) {

        var paramName = $('#inputParamName').val();

        var dFs= {
            name : paramName
        };

        $.ajax({
            url: location.origin + "/rest/settings/rozetka/params/add",
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