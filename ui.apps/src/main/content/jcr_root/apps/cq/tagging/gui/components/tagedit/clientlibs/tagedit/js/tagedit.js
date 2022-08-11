/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2015 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
(function (window, document, Granite, $) {
    "use strict";

    var selected = new Object();

    $(document).on("foundation-contentloaded", function () {
        // hide the default coral taglist on multiselect
        // TODO find a better way to do this
        $("#tagedit-settings-languagepicker .coral-TagList").remove();
        var requestPath = $(".foundation-content-path").data("foundationContentPath");
        // prefill the fields
        // TODO find a way to eliminate this ajax and perform the prefilling in some jsp itself
        $.ajax({
            type: "GET",
            url: requestPath + ".json" + '?_ck=' + Date.now(),
            contentType: "application/json"
        }).success(function (res) {
            var title = res["jcr:title"];
            var description = res["jcr:description"];
            var backgroundColor = res["backgroundColor"];
            $("#tagtitle").val(title);
            $("#tagdescription").val(description);
			$("#tagbackgroundColor").val(backgroundColor);
            for (var key in res) {
                var titlePrefix = "jcr:title.";
                if (res.hasOwnProperty(key) && key.lastIndexOf(titlePrefix, 0) === 0) {
                    var lang = key.substring(titlePrefix.length);
                    var val = res[key];
                    handleAdd(lang, val);
                }
            }

        });

        $(document).on("selected", function(e){
            e.preventDefault();
            var newSelectedList = e.selected;
            for (var i = 0; i < newSelectedList.length; i++) {
                if (!selected[newSelectedList[i]]) {
                    handleAdd(newSelectedList[i], null);
                    break;
                }
            }
        });

        $(document).on("click", ".tag-added-language-remove-button", function (e) {
            var $this = $(this);
            var addedLangDiv = $this.closest(".tag-added-language");
            var lang = addedLangDiv.data("lang");
            selected[lang] = false;
            var $select = $(".tag-languages-select");
            var currentSelectList = $select.data("select").getValue();
            var updatedSelectList = new Array();
            for (var i = 0; i < currentSelectList.length; i++) {
                if (lang != currentSelectList[i]) {
                    updatedSelectList.push(currentSelectList[i]);
                }
            }
            $select.data("select").setValue(updatedSelectList);
            addedLangDiv.remove();
        });

        var wizard = $("form#tag-edit-form");
        wizard.on("submit", function (e) {
            e.preventDefault();
            submit(wizard);
        });

    });

    function handleAdd(lastSelected, fieldValue) {
        var langLastSelected = document.getElementsByClassName(lastSelected)[0];
        if(!langLastSelected){
            selected[lastSelected] = false;
            return;
        }
        selected[lastSelected] = true;
        var lastSelectedTitle = langLastSelected.text;
        var fieldName = "./jcr:title." + lastSelected;
        var fieldValueAttr = (fieldValue != null) ? ("value = \"" + CQ.shared.XSS.getXSSValue(fieldValue) + "\"") : "";
        var languageMarkup = "<div class = \"tag-added-language\" data-lang=\"" + lastSelected + "\">"
                                  + "<div class=\"coral-Form-fieldlabel tag-added-language-label\">" + lastSelectedTitle + "</div>"
                                  + "<input class = \"coral-Textfield tag-added-language-textfield\" name = \"" + fieldName + "\"" + fieldValueAttr + ">"
                                  + "<button type=\"button\" class=\"coral-Button coral-Button--secondary coral-Button--square tag-added-language-remove-button\">"
                                      + "<i class=\"coral-Icon coral-Icon--sizeS coral-Icon--minus\"></i>"
                                  + "</button>"
                           + "</div>";
        var parent = $("#tag-selected-languages");
        parent.append(languageMarkup);

    }

    function submit(wizard) {
        var ui = $(window).adaptTo('foundation-ui');
        var cancel = wizard.find('[data-foundation-wizard-control-action="cancel"]');
        var foundationContentPath = $(".foundation-content-path").data("foundationContentPath");
        var successMessage = Granite.I18n.get("Tag Edited Successfully");
        var errorMessage = Granite.I18n.get("Failed to edit tag");
        var successTitle = Granite.I18n.get("Success");
        var errorTitle = Granite.I18n.get("Error");
        var okButtonText = Granite.I18n.get('OK');
        var closeButtonText = Granite.I18n.get('Close');
        var editTagSettings = $("#edittagsettings");

        var data;
        data = wizard.serialize();
        var processData = true;

        var contentType = wizard.prop("enctype");
        var length = Object.keys(selected).length;
        for(var i = 0; i < length; i++) {
            if(selected[Object.keys(selected)[i]] == false) {
                var toDelete = "./jcr:title." + Object.keys(selected)[i];
                data += "&" + toDelete + "@Delete";
            }
        }
        $.ajax({
            type: wizard.prop("method"),
            url: foundationContentPath,
            data: data,
            processData: processData,
            contentType: contentType
        }).done(function (html) {
          ui.prompt(successTitle, successMessage, 'success', [{
              id: 'ok',
              text: okButtonText
          }], function(btnId) {
              window.location = cancel.prop('href');
          });

        }).fail(function (xhr, error, errorThrown) {
          ui.prompt(errorTitle, errorMessage, 'error', [{
              id: 'close',
              text: closeButtonText
          }], function(btnId) {
              window.location = cancel.prop('href');
          });
        });
    }

})(window, document, Granite, Granite.$);
