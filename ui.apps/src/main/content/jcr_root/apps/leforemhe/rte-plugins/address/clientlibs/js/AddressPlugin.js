(function ($, CUI, granite) {

    const ADD_ADDRESS_FEATURE = 'addAddr',
        GROUP = 'address',
        TCP_DIALOG = 'addressDialog',
        DIALOG_URL = '/apps/leforemhe/rte-plugins/address/cq:dialog.html';

    if(typeof _ != 'undefined') {
        addDialogTemplate();
    }
    addPluginToDefaultUISettings();

    if (!CUI.rte.dialogs) CUI.rte.dialogs = {};

    CUI.rte.dialogs.AddressDialog = new Class({
        extend: CUI.rte.ui.cui.AbstractDialog,

        toString: 'AddressDialog',

        initialize: function (config) {
            this.exec = config.execute;
        },

        getDataType: function () {
            return TCP_DIALOG;
        }
    });

    CUI.rte.plugins.AddressPlugin = new Class({

        toString: 'ForemAbbreviationPlugin',

        extend: CUI.rte.plugins.Plugin,

        dialog: null,

        testUI: null,

        selection: null,

        getFeatures: function () {
            return [ADD_ADDRESS_FEATURE]
        },

        initializeUI: function (tbGenerator) {
            const plg = CUI.rte.plugins;

            if (!this.isFeatureEnabled(ADD_ADDRESS_FEATURE)) {
                return;
            }

            this.testUI = tbGenerator.createElement(ADD_ADDRESS_FEATURE, this, false, {title: granite.I18n.get('Address')});
            tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.testUI, 10);

            const groupFeature = GROUP + '#' + ADD_ADDRESS_FEATURE;
            tbGenerator.registerIcon(groupFeature, 'email');

            handleAddressesChanges()
        },

        execute: function (cmd, value, env) {
            const context = env.editContext;
            const dm = this.editorKernel.getDialogManager();
            const selection = this.editorKernel.analyzeSelection(context);
            this.createDialog(context);
            dm.show(this.dialog);
            this.dialog.$dialog.find('button[data-type="apply"]').click(() => {
                const name = this.dialog.$dialog.find('#rte-address-name').get(0).value;
                const street = this.dialog.$dialog.find('#rte-address-street').get(0).value;
                const number = this.dialog.$dialog.find('#rte-address-number').get(0).value;
                const postal = this.dialog.$dialog.find('#rte-address-postal').get(0).value;
                const locality = this.dialog.$dialog.find('#rte-address-locality').get(0).value;
                const country = this.dialog.$dialog.find('#rte-address-country').get(0).value;
                this.editorKernel.relayCmd(ADD_ADDRESS_FEATURE, {
                    name,
                    street,
                    number,
                    postal,
                    locality,
                    country,
                    selection
                });
                this.dialog.$container.find('coral-popover').removeClass('is-open');
                this.dialog.$dialog.find('button[data-type="apply"]').off('click');
            })
        },

        createDialog: function (context) {
            if (!this.dialog) {
                const propConfig = {
                    'parameters': {
                        'command': this.pluginId + '#' + ADD_ADDRESS_FEATURE
                    }
                };
                const $container = CUI.rte.UIUtils.getUIContainer($(context.root));
                const dialog = new CUI.rte.dialogs.AddressDialog;
                dialog.attach(propConfig, $container, this.editorKernel);
                this.dialog = dialog;
            }
        }
    });

    CUI.rte.plugins.PluginRegistry.register(GROUP, CUI.rte.plugins.AddressPlugin);

    CUI.rte.commands.AddressCommand = new Class({

        toString: 'AddressCommand',

        extend: CUI.rte.commands.Command,

        isCommand: function (cmdStr) {
            return (cmdStr === ADD_ADDRESS_FEATURE);
        },

        execute: function (execDef) {
            const { name, street, number, postal, locality, country, selection } = execDef.value;
            const context = execDef.editContext;
            const addressElement = context.createElement('address');
            const nameHTML = name ? `<b>${name}</b><br/>` : '';
            const streetNumberHTML = street ? number ? `<span>${number}, ${street}</span><br/>` : `<span>${street}</span><br/>` : '';
            const localityHTML = locality ? postal ? `<span>${postal}, ${locality}</span><br/>` : `<span>${locality}</span><br/>` : '';
            const countryHTML = country ? `<span>${country}</span><br/>`: '';
            const finalInnerHTML = nameHTML + streetNumberHTML + localityHTML + countryHTML;
            addressElement.innerHTML = finalInnerHTML;
            console.log(`Final HTML to print : <address>${finalInnerHTML}</address>`);
            const dpr = CUI.rte.DomProcessor;
            const dom = selection.selectedDom ? selection.selectedDom : selection.selection.endNode ? selection.selection.endNode : selection.selection.startNode;
            const offset = selection.selectedDom ? 0 : selection.selection.endNode ? selection.selection.endOffset : selection.selection.startOffset;
            if (dom.localName === 'br') {
                dpr.insertParagraph(context, dom, 0);
                dpr.insertElement(context, addressElement, dom, 0);
                dpr.removeWithoutChildren(dom);
                dpr.removeWithoutChildren(addressElement.parentNode);
            } else {
                const paragraph = dpr.insertParagraph(context,dom, offset);
                dpr.insertParagraph(context, paragraph, 0);
                dpr.insertElement(context, addressElement, paragraph, 0);
                dpr.removeWithoutChildren(paragraph);
            }
            handleAddressChange(addressElement, finalInnerHTML);
        }
    });

    CUI.rte.commands.CommandRegistry.register(ADD_ADDRESS_FEATURE, CUI.rte.commands.AddressCommand);

    function addPluginToDefaultUISettings() {
        let toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
        toolbar.splice(3, 0, GROUP + '#' + ADD_ADDRESS_FEATURE);

        toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
        toolbar.splice(3, 0, GROUP + '#' + ADD_ADDRESS_FEATURE);
    }

    async function addDialogTemplate() {

        const html = await CUI.rte.utils.getDialogHTML(DIALOG_URL);

        if (_.isUndefined(CUI.rte.Templates)) {
            CUI.rte.Templates = {};
        }

        if (_.isUndefined(CUI.rte.templates)) {
            CUI.rte.templates = {};
        }

        CUI.rte.templates['dlg-' + TCP_DIALOG] = CUI.rte.Templates['dlg-' + TCP_DIALOG] = Handlebars.compile(html);
    }

    function handleAddressesChanges() {
        $('.cq-RichText-editable, .rte-editor').click(function () {
            $('.cq-RichText-editable address, .rte-editor address').each(function () {
                const defaultValue = $(this).html();
                handleAddressChange(this, defaultValue);
            });
        });
        const $contentFrame = $('#ContentFrame');
        const handler = function () {
            $contentFrame.contents().find('.text.cq-Editable-dom.is-edited address').each(function () {
                const defaultValue = $(this).html();
                handleAddressChange(this,defaultValue);
            });
            $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').off('click', handler);
        };
        $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').click(handler)
            .keydown(function () {
                $contentFrame.contents().find('.text.cq-Editable-dom.is-edited address').each(function () {
                    const defaultValue = $(this).html();
                    handleAddressChange(this, defaultValue);
                });
                $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').off('keydown')
            });
    }

    function handleAddressChange(addressElement, defaultValue) {
        const defaultValueSub = defaultValue.substring(0, defaultValue.lastIndexOf('</span>'));
        $(addressElement).off('DOMSubtreeModified').on('DOMSubtreeModified', function () {
            const newValue = $(addressElement).html();
            $(addressElement).off('DOMSubtreeModified');
            if(newValue.length < defaultValue.length) {
                console.log('Deleting address ...');
                if(newValue.endsWith('<br>') || newValue.endsWith('<br/>')) {
                    if (addressElement.parentElement.localName === 'p') {
                        $(addressElement).parent().remove();
                    } else {
                        $(addressElement).remove();
                    }
                } else {
                    $(addressElement).html(defaultValue);
                }
            } else {
                const newValueSub = newValue.substring(0, newValue.lastIndexOf('</span>'));
                if(newValueSub.startsWith(defaultValueSub)) {
                    const addText = newValueSub.substring(defaultValueSub.length);
                    const paragraphNode = `<p>${addText}</p>`;
                    $(addressElement).html(defaultValue);
                    $(addressElement).after(paragraphNode);
                } else {
                    $(addressElement).html(defaultValue);
                }
                handleAddressChange(addressElement, defaultValue);
            }
        });
    }

})(Granite.$, window.CUI, Granite);