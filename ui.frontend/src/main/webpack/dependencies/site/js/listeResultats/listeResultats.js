const searchBtn = document.querySelector(".aem.searchBtn button");
const resultcontainer = document.querySelector(".result-counter ");
const resultLabel = document.querySelector(".result-counter h5");

const searchBox = document.querySelector(".searchContainer");
const filAriane = document.querySelector("#filAriane");
const titleContainer = document.querySelector(".aem.title-container");
const title = document.querySelector(".aem.title-container h1");
const subtitle = document.querySelector(".aem.title-container h5.subTitle");

searchBtn.addEventListener("click", () => {
  const rdmNr = Math.floor(Math.random() * (10 - 0 + 1) + 0);

  if (window.screen.width <= 1200) {
    searchBox.style.display = "none";
    filAriane.style.display = "block";
    titleContainer.style.alignItems = "start";
    title.innerText = "Résultats";

    subtitle.innerText = rdmNr + " métiers trouvés";
    if (rdmNr == 1) subtitle.innerText = rdmNr + " métier trouvé";

    return;
  }

  resultcontainer.style.display = "flex";

  resultLabel.innerText = rdmNr + " métiers trouvés";

  if (rdmNr == 1) resultLabel.innerText = rdmNr + " métier trouvé";
});

filAriane.addEventListener("click", () => {
  searchBox.style.display = "flex";
  filAriane.style.display = "none";
  titleContainer.style.alignItems = "center";
  title.innerText = "Rechercher des informations sur un métier";
  subtitle.innerText =
    "Un catalogue de près de 650 métiers pour vous aider à trouver votre voie.";
});
