var detailsBtn = document.getElementById(
  "accordion-2098845c33-item-ace86c638b-button"
);

var detailsCloseBtn = document.getElementById("close-container");

detailsCloseBtn.addEventListener("click", () => {
  detailsBtn.click();
});
