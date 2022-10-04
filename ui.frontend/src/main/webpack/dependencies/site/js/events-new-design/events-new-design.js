// JavaScript Document

var collapsibleBas = document.getElementsByClassName("collapsibleBas");
var collaps = document.querySelectorAll("#menuNav .collaps");
var menu = document.querySelector("#mobile");
var buttonPseudoSelect = document.querySelectorAll(".buttonPseudoSelect");

var searchBand = document.querySelector("#searchBand a");

var searchTopBar = document.getElementById("searchTop");
var inputSearch = document.getElementById("q");
var searchMobileIcone = document.getElementById("search-light-mobile");
var connectButton = document.getElementById("connectButton");
var warnings = document.getElementsByClassName("close-warning");
var mainBody = document.querySelector("body");
var mainNav = document.getElementById("mainNav");
var menuNav = document.getElementById("menuNav");
var menuOpen = false;
var menuConnectionOpen = false;
var searchDesktopOpen = false;
var searchMobileOpen = false;

//collapsibleBas

function init() {
  if (menuNav != null) {
    var unfocusable = menuNav.querySelectorAll("a , button");
    if (unfocusable != null) {
      unfocusable.forEach((element) => {
        element.setAttribute("tabindex", "-1");
      });
    }
    var unfocusableMenuConnectionLinks = document
      .getElementById("connectButton")
      .querySelectorAll("a , button");
    if (unfocusableMenuConnectionLinks != null) {
      unfocusableMenuConnectionLinks.forEach((element) => {
        element.setAttribute("tabindex", "-1");
      });
    }
    var unfocusableSearchDesktop = document.getElementById("q");
    if (unfocusableSearchDesktop != null) {
      unfocusableSearchDesktop.setAttribute("tabindex", "-1");
    }
    var focusableMenuConnection = document.getElementById("connectButton");
    if (focusableMenuConnection != null) {
      focusableMenuConnection.setAttribute("tabindex", "0");
    }
  }
}
init();

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

//SearchMobile
if (searchMobileIcone != null) {
  searchMobileIcone.addEventListener("click", OpenSearchMobile);
  searchMobileIcone.addEventListener("keypress", function (e) {
    if (e.key == "Enter") {
      OpenSearchMobile();
    }
  });
}

function OpenSearchMobile() {
  closeEverything("searchMobile");

  let menuMobile = document.getElementById("menuNav");
  if (searchMobile != null) {
    if (searchMobile.style.maxHeight) {
      searchMobileOpen = false;
      if (mainBody != null) {
        mainBody.classList.remove("searchOpenMobile");
      }
      menuMobile.classList.remove("searchOpen");
      searchMobile.style.maxHeight = null;
    } else {
      if (mainBody != null) {
        mainBody.classList.add("searchOpenMobile");
      }
      searchMobileOpen = true;
      menuMobile.classList.add("searchOpen");
      searchMobile.style.maxHeight = searchMobile.scrollHeight + "px";
      searchMobile.querySelector("input").focus();
    }
  }
}

//Recherche Top Bar
if (searchTopBar != null) {
  searchTopBar.addEventListener("click", function (e) {
    if (!searchTopBar.classList.contains("active")) {
      searchDesktopOpen = true;

      closeEverything("searchTopBar");
      if (connectButton != null) {
        connectButton.style.minWidth = "5rem";
      }
      searchTopBar.classList.add("active");

      searchTopBar.querySelector("input").focus();
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

//Key pressed checker for accessibility

window.addEventListener("keydown", function (e) {
  if (e.key == "Tab") {
    if (
      document.activeElement == this.document.getElementById("searchButton")
    ) {
      if (connectButton != null) {
        connectButton.style.minWidth = null;
      }
      searchTopBar.classList.remove("active");
    }
    if (
      document.getElementById("searchTopMobile") != null &&
      document.getElementById("searchTopMobile").style.display != "none"
    ) {
      document.getElementById("search-light-mobile").tabIndex = "0";
    } else {
      document.getElementById("search-light-mobile").tabIndex = "-1";
    }
  }

  if (e.key == "Escape" || e.key == "Esc") {
    if (menuOpen) {
      document.querySelector("header .partInf .leftPart a.menuLink").focus();
    } else if (menuConnectionOpen == true) {
      connectButton.focus();
    } else if (searchDesktopOpen == true) {
      document.getElementById("searchButton").focus();
    } else if (searchMobileOpen == true) {
      document.getElementById("search-light-mobile").focus();
    }

    closeEverything();
  }
});

//Connection menu
if (connectButton != null) {
  connectButton.addEventListener("click", clickMenuConnect, false);
  connectButton.addEventListener("keypress", function (e) {
    if (e.key == "Enter") {
      clickMenuConnect();
    }
  });
}

function clickMenuConnect() {
  closeEverything("connect");
  connectButton.classList.toggle("menuConnectOpen");
  let menuButtonConnection = document.getElementsByClassName(
    "menuButtonConnection"
  );
  menuButtonConnection = [...menuButtonConnection];
  menuButtonConnection.forEach((element) => {
    if (element.style.height) {
      menuConnectionOpen = false;
      element.style.height = null;
      element.style.bottom = null;
      var unfocusableMenuConnectionLinks = document
        .getElementById("connectButton")
        .querySelectorAll("a , button");
      if (unfocusableMenuConnectionLinks != null) {
        unfocusableMenuConnectionLinks.forEach((element) => {
          element.setAttribute("tabindex", "-1");
        });
      }
    } else {
      menuConnectionOpen = true;
      element.style.height = element.scrollHeight + "px";
      element.style.bottom = -element.scrollHeight + "px";
      var unfocusableMenuConnectionLinks = document
        .getElementById("connectButton")
        .querySelectorAll("a , button");
      if (unfocusableMenuConnectionLinks != null) {
        unfocusableMenuConnectionLinks.forEach((element) => {
          element.setAttribute("tabindex", "0");
        });
      }
    }
  });
}

//BurgerMenu
var initMenu = false;

if (menu != null) {
  var subMenu = mainNav.querySelectorAll("li");
  subMenu = [...subMenu];
  menu.addEventListener("click", function (e) {
    closeEverything("menu");

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
        menuOpen = true;
        if (subMenu.length > 0 && initMenu) {
          subMenu.forEach((element) => {
            if (!element.classList.contains("current")) {
              element.style.width = "";
            }
          });
        }
      } else if (initMenu) {
        menuOpen = false;
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
        var unfocusable = menuNav.querySelectorAll("a , button");
        if (unfocusable != null) {
          unfocusable.forEach((element) => {
            element.setAttribute("tabindex", "-1");
          });
        }
        var mainNavElements = mainNav.querySelectorAll("li:not(.current) a");

        if (mainNavElements != null) {
          mainNavElements.forEach((element) => {
            element.setAttribute("tabindex", "0");
          });
        }

        var menuHeaderElements = document.querySelectorAll(
          "header .partInf .icone-link a"
        );

        if (menuHeaderElements != null) {
          menuHeaderElements.forEach((element) => {
            element.setAttribute("tabindex", "0");
          });
        }
      }
    } else {
      if (menuNav != null) {
        menuNav.classList.add("open");

        var unfocusable = menuNav.querySelectorAll("a , button");
        if (unfocusable != null) {
          unfocusable.forEach((element) => {
            element.setAttribute("tabindex", "0");
          });
        }
        var menuExtraLinks = menuNav.getElementsByClassName("menuExtraLinks");
        if (menuExtraLinks.length != 0) {
          if (window.getComputedStyle(menuExtraLinks[0]).display == "none") {
            var unfocusableMenuExtraLinks =
              menuExtraLinks[0].querySelectorAll("a, button");
            if (unfocusableMenuExtraLinks != null) {
              unfocusableMenuExtraLinks.forEach((element) => {
                element.setAttribute("tabindex", "-1");
              });
            }
          }
        }
        var mainNavElements = mainNav.querySelectorAll("li:not(.current) a");

        if (mainNavElements != null) {
          mainNavElements.forEach((element) => {
            element.setAttribute("tabindex", "-1");
          });
        }
        var menuHeaderElements = document.querySelectorAll(
          "header .partInf .icone-link a"
        );

        if (menuHeaderElements != null) {
          menuHeaderElements.forEach((element) => {
            element.setAttribute("tabindex", "-1");
          });
        }
      }
    }
    this.classList.toggle("change");
  });
}

function closeEverything(target) {
  //closeMenu
  if (target != "menu") {
    menuOpen = false;
    var menuNav = document.querySelector("#menuNav");
    var mainNav = document.getElementById("mainNav");
    if (mainNav != null) {
      mainNav.classList.remove("menuOpen");
    }
    if (menuNav != null) {
      menuNav.classList.remove("open");
      var unfocusable = menuNav.querySelectorAll("a , button");
      if (unfocusable != null) {
        unfocusable.forEach((element) => {
          element.setAttribute("tabindex", "-1");
        });
      }
      var mainNavElements = mainNav.querySelectorAll("li:not(.current) a");

      if (mainNavElements != null) {
        mainNavElements.forEach((element) => {
          element.setAttribute("tabindex", "0");
        });
      }
      var menuHeaderElements = document.querySelectorAll(
        "header .partInf .icone-link a"
      );

      if (menuHeaderElements != null) {
        menuHeaderElements.forEach((element) => {
          element.setAttribute("tabindex", "0");
        });
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
  }

  //closeConnectionMenu
  if (target != "connect") {
    menuConnectionOpen = false;
    if (connectButton != null) {
      connectButton.classList.remove("menuConnectOpen");
      let menuButtonConnection = document.getElementsByClassName(
        "menuButtonConnection"
      );
      menuButtonConnection = [...menuButtonConnection];
      menuButtonConnection.forEach((element) => {
        element.style.height = null;
        element.style.bottom = null;
        var unfocusableMenuConnectionLinks = document
          .getElementById("connectButton")
          .querySelectorAll("a , button");
        if (unfocusableMenuConnectionLinks != null) {
          unfocusableMenuConnectionLinks.forEach((element) => {
            element.setAttribute("tabindex", "-1");
          });
        }
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

  if (target != "searchTopBar") {
    searchDesktopOpen = false;
    if (connectButton != null) {
      connectButton.style.minWidth = null;
    }
    searchTopBar.classList.remove("active");
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
