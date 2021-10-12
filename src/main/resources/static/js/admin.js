$(".openbtn").on("click", function () {
    $(".ui.sidebar").toggleClass("very thin icon");
    $(".asd").toggleClass("marginlefting");
    $(".asd").toggleClass("margincontent");
    $(".sidebar z").toggleClass("displaynone");
    $(".ui.accordion").toggleClass("displaynone");
    $(".ui.dropdown.item").toggleClass("displayblock");
    $(".logo").find('img').toggle();
});

$(".ui.dropdown").dropdown({
    allowCategorySelection: true,
    transition: "fade up",
    context: 'sidebar',
    on: "hover"
});

$('.ui.accordion').accordion({
    selector: {}
});

// Close Error Message for validation - general method
$('.message .close').on('click', function() {
    $(this)
        .closest('.message')
        .transition('fade');
});