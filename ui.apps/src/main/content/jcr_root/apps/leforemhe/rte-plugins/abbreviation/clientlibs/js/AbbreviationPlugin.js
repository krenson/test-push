(function ($, CUI, granite) {

        const ADD_ABBREVIATION_FEATURE = 'addAbbr';
        const GROUP = 'abbr';
        const TCP_DIALOG = 'abbreviationDialog';
        const DIALOG_URL = '/apps/leforemhe/rte-plugins/abbreviation/cq:dialog.html';

        if(typeof _ != 'undefined') {
            addDialogTemplate();
        }
        addPluginToDefaultUISettings();

        if (!CUI.rte.dialogs) CUI.rte.dialogs = {};

        CUI.rte.dialogs.AbbreviationDialog = new Class({
            extend: CUI.rte.ui.cui.AbstractDialog,

            toString: 'AbbreviationDialog',

            initialize: function (config) {
                this.exec = config.execute;
            },

            getDataType: function () {
                return TCP_DIALOG;
            }
        });

        CUI.rte.plugins.AbbreviationPlugin = new Class({

            toString: 'ForemAbbreviationPlugin',

            extend: CUI.rte.plugins.Plugin,

            dialog: null,

            testUI: null,

            selection: null,

            getFeatures: function () {
                return [ADD_ABBREVIATION_FEATURE]
            },

            initializeUI: function (tbGenerator) {
                const plg = CUI.rte.plugins;

                if (!this.isFeatureEnabled(ADD_ABBREVIATION_FEATURE)) {
                    return;
                }

                this.testUI = tbGenerator.createElement(ADD_ABBREVIATION_FEATURE, this, false, {title: granite.I18n.get('Abbreviation')});
                tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.testUI, 10);

                const groupFeature = GROUP + '#' + ADD_ABBREVIATION_FEATURE;
                tbGenerator.registerIcon(groupFeature, 'feedAdd');
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
                    const selectWords = this.dialog.$dialog.find('#rte-abbreviation-select-word').get(0);
                    const selectAbbreviations = this.dialog.$dialog.find('#rte-abbreviation-select-abbr').get(0);
                    const selectedWord = selectWords.items.getAll().filter(item => item.selected)[0];
                    const selectedAbbreviation = selectAbbreviations.items.getAll().filter(item => item.selected)[0];
                    this.editorKernel.relayCmd(ADD_ABBREVIATION_FEATURE, {
                        word: selectedWord.value,
                        abbr: selectedAbbreviation.value,
                        selection,
                    });
                    this.dialog.$container.find('coral-popover').removeClass('is-open');
                    this.dialog.$dialog.find('button[data-type="apply"]').off('click');
                })
            },

            createDialog: function (context) {
                if (!this.dialog) {
                    const propConfig = {
                        'parameters': {
                            'command': this.pluginId + '#' + ADD_ABBREVIATION_FEATURE
                        }
                    };
                    const $container = CUI.rte.UIUtils.getUIContainer($(context.root));
                    const dialog = new CUI.rte.dialogs.AbbreviationDialog();
                    dialog.attach(propConfig, $container, this.editorKernel);
                    this.dialog = dialog;

                    fetch('/content/leforemhe/abbreviations.leforemhe.json')
                        .then(resp => {
                            if (resp.status === 200) return resp.json()
                        })
                        .then(res => {
                            const language = granite.author.pageInfo.language.substring(0, granite.author.pageInfo.language.indexOf('_')) || 'fr';
                            const items = res[language].sort((a, b) => (a.word.toLowerCase() > b.word.toLowerCase()) ? 1 : -1);
                            const words = items.map(item => ({
                                value: item.word,
                                content: {
                                    textContent: item.word
                                }
                            }));
                            const abbreviations = items.map(item => ({
                                value: item.abbreviation,
                                content: {
                                    textContent: item.abbreviation
                                }
                            }));

                            const selectWords = this.dialog.$dialog.find('#rte-abbreviation-select-word').get(0);
                            const selectAbbreviations = this.dialog.$dialog.find('#rte-abbreviation-select-abbr').get(0);

                            if (selectWords) {
                                words.forEach(function (value) {
                                    selectWords.items.add(value);
                                });
                                selectWords.on('change', event => {
                                    const selectedWord = event.currentTarget.value;
                                    const abbrToSelect = items.filter(item => item.word === selectedWord)[0].abbreviation;
                                    const abbrItem = selectAbbreviations.items.getAll().filter(item => item.value === abbrToSelect)[0];
                                    abbrItem.selected = true;
                                });
                            }
                            if (selectAbbreviations) {
                                abbreviations.forEach(function (value) {
                                    selectAbbreviations.items.add(value);
                                });
                                selectAbbreviations.on('change', event => {
                                    const selectedAbbr = event.currentTarget.value;
                                    const wordToSelect = items.filter(item => item.abbreviation === selectedAbbr)[0].word;
                                    const wordItem = selectWords.items.getAll().filter(item => item.value === wordToSelect)[0];
                                    wordItem.selected = true;
                                })
                            }
                        })
                }
            }

        });

        CUI.rte.plugins.PluginRegistry.register(GROUP, CUI.rte.plugins.AbbreviationPlugin);

        CUI.rte.commands.AbbreviationCommand = new Class({

            toString: 'AbbreviationCommand',

            extend: CUI.rte.commands.Command,

            isCommand: function (cmdStr) {
                return (cmdStr === ADD_ABBREVIATION_FEATURE);
            },

            execute: function (execDef) {
                const {abbr, word, selection} = execDef.value;
                const context = execDef.editContext;
                const abbrElement = context.createElement('abbr');
                const dpr = CUI.rte.DomProcessor;

                abbrElement.innerText = abbr;
                abbrElement.title = word;
                const dom = selection.selectedDom ? selection.selectedDom : selection.selection.endNode ? selection.selection.endNode : selection.selection.startNode;
                const offset = selection.selectedDom ? 0 : selection.selection.endNode ? selection.selection.endOffset : selection.selection.startOffset;
                dpr.insertElement(context, abbrElement, dom, offset);
                bindAbbreviationChange(abbrElement);
                const selectionWin = window.getSelection();
                selectionWin.modify("move", "forward", "word");

                if (dom.localName === 'br') {
                    dpr.removeWithoutChildren(dom);
                }
            }
        });

        CUI.rte.commands.CommandRegistry.register(ADD_ABBREVIATION_FEATURE, CUI.rte.commands.AbbreviationCommand);

        function addPluginToDefaultUISettings() {
            let toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
            toolbar.splice(3, 0, GROUP + '#' + ADD_ABBREVIATION_FEATURE);

            toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
            toolbar.splice(3, 0, GROUP + '#' + ADD_ABBREVIATION_FEATURE);
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
            fetch('/content/leforemhe/abbreviations.leforemhe.json')
                .then(resp => {
                    if (resp.status === 200) return resp.json()
                })
                .then(res => {
                    const language = granite.author.pageInfo.language.substring(0, granite.author.pageInfo.language.indexOf('_')) || 'fr';
                    const items = res[language].sort((a, b) => (a.word.toLowerCase() > b.word.toLowerCase()) ? 1 : -1);
                    window.abbreviations = items;
                })
        }

        function handleAbbreviationChanged() {
            $('.cq-RichText-editable, .rte-editor').click(function () {
                $('.cq-RichText-editable abbr, .rte-editor abbr').each(function () {
                    bindAbbreviationChange(this);
                });
            });
            const $contentFrame = $('#ContentFrame');
            const handler = function () {
                $contentFrame.contents().find('.text.cq-Editable-dom.is-edited abbr').each(function () {
                    bindAbbreviationChange(this);
                });
                $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').off('click', handler);
            };
            $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').click(handler)
                .keydown(function () {
                    $contentFrame.contents().find('.text.cq-Editable-dom.is-edited abbr').each(function () {
                        bindAbbreviationChange(this);
                    });
                    $contentFrame.contents().find('.text.cq-Editable-dom.is-edited').off('keydown')
                });


        }

        function bindAbbreviationChange(abbreviation) {
            $(abbreviation).off('DOMSubtreeModified').on('DOMSubtreeModified', function () {
                if (window.abbreviations) {
                    const newText = $(abbreviation).text();
                    const originalText = window.abbreviations.filter(item => item.word === this.title)[0].abbreviation;
                    $(abbreviation).off('DOMSubtreeModified');
                    if (!newText.startsWith(originalText)) {
                        if (newText.length < originalText.length) {
                            $(abbreviation).remove();
                        } else {
                            $(abbreviation).off('DOMSubtreeModified');
                            $(abbreviation).text(originalText);
                        }
                    } else {
                        const addText = newText.substring(originalText.length);
                        $(abbreviation).after(addText);
                        $(abbreviation).text(originalText);
                        const selection = window.getSelection();
                        selection.modify("move", "forward", "word");
                        if (!(addText.charAt(0).toLowerCase() != addText.charAt(0).toUpperCase())) {
                            selection.modify("move", "forward", "character");
                        }
                    }
                    bindAbbreviationChange(abbreviation);
                }
            });
        }

    }

)(Granite.$, window.CUI, Granite);