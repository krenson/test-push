/*

Permet de gérer un élement visuel du type OfferSearch dans un Webcomponent.

Propriétés
-----------

data:
Permet de spécifier la liste des régions présentes.
Structure de l'array d'objet attendu :
[{
  code: code,
  label: label,
  childs: [{
      code: code,
      label: label,
      childs: [{
        ...
      }]
    }]
}]

hidden :
Permet de gérer la visibilité du composant.

disabled :
Permet de gérer si le composant est utilisable ou pas.

readonly :
Permet de gérer si le composant est en status readonly ou pas


Attributs
---------

disabled :
Permet de gérer si le composant est utilisable ou pas

offercount:
Permet de définir le nombre d'offre affiché dans le titre

hidetitle:
La présence de cet attribut permet de définir si le titre doit être masqué.

aidelink:
Permet de spécifier le path permettant d'atteindre la page d'aide de recherche.

arbolink:
Permet de spécifier le path permettant d'atteindre la page de recherche par arborescence.

advancedlink:
Permet de spécifier le path permettant d'atteindre la page de recherche avancée.

readonly :
Permet de passer le composant en readonly comme pour un input et un select classique.

Events
-------

onSearchClick:
Indique les informations de la recherche désirée:
detail: {
  values: Array des valeurs de critère de métier,
  regions: Array des valeurs de région sélectionnées,
  permissive: Indique si les critères ne doivent pas être utilisé cumulativement,
  options: Array des valeurs des différentes options custom (Voir la propriété value de offer-search-option pour en connaitre la structure)
}

displayTooltip:
Indique qu'un click sur l'icone de tooltip d'une option a été effectué et qu'une popup va apparaitre.
detail: Le texte du tooltip qui va être affiché


Methods 
-------

addSelectedRegion(region{key,value}): Permet d'ajouter une région à la liste des régions selectionnées
addSelectedJob(job): Permet d'ajouter un job à la liste des jobs


*/
// NOTE AEM > les imports suivant ne sont pas nécessaire pour cette version
// import '@le-forem-dsi/forem-inputautocomplete/input-autocomplete';
// import '@le-forem-dsi/forem-inputautocomplete/select-autocomplete';
// import '@le-forem-dsi/forem-tags/tags';

// NOTE AEM > l'import du popup est nécessaire, faire une référence vers votre dossier
// import '@le-forem-dsi/forem-popup/popup';
import Template from './offer-search.html';
import ForemOfferSearchUtils from './utils';
import './offer-search-option';

export default class ForemOfferSearch extends HTMLElement {
  static get observedAttributes() {
    return ['disabled', 'offercount', 'hidetitle', 'aidelink', 'arbolink', 'advancedlink', 'readonly', 'usenatifsinputs'];
  }

  constructor() {
    super();
    this.template = document.createElement('template');
    this.template.innerHTML = Template;
    this._params = {};
    this._params.options = Array.from(this.querySelectorAll('forem-offer-search-option'));
    this._utils = new ForemOfferSearchUtils();
  }

  connectedCallback() {
    this.bindDom();
    if (!this.elements) return;
    this._setupPopup();
    this.appendChild(this.template.content);
    this._manageUseNatifsInpts(this.hasAttribute('usenatifsinputs'));
    this._manageDisabled(this.hasAttribute('disabled'));
    this._manageTitleOfferNumber(this.getAttribute('offercount') || '');
    this._manageHideTitle(this.hasAttribute('hidetitle'));
    this._manageAideLink(this.getAttribute('aidelink' || ''));
    this._manageArboLink(this.getAttribute('arbolink' || ''));
    this._manageAdvancedLink(this.getAttribute('advancedlink' || ''));
    this._manageReadOnly(this.hasAttribute('readonly'));

    this._setupIds();
    this._setupOptions();

    this._utils.addEventListener(this.elements.searchButton, 'click', this._onSearchClick.bind(this));
    this._utils.addEventListener(this.elements.inputJob, 'onSearchChange', this._buildJobOption.bind(this));
    this._utils.addEventListener(this.elements.inputJob, 'onOptionSelectedEnd', this._onSelectJob.bind(this));
    this._utils.addEventListener(this.elements.tagsJob, 'onDeleteElement', this._onDeleteJob.bind(this));
    this._utils.addEventListener(this.elements.selectRegion, 'onOptionSelectedEnd', this._onSelectRegion.bind(this));
    this._utils.addEventListener(this.elements.tagsRegion, 'onDeleteElement', this._onDeleteRegion.bind(this));
    this._utils.addEventListener(this, 'displayTooltip', this._displayPopupTooltip.bind(this));
    this._utils.addEventListener(this.elements.inputJobNatifInfoBtn, 'click', this._displayPopupTooltipMetier.bind(this));
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
      case 'offercount':
        this._manageTitleOfferNumber(newValue, oldValue);
        break;
      case 'hidetitle':
        this._manageHideTitle(this.hasAttribute('hidetitle'));
        break;
      case 'aidelink':
        this._manageAideLink(newValue, oldValue);
        break;
      case 'arbolink':
        this._manageArboLink(newValue, oldValue);
        break;
      case 'advancedlink':
        this._manageAdvancedLink(newValue, oldValue);
        break;
      case 'readonly':
        this._manageReadOnly(this.hasAttribute('readonly'));
        break;
      case 'usenatifsinputs':
        this._manageUseNatifsInpts(this.hasAttribute('usenatifsinputs'));
        break;    
    }
  }

  bindDom() {
    this.elements = {};
    this.elements.title = this.template.content.querySelector('.refTitle');
    this.elements.offerNumber = this.template.content.querySelector('.refTitleOfferNumber');
    this.elements.inputLabel = this.template.content.querySelector('.refInputLabel');
    this.elements.helpLink = this.template.content.querySelector('.refHelpLink');
    this.elements.helpLinkContainer = this.template.content.querySelector('.refHelpLinkContainer');
    this.elements.inputJob = this.template.content.querySelector('.refInputJob');
    this.elements.inputJobNatif = this.template.content.querySelector('.refInputJobNatif');
    this.elements.inputJobNatifInfoBtn = this.template.content.querySelector('.refInputJobNatifInfo');
    this.elements.tagsJob = this.template.content.querySelector('.refJobTags');
    this.elements.labelRegion = this.template.content.querySelector('.refRegionLabel');
    this.elements.selectRegion = this.template.content.querySelector('.refRegionSelect');
    this.elements.selectRegionNatif = this.template.content.querySelector('.refRegionSelectNatif');
    this.elements.tagsRegion = this.template.content.querySelector('.refRegionTags');
    this.elements.searchButton = this.template.content.querySelector('.refSearchButton');
    this.elements.linkContainer = this.template.content.querySelector('.refLinkContainer');
    this.elements.arboLink = this.template.content.querySelector('.refArboLink');
    this.elements.advancedLink = this.template.content.querySelector('.refAdvancedLink');
    this.elements.partialJobCheckbox = this.template.content.querySelector('.refPartialJobCheckbox');
    this.elements.partialJobCheckboxLabel = this.template.content.querySelector('.refPartialJobCheckboxLabel');
    this.elements.partialJobLabel = this.template.content.querySelector('.refPartialJobLabel');
    this.elements.optionContainer = this.template.content.querySelector('.refOptionContainer');
    this.elements.refRechercherOffre = this.template.content.querySelector('.refRechercherOffre');
    this.elements.popup = this.template.content.querySelector('.refPopup');
    this.elements.popupContent = this.template.content.querySelector('.refPopupContent');
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

  /**
   * Permet de définir la liste des régions à afficher
   */
  set data(dataset) {
    if (this._params.dataset === dataset) return;
    this._params.dataset = dataset;
    this._buildRegions(dataset);
  }

  /**
   * Permet d'ajouter une région aux régions sélectionnées
   * @param {*} region Object {key, value}
   */
  addSelectedRegion(region){
    if(!region || !region.hasOwnProperty('key') || !region.hasOwnProperty('value')) return;
    this.elements.tagsRegion.hidden = false;
    const newRegion = {key: region.value, value: region.key};
    if (!this.elements.tagsRegion.value.some(item => item.key === newRegion.key))
      this.elements.tagsRegion.addElement(newRegion);
    this.elements.selectRegion.value = undefined;
    this.elements.selectRegion.classList.add('combobox-prevent-radius');
  }

  /**
  * Permet de définir si le composant est readonly ou pas
  */
  _manageReadOnly(isReadOnly) {
    if (this._params.readonly === isReadOnly) return;
    this._params.readonly = isReadOnly;

    this.elements.inputJob.toggleAttribute('readonly', isReadOnly);
    this.elements.tagsJob.toggleAttribute('readonly', isReadOnly);
    this.elements.selectRegion.toggleAttribute('readonly', isReadOnly);
    this.elements.tagsRegion.toggleAttribute('readonly', isReadOnly );
    this.elements.partialJobCheckbox.toggleAttribute('readonly', isReadOnly );
    this.elements.partialJobCheckboxLabel.classList.toggle('click-through', isReadOnly);
    this.elements.searchButton.toggleAttribute('disabled', isReadOnly || this._params.disabled);

    this._manageLinkContainerVisibility();

    this.elements.optionContainer.querySelectorAll('forem-offer-search-option').forEach(element => {
      element.toggleAttribute('readonly', isReadOnly);
    });    
  }

  /**
   * Permet de définir si le composant est utilisable ou pas
   */
  _manageDisabled(isDisabled) {
    if (this._params.disabled === isDisabled) return;
    this._params.disabled = isDisabled;

    this.elements.inputJob.toggleAttribute('disabled', isDisabled);
    this.elements.tagsJob.toggleAttribute('disabled', isDisabled);
    this.elements.selectRegion.toggleAttribute('disabled', isDisabled);
    this.elements.tagsRegion.toggleAttribute('disabled', isDisabled );
    this.elements.partialJobCheckbox.toggleAttribute('disabled', isDisabled );
    this.elements.searchButton.toggleAttribute('disabled', isDisabled || this._params.readonly);
    
    this._manageLinkContainerVisibility();

    this.elements.optionContainer.querySelectorAll('forem-offer-search-option').forEach(element => {
      element.toggleAttribute('disabled', isDisabled);
    });
  }

  /**
   * Permet de définir le nombre d'offres affiché dans le titre
   */
  _manageTitleOfferNumber(newValue, oldValue) {
    if (oldValue === newValue) return;
    this.elements.offerNumber.innerText = newValue;
  }

  /**
   * Permet de gérer l'affichage de la zone de titre
   */
  _manageHideTitle(newValue) {
    this._params.hideTitle = newValue;
    if (newValue) {
      this.elements.title.setAttribute('hidden', 'hidden');
    } else {
      this.elements.title.removeAttribute('hidden');
    }
  }

  _manageAideLink(newValue, oldValue) {
    if (newValue === oldValue) return;
    this._params.aideLink = newValue
    if(newValue){
      this.elements.helpLinkContainer.hidden = false;
      this.elements.helpLink.href = newValue;
    }else{
      this.elements.helpLinkContainer.hidden = true;
    }
  }

  _manageArboLink(newValue, oldValue) {
    if (newValue === oldValue) return;
    this._params.arboLink = newValue;
    this._manageLink(this.elements.arboLink, newValue, oldValue);
    this._manageLinkContainerVisibility();
  }

  _manageAdvancedLink(newValue, oldValue) {
    if (newValue === oldValue) return;
    this._params.advancedLink = newValue;
    this._manageLink(this.elements.advancedLink, newValue, oldValue);
    this._manageLinkContainerVisibility();
  }

  _manageLink(domElement, newValue) {
    this._params.link = newValue;
    if (newValue) {
      domElement.removeAttribute('hidden');
      domElement.href = newValue;
    } else {
      domElement.setAttribute('hidden', 'hidden');
    }
  }

  _manageLinkContainerVisibility() { 
    if((this._params.arboLink || this._params.advancedLink) ){
      this.elements.linkContainer.toggleAttribute('hidden', (this._params.disabled || this._params.readonly));
    }else{
      this.elements.linkContainer.toggleAttribute('hidden', (!this._params.arboLink && !this._params.advancedLink));
    }
  
    if(this._params.aideLink){
      this.elements.helpLinkContainer.toggleAttribute('hidden', this._params.disabled || this._params.readonly);  
    }
  }

  _manageUseNatifsInpts(useNatifsInputs){
    if(!useNatifsInputs == !this._params.useNatifsInputs) return;
    this._params.useNatifsInputs = useNatifsInputs;
    this.elements.selectRegion.hidden = true;
    this.elements.selectRegionNatif.hidden = false;
    this.elements.inputJob.hidden = true;
    this.elements.inputJobNatif.hidden = false;
  }

  /**
   * Transmet la volonté de faire une recherche d'offre sur base des infos sélectionnées
   */
  _onSearchClick(e) {
    e.preventDefault();
    const options = [];
    for (const option of this._params.options) {
      options.push(option.value);
    }
    const event = new CustomEvent('onSearchClick', {
      detail: {
        values: this._params.useNatifsInputs ? this.elements.inputJobNatif.value : this.elements.tagsJob.value.map(item => item.key),
        permissive: this.elements.partialJobCheckbox.checked,
        regions: this._params.useNatifsInputs ? this.elements.selectRegionNatif.value : this.elements.tagsRegion.value.map(item => item.value),
        options: options,
      },
    });
    this.dispatchEvent(event);
  }

  _setupPopup() {
    const idTag = this._utils.generateUniqueId('popupContent');
    this.elements.popupContent.setAttribute('id', idTag);
    this.elements.popup.setAttribute('idtag', idTag);
    this.elements.popup.initialContent = this.elements.popupContent.outerHTML;
  }

  _setupIds() {
    const newId = this._utils.generateUniqueId('offerSearch');
    if(this._params.useNatifsInputs)
      this.elements.inputJobNatif.setAttribute('id', newId);
    else
      this.elements.inputJob.setAttribute('id', newId);
    this.elements.inputLabel.setAttribute('for', newId);

    const partialCheckboxId = this._utils.generateUniqueId('partialJobCheckbox');
    this.elements.partialJobCheckbox.setAttribute('id', partialCheckboxId);
    this.elements.partialJobCheckboxLabel.setAttribute('for', partialCheckboxId);

    const regionId = this._utils.generateUniqueId('regionSelect');
    if(this._params.useNatifsInputs)
      this.elements.selectRegionNatif.setAttribute('id', regionId);
    else 
      this.elements.selectRegion.setAttribute('id', regionId);
    this.elements.labelRegion.setAttribute('for', regionId);
  }

  _buildJobOption(e) {
    if (e.detail) {
      this.elements.inputJob.data = [{
        value: e.detail,
        displayValue: `Ajouter ${e.detail}`,
      }]
    } else {
      this.elements.inputJob.data = [];
    }
  }

  addSelectedJob(job){
    this.elements.tagsJob.hidden = false;
    if (!this.elements.tagsJob.value.some(item => item.key === job))
      this.elements.tagsJob.addElement({key: job});
  }
  
  _onSelectJob(e) {
    this.addSelectedJob(e.detail)
    this.elements.inputJob.value = undefined;
    this.elements.inputJob.data = [];
  }

  _onDeleteJob(e) {
    this.elements.tagsJob.hidden = this.elements.tagsJob.value.length === 0;
  }

  /**
   * Construit l'ensemble des 'options' du composant select en fonction des regions reçus en paramètre
   * @param {*} data
   */
  _buildRegions(data) {
    if(this._params.useNatifsInputs){
      this.elements.selectRegionNatif.innerHTML = data;
    }else if(typeof data != 'string'){
      const buildedData = [];
      for (const region of data) {
        buildedData.push(this._buildRegionAndChilds(region));
      }
      this.elements.selectRegion.data = buildedData;
    }
  }

  _buildRegionAndChilds(data) {
    const option = {};
    option.key = data.code;
    option.value = data.label;

    if (data.childs) {
      option.childs = [];
      for (const child of data.childs) {
        option.childs.push(this._buildRegionAndChilds(child));
      }
    }
    return option;
  }

  _onSelectRegion(e) {
    this.addSelectedRegion({key: e.detail.key, value: e.detail.value});
  }

  _onDeleteRegion(e) {
    if(this.elements.tagsRegion.value.length === 0){
      this.elements.tagsRegion.hidden = true;
      this.elements.selectRegion.classList.remove('combobox-prevent-radius');
    }
  }

  _setupOptions() {
    for (const option of this._params.options) {
      this.elements.optionContainer.appendChild(option);
    }
  }

  _displayPopupTooltip(e) {
    if (!e.defaultPrevented) {
      this.elements.popup.getComponent().innerHTML = e.detail;
      this.elements.popup.setAttribute('label','<i class="fal fa-info-circle mr-1" aria-hidden="true"></i> Information');
      this.elements.popup.open();
    }
  }

  _displayPopupTooltipMetier(e){
    this.elements.popup.getComponent().innerHTML = this.elements.inputJobNatifInfoBtn.title;
    this.elements.popup.setAttribute('label','<i class="fal fa-info-circle mr-1" aria-hidden="true"></i> Le saviez-vous ?');
    this.elements.popup.open();
  }
}

customElements.define('forem-offer-search', ForemOfferSearch);