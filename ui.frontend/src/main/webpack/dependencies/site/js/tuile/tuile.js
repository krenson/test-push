try {
  let tuiles = document.querySelectorAll(".tuileContainer");

  tuiles.forEach((tuile) => {
    tuile.tabIndex = 0;
    tuile.querySelector("a").tabIndex = -1;
  });
} catch (error) {}
