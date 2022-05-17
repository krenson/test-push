import Template from './popup.html';
import './popup-action';
import ForemPopupUtils from './utils';

/**
 * Composant Popup.
 *
 * # Usage :
 * 1. Ajouter l'élément dans votre formulaire
 * Ajouter le titre de la popup avec l'attribut 'label'
 * Ajouter le nom de l'id à récupérer 'composant' dans la popup avec l'attribute 'idtag'
 * `<forem-popup label="Classification" idTag="foremClassification">`
 * 2. Selectionner l'élément dans votre ViewComponent :
 * `const maPopup = this.querySelector('forem-popup');
 * 3. Le composant utilisera une promesse afin d'informer l'app qu'il se ferme avec confirmation ou annulation
 * if(await this.maPopup.open()){
 *  // To do
 * }
 * 4. Récupération du composant dans la popup 'getComponent''
 * if(await this.maPopup.open()){
 *  const myCompo = this.maPopup.getComponent();
 * 
 * 
 * Attributs
 * ----------
label :
Permet de modifier le titre de la popup.

idtag: 
Permet de récupérer un composant se trouvant dans la popup grâce à la méthode getComponent(). La valeur de l'attribut doit correspondre à l'id de ce composant.

loadcomponentdata: 
Permet de spécifier une méthode à utiliser provenant du composant lié via l'attribut idtag. Cette méthode sera exécutée à l'ouverture de la popup.

required: 
Permet de spécifier si certains champs du composant lié à la popup sont requis. Si c'est le cas, une vérification sera effectuée et le bouton de confirmation sera désactivé si ces champs ne sont pas valides.

size: 
Permet de modifier la taille de la popup. Différents valeurs sont possibles:
  auto : la popup s'adapte à son contenu.
  sm : la popup prend une petite taille.
  lg : la popup prend une grande taille.

displaybuttons:
Permet de spécifier si on affiche le bouton de validation "Enregistrer".

cancelbtnlabel:
Permet de spécifier le texte du bouton de fermeture.

confirmbtnlabel:
Permet de spécifier le texte du bouton de confirmation.

Méthodes
----------
open(elementToFocusOnClose) :
Permet d'ouvrir la popup. Le retour de la méthode vous informera avec quel bouton la popup a été fermée.
  elementToFocusOnClose : node sur laquel sera renvoyé le focus une fois la popup fermé. Utilisé en général si la popup est déclanché suite au clique sur un bouton. Peut ne pas être spécifié

close(confirmation):
Permet de fermer la popup. Cette méthode est utilisée lors du clique sur le bouton Fermer/La croix ou le bouton de confirmation.
  confirmation : true/false, permet de spécifier quel bouton a lancé la fermeture de la popup

getComponent():
Permet de récupérer le composant lié à la popup grâce à l'attribut idtag

Events
----------
onCloseClick:
S'exécute avant la fermeture de la popup quand on clique sur le bouton fermer/la croix en haut à droite.

onCloseClickEnd:
S'exécute après la fermeture de la popup quand on clique sur le bouton fermer/la croix en haut à droite.

onConfirmClick:
S'exécute avant la fermeture de la popup quand on clique sur le bouton Confirmer.
  Cancelable - Si la fermeture de la popup est empêchée, il est nécessaire d'afficher une explication à l'utilisateur.

onConfirmClickEnd:
S'exécute après la fermeture de la popup quand on clique sur le bouton Confirmer.

 *  * 
 * }
 * @class PopupComponent
 * @extends {HTMLElement}
 */
export default class ForemPopup extends HTMLElement {
  static get observedAttributes() {
    return ['label', 'idtag', 'loadcomponentdata', 'required', 'size', 'displaybuttons', 'cancelbtnlabel', 'confirmbtnlabel'];
  }

  constructor() {
    super();
    // Init variable
    this.resolvePopup = undefined;
        
    this.onLoadEvent = function () { };
    this._actions = [];
    this.querySelectorAll('forem-popup-action').forEach(action => {
      this._actions.push( action.cloneNode(true));
      action.remove();
    });
    this.initalContentHtml = this.innerHTML; 
    // Remplacement du contenu initial de la Popup par le template de base    
    this.innerHTML = "";
    this.template = document.createElement('template');
    this.template.innerHTML = Template;
    this._callbacks = {};
    this._callbacks.loseFocus = this.checkFocusOut.bind(this);
    this._callbacks.handleEscape = this._handleEscape.bind(this);
    this._utils = new ForemPopupUtils();
  }

  connectedCallback() {
    this.bindDom();    
    this.params = {};
    this.setAttribute('is','div');
    /* this.setAttribute('aria-live','assertive');
    this.setAttribute('aria-atomic','true'); */
    this.setAttribute('aria-hidden','true');
    this.setAttribute('hidden','hidden');
    this.setAttribute('role','dialog')
    this.setAttribute('aria-modal', 'true')

    this.classList.add('modal','modal-scroll-body');
    
    this._manageLabel(this.getAttribute('label') || undefined);
    this._manageSize(this.getAttribute('size') || 'auto');
    this._manageIdTag(this.getAttribute('idtag') || undefined);
    this._manageLoadComponentData(this.getAttribute('loadcomponentdata') || undefined);
    this._manageDisplayButtons(this.hasAttribute('displaybuttons') ? true : null);
    this._manageRequired(this.hasAttribute('required') ? true : null);
    this._manageCancelBtnLabel(this.getAttribute('cancelbtnlabel') || 'Fermer');
    this._manageConfirmBtnLabel(this.getAttribute('confirmbtnlabel') || 'Enregistrer');

    // Ajout d'un ID unique pour l'accessibilité
    const titleId = this._getUniqueId();
    const contentId = this._getUniqueId();    
    this.setAttribute('aria-labelledby', titleId);
    this.setAttribute('aria-describedby', contentId);
    this.elements.modalTitle.setAttribute('id', titleId);
    this.elements.popupBody.setAttribute('id', contentId);


    this.elements.popupBody.innerHTML = this.initalContentHtml + this.elements.popupBody.innerHTML;
    // Events    
    this._utils.addEventListener(this.elements.popupCloseButton, 'click', this.close.bind(this, false));
    this._utils.addEventListener(this.elements.popupCloseButtonTop, 'click', this.close.bind(this, false));
    this._utils.addEventListener(this.elements.popupConfirmButton, 'click', this.close.bind(this, true));
        
    this.appendChild(this.template.content);
    this._actions.forEach(action => this.elements.popupCustomButtons.appendChild(action));
  }

  disconnectedCallback(){
    this.close();
    this._utils.unbindEvents();
  }

  attributeChangedCallback(attributeName, oldValue, newValue) {
    if(this.params) {
      switch(attributeName) {
        case 'label': {
          this._manageLabel(newValue);
          break;
        }
        case 'size': {
          this._manageSize(newValue, oldValue);
          break;
        }
        case 'idtag': {
          this._manageIdTag(newValue);
          break;
        }
        case 'loadcomponentdata': {
          this._manageLoadComponentData(newValue);
          break;
        }
        case 'required': {
          this._manageRequired(newValue);
          break;
        }
        case 'displaybuttons': {
          this._manageDisplayButtons(newValue);
          break;
        }
        case 'cancelbtnlabel': {
          this._manageCancelBtnLabel(newValue);
          break;
        }
        case 'confirmbtnlabel': {
          this._manageConfirmBtnLabel(newValue);
          break;
        }
      }
    }
  }

  bindDom() {
    this.elements = {};
    this.elements.modalTitle = this.template.content.querySelector('.refModalTitle');
    this.elements.popupCloseButtonTop = this.template.content.querySelector('.refPopupCloseButtonTop');
    this.elements.popupBody = this.template.content.querySelector('.refPopupBody');
    this.elements.popupHeader = this.template.content.querySelector('.refPopupHeader');
    this.elements.popupFooter = this.template.content.querySelector('.refPopupFooter');
    this.elements.popupContent = this.template.content.querySelector('.refPopupContent');
    this.elements.popupCloseButton = this.template.content.querySelector('.refPopupCloseButton');
    this.elements.popupConfirmButton = this.template.content.querySelector('.refPopupConfirmButton');
    this.elements.popupCustomButtons = this.template.content.querySelector('.refPopupCustomButtons');
  }

  checkFocusOut(e) {
    if( (!this.contains(e.relatedTarget) && !this.contains(e.target)) || e.relatedTarget == this.elements.firstEmptyFocusableDiv || e.relatedTarget == this.elements.lastEmptyFocusableDiv ){
      this._focusFirstElement();
    }    
  }

  _handleEscape(event){
    const key = event.which || event.keyCode;
    if (key === 27) {
      this.close();
    }
  }

  /**
   * Permet de gérer le titre an fonction de l'attribut Label
   * @param {*} value 
   */
  _manageLabel(value) {
    this.params.label = value;
    this.elements.modalTitle.innerHTML = this.params.label;
  }

  _manageSize(value, oldValue) {
    if(oldValue == value) return;
    oldValue = 'modal-' + oldValue;
    value = 'modal-' + value;
    this.params.size = value;
    this.classList.remove(oldValue);
    this.classList.add(this.params.size);
  }

  _manageIdTag(value) {
    this.params.idTag = value;
  }

  _manageLoadComponentData(value) {
    this.params.loadComponentData = value;
  }

  _manageDisplayButtons(value) {
    const boolValue = value !== null;
    this.params.displayFormButton = boolValue;
    this.elements.popupConfirmButton.hidden = !boolValue;
  }

  _manageRequired(value){
    this.params.required = value !== null;
  } 

  _manageCancelBtnLabel(value) {
    this.params.cancelBtnLabel = value;
    this.elements.popupCloseButton.innerHTML = value;
  }

  _manageConfirmBtnLabel(value) {
    this.params.confirmBtnLabel = value;
    this.elements.popupConfirmButton.innerHTML = value;
  }
  
  /**
   * Fonction de fermeture de la zone
   */
  close(confirmation) {
    if (this.hidden) return;

    if(confirmation){
      const confirmEvent = new CustomEvent('onConfirmClick', {cancelable: true})
      this.dispatchEvent(confirmEvent);
      if (confirmEvent.defaultPrevented) {
        return;
      }
    } else {
      this.dispatchEvent(new Event('onCloseClick'));
    }

    this._utils.removeEventListener(window, 'focusout', this._callbacks.loseFocus);
    this._utils.removeEventListener(window, 'keyup', this._callbacks.handleEscape)

    this.classList.remove('open');
    this.setAttribute('aria-hidden', true);
    this.setAttribute('hidden', 'hidden');
    
    document.body.classList.remove('no-scroll');

    if (this.intervalCheckingRecquired) {
      clearInterval(this.intervalCheckingRecquired);
    }

    if(this.params.elementToFocusOnClose){
      this.params.elementToFocusOnClose.focus();
      this.params.elementToFocusOnClose = undefined;
    }

    if (this.resolvePopup) {
      this.resolvePopup(confirmation);
      this.resolvePopup = undefined;
    }
   
    if(confirmation)
      this.dispatchEvent(new Event('onConfirmClickEnd'));
    else
      this.dispatchEvent(new Event('onCloseClickEnd'));

    this._removeEmptyFocusableDiv();
  }

  /**
   * Récupération du component
   */
  getComponent() {
    if(this.params.idTag)
      return this.querySelector(`#${this.params.idTag}`);
    
    return null;
  }

  /**
   * Pour un champs de justification dans la confirmation
   */
  requiredFields() {
    if (this.params.idTag !== undefined && this.params.required && this.params.displayFormButton) {
      this.elements.popupConfirmButton.disabled = !this.getComponent().isValid;
    }
  }


  /**
   * Permet de vérifier si la class modal scroll body est présente
   * Et si tel est le cas, vérifier la hauteur disponible pour la modal
   */
  _manageBodySize() {
    this.elements.popupBody.style.maxHeight = `calc(100vh - ${
      this.elements.popupHeader.offsetHeight + this.elements.popupFooter.offsetHeight + 50 + 15 + 30
    }px)`;
  }


  /**
   * Fonction d'ouverture de la popup
   */
  open(elementToFocusOnClose) {
    if (!this.hidden) return;
    this._utils.addEventListener(window, 'focusout', this._callbacks.loseFocus);
    this._utils.addEventListener(window, 'keyup', this._callbacks.handleEscape);

    if(elementToFocusOnClose) {
      this.params.elementToFocusOnClose = elementToFocusOnClose;
    }
    
    this.removeAttribute('hidden');

    this._createEmptyFocusableDiv();

    // add focus sur content modal pour l'Accessibilité   
    this._focusFirstElement();
    
    this.classList.add('open');
    this.setAttribute('aria-hidden', false);
    
    document.body.classList.add('no-scroll');

    this._manageBodySize();

    if (this.resolvePopup) {
      this.resolvePopup(false);
    }

    this.resolvePopup = undefined;
    
    // Load data of component
    if (this.params.idTag !== undefined && this.params.loadComponentData !== undefined)
      this.getComponent()[this.params.loadComponentData]();
    
    this.requiredFields();

    if (this.intervalCheckingRecquired) {
      clearInterval(this.intervalCheckingRecquired);
    }

    this.intervalCheckingRecquired = setInterval(() => {
      this.requiredFields();
    }, 200);
    
    if (this.onLoadEvent) {
      this.onLoadEvent(this);
    }

    return new Promise((resolve) => {
      this.resolvePopup = resolve;
    });
  }

  _focusFirstElement(){
    const focusableElements = ['button', '[href]', 'input', 'select', 'textarea', '[tabindex]'];
    const notFocusableCases = ['[hidden]', '[tabindex="-1"]', '[disabled]', '[preventfocus]'];

    const focusableSelector = focusableElements.map(  
      focusableElement => focusableElement + notFocusableCases.map( 
        notFocusableCase => `:not(${notFocusableCase})` 
      ).join('') 
    ).join(',');

    const focusableNodes = this.querySelectorAll(focusableSelector);

    if(focusableNodes[0]){
      focusableNodes[0].focus();
    }
  }

  _createEmptyFocusableDiv(){
    this.elements.firstEmptyFocusableDiv = document.createElement('div');
    this.elements.lastEmptyFocusableDiv = document.createElement('div');
    this.elements.firstEmptyFocusableDiv.setAttribute('tabindex','0');
    this.elements.firstEmptyFocusableDiv.setAttribute('preventfocus','');
    this.elements.lastEmptyFocusableDiv.setAttribute('tabindex','0');
    this.elements.lastEmptyFocusableDiv.setAttribute('preventfocus','');
    this.insertBefore(this.elements.firstEmptyFocusableDiv, this.firstChild);
    this.appendChild(this.elements.lastEmptyFocusableDiv);
  }

  _removeEmptyFocusableDiv(){
    this.elements.firstEmptyFocusableDiv.remove();
    this.elements.lastEmptyFocusableDiv.remove();
  }

  _getUniqueId(){
    return (this.id ? this.id : '') + `${Math.floor(Math.random() * (999999 - 10000)) + 10000}`;
  }
}

customElements.define('forem-popup', ForemPopup);