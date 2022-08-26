// JavaScript Document
/* var arrowTop = document.querySelector(".arrowTop");
var collapsible = document.querySelectorAll("#myNav .collapsible"); */
var collapsibleBas = document.getElementsByClassName("collapsibleBas");
var collaps = document.querySelectorAll("#menuNav .collaps");
var menu = document.querySelector("#mobile");
var buttonPseudoSelect = document.querySelectorAll(".buttonPseudoSelect");
/* 
var formRechercheOffres = document.querySelector("#formRechercheOffres"); */
var searchBand = document.querySelector("#searchBand a");
var searchTopBar = document.getElementById("searchTop");
var inputSearch = document.getElementById("q");
var searchMobileIcone = document.getElementById("search-light-mobile");
var connectButton = document.getElementById("connectButton");
var warnings = document.getElementsByClassName("close-warning");
var mainBody = document.querySelector("body");

//menu
/* if (menu != null) {
  menu.addEventListener("click", function(e) {
    var mynav = document.querySelector("#myNav");
    var menuNav = document.querySelector("#menuNav");
    var searchBand = document.querySelector("#searchBand");
    var blocContact = document.querySelector("#blocContact");
    var blocLinkConnect = document.querySelector("#blocLinkConnect");
    var mainNav = document.getElementById("mainNav");
    if (mainNav != null) {
      mainNav.classList.toggle("menuOpen");
    }
    if (this.classList.contains("change") == true) {
      if (mynav != null) {
        mynav.style.width = "0%";
      }

      if (menuNav != null) {
        menuNav.classList.remove("open");
      }
    } else {
      if (menuNav != null) {
        menuNav.classList.add("open");
      }

      if (mynav != null) {
        mynav.style.width = "100%";
      }
      if (searchBand != null) {
        if (!searchBand.classList.contains("element-invisible")) {
          searchBand.classList.toggle("element-invisible");
        }
      }
      if (blocContact != null) {
        if (!blocContact.classList.contains("element-invisible")) {
          blocContact.classList.toggle("element-invisible");
        }
      }
      if (blocLinkConnect != null) {
        if (!blocLinkConnect.classList.contains("element-invisible")) {
          blocLinkConnect.classList.toggle("element-invisible");
        }
      }
    }

    this.classList.toggle("change");
  });
} */

//arrowTop
/* if (arrowTop != null) {
  var scrollTrigger = 100,
    backToTop = function() {
      var scrollTop = window.scrollY;

      if (scrollTop > scrollTrigger) {
        arrowTop.style.bottom = "10px";
      } else {
        arrowTop.style.bottom = "-60px";
      }
    };

  backToTop();

  window.addEventListener("scroll", function(e) {
    backToTop();
  });

  arrowTop.addEventListener("click", function(e) {
    e.preventDefault();
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
  });
} */

//collapsibleBas

if (collapsibleBas != null) {
  for (var i = 0; i < collapsibleBas.length; i++) {
    collapsibleBas[i].addEventListener("click", function (e) {
      this.classList.toggle("activeBas");
      collapsibleBas = [...collapsibleBas];
      index = collapsibleBas.indexOf(this);
      closeAllCollapsible(index);
      var sibling = this.nextElementSibling;

      if (sibling.style.maxHeight) {
        sibling.style.maxHeight = null;
      } else {
        sibling.style.maxHeight = sibling.scrollHeight + "px";
      }
    });
  }
}

function closeAllCollapsible(index) {
  for (var i = 0; i < collapsibleBas.length; i++) {
    if (i != index) {
      collapsibleBas[i].classList.remove("activeBas");
      var sibling = collapsibleBas[i].nextElementSibling;
      sibling.style.maxHeight = null;
    }
  }
}

//collapsible
/* if (collapsible != null) {
  for (var i = 0; i < collapsible.length; i++) {
    collapsible[i].addEventListener("click", function(e) {
      this.classList.toggle("active");
      var sibling = this.nextElementSibling;

      if (sibling.style.maxHeight) {
        sibling.style.maxHeight = null;
      } else {
        sibling.style.maxHeight = sibling.scrollHeight + "px";
      }
    });
  }
} */

if (collaps != null) {
  for (var i = 0; i < collaps.length; i++) {
    collaps[i].addEventListener("click", function (e) {
      collaps = [...collaps];
      index = collaps.indexOf(this);
      closeAllCollapse(index);
      this.classList.toggle("active");
      var sibling = this.nextElementSibling;

      if (sibling.style.maxHeight) {
        sibling.style.maxHeight = null;
      } else {
        sibling.style.maxHeight = sibling.scrollHeight + "px";
      }
    });
  }
}

function closeAllCollapse(index) {
  for (var i = 0; i < collaps.length; i++) {
    if (i != index) {
      collaps[i].classList.remove("active");
      var sibling = collaps[i].nextElementSibling;
      sibling.style.maxHeight = null;
    }
  }
}

//buttonPseudoSelect
if (buttonPseudoSelect != null) {
  for (var i = 0; i < buttonPseudoSelect.length; i++) {
    buttonPseudoSelect[i].addEventListener("click", function (e) {
      e.preventDefault();

      var sibling = this.nextElementSibling;
      this.parentElement.classList.toggle("element-invisible");
      if (this.parentElement.classList.contains("element-invisible")) {
        sibling.style.height = "0px";
      } else {
        sibling.style.height = sibling.scrollHeight + "px";
      }
    });
  }
}

//formRechercheOffres
/* if (formRechercheOffres != null) {
  formRechercheOffres.addEventListener("submit", function(e) {
    var url = this.getAttribute("action") + "#searchurl-results/1?";
    var regionTerm = document.getElementById("lieu_trav").value;
    var queryTerm = document.getElementById("query").value;

    url = url + "query=" + encodeURIComponent(queryTerm);
    url = url + "&lieu_trav=" + encodeURIComponent(regionTerm);

    e.preventDefault();

    window.open(url, "_self");
  });
} */

//searchBand
/* if (searchBand != null) {
  searchBand.addEventListener("click", function(e) {
    e.preventDefault();
    searchBand.classList.toggle("element-invisible");
  });
} */

/* document.addEventListener("DOMContentLoaded", function(e) {
  document.querySelectorAll("table").forEach(function(table) {
    var x = table.querySelectorAll("tbody tr th").length;
    if (x != 0) return;
    table.classList.add("table-responsive");
    let labels = Array.from(table.querySelectorAll("th")).map(function(th) {
      return th.innerText;
    });

    table.querySelectorAll("td").forEach(function(td, i) {
      td.setAttribute("data-label", labels[i % labels.length]);
    });
  });
}); */

//SearchMobile
if (searchMobileIcone != null) {
  searchMobileIcone.addEventListener("click", function (e) {
    closeEverything("searchMobile");

    let menuMobile = document.getElementById("menuNav");
    if (searchMobile != null) {
      if (searchMobile.style.maxHeight) {
        if (mainBody != null) {
          mainBody.classList.remove("searchOpenMobile");
        }
        menuMobile.classList.remove("searchOpen");
        searchMobile.style.maxHeight = null;
      } else {
        if (mainBody != null) {
          mainBody.classList.add("searchOpenMobile");
        }
        menuMobile.classList.add("searchOpen");
        searchMobile.style.maxHeight = searchMobile.scrollHeight + "px";
      }
    }
  });
}

//Recherche Top Bar
if (searchTopBar != null) {
  searchTopBar.addEventListener("click", function (e) {
    if (!searchTopBar.classList.contains("active")) {
      closeEverything("searchTopBar");
      if (connectButton != null) {
        connectButton.style.minWidth = "5rem";
      }
      searchTopBar.classList.add("active");

      searchTopBar.querySelector("input").focus();
      isSearchOpen = true;
    }
  });
}

if (inputSearch != null) {
  inputSearch.addEventListener("focusout", function (e) {
    let butt = document.getElementById("searchButton");

    if (butt != e.relatedTarget) {
      if (connectButton != null) {
        connectButton.style.minWidth = null;
      }
      searchTopBar.classList.remove("active");
    }
  });
}

if (connectButton != null) {
  connectButton.addEventListener("click", function (e) {
    closeEverything("connect");
    connectButton.classList.toggle("menuConnectOpen");
    let menuButtonConnection = document.getElementsByClassName(
      "menuButtonConnection"
    );
    menuButtonConnection = [...menuButtonConnection];
    menuButtonConnection.forEach((element) => {
      if (element.style.height) {
        element.style.height = null;
        element.style.bottom = null;
      } else {
        element.style.height = element.scrollHeight + "px";
        element.style.bottom = -element.scrollHeight + "px";
      }
    });
  });
}

//BurgerMenu
var initMenu = false;

if (menu != null) {
  var subMenu = mainNav.querySelectorAll("li");
  subMenu = [...subMenu];
  menu.addEventListener("click", function (e) {
    closeEverything("menu");
    var menuNav = document.querySelector("#menuNav");
    var mainNav = document.getElementById("mainNav");
    if (!initMenu && mainNav != null && subMenu.length > 0) {
      subMenu.forEach((element) => {
        if (!element.classList.contains("current")) {
          if (window.getComputedStyle(element, null).display != "none") {
            element.setAttribute(
              "width-save",
              element.querySelector("a").scrollWidth
            );
            element.style.width = element.getAttribute("width-save") + "px";
            initMenu = true;
          }
        }
      });
    }

    if (mainNav != null) {
      if (!mainNav.classList.contains("menuOpen")) {
        if (subMenu.length > 0 && initMenu) {
          subMenu.forEach((element) => {
            if (!element.classList.contains("current")) {
              element.style.width = "";
            }
          });
        }
      } else if (initMenu) {
        subMenu.forEach((element) => {
          if (!element.classList.contains("current")) {
            element.style.width = element.style.width =
              element.getAttribute("width-save") + "px";
          }
        });
      }
      mainNav.classList.toggle("menuOpen");
    }
    if (this.classList.contains("change") == true) {
      if (menuNav != null) {
        menuNav.classList.remove("open");
      }
    } else {
      if (menuNav != null) {
        menuNav.classList.add("open");
      }
    }
    this.classList.toggle("change");
  });
}

function closeEverything(target) {
  //closeMenu
  if (target != "menu") {
    var menuNav = document.querySelector("#menuNav");
    var mainNav = document.getElementById("mainNav");
    if (mainNav != null) {
      mainNav.classList.remove("menuOpen");
    }
    if (menuNav != null) {
      menuNav.classList.remove("open");
    }
    if (menu != null) {
      menu.classList.remove("change");
    }

    if (subMenu.length > 0 && initMenu) {
      subMenu.forEach((element) => {
        if (!element.classList.contains("current")) {
          element.style.width = element.getAttribute("width-save") + "px";
        }
      });
    }
  }

  //closeConnectionMenu
  if (target != "connect") {
    if (connectButton != null) {
      connectButton.classList.remove("menuConnectOpen");
      let menuButtonConnection = document.getElementsByClassName(
        "menuButtonConnection"
      );
      menuButtonConnection = [...menuButtonConnection];
      menuButtonConnection.forEach((element) => {
        element.style.height = null;
        element.style.bottom = null;
      });
    }
  }

  //closeSearchMobile
  if (target != "searchMobile") {
    if (searchMobileIcone != null) {
      let menuMobile = document.getElementById("menuNav");
      if (searchMobile != null) {
        if (mainBody != null) {
          mainBody.classList.remove("searchOpenMobile");
        }
        menuMobile.classList.remove("searchOpen");
        searchMobile.style.maxHeight = null;
      }
    }
  }

  //closeAllCollapse
  closeAllCollapse(-1);
}

//Warnings
warnings = [...warnings];

if (warnings.length > 0) {
  warnings.forEach((warning) => {
    warning.addEventListener("click", function (e) {
      this.parentElement.style.height = this.parentElement.scrollHeight + "px";
      setTimeout(() => {
        this.parentElement.classList.add("close-warning");
        this.parentElement.style.height = 0;
      }, 1);
    });
  });
}

//Cobrowsing
/*
var footer = document.querySelector("footer.aem");

if (footer != null) {
  var emplacement = footer.querySelector(".mentionsLegales ul");
  var coBrowsingLink = document.createElement("li");
  coBrowsingLink.classList.add("cobrowsing");
  coBrowsingLink.innerHTML = `<a href="" >Cobrowsing</a><div class="cobrowsing-container"></div>`;

  coBrowsingLink.querySelector("a").addEventListener("click", function (e) {
    e.preventDefault();
    openCowbrowsing(e, this);
  });

  emplacement.append(coBrowsingLink);
}

function openCowbrowsing(events, element) {
  var popup = element.parentElement.querySelector(".cobrowsing-container");
  if (popup != null) {
    if (!popup.classList.contains("open")) {
      fillcobrowsing(cobrowsingNum);
    }
    popup.classList.toggle("open");
  }
}

var cobrowsingNum = null;

function fillcobrowsing(num) {
  var cowBrowsingContainer = document.querySelector(".cobrowsing-container");
  if (num == null && cobrowsingNum == null) {
    var popupText = `<div class="close-popup"></div><p>Si vous êtes actuellement en contact téléphonique ou chat avec un opérateur de notre centre de contact, vous pouvez cliquer ci-dessous pour obtenir un code de partage.</p><button class="btn btn-primary">Enregistrer</button>`;
    cowBrowsingContainer.innerHTML = popupText;
    var buttonCoBrowsing = cowBrowsingContainer.querySelector("button.btn");
    buttonCoBrowsing.addEventListener("click", function (e) {
      fillcobrowsing("542 105 920");
    });
  } else {
    cobrowsingNum = num;
    var popupText = `<div class="close-popup"></div><p class="bold">Partage du numéro de session</p><p> Afin d'activer le partage de votre écran de votre espace personnel Le Forem, veuillez, s'il vous plaît, communiquer le numéro de session au conseiller avec qui vous êtes en relation.</p><p class="bold"> Numéro de session : ${cobrowsingNum} </p>`;
    cowBrowsingContainer.innerHTML = popupText;
  }

  var closeCobrowsing = cowBrowsingContainer.querySelector(".close-popup");

  closeCobrowsing.addEventListener("click", function (e) {
    e.preventDefault();

    var popup = coBrowsingLink.querySelector(".cobrowsing-container");
    popup.classList.remove("open");
  });
}
*/
//SEARCH GOOGLE

var resultsElement = document.getElementById("results");
var initSearch = false;
if (resultsElement != null) {
  var MutationObserver =
    window.MutationObserver || window.WebKitMutationObserver;
  new MutationObserver(function (mutations) {
    for (var i = 0; i < mutations.length; ++i) {
      if (mutations[i].target.classList.contains("gsc-results")) {
        if (!initSearch) {
          buttonToggle();
          initSearch = true;
        }
        googleRedesign();
        break;
      }
      if (mutations[i].target.classList.contains("gsc-control-cse")) {
        googleSearchBar();
      }
    }
  }).observe(resultsElement, {
    subtree: true,
    attributes: false,
    childList: true,
    characterData: false,
  });
}

function googleRedesign() {
  var googleSearch = document.querySelector(
    ".gsc-result-info-container .gsc-result-info"
  );
  if (googleSearch != null) {
    var headerInject = document.querySelector(".aem.title-container");
    if (document.querySelector(".accroche-container") == null) {
      var inject = document.createElement("div");
      inject.classList.add("aem");
      inject.classList.add("accroche-container");
      var para = document.createElement("p");
      para.classList.add("accroche");
      inject.append(para);
      para.innerHTML = googleSearch.innerHTML;

      if (headerInject != null) {
        headerInject.after(inject);
      }
    } else {
      var accroche = document.querySelector(".accroche-container .accroche");
      accroche.innerHTML = googleSearch.innerHTML;
    }
  }
}
function buttonToggle() {
  const btnToggle = document.querySelector(".gsc-option-menu-invisible");
  const parentToggle = document.querySelector(".gsc-option-menu-container");
  var observer = new MutationObserver(function (event) {
    if (event[0].target.classList.contains("gsc-option-menu-invisible")) {
      parentToggle.classList.remove("menuOpen");
      btnToggle.style.maxHeight = 0 + "px";
    } else {
      parentToggle.classList.add("menuOpen");
      btnToggle.style.top = "100%";
      btnToggle.style.maxHeight = btnToggle.scrollHeight + "px";
    }
  });
  observer.observe(btnToggle, {
    attributes: true,
    attributeFilter: ["class"],
    childList: false,
    characterData: false,
    attributeOldValue: true,
  });
}

function googleSearchBar() {
  var searchformInput = document.querySelector(
    "form.gsc-search-box.gsc-search-box-tools"
  );
  if (searchformInput != null) {
    var inputSearchContainer = document.createElement("div");
    inputSearchContainer.classList.add("aem");
    inputSearchContainer.classList.add("search-google-header");
    inputSearchContainer.append(searchformInput);

    var headerInject = document.querySelector(".aem.title-container");
    headerInject.after(inputSearchContainer);
  }
}
