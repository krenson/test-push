(function ($, $document) {

    "use strict";

    // **************************************************************************
    // ** CONSTANTS
    // **************************************************************************

    const EVENT_CLICK = "click";

    const ATTRIBUTE_MULTIFIELD_ADD = "coral-multifield-add";
    const ATTRIBUTE_MULTIFIELD_MAX_ITEMS = "data-max-mf-items";
    const ATTRIBUTE_MULTIFIELD_REMOVE = 'handle="remove"';

    const SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON = `button[${ATTRIBUTE_MULTIFIELD_ADD}]`;
    const SELECTOR_MULTIFIELD_ITEM = "coral-multifield-item";
    const SELECTOR_MULTIFIELD_REMOVE_ITEM_BUTTON = `button[${ATTRIBUTE_MULTIFIELD_REMOVE}]`;

    // **************************************************************************
    // ** CLASSES
    // **************************************************************************

    class Listener {

        constructor(selector, event, handler) {
            this.selector = selector;
            this.event = event;
            this.handler = handler;
        }

        getSelector() {
            return this.selector;
        }

        getEvent() {
            return this.event;
        }

        getHandler() {
            return this.handler;
        }
    }

    // **************************************************************************
    // ** LISTENER HANDLERS
    // **************************************************************************

    function multifieldMaxItemsHandler(context, dialogLoaded) {

        $(context).each(function() {

            let $field = $(this).parent();
            const maxSize = $field.attr(ATTRIBUTE_MULTIFIELD_MAX_ITEMS);

            if (maxSize) {
                if (dialogLoaded && $field.length > 1) {
                    $field = $($field[0]);
                }
                const currentSize = $field.children(SELECTOR_MULTIFIELD_ITEM).length;
                if ((dialogLoaded && currentSize >= maxSize) || (!dialogLoaded && currentSize >= (maxSize - 1))) {
                    $(this).hide();
                    return true;
                }
            }
        });

    }

    function removed(context) {
        if ($(SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON)[0]) {
            const add = $(SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON)[0];
            $(add).show();
            $(add).attr("style", "");
        }
        return true;
    }

    // **************************************************************************
    // ** LISTENERS
    // **************************************************************************

    const listeners = [];
    listeners.push(new Listener(SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON, EVENT_CLICK, multifieldMaxItemsHandler));

    // **************************************************************************
    // ** REGISTER LOGIC
    // **************************************************************************

    $document.on("dialog-ready", function () {
        multifieldMaxItemsHandler($(SELECTOR_MULTIFIELD_ADD_ITEM_BUTTON), true);

        $.each(listeners, function (index, listener) {
            $(listener.getSelector()).on(listener.getEvent(), function () {
                return listener.getHandler()(this);
            })
        });

        $document.on(EVENT_CLICK, SELECTOR_MULTIFIELD_REMOVE_ITEM_BUTTON, function () {
            return removed(this);
        });
    });

})($, $(document));