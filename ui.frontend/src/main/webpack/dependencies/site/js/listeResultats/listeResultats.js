const searchBtn = document.querySelectorAll(".aem.searchBtn button")[0];
const resultcontainer = document.querySelectorAll(".result-counter ")[0];
const resultLabel = document.querySelectorAll(".result-counter h5")[0];

const searchBox = document.querySelectorAll(".searchContainer")[0];
const filAriane = document.querySelectorAll("#filAriane")[0];
const titleContainer = document.querySelectorAll(".aem.title-container")[0];
const title = document.querySelectorAll(".aem.title-container h1")[0];
const subtitle = document.querySelectorAll(
  ".aem.title-container h5.subTitle"
)[0];

searchBtn.addEventListener("click", () => {
  const rdmNr = Math.floor(Math.random() * (10 - 0 + 1) + 0);

  if (window.screen.width <= 1200) {
    searchBox.style.display = "none";
    filAriane.style.display = "block";
    titleContainer.style.alignItems = "start";

    subtitle.innerText = rdmNr + " métiers trouvés";
    if (rdmNr == 1) subtitle.innerText = rdmNr + " métier trouvé";

    return;
  }

  resultcontainer.style.display = "flex";

  resultLabel.innerText = rdmNr + " métiers trouvés";

  if (rdmNr == 1) resultLabel.innerText = rdmNr + " métier trouvé";
});

filAriane.addEventListener("click", () => {
  location.reload();
});
