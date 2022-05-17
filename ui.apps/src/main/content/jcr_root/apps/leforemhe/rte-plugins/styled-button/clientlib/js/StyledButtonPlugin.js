(function($, CUI) {
    const GROUP = 'leforemhe-extensions-links',
        BUTTON_FEATURE = 'button',
        TCP_DIALOG = 'eaemTouchUIStyledButtonDialog';
    const NO_SELECTED_TEXT = 'Sélectionner un texte dans l\'éditeur';
    const EXIST_LINK = 'Impossible de faire un lien avec cette sélection';
    const INTERNAL_LINK = 'Impossible de faire un lien, ce lien est un lien externe';
    const HTTP = "http://";
    const HTTPS = "https://";
    const MAILTO = "mailto:";

    addPluginToDefaultUISettings();

    CUI.rte.plugins.ForemLinkPlugin = new Class({

        toString: 'ForemLinkPlugin',

        extend: CUI.rte.plugins.LinkPlugin,

        LINKABLE_OBJECTS: [
            'img'
        ],

        contextCopy: null,

        execute: function(cmd, value, env) {
            const context = env.editContext;
            var showDialog = false;
            this.savedRange = context.win.getSelection().getRangeAt(0);

            const dm = this.editorKernel.getDialogManager();
            const selection = this.editorKernel.analyzeSelection(context);
            const domCheck = selection.selectedDom ? selection.selectedDom : selection.selection.endNode ? selection.selection.endNode : selection.selection.startNode;

            if (selection.isSelection){
                TextCheck = domCheck.textContent.substring(selection.selection.startOffset, selection.selection.endOffset);
            } else {
                TextCheck = domCheck.textContent.substring(0, domCheck.textContent.length);
            }

            if (selection.anchorCount > 0) {
                TextCheck = TextCheck.replace(/\s/g, "&nbsp;");
                SelectCheck = selection.anchors[0].dom.innerHTML;
                SelectCheck = SelectCheck.replace(/\s/g, "&nbsp;");

                // Needed for Styled Asset Button
                SelectCheck = SelectCheck.replace("<span>", "");
                SelectCheck = SelectCheck.replace("</span>", "");

                if (SelectCheck != TextCheck) {
                    alert(EXIST_LINK);
                    showDialog = false;
                } else {
                    if (selection.anchors[0].dom.attributes.href) {
                        linkCheck = selection.anchors[0].dom.attributes.href.value;
                        if (linkCheck.startsWith(HTTP, 0) || linkCheck.startsWith(HTTPS, 0) || linkCheck.startsWith(MAILTO, 0)) {

                            if (cmd === 'unlink') {
                                showDialog = true;
                            } else {
                                alert(INTERNAL_LINK);
                                showDialog = false;
                            }

                        } else {
                            showDialog = true;
                        }
                    }
                }
            } else {
                showDialog = true;
            }
            if (showDialog) {
                if (cmd === 'modifylink') {
                    this.modifyLink(env.editContext);
                } else if (cmd === 'anchor') {
                    this.modifyAnchor(env.editContext);
                } else {
                    this.editorKernel.relayCmd('unlinkForem');
                }
            }
        },

        initializeUI: function(tbGenerator) {
            var plg = CUI.rte.plugins;
            var ui = CUI.rte.ui;

            if (this.isFeatureEnabled('modifylink')) {
                this.linkUI = tbGenerator.createElement('modifylink', this, false, { title: 'Lien interne' });
                tbGenerator.addElement(GROUP, plg.Plugin.SORT_LINKS, this.linkUI, 10);
                tbGenerator.registerIcon(GROUP + '#modifylink', 'linkPage')
            }
            if (this.isFeatureEnabled('unlink')) {
                this.removeLinkUI = tbGenerator.createElement('unlink', this, false, { title: 'Supprimer un lien' });
                tbGenerator.addElement(GROUP, plg.Plugin.SORT_LINKS, this.removeLinkUI, 20);
                tbGenerator.registerIcon(GROUP + '#unlink', 'linkOff')
            }
            if (this.isFeatureEnabled('anchor')) {
                this.anchorUI = tbGenerator.createElement('anchor', this, true,
                    this.getTooltip('anchor'));
                tbGenerator.addElement(GROUP, plg.Plugin.SORT_LINKS, this.anchorUI, 30);
                tbGenerator.registerIcon(GROUP + '#anchor', 'anchor')
            }
        },

        modifyLink: function(context) {
            CUI.rte.plugins.LinkPlugin.prototype.modifyLink.call(this, context);
            this.savedRange = context.win.getSelection().getRangeAt(0);
            var linkToEdit = null;
            var selectionDef = this.editorKernel.analyzeSelection();
            if (selectionDef.anchorCount === 1) {
                linkToEdit = selectionDef.anchors[0];
            }
            let cssClass = undefined;
            let href = undefined;


            if (linkToEdit) {
                cssClass = linkToEdit.cssClass || '';

                $.ajax({
                    url: "/bin/fullPath",
                    type: 'GET',
                    data: {
                        'vanityPath': linkToEdit.href
                    },
                    success: (result) => {
                        href = result.fullPath;

                        this.addPathPickerButton(href);
                        this.addStyleSelect(cssClass);
                        this.applyEvents(context);
                    },
                    error: (request, error) => {
                        console.log("Request: " + JSON.stringify(request) + "\n" + "Error: " + JSON.stringify(error));

                        href = linkToEdit.href;
                        this.addPathPickerButton(href);
                        this.addStyleSelect(cssClass);
                        this.applyEvents(context);
                    }
                });

            } else {
                href = '';
                this.addPathPickerButton(href);
                this.addStyleSelect(cssClass);
                this.applyEvents(context);
            }
        },

        applyLink: function(context) {
            var dm = this.editorKernel.getDialogManager();
            if (dm.isShown(this.linkDialog)) {
                var com = CUI.rte.Common;
                this.linkDialog.objToEdit.selectType = this.linkDialog.$dialog.find('.rte-dialog-style-select input').val();
                this.linkDialog.objToEdit.href = this.linkDialog.$dialog.find('input[name="href"]').val();
                this.linkDialog.objToEdit.attributes.title = this.linkDialog.$dialog.find('input[data-type="title"]').val();
                this.linkDialog.objToEdit.target = this.linkDialog.$dialog.find('coral-select[handle="targetSelect"] input').val();
                var linkObj = this.linkDialog.objToEdit;
                if (linkObj) {
                    let linkUrl = linkObj.href;
                    //adding .html if selected link is a page
                    if (this.isHtmlExtensionNeeded(linkUrl)) linkUrl += '.html';
                    const target = linkObj.target;
                    let cssClass = linkObj.selectType === 'phone' ? 'numTelBleu' : linkObj.selectType === 'button' ? 'BtBleuPrusse34495E' : linkObj.selectType === 'assetButton' ? 'BtBleuPrusse34495E linkAssetButton' : linkObj.selectType === 'asset' ? 'linkAsset' : '';
                    linkObj.attributes.class = cssClass;
                    context.win.getSelection().removeRange(context.win.getSelection().getRangeAt(0));
                    context.win.getSelection().addRange(this.savedRange);

                    // Get vanity path of linkUrl in case of internal links
                    if (linkUrl.startsWith("/content/leforemhe/fr") || linkUrl.startsWith("/content/dam/")) {
                        var isDamAsset = false;
                        var endpoint = "";
                        if (linkUrl.endsWith(".html")) {
                            linkUrl = linkUrl.substring(0, linkUrl.length - 5);
                        }
                        if (linkUrl.startsWith("/content/dam/leforemhe")) {
                            isDamAsset = true;
                            endpoint = "/content/leforemhe/vanityPath";
                        } else {
                            endpoint = linkUrl;
                        }
                        var url = endpoint + (!isDamAsset ? "/jcr:content.leforemhe.json" : ".leforemhe.json");
                        $.ajax({
                            url: url,
                            type: 'GET',
                            data: {
                                'fullPath': linkUrl,
                                'type': isDamAsset ? 'asset' : 'page'
                            },
                            success: (result) => {
                                let vanityUrl = result.vanityPath;
                                if (this.isHtmlExtensionNeeded(vanityUrl)) vanityUrl += '.html';

                                this.editorKernel.relayCmd('modifylinkForem', {
                                    'url': vanityUrl,
                                    'target': target,
                                    'css': cssClass,
                                    'selectType': linkObj.selectType,
                                    'trimLinkSelection': this.config.trimLinkSelection,
                                    'attributes': linkObj.attributes
                                });
                            },
                            error: (request, error) => {
                                console.log("Request: " + JSON.stringify(request) + "\n" + "Error: " + JSON.stringify(error));

                                if (this.isHtmlExtensionNeeded(linkUrl)) linkUrl += '.html';

                                this.editorKernel.relayCmd('modifylinkForem', {
                                    'url': linkUrl,
                                    'target': target,
                                    'css': cssClass,
                                    'selectType': linkObj.selectType,
                                    'trimLinkSelection': this.config.trimLinkSelection,
                                    'attributes': linkObj.attributes
                                });
                            }
                        });
                    } else {
                        this.editorKernel.relayCmd('modifylinkForem', {
                            'url': linkUrl,
                            'target': target,
                            'css': cssClass,
                            'selectType': linkObj.selectType,
                            'trimLinkSelection': this.config.trimLinkSelection,
                            'attributes': linkObj.attributes
                        });
                    }

                }
                this.linkDialog.hide();
            }
        },

        isHtmlExtensionNeeded: function(href) {
            if (href === undefined) return false;

            return href.startsWith("/content/leforemhe/fr") && !href.endsWith(".html");

            // /*If path is not relative, it's assumed that the user has parsed the full path needed for the navigation*/
            // if (!href.startsWith('/')) return false;
            // let extension = null;
            // if (href.lastIndexOf('.') >= 0) {
            //     extension = href.substring(href.lastIndexOf('.') + 1, href.length) || '';
            // }
            // return !extension;
        },

        addPathPickerButton: function(existingHref) {
            const href = existingHref ? existingHref : '';
            const pathPickerHTML = `<foundation-autocomplete pickersrc="/mnt/overlay/granite/ui/content/coral/foundation/form/pathfield/picker.html?_charset_=utf-8&path={value}&root=%2fcontent&filter=hierarchyNotFile&selectionCount=single" placeholder="Path" name="href" class="rte-linkfield"><div class="foundation-autocomplete-inputgroupwrapper"><div class="coral-InputGroup" role="presentation"><input is="coral-textfield" class="coral3-Textfield coral-InputGroup-input" autocomplete="off" placeholder="Path" id="coral-id-570" role="combobox" aria-label="Path" aria-invalid="false" value="${href}"><span class="coral-InputGroup-button"><button is="coral-button" class="coral3-Button coral3-Button--secondary" size="M" variant="secondary" title="Open Selection Dialog" type="button" aria-label="Open Selection Dialog"><coral-icon class="coral3-Icon coral3-Icon--sizeS coral3-Icon--select" icon="select" size="S" autoarialabel="on" role="img" aria-label="select"></coral-icon><coral-button-label></coral-button-label></button></span></div></div>\n` +
                '        <coral-overlay class="coral3-Overlay foundation-autocomplete-value foundation-picker-buttonlist" aria-hidden="true" data-foundation-picker-buttonlist-src="/mnt/overlay/cq/gui/content/linkpathfield/suggestion{.offset,limit}.html?root=/content&amp;filter=hierarchyNotFile{&amp;query}" style="display: none;"></coral-overlay>\n' +
                `        <coral-taglist class="coral3-TagList" aria-disabled="false" name="href" role="list" aria-live="off" aria-atomic="false" aria-relevant="additions" foundation-autocomplete-value="" aria-invalid="false" style="position: relative;"><coral-tag class="coral3-Tag coral3-Tag--large" closable="" role="listitem" tabindex="0"><button is="coral-button" class="coral3-Button coral3-Button--minimal coral3-Tag-removeButton" size="M" variant="minimal" tracking="off" handle="button" type="button" icon="close" iconsize="XS" title="Remove" tabindex="-1" coral-close=""><coral-icon class="coral3-Icon coral3-Icon--close coral3-Icon--sizeXS" icon="close" size="XS" autoarialabel="on" role="img" aria-label="close"></coral-icon><coral-button-label></coral-button-label></button><coral-tag-label>${href}</coral-tag-label><input handle="input" type="hidden" name="href" value="${href}"></coral-tag><object aria-hidden="true" tabindex="-1" style="display:block; position:absolute; top:0; left:0; height:100%; width:100%; opacity:0; overflow:hidden; z-index:-100;" type="text/html" data="about:blank">​</object></coral-taglist>\n` +
                '      </foundation-autocomplete>';
            this.linkDialog.$dialog.find('.rte-linkfield').replaceWith(pathPickerHTML);

        },

        addStyleSelect: function(cssClass) {
            let buttonName;
            let value;
            if (cssClass) {
                buttonName = cssClass === 'BtBleuPrusse34495E' ? Granite.I18n.get('Bouton stylé') : cssClass === 'numTelBleu' ? Granite.I18n.get('Numéro de téléphone') : cssClass === 'BtBleuPrusse34495E linkAssetButton' ? Granite.I18n.get('Asset (Button)') : cssClass === 'linkAsset' ? Granite.I18n.get('Asset') : Granite.I18n.get('Link');
                value = cssClass === 'BtBleuPrusse34495E' ? 'button' : cssClass === 'numTelBleu' ? 'phone' : cssClass === 'BtBleuPrusse34495E linkAssetButton' ? 'assetButton' : cssClass === 'linkAsset' ? 'asset' : 'link';
            } else {
                buttonName = 'Link';
                value = 'link';
            }
            const selectHTML = '<div class="rte-dialog-style-select rte-dialog-columnContainer">\n' +
                '    <div class=" rte-dialog-column">\n' +
                '        <coral-select class="coral3-Select" placeholder="" __vent-id__="1185" handle="styleSelect" aria-invalid="false" aria-disabled="false">\n' +
                '<button is="coral-button" class="coral3-Button coral3-Button--secondary coral3-Button--block coral3-Select-button" size="M" variant="secondary" tracking="off" handle="button" type="button" block="" aria-haspopup="true" id="coral-id-556" aria-controls="coral-id-557" aria-expanded="false" aria-labelledby="coral-id-558"><coral-button-label></coral-button-label>\n' +
                '  <coral-icon class="coral3-Icon coral3-Select-openIcon coral3-Icon--chevronDown coral3-Icon--sizeXS" icon="chevronDown" size="XS" autoarialabel="on" handle="icon" role="img" aria-label="chevron down"></coral-icon>\n' +
                `  <span id="coral-id-558" handle="label" class=" coral3-Select-label">${buttonName}</span>\n` +
                '</button>\n' +
                '<coral-overlay class="coral3-Overlay coral3-Select-overlay" aria-hidden="true" tracking="off" handle="overlay" trapfocus="on" tabindex="0" focusonshow="on" role="presentation" offset="-1" alignmy="left top" alignat="left bottom" style="display: none;"><div coral-tabcapture="top" tabindex="0"></div><div coral-tabcapture="intermediate" tabindex="0"></div><div coral-tabcapture="bottom" tabindex="0"></div>\n' +
                '  <coral-selectlist class="coral3-SelectList coral3-Select-selectList" role="listbox" id="coral-id-557" tracking="off" handle="list" aria-multiselectable="false" style="max-height: 252px;">' +
                `       <coral-selectlist-item class="coral3-SelectList-item ${value === 'link' ? 'is-selected' : ''}" role="option" selected="${value === 'link'}" value="link" tabindex="0" aria-disabled="false" aria-selected="${value === 'link'}">${Granite.I18n.get('Link')}</coral-selectlist-item>` +
                `       <coral-selectlist-item class="coral3-SelectList-item ${value === 'button' ? 'is-selected' : ''}" role="option" selected="${value === 'button'}" value="button" tabindex="-1" aria-selected="${value === 'button'}" aria-disabled="false">${Granite.I18n.get('Bouton stylé')}</coral-selectlist-item>` +
                `       <coral-selectlist-item class="coral3-SelectList-item ${value === 'phone' ? 'is-selected' : ''}" role="option" selected="${value === 'phone'}" value="phone" tabindex="-1" aria-selected="${value === 'phone'}" aria-disabled="false">${Granite.I18n.get('Numéro de téléphone')}</coral-selectlist-item>` +
                `       <coral-selectlist-item class="coral3-SelectList-item ${value === 'asset' ? 'is-selected' : ''}" role="option" selected="${value === 'asset'}" value="asset" tabindex="-1" aria-selected="${value === 'asset'}" aria-disabled="false">${Granite.I18n.get('Asset')}</coral-selectlist-item>` +
                `       <coral-selectlist-item class="coral3-SelectList-item ${value === 'assetButton' ? 'is-selected' : ''}" role="option" selected="${value === 'assetButton'}" value="assetButton" tabindex="-1" aria-selected="${value === 'assetButton'}" aria-disabled="false">${Granite.I18n.get('Asset (Button)')}</coral-selectlist-item>` +
                '   </coral-selectlist>\n' +
                '</coral-overlay>\n' +
                `<input handle="input" type="hidden" name="" value="${value}">\n` +
                '\n' +
                '\n' +
                '\n' +
                `            <coral-select-item value="link" ${value === 'link' ? 'selected' : ''}>Link</coral-select-item>\n` +
                `            <coral-select-item value="button" ${value === 'button' ? 'selected' : ''}>Styled Button</coral-select-item>\n` +
                `            <coral-select-item value="phone" ${value === 'phone' ? 'selected' : ''}>Phone Number</coral-select-item>\n` +
                `            <coral-select-item value="asset" ${value === 'asset' ? 'selected' : ''}>Asset</coral-select-item>\n` +
                `            <coral-select-item value="assetButton" ${value === 'assetButton' ? 'selected' : ''}>Asset (Button)</coral-select-item>\n` +
                '        </coral-select>\n' +
                '    </div>\n' +
                '</div>';
            if (this.linkDialog.$dialog.find('.rte-dialog-style-select').length == 1) {
                this.linkDialog.$dialog.find('.rte-dialog-style-select').remove();
            }
            this.linkDialog.$dialog.find('coral-popover-content').prepend(selectHTML);
        },

        applyEvents: function(context) {
            this.linkDialog.$dialog.find('button[data-type="apply"]').click(() => this.applyLink(context));
        }

    });

    CUI.rte.plugins.ForemLinkPlugin.LINKABLE_OBJECTS = [
        'img'
    ];

    CUI.rte.plugins.PluginRegistry.register(GROUP, CUI.rte.plugins.ForemLinkPlugin);

    CUI.rte.commands.ForemLink = new Class({

        toString: 'ForemLink',

        extend: CUI.rte.commands.Link,

        isCommand: function(cmdStr) {
            var cmdLC = cmdStr.toLowerCase();
            return (cmdLC == "modifylinkforem") || (cmdLC == "unlinkforem")
        },

        addLinkToDom: function(execDef) {
            var context = execDef.editContext;
            var nodeList = execDef.nodeList;
            var url = execDef.value.url;
            var styleName = execDef.value.css;
            var target = execDef.value.target;
            var attributes = execDef.value.attributes || {};
            var links = [];
            const selectType = execDef.value.selectType;
            nodeList.getAnchors(context, links, true);
            if (links.length > 0) {
                // modify existing link(s)
                for (var i = 0; i < links.length; i++) {
                    this.applyLinkProperties(links[i].dom, url, styleName, target, attributes);
                    const dpr = CUI.rte.DomProcessor
                    if (selectType !== 'assetButton' && links[i].dom.firstChild.localName === 'span') {
                        dpr.removeWithoutChildren(links[i].dom.firstChild)
                    }
                    if (selectType === 'assetButton' && links[i].dom.firstChild.localName !== 'span') {
                        dpr.insertAsParent(context, links[i].dom.firstChild, 'span', {})
                    }
                }
                if (selectType === 'assetButton' && !nodeList.nodesChanged[0].innerHTML.startsWith('<span>')) {
                    nodeList.nodesChanged[0].innerHTML = '<span>' + nodeList.nodesChanged[0].innerHTML + '</span>'
                }

            } else {
                // create new link
                var sel = CUI.rte.Selection;
                var dpr = CUI.rte.DomProcessor;
                if (execDef.value.trimLinkSelection === true) {
                    var range = sel.getLeadRange(context);
                    range = sel.trimRangeWhitespace(context, range);
                    sel.selectRange(context, range);
                    nodeList = dpr.createNodeList(context, sel.createProcessingSelection(
                        context));
                }
                // handle HREF problems on IE with undo (IE will deliberately change the
                // HREF, causing the undo mechanism to fail):
                var helperSpan = context.createElement('span');
                helperSpan.innerHTML = '<a></a>';
                helperSpan.childNodes[0].href = url;
                attributes.href = helperSpan.childNodes[0].href;
                attributes[CUI.rte.Common.HREF_ATTRIB] = url;
                if (styleName) {
                    attributes.className = styleName;
                }
                if (target) {
                    attributes.target = target;
                } else {
                    delete attributes.target;
                }
                for (var key in attributes) {
                    if (attributes.hasOwnProperty(key)) {
                        var attribValue = attributes[key];
                        if ((attribValue === null) || (attribValue === undefined) || (attribValue.length === 0) ||
                            (attribValue === CUI.rte.commands.Link.REMOVE_ATTRIBUTE)) {
                            delete attributes[key];
                        }
                    }
                }
                nodeList.surround(context, 'a', attributes);
                if (selectType === 'assetButton' && !nodeList.nodesChanged[0].innerHTML.startsWith('<span>')) {
                    nodeList.nodesChanged[0].innerHTML = '<span>' + nodeList.nodesChanged[0].innerHTML + '</span>'
                }
                if (selectType !== 'assetButton' && nodeList.nodesChanged[0].innerHTML.startsWith('<span>')) {
                    nodeList.nodesChanged[0].innerHTML = nodeList.nodesChanged[0].firstChild.innerHTML;
                }
            }
        },

        removeLinkFromDom: function(execDef) {
            var dpr = CUI.rte.DomProcessor;
            var context = execDef.editContext;
            var nodeList = execDef.nodeList;
            var links = [];
            nodeList.getAnchors(context, links, true);
            for (var i = 0; i < links.length; i++) {
                if (links[i].dom.firstChild.localName === 'span') {
                    dpr.removeWithoutChildren(links[i].dom.firstChild)
                }
                dpr.removeWithoutChildren(links[i].dom);
            }
        },

        execute: function(execDef) {
            switch (execDef.command.toLowerCase()) {
                case 'modifylinkforem':
                    this.addLinkToDom(execDef);
                    break;
                case 'unlinkforem':
                    this.removeLinkFromDom(execDef);
                    break;
            }
        },

    });

    CUI.rte.commands.CommandRegistry.register('forem-link', CUI.rte.commands.ForemLink);

    function addPluginToDefaultUISettings() {
        let toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.inline.toolbar;
        toolbar.splice(3, 0, GROUP + '#' + 'unlink');
        toolbar.splice(3, 0, GROUP + '#' + 'modifylink');

        toolbar = CUI.rte.ui.cui.DEFAULT_UI_SETTINGS.fullscreen.toolbar;
        toolbar.splice(3, 0, GROUP + '#' + 'unlink');
        toolbar.splice(3, 0, GROUP + '#' + 'modifylink');
    }

})(jQuery, window.CUI);