// JavaScript Document
var slides = document.querySelectorAll(".slide");
var navigation = document.querySelector(".navSlider");
var left = document.querySelector(".zoneFlecheG");
var right = document.querySelector(".zoneFlecheD");
var contenair = document.querySelector(".testimonyContainer");

var currentPosition = 0;

var interval = 0;
var playing = true;
var waitingTime = 8000;

if (slides && slides.length != 0) {
  slides.forEach((element, index) => {
    element.setAttribute("index-slide", index);
  });
}

if (slides.length != 0) {
  addNavigation();
}

if (currentPosition === null && navigation != "null") {
  navigation.firstElementChild.classList.add("displayed");
}

if (slides.length > 0 && slides.length <= 3) {
  pause();
} else {
  if (slides.length > 0 && playing === true) {
    pause();
  }
}

var eventCLICKNavigation = function (e) {
  e.preventDefault();
  if (e.target.className == "pause") {
    pause();
  } else if (e.target.className == "play") {
    start();
  } else {
    currentPosition = e.target.getAttribute("data-index");
    bullet();
  }
};

var eventCLICKLeft = function (e) {
  prev();
};

var eventCLICKRight = function (e) {
  next();
};

var eventKeyUp = function (e) {
  switch (e.keyCode) {
    case 13:
      e.preventDefault();
      playing === true ? pause() : start();
      break;
    case 37:
      e.preventDefault();
      prev();
      break;
    case 39:
      e.preventDefault();
      next();
      break;
  }
};

if (slides.length != 0) {
  document.addEventListener("keyup", eventKeyUp);
  navigation.addEventListener("click", eventCLICKNavigation);
  left.addEventListener("click", eventCLICKLeft);
  right.addEventListener("click", eventCLICKRight);

  contenair.addEventListener("swiped-left", function (e) {
    prev();
  });

  contenair.addEventListener("swiped-right", function (e) {
    next();
  });
}

function start() {
  playing = true;

  var playPause = document.querySelector(".playPause div");
  var lecture = document.querySelector("#lecture");
  var span = document.querySelector("#lecture span");

  playPause.classList.remove("play");
  playPause.classList.add("pause");
  lecture.removeChild(span);
  lecture.insertAdjacentHTML("beforeend", '<span lang="en">pause</span>');

  interval = setInterval(next, waitingTime);
}

function pause() {
  playing = false;

  var playPause = document.querySelector(".playPause div");
  var lecture = document.querySelector("#lecture");
  var span = document.querySelector("#lecture span");

  playPause.classList.remove("pause");
  playPause.classList.add("play");
  lecture.removeChild(span);
  lecture.insertAdjacentHTML("beforeend", '<span lang="en">play</span>');

  clearInterval(interval);
}

function next() {
  if (currentPosition >= slides.length - 1) {
    currentPosition = 0;
  } else if (currentPosition < 0) {
    currentPosition = slides.length - 1;
  } else {
    currentPosition++;
  }

  moveL();
}

function prev() {
  if (currentPosition > slides.length - 1) {
    currentPosition = 0;
  } else if (currentPosition <= 0) {
    currentPosition = slides.length - 1;
  } else {
    currentPosition--;
  }

  moveR();
}

function addNavigation() {
  var list = "";
  var position = null;

  if (slides.length <= 3) {
    navigation.remove();
    left.remove();
    right.remove();

    return;
  }

  for (var index = 0; index < slides.length; index++) {
    if (index === currentPosition) {
      list +=
        '<li class="bullet displayed"><a data-index="' +
        index +
        '" title="lien vers tï¿½moignage ' +
        index +
        '" href="#" ><span>image suivante</span></a></li>';
    } else {
      list +=
        '<li class="bullet"><a data-index="' +
        index +
        '" title="lien vers tï¿½moignage ' +
        index +
        '" href="#" ><span>image suivante</span></a></li>';
    }
  }

  if (playing === true) {
    list +=
      '<li class="playPause" role="button" tabindex="0"><a id="lecture" href="#" ><div class="pause"> </div><span lang="en">pause</span></a></li>';
  } else {
    list +=
      '<li class="playPause" role="button" tabindex="0"><a id="lecture" href="#" ><div class="play"></div><span lang="en">play</span></a></li>';
  }

  navigation.insertAdjacentHTML("beforeend", list);
}

function display() {
  for (var i = 0; i < navigation.children.length; i++) {
    if (navigation.children[i].classList.contains("displayed")) {
      navigation.children[i].classList.toggle("displayed");
    }
  }

  navigation.children[currentPosition].classList.toggle("displayed");
}

function moveR() {
  var node = contenair.lastElementChild;
  contenair.prepend(node);

  display();
}

function moveL() {
  var node = contenair.firstElementChild;
  contenair.append(node);

  display();
}

function bullet() {
  var j = currentPosition;
  for (var i = slides.length - 1; i >= 0; i--) {
    j--;
    if (j < 0) {
      j = slides.length - 1;
    }
    var slideToAdd = document.querySelector("[index-slide='" + j + "']");
    contenair.prepend(slideToAdd);
  }

  for (var i = 0; i < navigation.children.length; i++) {
    if (navigation.children[i].classList.contains("displayed")) {
      navigation.children[i].classList.toggle("displayed");
    }
  }

  navigation.children[currentPosition].classList.toggle("displayed");
}

var container = document.querySelector(".testimonyContainer");
if (container != null) {
  container.addEventListener("touchstart", startTouch, false);
  container.addEventListener("touchmove", moveTouch, false);

  // Swipe Up / Down / Left / Right
  var initialX = null;
  var initialY = null;

  function startTouch(e) {
    initialX = e.touches[0].clientX;
    initialY = e.touches[0].clientY;
  }

  function moveTouch(e) {
    if (initialX === null) {
      return;
    }

    if (initialY === null) {
      return;
    }

    var currentX = e.touches[0].clientX;
    var currentY = e.touches[0].clientY;

    var diffX = initialX - currentX;
    var diffY = initialY - currentY;

    if (Math.abs(diffX) > Math.abs(diffY)) {
      // sliding horizontally
      if (diffX > 0) {
        e.preventDefault();
        next();
      } else {
        e.preventDefault();
        prev();
      }
    }

    initialX = null;
    initialY = null;
  }
}
