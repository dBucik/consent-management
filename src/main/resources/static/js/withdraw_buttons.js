$(document).ready(function () {
    $('.btn-delete-consent').on('click', function () {
        const consentId = $(this).attr('data-consent-id');
        $.ajax({
            //TODO: dynamic URL based on deployment
            url: "http://localhost:8080/api/consent/" + consentId,
            type: 'DELETE',
            success: function (response) {
                if (!response) {
                    $('#consent-' + consentId).fadeOut(300, function () {
                        $(this).remove();
                    });
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                //TODO: handle error
                const status = xhr.status;
                console.log(xhr);
                console.log(ajaxOptions);
                console.log(thrownError);
            }
        });
    });
});