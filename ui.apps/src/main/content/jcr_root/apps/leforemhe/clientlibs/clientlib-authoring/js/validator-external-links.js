(function ($window) {
    $window.adaptTo("foundation-registry").register("foundation.validation.validator", {
        selector: "[data-should-start-with-http-or-empty]",
        validate: function (el) {
            if (el.getAttribute("data-should-start-with-http-or-empty") === 'true') {
                if (el.value.length > 0 && !el.value.startsWith('http://') && !el.value.startsWith('https://')) {
                    return "Cet url doit obligatoirement commencer par 'https://' ou 'http://'.";
                }
            }
        }
    });
})($(window));
