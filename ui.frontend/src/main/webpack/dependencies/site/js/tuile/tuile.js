try {
  // function to make sure the tuile gets a tabindex of 0
  function dynamicTab() {
    let tuiles = document.querySelectorAll(".tuileContainer");

    tuiles.forEach((tuile) => {
      tuile.addEventListener("keypress", (event) => {
        if (event.key === "Enter") {
          tuile.querySelector("a").click();
        }
      });

      tuile.tabIndex = 0;
      tuile.querySelector("a").tabIndex = -1;
    });
  }

  dynamicTab();
} catch (error) {}
