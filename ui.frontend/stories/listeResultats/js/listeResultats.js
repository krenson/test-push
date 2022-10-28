window.onload = function () {
  const searchBtn = document.querySelector(".aem.searchBtn button");
  const resultcontainer = document.querySelector(".result-counter ");
  const resultLabel = document.querySelector(".result-counter h5");

  const searchContainer = document.querySelector(".searchContainer");
  const filAriane = document.querySelector("#filAriane");
  const titleContainer = document.querySelector(".aem.title-container");
  const title = document.querySelector(".aem.title-container h1");
  const subtitle = document.querySelector(".aem.title-container h5.subTitle");

  searchBtn.addEventListener("click", () => {
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

  filAriane.addEventListener("click", () => {
    searchContainer.style.display = "flex";
    filAriane.style.display = "none";
    titleContainer.style.alignItems = "center";

    title.innerText = "Rechercher des informations sur un métier";
    subtitle.innerText =
      "Un catalogue de près de 650 métiers pour vous aider à trouver votre voie.";
  });
};
