/*

Permet de gérer les boutons d'actions custom de la popup


Attributs
---------
action : Permet de définir le nom de l'event qui se dispatcher au clique

*/
export default class ForemPopupAction extends HTMLElement {
  static get observedAttributes() {
    return ['action'];
  }

  constructor() {
    super();    
  }

  connectedCallback() {
    this.setAttribute('is','button');
    this.classList.add('btn');
    this.bindDom();    
    this._params = {};
    this._manageAction(this.getAttribute('action') || '');
  }

  attributeChangedCallback(attributeName, oldValue, newValue) {
    if(!this.params || !this.elements)
      return;
    
    switch(attributeName) {
      case 'action': {
        this._manageAction(newValue, oldValue);
        break;
      }
    }    
  }

  bindDom() {
    this.elements = {};
    this.addEventListener('click', this._dispatchAction.bind(this));
  }

  /**
   * Permet de gérer l'attribut action
   * @param {*} newValue 
   * @param {*} oldValue 
   */
  _manageAction(newValue, oldValue){
    if(newValue == oldValue) return;
    this._params.action = newValue;
  }

  _dispatchAction(){
    this.dispatchEvent(new CustomEvent(this._params.action, {'bubbles': true }));
  }
}

customElements.define('forem-popup-action', ForemPopupAction);