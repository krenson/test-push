let resultcontainer = document.querySelector(".result-counter ");
let resultLabel = document.querySelector(".result-counter h5");

let searchContainer = document.querySelector(".searchContainer");

let titleContainer = document.querySelector(".aem.title-container");
let title = document.querySelector(".aem.title-container h1");
let subtitle = document.querySelector(".aem.title-container h5.subTitle");

document
  .querySelector(".aem.searchBtn button")
  .addEventListener("click", () => {
    if (window.screen.width <= 1200) {
      searchContainer.style.display = "none";
      filAriane.style.display = "block";
      titleContainer.style.alignItems = "start";

      title.innerText = "Résultats";

      subtitle.innerText =
        resultLabel.innerText.split(" ")[0] + " métiers trouvés";

      if (resultLabel.innerText.split(" ")[0] == 1)
        subtitle.innerText =
          resultLabel.innerText.split(" ")[0] + " métier trouvé";

      return;
    }

    resultcontainer.style.display = "flex";
  });

document.querySelector("#filAriane").addEventListener("click", () => {
  searchContainer.style.display = "flex";
  filAriane.style.display = "none";
  titleContainer.style.alignItems = "center";

  title.innerText = "Rechercher des informations sur un métier";
  subtitle.innerText =
    "Un catalogue de près de 650 métiers pour vous aider à trouver votre voie.";
});
