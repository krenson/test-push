(function ($, CUI, granite) {

        const ADD_ACRONYM_FEATURE = 'addAcr';
        const GROUP = 'acronym';
        const TCP_DIALOG = 'acronymDialog';
        const DIALOG_URL = '/apps/leforemhe/rte-plugins/acronym/cq:dialog.html';

        if(typeof _ != 'undefined') {
            addDialogTemplate();
        }
        addPluginToDefaultUISettings();

        if (!CUI.rte.dialogs) CUI.rte.dialogs = {};

        CUI.rte.dialogs.AcronymDialog = new Class({
            extend: CUI.rte.ui.cui.AbstractDialog,

            toString: 'AcronymDialog',

            initialize: function (config) {
                this.exec = config.execute;
            },

            getDataType: function () {
                return TCP_DIALOG;
            }
        });

        CUI.rte.plugins.AcronymPlugin = new Class({

            toString: 'ForemAcronymPlugin',

            extend: CUI.rte.plugins.Plugin,

            dialog: null,

            testUI: null,

            selection: null,

            getFeatures: function () {
                return [ADD_ACRONYM_FEATURE]
            },

            initializeUI: function (tbGenerator) {
                const plg = CUI.rte.plugins;

                if (!this.isFeatureEnabled(ADD_ACRONYM_FEATURE)) {
                    return;
                }

                this.testUI = tbGenerator.createElement(ADD_ACRONYM_FEATURE, this, false, {title: granite.I18n.get('Acronyme')});
                tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.testUI, 10);

                const groupFeature = GROUP + '#' + ADD_ACRONYM_FEATURE;
                tbGenerator.registerIcon(groupFeature, 'abc');
                initiateItems();
                handleAbbreviationChanged();
            },

            execute: function (cmd, value, env) {
                const context = env.editContext;
                const dm = this.editorKernel.getDialogManager();
                const selection = this.editorKernel.analyzeSelection(context);
                this.createDialog(context);
                dm.show(this.dialog);
                this.dialog.$dialog.find('button[data-type="apply"]').click(() => {
                    const selectText = this.dialog.$dialog.find('#rte-acronym-select-text').get(0);
                    const selectAcronym = this.dialog.$dialog.find('#rte-acronym-select-acr').get(0);
                    const url = this.dialog.$dialog.find('#rte-acronym-url').get(0).value;
                    const label = this.dialog.$dialog.find('#rte-acronym-label').get(0).value;
                    const selectedText = selectText.items.getAll().filter(item => item.selected)[0];
                    const selectedAcronym = selectAcronym.items.getAll().filter(item => item.selected)[0];
                    this.editorKernel.relayCmd(ADD_ACRONYM_FEATURE, {
                        text: selectedText.value,
                        acronym: selectedAcronym.value,
                        url,
                        label,
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
                            'command': this.pluginId + '#' + ADD_ACRONYM_FEATURE
                        }
                    };
                    const $container = CUI.rte.UIUtils.getUIContainer($(context.root));
                    const dialog = new CUI.rte.dialogs.AcronymDialog();
                    dialog.attach(propConfig, $container, this.editorKernel);
                    this.dialog = dialog;

                    fetch('/content/leforemhe/acronyms.leforemhe.json')
                        .then(resp => {
                            if (resp.status === 200) return resp.json()
                        })
                        .then(res => {
                            const items = res.sort((a, b) => (a.acronym.toLowerCase() > b.acronym.toLowerCase()) ? 1 : -1);
                            const texts = items.map(item => ({
                                value: item.text,
                                content: {
                                    textContent: item.text
                                }
                            }));
                            const acronyms = items.map(item => ({
                                value: item.acronym,
                                content: {
                                    textContent: item.acronym
                                }
                            }));

                            const selectText = this.dialog.$dialog.find('#rte-acronym-select-text').get(0);
                            const selectAcronym = this.dialog.$dialog.find('#rte-acronym-select-acr').get(0);

                            if (selectText) {
                                texts.forEach(function (value) {
                                    selectText.items.add(value);
                                });
                                selectText.on('change', event => {
                                    const selectedText = event.currentTarget.value;
                                    const acrToSelect = items.filter(item => item.text === selectedText)[0].acronym;
                                    const acrItem = selectAcronym.items.getAll().filter(item => item.value === acrToSelect)[0];
                                    acrItem.selected = true;
                                });
                            }
                            if (selectAcronym) {
                                acronyms.forEach(function (value) {
                                    selectAcronym.items.add(value);
                                });
                                selectAcronym.on('change', event => {
                                    const selectedAcr = event.currentTarget.value;
                                    const textToSelect = items.filter(item => item.acronym === selectedAcr)[0].text;
                                    const textItem = selectText.items.getAll().filter(item => item.value === textToSelect)[0];
                                    textItem.selected = true;
                                })
                            }
                        })
                }
            }

        });

        CUI.rte.plugins.PluginRegistry.register(GROUP, CUI.rte.plugins.AcronymPlugin);

        CUI.rte.commands.AcronymCommand = new Class({

            toString: 'AcronymCommand',

            extend: CUI.rte.commands.Command,

            isCommand: function (cmdStr) {
                return (cmdStr === ADD_ACRONYM_FEATURE);
            },

            execute: function (execDef) {
                const {acronym, text, url, label, selection} = execDef.value;
                const context = execDef.editContext;
                const dpr = CUI.rte.DomProcessor;
                let element;
                const acronymElement = context.createElement('acronym');
                acronymElement.innerText = acronym;
                acronymElement.title = text;
                if (url) {
                    element = context.createElement('a');
                    if (label) element.title = label;
                    element.appendChild(acronymElement);
                    element.target = '_blank';
                    element.href = url;
                    $(element).attr('_rte_href', url);
                } else {
                    element = acronymElement;
                }
                if (element) {
                    const dom = selection.selectedDom ? selection.selectedDom : selection.selection.endNode ? selection.selection.endNode : selection.selection.startNode;
                    const offset = selection.selectedDom ? 0 : selection.selection.endNode ? selection.selection.endOffset : selection.selection.startOffset;
                    dpr.insertElement(context, element, dom, offset);
                    bindAbbreviationChange(acronymElement);
                    const selectionWin = window.getSelection();
                    selectionWin.modify('move', 'forward', 'word');

                    if (dom.localName === 'br') {
                        dpr.removeWithoutChildren(dom);
                    }
                }
            }
        });

        CUI.rte.commands.CommandRegistry.register(ADD_ACRONYM_FEATURE, CUI.rte.commands.AcronymCommand);

        function addPluginToDefaultUISettings() {
            let toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
            toolbar.splice(3, 0, GROUP + '#' + ADD_ACRONYM_FEATURE);

            toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
            toolbar.splice(3, 0, GROUP + '#' + ADD_ACRONYM_FEATURE);
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

        async function initiateItems() {
            fetch('/content/leforemhe/acronyms.leforemhe.json')
                .then(resp => {
                    if (resp.status === 200) return resp.json()
                })
                .then(res => {
                    const items = res.sort((a, b) => (a.acronym.toLowerCase() > b.acronym.toLowerCase()) ? 1 : -1);
                    window.acronyms = items;
                })
        }

        function handleAbbreviationChanged() {
            $('.cq-RichText-editable, .rte-editor').click(function () {
                $('.cq-RichText-editable acronym, .rte-editor acronym').each(function () {
                    bindAbbreviationChange(this);
                });
            });
            const $contentFrame = $('#ContentFrame');
            const handler = function () {
                $contentFrame.contents().find('.text.cq-Editable-dom.is-edited acronym').each(function () {
                    bindAbbreviationChange(this);
                });
                $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').off('click', handler);
            };
            $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').click(handler)
                .keydown(function () {
                    $contentFrame.contents().find('.text.cq-Editable-dom.is-edited acronym').each(function () {
                        bindAbbreviationChange(this);
                    });
                    $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').off('keydown')
                });


        }

        function bindAbbreviationChange(acronym) {
            $(acronym).off('DOMSubtreeModified').on('DOMSubtreeModified', function (event) {
                if (window.acronyms) {
                    const newText = $(acronym).text();
                    const originalText = window.acronyms.filter(item => item.text === this.title)[0].acronym;
                    $(acronym).off('DOMSubtreeModified');
                    if (!newText.startsWith(originalText)) {
                        if (newText.length < originalText.length) {
                            if (acronym.parentElement.localName === 'a') $(acronym).parent().remove();
                            else $(acronym).remove()
                        } else {
                            $(acronym).off('DOMSubtreeModified');
                            $(acronym).text(originalText);
                        }
                    } else {
                        const addText = newText.substring(originalText.length);
                        let linkElement;
                        if (acronym.lastElementChild && acronym.lastElementChild.localName === 'a') linkElement = acronym.lastElementChild;
                        $(acronym).after(addText);
                        $(acronym).text(originalText);
                        if (linkElement) {
                            const linkHTML = `<a _rte_href="${linkElement.href}" href="${linkElement.href}" ${linkElement.title ? 'title="' + linkElement.title + '"' : ''}></a>`
                            $(acronym).wrap(linkHTML);
                        }
                        const selection = window.getSelection();
                        selection.modify('move', 'forward', 'word');
                        if (!(addText.charAt(0).toLowerCase() != addText.charAt(0).toUpperCase())) {
                            selection.modify('move', 'forward', 'character');
                        }
                    }
                    bindAbbreviationChange(acronym);
                }
            });
        }

    }

)(Granite.$, window.CUI, Granite);