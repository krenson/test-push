var input = document.getElementById("searchInput");
var suggestions = document.getElementById("searchSuggestions");
var suggeBtn = document.getElementById("suggeBtn");

var valueChips = document.getElementById("valueChips");

// get value form the input field
// and add it to the suggestion drop down container
function getValue(e) {
  if (e.value == "" || e.value.split("")[0] == " ") {
    suggestions.style.display = "none";
    return;
  }

  if (window.screen.width <= 1425) {
    suggestions.style.display = "none";
    valueChips.style.display = "none";
    return;
  }

  suggestions.style.display = "block";

  suggeBtn.innerText = "Ajouter: " + e.value;
}

// on click of the suggestion
// add a chip to the second value drop down menu
function suggestionClick(e) {
  input.value = "";
  suggestions.style.display = "none";

  valueChips.style.display = "block";

  var chip = document.createElement("div");

  chip.className = "chips chips-secondary";

  chip.innerText = e.innerText.split(":")[1].trim();

  chip.addEventListener("click", function () {
    deleteValueChip(this);
  });

  valueChips.appendChild(chip);

  console.log(chip);
}

// on click on the chip
// delete the chip
function deleteValueChip(e) {
  e.remove();

  if (valueChips.childNodes.length < 1) {
    valueChips.style.display = "none";
  }
}
