(function (window, $) {
    "use strict";
    // when dialog gets injected
    $(window).adaptTo("foundation-registry").register("foundation.validation.validator", {
        selector: "[data-leforemhe-allowedextensions]",
        validate: function (el) {
    
            const getExtensionFromPathField = (pathField) => {
                if (pathField) {
                    const configuredPath = pathField.value;
                    if (configuredPath) {
                        return configuredPath.split('.').pop();
                    }
                }
                return null;
            }
    
            var allowedExtensions = el.getAttribute("data-leforemhe-allowedextensions");
            if (!allowedExtensions || (allowedExtensions && allowedExtensions == '*')) {
                return;
            } else {
                allowedExtensions = allowedExtensions.split(',');
            }
            const inputField = el.querySelector(`input[name="${el.getAttribute("name")}"]`);
            const extension = getExtensionFromPathField(inputField);
            if (extension) {
                if (allowedExtensions.includes(extension.toLowerCase())) {
                    return;
                } else {
                    return `File should should be one of the following extensions (${allowedExtensions.join(',')})`
                }
            }
    
    
        }
    });

})(window, Granite.$);
