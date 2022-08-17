var isMobile = {
  Android: function () {
    return navigator.userAgent.match(/Android/i);
  },
  BlackBerry: function () {
    return navigator.userAgent.match(/BlackBerry/i);
  },
  iOS: function () {
    return navigator.userAgent.match(/iPhone|iPad|iPod/i);
  },
  Opera: function () {
    return navigator.userAgent.match(/Opera Mini/i);
  },
  Windows: function () {
    return navigator.userAgent.match(/IEMobile/i);
  },
  any: function () {
    return (
      isMobile.Android() ||
      isMobile.BlackBerry() ||
      isMobile.iOS() ||
      isMobile.Opera() ||
      isMobile.Windows()
    );
  },
};

var tabs = document.querySelectorAll(".cmp-tabs");

if (tabs != null) {
  tabs.forEach((element) => {
    if (isMobile.any()) {
      element.classList.add("mobile");
    } else {
      element.classList.add("desktop");
    }
    var ol = element.querySelector(".cmp-tabs__tablist");
    var stylingList = element.querySelector(".cmp-tabs-scrollable");
    var scrollBar = element.querySelector(".tabscrollBar");
    var scrollgizmo = element.querySelector(".scrollGizmo");
    if (
      stylingList != null &&
      scrollgizmo != null &&
      ol != null &&
      scrollBar != null
    ) {
      var scrollLength = (stylingList.offsetWidth * 100) / ol.offsetWidth;
      scrollgizmo.style.width =
        (stylingList.offsetWidth * scrollLength) / 100 + "px";
      stylingList.addEventListener("wheel", function (e) {
        e.preventDefault();
        stylingList.scrollLeft = stylingList.scrollLeft + e.deltaY / 2;
        var offsetPourcentage =
          (stylingList.scrollLeft * 100) /
          (ol.offsetWidth - stylingList.offsetWidth);

        scrollgizmo.style.left =
          (stylingList.offsetWidth * offsetPourcentage) / 100 -
          (scrollgizmo.offsetWidth * offsetPourcentage) / 100 +
          "px";
      });
      stylingList.addEventListener("mouseover", function (e) {
        if (stylingList.offsetWidth < ol.offsetWidth) {
          scrollBar.classList.add("hover");
        }
      });
      stylingList.addEventListener("mouseout", function (e) {
        scrollBar.classList.remove("hover");
      });
      scrollBar.addEventListener("mouseover", function (e) {
        if (stylingList.offsetWidth < ol.offsetWidth) {
          scrollBar.classList.add("hover");
        }
      });
      scrollBar.addEventListener("mouseout", function (e) {
        if (!moving) {
          scrollBar.classList.remove("hover");
        }
      });
      var moving = false;
      scrollBar.addEventListener("mousedown", function (e) {
        moving = true;
        scrollBar.classList.add("grabbing");
        scrollmove(e, scrollgizmo, stylingList, ol);
      });
      document.addEventListener("mouseup", function (e) {
        moving = false;
        scrollBar.classList.remove("grabbing");
        scrollBar.classList.remove("hover");
      });
      document.addEventListener("mousemove", function (e) {
        if (moving) {
          e.preventDefault();
          scrollmove(e, scrollgizmo, stylingList, ol);
        }
      });
    }
  });
}

function scrollmove(e, scrollgizmo, stylingList, ol) {
  if (e.pageX - scrollgizmo.offsetWidth / 2 < 0) {
    scrollgizmo.style.left = 0 + "px";
  } else if (
    e.pageX - scrollgizmo.offsetWidth / 2 >
    stylingList.offsetWidth - scrollgizmo.offsetWidth
  ) {
    scrollgizmo.style.left =
      stylingList.offsetWidth - scrollgizmo.offsetWidth + "px";
  } else {
    scrollgizmo.style.left = e.pageX - scrollgizmo.offsetWidth / 2 + "px";
  }

  var offsetPourcentage =
    (parseInt(scrollgizmo.style.left) * 100) /
    (stylingList.offsetWidth - scrollgizmo.offsetWidth);

  stylingList.scrollLeft =
    (ol.offsetWidth * offsetPourcentage) / 100 -
    (stylingList.offsetWidth * offsetPourcentage) / 100;
}

window.addEventListener("resize", function (e) {
  if (tabs != null) {
    tabs.forEach((element) => {
      var ol = element.querySelector(".cmp-tabs__tablist");
      var stylingList = element.querySelector(".cmp-tabs-scrollable");
      var scrollgizmo = element.querySelector(".scrollGizmo");
      if (stylingList != null && scrollgizmo != null && ol != null) {
        var scrollLength = (stylingList.offsetWidth * 100) / ol.offsetWidth;
        scrollgizmo.style.width =
          (stylingList.offsetWidth * scrollLength) / 100 + "px";
      }
    });
  }
});
