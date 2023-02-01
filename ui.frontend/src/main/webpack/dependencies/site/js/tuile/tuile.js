try {
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

  dynamicTab();
} catch (error) {}
