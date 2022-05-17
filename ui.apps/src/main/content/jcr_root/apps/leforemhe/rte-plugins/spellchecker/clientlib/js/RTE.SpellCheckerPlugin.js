(function ($, CUI, author) {
    CUI.rte.plugins.FixedSpellCheckerPlugin = new Class({

        extend: CUI.rte.plugins.SpellCheckerPlugin,

        initializeUI: function (tbGenerator) {
            var plg = CUI.rte.plugins;
            if (this.isFeatureEnabled('checktext')) {
                this.checkTextUI = tbGenerator.createElement('checktext', this, true,
                    this.getTooltip('checktext'));
                tbGenerator.addElement('spellcheck', plg.Plugin.SORT_LISTS, this.checkTextUI,
                    10);
            }
        },

        doCheckText: function (html, contentPath, successFn, failureFn) {
            const fixedContentPath = contentPath ? contentPath : author.page && author.page.path ? author.page.path : '';
            const url = this.config.spellcheckerUrl;
            const method = this.config.method;
            const callback = function (jqXHR, textStatus) {
                if (textStatus == 'success') {
                    let isError = true;
                    let spellcheckResults;
                    try {
                        if (method == 'POST') {
                            if (jqXHR && jqXHR.responseJSON) {
                                spellcheckResults = jqXHR.responseJSON;
                                isError = false;
                            }
                        } else if (method == 'GET') {
                            if (jqXHR && jqXHR.responseText) {
                                spellcheckResults = CUI.rte.Utils.jsonDecode(jqXHR.responseText);
                                isError = false;
                            }
                        }
                    } catch (e) {
                        // ignore by default
                    }
                    if (isError) {
                        failureFn();
                    } else {
                        successFn(spellcheckResults);
                    }
                } else {
                    failureFn();
                }
            };
            const params = {
                '_charset_': 'utf-8',
                'mode': 'text',
                'html': 'true',
                'text': html,
                'cp': fixedContentPath,
                'json': 'true'
            };
            $.ajax({
                method: method,
                url: url,
                data: params,
                complete: callback
            });
        },

        checkSuccess: function (spellcheckResults) {
            var hasErrors = false;
            var words = spellcheckResults.words;
            var wordCnt = words.length;
            this.misSpelledWords = [];
            for (var w = 0; w < wordCnt; w++) {
                var word = words[w];
                var result = word.result;
                if (!result.isCorrect) {
                    var startPos = word.start;
                    var charCnt = word.chars;
                    var suggestions = result.suggestions;
                    const nodeList = this.markInvalidWord(this.editorKernel.getEditContext(), startPos, charCnt, suggestions)
                    if(nodeList) {
                        this.misSpelledWords.push({
                            'word': word,
                            'span': nodeList[0]
                        });
                    }
                    hasErrors = true;
                }
            }
            if (!hasErrors) {
                this.editorKernel.getDialogManager().alert(
                    CUI.rte.Utils.i18n('plugins.spellCheck.spellChecking'),
                    CUI.rte.Utils.i18n('plugins.spellCheck.noMistakeAlert'));
                return;
            }
            this.isInspectionOn = true;
            this.checkTextUI.setHighlighted(true);
        },

        markInvalidWord: function (context, startPos, charCnt, suggestions) {
            var com = CUI.rte.Common;
            var dpr = CUI.rte.DomProcessor;
            var startDef = com.getNodeAtPosition(context, startPos, true);
            var endDef = com.getNodeAtPosition(context, startPos + charCnt, true);
            // handle EOP situations correctly
            var endNode = endDef.dom;
            var endOffset = endDef.offset;
            if(endNode.parentElement.localName !== 'acronym' && endNode.parentElement.localName !== 'abbr' && !this.isInsideAddress(endNode)) {
                if ((endNode.nodeType === 1) && (endDef.offset === null || endDef.offset === undefined)) {
                    var baseEndNode = endNode;
                    endNode = com.getLastTextChild(baseEndNode);
                    if (!endNode) {
                        endNode = com.getPreviousTextNode(context, baseEndNode);
                    }
                    endOffset = com.getNodeCharacterCnt(endNode);
                }
                var nodeList = dpr.createNodeList(context, {
                    'startNode': startDef.dom,
                    'startOffset': startDef.offset,
                    'endNode': endNode,
                    'endOffset': endOffset
                });
                var suggAttrib = null;
                if (suggestions !== null && suggestions !== undefined) {
                    var suggCnt = suggestions.length;
                    for (var s = 0; s < suggCnt; s++) {
                        var suggestion = suggestions[s];
                        if (s === 0) {
                            suggAttrib = suggestion;
                        } else {
                            suggAttrib += '|' + suggestion;
                        }
                    }
                }
                var attribs = {
                    '_rtetemp': 'spchk'
                };
                if (this.config.invalidStyle) {
                    attribs['style'] = this.config.invalidStyle;
                }
                if (this.config.invalidClass) {
                    attribs['className'] = this.config.invalidClass;
                }
                if (suggAttrib !== null && suggAttrib !== undefined) {
                    attribs['_rtespchksugg'] = suggAttrib;
                }
                return nodeList.surround(context, 'span', attribs);
            }
        },

        isInsideAddress: function(node) {
            let currentNode = node
            while(currentNode && !(currentNode.className && currentNode.className.includes("is-edited"))) {
                if(currentNode.localName === 'address') {
                    return true
                }
                currentNode = currentNode.parentNode;
            }
            return false
        }
    });

    CUI.rte.plugins.PluginRegistry.register('spellcheck', CUI.rte.plugins.FixedSpellCheckerPlugin);
})(Granite.$, window.CUI, Granite.author);