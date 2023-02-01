// function to make sure the tuile gets a tabindex of 0
function dynamicTab() {
  let tuiles = document.querySelectorAll(".tuileContainer");

  tuiles.forEach((tuile) => {
    if (tuile.tabIndex == 0) {
      observer.disconnect();
      return;
    }
    tuile.tabIndex = 0;
    tuile.querySelector("a").tabIndex = -1;
  });
}

let observer = new MutationObserver((mutations) => {
  mutations.forEach((mutation) => {
    let oldValue = mutation.oldValue;
    let newValue = mutation.target.textContent;
    if (oldValue !== newValue) {
      dynamicTab();
      console.log("test");
    }
  });
});

// observe body for changes
// once api call response 200
// add tabindex to tuiles
observer.observe(document.body, {
  characterDataOldValue: true,
  subtree: true,
  childList: true,
  characterData: true,
});
