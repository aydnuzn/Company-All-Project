$('.PhotoCard').click(function() {
    var image = $(this).children('img').attr('src');
    $('body').append('<div class="ui basic modal"><div class="content"><img src="'+image+'" width="100%" /></div></div>');
    $('.ui.basic.modal')
        .remove()
        .modal('show');
});

function deleteModal() {
    $('.ui.basic.modal').each(function() {
        $(this).remove();
    });
}

$('.PhotoCard').click(function() {
    // Delete any modals hanging around
    deleteModal();

    var image = $(this).children('img').attr('src');
    $('body').append('<div class="ui basic modal"><div class="content"><img src="'+image+'" width="100%" /></div></div>');
    $('.ui.basic.modal')
        .modal('show');
});