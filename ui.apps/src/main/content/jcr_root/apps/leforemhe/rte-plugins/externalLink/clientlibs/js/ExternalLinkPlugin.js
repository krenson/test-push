(function ($, CUI, granite) {

    const ADD_EXTERNALLINK_FEATURE = 'addExternalLink',
        GROUP = 'externallink',
        TCP_DIALOG = 'ExternalLinkDialog',
        DIALOG_URL = '/apps/leforemhe/rte-plugins/externalLink/cq:dialog.html';

    const NO_SELECTED_TEXT = granite.I18n.get('Sélectionner un texte dans l\'éditeur');
    const EXIST_LINK = granite.I18n.get('Impossible de faire un lien avec cette sélection');
    const INTERNAL_LINK = granite.I18n.get('Impossible de faire un lien, ce lien est un lien interne');
    const STYLEBUTTON = 'BtBleuPrusse34495E';
    const HTTP = "http://";
    const HTTPS = "https://";
    const MAILTO = "mailto:";

    if(typeof _ != 'undefined') {
        addDialogTemplate();
    }
    addPluginToDefaultUISettings();

    if (!CUI.rte.dialogs) CUI.rte.dialogs = {};

    CUI.rte.dialogs.ExternalLinkDialog = new Class({
        extend: CUI.rte.ui.cui.AbstractDialog,

        toString: 'ExternalLinkDialog',

        initialize: function (config) {
            this.exec = config.execute;
        },

        getDataType: function () {
            return TCP_DIALOG;
        }
    });

    CUI.rte.plugins.ExternalLinkPlugin = new Class({

        toString: 'ForemLinkPlugin',

        extend: CUI.rte.plugins.Plugin,

        initializeUI: function (tbGenerator) {
            const plg = CUI.rte.plugins;

            if (!this.isFeatureEnabled(ADD_EXTERNALLINK_FEATURE)) {
                return;
            }

            this.testUI = tbGenerator.createElement(ADD_EXTERNALLINK_FEATURE, this, false, {title: granite.I18n.get('Lien externe')});
            tbGenerator.addElement(GROUP, plg.Plugin.SORT_FORMAT, this.testUI, 10);

            const groupFeature = GROUP + '#' + ADD_EXTERNALLINK_FEATURE;
            tbGenerator.registerIcon(groupFeature, 'linkGlobe');

            handleExternalLinkChange();
        },

        execute: function (cmd, value, env) {
            const context = env.editContext;
            this.savedRange = context.win.getSelection().getRangeAt(0);
            var showDialog = false;
            var title = "";
            var classname = "";
            var targetname = "";
            var LinkType = "";
            var linkCheck = "";
            var mutationCount = 0;
            const selection = this.editorKernel.analyzeSelection(context);
            const dm = this.editorKernel.getDialogManager();
            const domCheck = selection.selectedDom ? selection.selectedDom : selection.selection.endNode ? selection.selection.endNode : selection.selection.startNode;

            if (this.savedRange != "" || selection.anchorCount > 0) {
                if (selection.isSelection) {
                    TextCheck = domCheck.textContent.substring(selection.selection.startOffset, selection.selection.endOffset);
                } else {
                    TextCheck = domCheck.textContent.substring(0, domCheck.textContent.length);
                }
                if (selection.anchorCount > 0) {
                    TextCheck = TextCheck.replace(/\s/g, "&nbsp;");
                    SelectCheck = selection.anchors[0].dom.innerHTML;
                    SelectCheck = TextCheck.replace(/\s/g, "&nbsp;");
                    if (SelectCheck != TextCheck) {
                        alert(EXIST_LINK);
                        showDialog = false;
                    } else {
                        // get all info from dom
                        classname = selection.anchors[0].cssClass;
                        targetname = selection.anchors[0].dom.target;
                        title = selection.anchors[0].dom.title;

                        if (selection.anchors[0].dom.attributes.href) {
                            linkCheck = selection.anchors[0].dom.attributes.href.value;
                            if (linkCheck.startsWith(HTTP, 0) || linkCheck.startsWith(HTTPS, 0) || linkCheck.startsWith(MAILTO, 0)) {

                                if (linkCheck.startsWith(HTTP, 0)) {
                                    LinkType = HTTP;
                                    linkCheck = linkCheck.replace(HTTP, "");
                                }
                                if (linkCheck.startsWith(HTTPS, 0)) {
                                    LinkType = HTTPS;
                                    linkCheck = linkCheck.replace(HTTPS, "");
                                }
                                if (linkCheck.startsWith(MAILTO, 0)) {
                                    LinkType = MAILTO;
                                    linkCheck = linkCheck.replace(MAILTO, "");
                                }
                                showDialog = true;
                            } else {
                                alert(INTERNAL_LINK);
                                showDialog = false;
                            }
                        } else {
                            alert(EXIST_LINK);
                        }
                    }
                } else {
                    showDialog = true;
                }
                if (showDialog) {
                    this.createDialog(context);
                    dm.show(this.dialog);
                    if (document.querySelector("coral-selectlist-item[value='external-link']") == null) {
                        // select the target node
                        var target = document.getElementsByClassName('coral-Well')[0];

                        // create an observer instance
                        var observer = new MutationObserver((mutations) => {
                                mutationCount = mutationCount + 1;
                                if (mutationCount == 16) {
                                    this.modifyDialogRTE(LinkType, linkCheck, title, classname, targetname);
                                    observer.disconnect();
                                }
                            }
                        );

                        // configuration of the observer:
                        var config = {attributes: false, subtree: true, childList: true, characterData: false};

                        // pass in the target node, as well as the observer options
                        observer.observe(target, config);
                    } else {
                        this.modifyDialogRTE(LinkType, linkCheck, title, classname, targetname);
                    }

                    this.dialog.$dialog.find('button[data-type="apply"]').click(() => {
                        const selectLinkType = this.dialog.$dialog.find('#rte-linkType-select').get(0);
                        const selectExternalLinkType = this.dialog.$dialog.find('#rte-externalLink-Type').get(0);
                        const externalLinkLink = this.dialog.$dialog.find('#rte-externalLink-Link').get(0).value;
                        const externalLinkTitle = this.dialog.$dialog.find('#rte-externalLink-Title').get(0).value;
                        const selectExternalLinkClass = this.dialog.$dialog.find('#rte-externalLink-Class').get(0);
                        const selectTarget = this.dialog.$dialog.find('#rte-externalLink-target').get(0);
                        const mailLinkLink = this.dialog.$dialog.find('#rte-mailLink-Link').get(0).value;
                        const selectmailLinkClass = this.dialog.$dialog.find('#rte-mailLink-Class').get(0);

                        const selectedLinkType = selectLinkType.items.getAll().filter(item => item.selected)[0];
                        const selectedExternalLinkType = selectExternalLinkType.items.getAll().filter(item => item.selected)[0];
                        const selectedExternalLinkClass = selectExternalLinkClass.items.getAll().filter(item => item.selected)[0];
                        const selectedExternalLinkTarget = selectTarget.items.getAll().filter(item => item.selected)[0];
                        const selectedmailLinkClass = selectmailLinkClass.items.getAll().filter(item => item.selected)[0];

                        this.editorKernel.relayCmd(ADD_EXTERNALLINK_FEATURE, {
                            linktype: selectedLinkType.value,
                            externalLinkType: selectedExternalLinkType.value,
                            externalLinkLink,
                            externalLinkTitle,
                            externalLinkClass: selectedExternalLinkClass.value,
                            selectTarget: selectedExternalLinkTarget.value,
                            mailLinkLink,
                            mailLinkClass: selectedmailLinkClass.value,
                            selection
                        });

                        this.dialog.$container.find('coral-popover').removeClass('is-open');
                        this.dialog.$dialog.find('button[data-type="apply"]').off('click');
                    })
                }
            } else {
                alert(NO_SELECTED_TEXT);
            }
        }
        ,

        createDialog: function (context) {
            if (!this.dialog) {
                const propConfig = {
                    'parameters': {
                        'command': this.pluginId + '#' + ADD_EXTERNALLINK_FEATURE
                    }
                };
                const $container = CUI.rte.UIUtils.getUIContainer($(context.root));
                const dialog = new CUI.rte.dialogs.ExternalLinkDialog;
                dialog.attach(propConfig, $container, this.editorKernel);
                this.dialog = dialog;
            }
        }
        ,
        modifyDialogRTE: function (LinkType, linkCheck, title, classname, targetname) {
            if (LinkType == MAILTO) {
                document.querySelector("coral-selectlist-item[value='mail-link']").click();

                var elementMailContainer = document.getElementById('rte-externalMail-container');
                elementMailContainer.style.display = "block";

                var elementLinkContainer = document.getElementById('rte-externalLink-container');
                elementLinkContainer.style.display = "none";

                var elementMailLink = document.getElementById('rte-mailLink-Link');
                elementMailLink.value = linkCheck;

                if (classname === 'link')
                    document.querySelector("coral-selectlist-item[value='link']").click();

                if (classname === 'BtBleuPrusse34495E')
                    document.querySelector("coral-selectlist-item[value='styledbutton']").click();

            } else {
                document.querySelector("coral-selectlist-item[value='external-link']").click();
                var elementMailContainer = document.getElementById('rte-externalMail-container');
                elementMailContainer.style.display = "none";

                var elementLinkContainer = document.getElementById('rte-externalLink-container');
                elementLinkContainer.style.display = "block";

                var elementLink = document.getElementById('rte-externalLink-Link');
                elementLink.value = linkCheck;

                var elementTitle = document.getElementById('rte-externalLink-Title');
                elementTitle.value = title;

                if (LinkType === 'http://')
                    document.querySelector("coral-selectlist-item[value='http://']").click();

                if (LinkType === 'https://')
                    document.querySelector("coral-selectlist-item[value='https://']").click();

                if (targetname === '_blank')
                    document.querySelector("coral-selectlist-item[value='_blank']").click();

                if (targetname === '_self')
                    document.querySelector("coral-selectlist-item[value='_self']").click();

                if (targetname === '_parent')
                    document.querySelector("coral-selectlist-item[value='_parent']").click();

                if (targetname === '_top')
                    document.querySelector("coral-selectlist-item[value='_top']").click();

                if (classname === 'linkClassExternal')
                    document.querySelector("coral-selectlist-item[value='linkClassExternal']").click();

                if (classname === 'BtBleuPrusse34495E')
                    document.querySelector("coral-selectlist-item[value='styledbuttonClassExternal']").click();

            }
        }
        ,
    });

    CUI.rte.plugins.PluginRegistry.register(GROUP, CUI.rte.plugins.ExternalLinkPlugin);

    CUI.rte.commands.ExternalLinkCommand = new Class({

        toString: 'ExternalLinkCommand',

        extend: CUI.rte.commands.Command,

        isCommand: function (cmdStr) {
            return (cmdStr === ADD_EXTERNALLINK_FEATURE);
        },

        execute: function (execDef) {
            const {
                linktype,
                externalLinkType,
                externalLinkLink,
                externalLinkTitle,
                externalLinkClass,
                selectTarget,
                mailLinkLink,
                mailLinkClass,
                selection
            } = execDef.value;
            const context = execDef.editContext;
            const dpr = CUI.rte.DomProcessor;


            if (selection.anchorCount > 0) {
                if (linktype == 'external-link') {
                    if (externalLinkTitle)
                        selection.anchors[0].dom.setAttribute("title", externalLinkTitle);
                    else
                        selection.anchors[0].dom.removeAttribute("title");

                    if (selectTarget)
                        selection.anchors[0].dom.setAttribute("target", selectTarget);
                    else
                        selection.anchors[0].dom.removeAttribute("target");

                    if (externalLinkClass == 'styledbuttonClassExternal')
                        selection.anchors[0].dom.setAttribute("class", STYLEBUTTON);
                    else
                        selection.anchors[0].dom.removeAttribute("class");

                    selection.anchors[0].dom.setAttribute("_rte_href", externalLinkType + externalLinkLink);
                    selection.anchors[0].dom.setAttribute("href", externalLinkType + externalLinkLink);
                }
                if (linktype == 'mail-link') {

                    if (mailLinkClass == 'styledbutton')
                        selection.anchors[0].dom.setAttribute("class", STYLEBUTTON);
                    else
                        selection.anchors[0].dom.removeAttribute("class");

                    selection.anchors[0].dom.setAttribute("_rte_href", MAILTO + mailLinkLink);
                    selection.anchors[0].dom.setAttribute("href", MAILTO + mailLinkLink);
                    selection.anchors[0].dom.removeAttribute("title");
                    selection.anchors[0].dom.removeAttribute("target");
                }
            } else {
                let element;
                const aElement = context.createElement('a');
                if (aElement) {
                    if (linktype == 'external-link') {
                        if (externalLinkTitle) aElement.title = externalLinkTitle;
                        if (selectTarget) aElement.target = selectTarget;

                        if (externalLinkClass == 'styledbuttonClassExternal') {
                            aElement.class = STYLEBUTTON;
                        }

                        aElement.href = externalLinkType + externalLinkLink;
                        $(aElement).attr('_rte_href', externalLinkType + externalLinkLink);
                    }
                    if (linktype == 'mail-link') {

                        if (mailLinkClass == 'styledbutton') {
                            aElement.class = STYLEBUTTON;
                        }

                        aElement.href = MAILTO + mailLinkLink;
                        $(aElement).attr('_rte_href', MAILTO + mailLinkLink);
                    }
                    const dom = selection.selectedDom ? selection.selectedDom : selection.selection.endNode ? selection.selection.endNode : selection.selection.startNode;
                    //const offset = selection.selectedDom ? 0 : selection.selection.endNode ? selection.selection.endOffset : selection.selection.startOffset;
                    aElement.innerHTML = dom.textContent.substring(selection.selection.startOffset, selection.selection.endOffset);
                    dom.deleteData(selection.selection.startOffset, selection.selection.endOffset - selection.selection.startOffset);
                    dpr.insertElement(context, aElement, dom, selection.selection.startOffset);
                }
                selection.modify('move', 'forward', 'word');

                if (dom.localName === 'br') {
                    dpr.removeWithoutChildren(dom);
                }
            }
        },
    });

    CUI.rte.commands.CommandRegistry.register(ADD_EXTERNALLINK_FEATURE, CUI.rte.commands.ExternalLinkCommand);

    function addPluginToDefaultUISettings() {
        let toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
        toolbar.splice(3, 0, GROUP + '#' + ADD_EXTERNALLINK_FEATURE);

        toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
        toolbar.splice(3, 0, GROUP + '#' + ADD_EXTERNALLINK_FEATURE);
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

    function handleExternalLinkChange() {

    }

})
(Granite.$, window.CUI, Granite);
(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
    });

    $(document).on("change", "", function (e) {
        if (document.getElementById("rte-linkType-select") != null) {
            if (document.getElementById("rte-linkType-select").value == "external-link") {
                var elementLink = document.getElementById('rte-externalLink-container');
                elementLink.style.display = "block";
                var elementMail = document.getElementById('rte-externalMail-container');
                elementMail.style.display = "none";
            } else {
                var elementLink = document.getElementById('rte-externalLink-container');
                elementLink.style.display = "none";
                var elementMail = document.getElementById('rte-externalMail-container');
                elementMail.style.display = "block";
            }
        }
    });

})(document, Granite.$);
