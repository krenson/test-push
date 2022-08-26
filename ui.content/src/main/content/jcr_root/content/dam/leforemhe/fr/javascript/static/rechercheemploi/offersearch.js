var searchEmploi = document.querySelector(".fcs");
var initSearch = false;

if (searchEmploi != null) {
  var MutationObserver =
    window.MutationObserver || window.WebKitMutationObserver;
  new MutationObserver(function (mutations) {
    /* console.log(mutations); */
    for (var i = 0; i < mutations.length; ++i) {
      if (mutations[i].target.nodeName == "FOREM-OFFER-SEARCH") {
        if (!initSearch) {
          initSearchEmploi();
          initSearch = true;
        }
      }
    }
  }).observe(searchEmploi, {
    subtree: true,
    attributes: true,
    childList: true,
    characterData: false,
  });
}

function initSearchEmploi() {
  var resultsElement = document.querySelector("forem-offer-search");
  if (resultsElement != null) {
    resultsElement.data = ` <option selected="selected" value="">
Toutes les régions
</option>
<option value="WAL">
Région wallonne
</option>
<option value="BE-WBR">
&nbsp;&nbsp;&nbsp;Brabant wallon
</option>
<option value="NIV">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nivelles
</option>
<option value="BE-WHT">
&nbsp;&nbsp;&nbsp;Hainaut
</option>
<option value="ATH">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ath
</option>
<option value="CHA">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Charleroi
</option>
<option value="MON">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mons
</option>
<option value="MOU">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Mouscron
</option>
<option value="SOI">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Soignies
</option>
<option value="THU">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Thuin
</option>
<option value="TOU">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tournai
</option>
<option value="BE-WLG">
&nbsp;&nbsp;&nbsp;Liège
</option>
<option value="HUY">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Huy
</option>
<option value="LIE">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Liège
</option>
<option value="VER">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Verviers
</option>
<option value="WAR">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Waremme
</option>
<option value="BE-WLX">
&nbsp;&nbsp;&nbsp;Luxembourg
</option>
<option value="ARL">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Arlon
</option>
<option value="BAS">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bastogne
</option>
<option value="MAR">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Marche-en-Famenne
</option>
<option value="NEU">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Neufchateau
</option>
<option value="VIR">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Virton
</option>
<option value="BE-WNA">
&nbsp;&nbsp;&nbsp;Namur
</option>
<option value="DIN">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dinant
</option>
<option value="NAM">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Namur
</option>
<option value="PHI">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Philippeville
</option>
<option value="BRU">
Région de Bruxelles-Capitale
</option>
<option value="VLG">
Région flamande
</option>
<option value="BE-VAN">
&nbsp;&nbsp;&nbsp;Anvers
</option>
<option value="ANT">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Anvers
</option>
<option value="MEC">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Malines
</option>
<option value="TUR">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Turnhout
</option>
<option value="BE-VBR">
&nbsp;&nbsp;&nbsp;Brabant flamand
</option>
<option value="HAL">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Halle-Vilvoorde
</option>
<option value="LEU">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Louvain
</option>
<option value="BE-VWV">
&nbsp;&nbsp;&nbsp;Flandre Occidentale
</option>
<option value="BRG">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Bruges
</option>
<option value="KOR">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Courtrai
</option>
<option value="DIK">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Dixmude,
</option>
<option value="VEU">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Furnes
</option>
<option value="OOS">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ostende
</option>
<option value="ROE">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Roulers
</option>
<option value="TIE">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Thielt
</option>
<option value="IEP">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ypres
</option>
<option value="BE-VOV">
&nbsp;&nbsp;&nbsp;Flandre Orientale
</option>
<option value="AAL">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Alost
</option>
<option value="OUD">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Audenaerde
</option>
<option value="EEK">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Eeklo
</option>
<option value="GEN">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gand
</option>
<option value="SIN">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Saint-Nicolas
</option>
<option value="DEN">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Termonde
</option>
<option value="BE-VLI">
&nbsp;&nbsp;&nbsp;Limbourg
</option>
<option value="HAS">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Hasselt
</option>
<option value="MAA">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Maaseik
</option>
<option value="TON">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tongres
</option>`;
    var button = document.querySelector(".rechercher-offre-form-submit-btn");
    button.addEventListener("click", function (e) {
      var url = resultsElement.getAttribute("url");

      form = resultsElement.querySelector("form");
      var ope = "";

      if (
        resultsElement.querySelector(".refPartialJobCheckbox.form-check-input")
          .checked == true
      ) {
        ope = "OU";
      } else {
        ope = "ET";
      }
      url +=
        "#searchurl-results/1?query=" +
        resultsElement.querySelector(".input-group input").value +
        "&lieu_trav=" +
        resultsElement.querySelector("select.refRegionSelectNatif").value +
        "&operateur=" +
        ope;

      window.open(url, "_self");
    });
  }
  var mainContainer = document.getElementById("exempleOfferSearch");
  var jobNumber = mainContainer.querySelector(".rechercher-offre-title-number");
  var link = mainContainer.getAttribute("data-link");

  const fetchJobCount = (link) => {
    const data = fetch(link)
      .then((resp) => resp.json())
      .then((res) => {
        if (res.OfferCount && res.OfferCount.numberOfOffers) {
          return res.OfferCount.numberOfOffers;
        } else {
          return 0;
        }
      })
      .catch(() => {
        return 0;
      });
    return data;
  };
  fetchJobCount(link).then((data) => {
    if (data != null) {
      jobNumber.innerHTML = data;
    }
  });
}
