// JavaScript Document
var slides = document.querySelectorAll(".slide");
var navigation = document.querySelector(".navSlider");
var left = document.getElementsByClassName("zoneFlecheG");
var right = document.getElementsByClassName("zoneFlecheD");

var currentPosition = 0;
var interval = 0;
var playing = true;
var waitingTime = 5000;

if (slides.length > 1) {
  addNavigation();
}

display();

if (slides.length > 1 && playing === true) {
  /*  start(); */
  pause();
}

if (slides.length == 1) {
  left[0].remove();
  right[0].remove();
}

var eventCLICKNavigation = function (e) {
  e.preventDefault();
  if (e.target.className == "controle") {
    var str = e.target.id;
    var position = str.replace("link-", "");

    currentPosition = position;

    display();

    paging();
  } else if (e.target.className != "navSlider") {
    if (playing === true) {
      pause();
    } else {
      /*  start(); */
      pause();
    }
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
      /*  playing === true ? pause() : start(); */
      pause();
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

document.addEventListener("keyup", eventKeyUp);

if (slides.length > 1) {
  navigation.addEventListener("click", eventCLICKNavigation);
}

for (var i = 0; i < left.length; i++) {
  left[i].addEventListener("click", eventCLICKLeft);
}
for (var i = 0; i < right.length; i++) {
  right[i].addEventListener("click", eventCLICKRight);
}

function display() {
  for (var index = 0; index < slides.length; index++) {
    if (index == currentPosition) {
      slides[index].classList.remove("element-invisible");
    } else {
      slides[index].classList.add("element-invisible");
    }
  }
}

function paging() {
  var elements = navigation.getElementsByTagName("li");

  for (var index = 0; index < elements.length; index++) {
    if (elements[index].className != "playPause") {
      if (index == currentPosition) {
        elements[index].classList.add("displayed");
      } else {
        elements[index].classList.remove("displayed");
      }
    }
  }
}

function addNavigation() {
  var mynav = document.querySelector(".containerNavSlider ul");
  var list = "";

  for (var index = 0; index < slides.length; index++) {
    if (index == currentPosition) {
      list +=
        '<li class="displayed"><a class="controle" id="link-' +
        index +
        '"  title="lien vers image ' +
        index +
        '" href="#" ><span>image suivante</span></a></li>';
    } else {
      list +=
        '<li><a class="controle" id="link-' +
        index +
        '"  title="lien vers image ' +
        index +
        '" href="#" ><span>image suivante</span></a></li>';
    }
  }

  if (playing === true) {
    list +=
      '<li class="playPause" role="button" tabindex="0"><a id="lecture" href="#" ><div class="pause">&nbsp;</div><span lang="en">pause</span></a></li>';
  } else {
    list +=
      '<li class="playPause" role="button" tabindex="0"><a id="lecture" href="#" ><div class="play"></div><span lang="en">play</span></a></li>';
  }

  mynav.insertAdjacentHTML("beforeend", list);
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

  reset();

  display();

  paging();
}

function prev() {
  if (currentPosition > slides.length - 1) {
    currentPosition = 0;
  } else if (currentPosition <= 0) {
    currentPosition = slides.length - 1;
  } else {
    currentPosition--;
  }

  reset();

  display();

  paging();
}

function reset() {
  playing = true;

  clearInterval(interval);
  /*  interval = setInterval(next, waitingTime ); */
}

var container = document.querySelector(".slider-container");
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
