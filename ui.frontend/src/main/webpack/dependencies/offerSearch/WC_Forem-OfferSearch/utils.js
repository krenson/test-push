export default class ForemOfferSearchUtils {
    
    /**
     * Permet de générer un Id unique
     */
    generateUniqueId(name){
        let random = Math.round(Math.random() * 10000000);
        let timestamp = new Date().getTime();
        return `${name ? name + '-': ''}${timestamp}-${random}`;
    }
    
    /**
     * Enregistrement d'un évènement
     *
     * @param {*} elementId
     * @param {*} event
     * @param {*} bindedFunction
     */
    addEventListener(element, event, bindedFunction) {
        this.bindedEvents = this.bindedEvents || [];
        this.bindedEvents
            .push({
                function: bindedFunction,
                event,
                element
            });
        element.addEventListener(event, bindedFunction);
    }

    /**
      * Déconnexion des différents events
      */
    unbindEvents() {
        if(!this.bindedEvents) return;
        for (let index = 0; index < this.bindedEvents.length; index++) {
            const item = this.bindedEvents[index];
            item.element.removeEventListener(item.event, item.function);
        }
        this.bindedEvents = [];
    }
}