(function ($, CUI) {

    if (!CUI.rte.utils) CUI.rte.utils = {};

    CUI.rte.utils.getDialogHTML = async (dialogURL) => {
        const dialog = await fetch(dialogURL)
            .then(resp => resp.text())
            .then(res => res);
        const $dialog = $($.parseHTML(dialog));
        const $dialogContent = $($dialog.find('coral-dialog-content')[0]);
        const dialogContent = $dialogContent.html();
        const validationButton = '<div class=" rte-dialog-column rte-dialog-column--rightAligned">\n' +
            `        <button is="coral-button" class="coral3-Button coral3-Button--secondary" size="M" variant="secondary" icon="close" title="${Granite.I18n.get('Annuler')}" aria-label="${Granite.I18n.get('Annuler')}" iconsize="S" type="button" data-type="cancel" tabindex="0"><coral-icon class="coral3-Icon coral3-Icon--sizeS coral3-Icon--close" icon="close" size="S" autoarialabel="on" role="img" aria-label="close"></coral-icon><coral-button-label></coral-button-label></button>\n` +
            `        <button is="coral-button" class="coral3-Button coral3-Button--primary" size="M" variant="primary" icon="check" title="${Granite.I18n.get('Appliquer')}" aria-label="${Granite.I18n.get('Appliquer')}" iconsize="S" type="button" data-type="apply" tabindex="0"><coral-icon class="coral3-Icon coral3-Icon--sizeS coral3-Icon--check" icon="check" size="S" autoarialabel="on" role="img" aria-label="check"></coral-icon><coral-button-label></coral-button-label></button>\n` +
            '    </div>';

        const dialogFinal = '<div class=" rte-dialog-column">\n' + dialogContent + '</div>';

        return '<div class="rte-forem-dialog">' + dialogFinal + '\n' + validationButton + '</div>';
    }

})(jQuery, window.CUI);