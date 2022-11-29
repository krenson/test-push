var popupOpener = document.getElementsByClassName("popupOpener");
if (popupOpener.length > 0) {
  [...popupOpener].forEach((element) => {
    element.addEventListener("click", function (e) {
      e.preventDefault();
      var popupId = element.children[0].getAttribute("popup-id");

      if (popupId != null && popupId != "") {
        var popup = document.querySelector(`.popup[popup-id="${popupId}"]`);
        if (popup != null) {
          popup.classList.add("open");
          document.querySelector("html").classList.add("openPopup");
          popup.scrollIntoView();
          document.body.appendChild(popup);
        }
      }
    });
  });
}

var popups = document.getElementsByClassName("popup aem");
if (popups != null) {
  [...popups].forEach((element) => {
    var closeCross = element.getElementsByClassName("popup-close");
    if (closeCross != null) {
      closeCross[0].addEventListener("click", function (e) {
        closePopup(element);
      });
    }

    var topCloseBtn = element.getElementsByClassName("popup-close-top-btn");
    topCloseBtn[0].addEventListener("keyup", function (event) {
      event.preventDefault();
      if (event.keyCode === 13) {
        closePopup(element);
      }
    });

    var closeButton = element.querySelectorAll(".popup-button.btn.closeButton");
    if (closeButton != null) {
      closeButton[0].addEventListener("click", function (e) {
        closePopup(element);
      });
    }

    var bottomCloseButton = element.getElementsByClassName(
      "popup-close-bottom-btn"
    );
    bottomCloseButton[0].addEventListener("keyup", function (event) {
      event.preventDefault();
      if (event.keyCode === 13) {
        closePopup(element);
      }
    });
  });
}

function closePopup(element) {
  document.querySelector("html").classList.remove("openPopup");
  element.classList.remove("open");
}
