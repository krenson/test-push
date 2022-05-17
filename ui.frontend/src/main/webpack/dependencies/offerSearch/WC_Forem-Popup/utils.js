export default class ForemPopupUtils {
    
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
        if (!element) return;
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

    /**
     * Déconnexion des différents events pour une recherche précise
     * @param {*} element 
     * @param {*} event 
     * @param {*} bindedFunction 
     */
    removeEventListener(element, event = undefined, bindedFunction = undefined) {
        if(!this.bindedEvents) return;
        let index = this._findIndex(element, event, bindedFunction);
        while (index > -1) {
            const item = this.bindedEvents[index];
            item.element.removeEventListener(item.event, item.function);
            this.bindedEvents.splice(index, 1);
            index = this._findIndex(element, event, bindedFunction);
        }
    }

    /**
     * Permet de faire une recherche d'event précedemment ajouté sur base de critère distincts
     * @param {*} element 
     * @param {*} event 
     * @param {*} bindedFunction 
     */
    _findIndex(element, event, bindedFunction) {
        return this.bindedEvents.findIndex((be) =>{
            let isEqual = true;
            if (isEqual && element) {
                isEqual = be.element === element;
            }
            if (isEqual && event) {
                isEqual = be.event === event;
            }
            if (isEqual && bindedFunction) {
                isEqual = be.function === bindedFunction;
            }
            return isEqual;
        });
    }
}