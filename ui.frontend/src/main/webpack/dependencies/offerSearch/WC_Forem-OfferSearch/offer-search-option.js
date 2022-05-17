
/*

Permet de gérer un élement visuel du type OfferSearchOption dans un Webcomponent.

Propriétés
-----------

hidden :
Permet de gérer la visibilité du composant.

disabled :
Permet de gérer si le composant est utilisable ou pas.

value :
Permet de récupérer la valeur de l'option sous la forme d'un objet
{value: [La valeur de l'attribut 'id'], checked: Indique si la checkbox de l'option est sélectionnée}

readonly :
Permet de gérer si le composant est en status readonly ou pas


Attributs
---------

disabled :
Permet de gérer si le composant est utilisable ou pas

label :
Permet de définir le texte associé à la checkbox

icontooltip:
Permet de spécifier le texte d'un icon de tooltip

id:
Attribut natif, nécessaire pour l'identification de la valeur de retour lors de la recherche du parent

readonly :
Permet de passer le composant en readonly comme pour un input et un select classique.

Events
-------


*/

import Template from './offer-search-option.html';
import ForemOfferSearchUtils from './utils';

export default class ForemOfferSearchOption extends HTMLElement {
  static get observedAttributes() {
    return ['disabled', 'label', 'icontooltip', 'readonly'];
  }

  constructor() {
    super();
    this.template = document.createElement('template');
    this._params = {};
    this._utils = new ForemOfferSearchUtils();
  }

  connectedCallback() {
    this.innerHTML = '';
    this.template.innerHTML = Template;
    this.bindDom();
    if (!this.elements) return;
    this.appendChild(this.template.content);
    this._manageDisabled(this.hasAttribute('disabled'));
    this._manageLabel(this.getAttribute('label') || '');
    this._manageIconTooltip(this.getAttribute('icontooltip'));
    this._manageReadOnly(this.hasAttribute('readonly'));

    this._setupInputId();
    this._utils.addEventListener(this.elements.icon, 'click', this._displayTooltip.bind(this));
  }

  disconnectedCallback() {
    this._utils.unbindEvents();
  }

  attributeChangedCallback(attributeName, oldValue, newValue) {
    if (!this._params || !this.elements)
      return;

    switch(attributeName) {
      case 'disabled':
        this._manageDisabled(this.hasAttribute('disabled'));
        break;
      case 'label':
        this._manageLabel(newValue);
        break;
      case 'icontooltip':
        this._manageIconTooltip(newValue);
        break;
      case 'readonly':
        this._manageReadOnly(this.hasAttribute('readonly'));
        break;          
    }
  }

  bindDom() {
    this.elements = {};
    this.elements.checkbox = this.template.content.querySelector('.refCheckbox');
    this.elements.label = this.template.content.querySelector('.refLabel');
    this.elements.labelContent = this.template.content.querySelector('.refLabelContent');
    this.elements.icon = this.template.content.querySelector('.refIcon');
  }

  // Permet de désactiver l'utilisation du composant
  set disabled(value) {
    this.toggleAttribute('disabled', value);
  }

  get disabled() {
    return this.hasAttribute('disabled');
  }

  /**
   *  Permet de gérer si le composant est readonly ou pas
   */
    set readonly(value) {
    this.toggleAttribute('readonly', value);
  }

  /**
   * Permet de récupérer l'etat readonly de l'input
   */
  get readonly() {
      return this.hasAttribute('readonly');
  }

  get value() {
    return {value: this.getAttribute('id'), checked: this.elements.checkbox.checked}
  }
  
  /**
   * Permet de définir si le composant est utilisable ou pas
   */
  _manageDisabled(isDisabled) {
    if (this._params.disabled === isDisabled) return;
    this.elements.checkbox.disabled = isDisabled;
    this.elements.checkbox.toggleAttribute('disabled', isDisabled);
  }

  /**
  * Permet de définir si le composant est readonly ou pas
  */
   _manageReadOnly(isReadOnly) {
    if (this._params.readonly === isReadOnly) return;
    this._params.readonly = isReadOnly;

    this.elements.checkbox.toggleAttribute('readonly', isReadOnly);
    this.elements.label.classList.toggle('click-through', isReadOnly);
  }

  _manageLabel(newValue) {
    this.elements.labelContent.innerHTML = newValue;
  }

  _manageIconTooltip(newValue) {
    this._params.tooltip = newValue;
    this.elements.icon.hidden = !newValue;
    this.elements.icon.title = newValue;
  }

  _setupInputId(){
    const newId = this._utils.generateUniqueId('offerSearchOption');
    this.elements.checkbox.setAttribute('id', newId);
    this.elements.label.setAttribute('for', newId);
  }

  _displayTooltip(e) {
    e.preventDefault();
    const event = new CustomEvent('displayTooltip', {detail: this._params.tooltip, bubbles: true, cancelable: true});
    this.dispatchEvent(event);
  }
}

customElements.define('forem-offer-search-option', ForemOfferSearchOption);
