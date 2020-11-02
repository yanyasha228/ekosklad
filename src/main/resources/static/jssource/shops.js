$(function () {

    $(document).on('show.bs.modal', '#addParamModal', function (event) {
    });

    $(document).on('show.bs.modal', '#editParamModal', function (event) {

        var referrerItem = $(event.relatedTarget);
        var modal = $(this);

        var paramId = referrerItem.data('param-id');
        var paramName = referrerItem.data('param-name');
        var paramKey = referrerItem.data('param-key');


        modal.find('#editInputParamName').val(paramName);
        modal.find('#editInputParamKey').val(paramKey);
        modal.find('#editParamButton').attr('data-param-id', paramId);

    });

    $(document).on('click', '#editParamButton', function (ev) {

        var paramId = $(this).data('param-id');
        var paramKey = $('#editInputParamKey').val();
        var paramName = $('#editInputParamName').val();
        var dFs= {
            id : paramId ,
            key : paramKey,
            name: paramName
        };

        $.ajax({
            url: location.origin + "/rest/keys/edit",
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


    $(document).on('click', '#deleteParamButton', function (ev) {

        var paramId = $(this).data('param-id');

        var dFs= {
            id : paramId
        };

        $.ajax({
            url: location.origin + "/rest/keys/delete",
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

        var paramKey = $('#inputParamKey').val();

        var dFs= {
            name : paramName,
            key : paramKey
        };

        $.ajax({
            url: location.origin + "/rest/keys/add",
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