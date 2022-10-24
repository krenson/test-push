const searchBtn = document.querySelectorAll(".aem.searchBtn button")[0];
const resultcontainer = document.querySelectorAll(".result-counter ")[0];
const resultLabel = document.querySelectorAll(".result-counter h5")[0];

searchBtn.addEventListener("click", () => {
  const rdmNr = Math.floor(Math.random() * (10 - 0 + 1) + 0);

  resultcontainer.style.display = "flex";

  resultLabel.innerText = rdmNr + " métiers trouvés";

  if (rdmNr == 1) resultLabel.innerText = rdmNr + " métier trouvé";
});
