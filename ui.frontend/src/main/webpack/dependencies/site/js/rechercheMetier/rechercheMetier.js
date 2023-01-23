try {
  let input = document.getElementById("searchInput");
  let suggestions = document.getElementById("searchSuggestions");
  const form = document.getElementsByClassName("searchContainer")[0];
  const checkboxes = document.querySelectorAll(".checkbox-container input");
  let valueChips = document.getElementById("valueChips");
  let searchCheckbox = document.querySelector(".searchCheckbox-container");
  let innerValueChips = [];
  let tagsValue = "";
  let inputContainer = document.querySelectorAll(".searchbox div")[0];

  // get value form the input field
  // and add it to the suggestion drop down container
  function getValue(e) {
    if (e.value == "" || e.value.split("")[0] == " ") {
      suggestions.style.display = "none";
      form.style.bottom = "0rem";
      inputContainer.style.marginBottom = "0rem";

      if (window.screen.width < 1200) {
        form.style.bottom = "-10rem";
      }

      return;
    }
    const suggestionsList = document.querySelector("#searchSuggestions");
    if (form.dataset.quickSuggestionsEnabled === "true") {
      $.get(form.dataset.quickSuggestions, `q=${e.value}`, function (data) {
        suggestionsList.innerHTML = "";
        $.each(data.suggestions, function (index, suggestion) {
          const html = $(
            `<button onclick="suggestionClick(this)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">${suggestion}</button>`
          );
          suggestionsList.append(html.get(0));
        });
      });
    } else {
      let suggeText = "Ajouter: " + e.value;

      suggestionsList.querySelector("button").innerText = suggeText;
    }

    suggestions.style.display = "block";
    searchCheckbox.style.bottom = "-8.8rem";

    if (window.screen.width < 1200 && suggestions.style.display == "block") {
      form.style.bottom = "-18rem";
      inputContainer.style.marginBottom = "8rem";
    }
  }

  // on click of the suggestion
  // add a chip to the second value drop down menu
  function suggestionClick(e) {
    input.value = "";
    suggestions.style.display = "none";

    valueChips.style.display = "block";
    searchCheckbox.style.bottom = "-8.8rem";

    var chip = document.createElement("span");

    chip.className = "chips chips-secondary";

    chip.innerText = e.innerText.trim().split(":")[1];
    innerValueChips.push(e.innerText.trim().split(":")[1]);

    chip.addEventListener("click", function () {
      deleteValueChip(this);
    });

    valueChips.appendChild(chip);
    let query = "";
    innerValueChips.forEach((value) => {
      query = query + value + ",";
    });
    getQuickResults(query);
    getTagValues(query);
  }

  // on click on the chip
  // delete the chip
  function deleteValueChip(e) {
    e.remove();

    if (valueChips.childNodes.length < 1) {
      valueChips.style.display = "none";
      searchCheckbox.style.bottom = "-4.8rem";

      form.style.bottom = "0rem";
      inputContainer.style.marginBottom = "0rem";

      if (window.screen.width < 1200) {
        form.style.bottom = "-10rem";
      }
    }
    let index = innerValueChips.indexOf(e.innerText);
    innerValueChips.splice(index, 1);
    let query = "";
    innerValueChips.forEach((value) => {
      query = query + value + ",";
    });
    getQuickResults(query);
    getTagValues(query);
  }

  function checkboxClick(e) {
    let query = "";
    innerValueChips.forEach((value) => {
      query = query + value + ",";
    });
    getQuickResults(query);
    getTagValues(query);
  }

  function getTagQuery() {
    let tagsValue = "";
    let index = 0;
    checkboxes.forEach((checkbox) => {
      if (checkbox.checked) {
        if (index > 0) {
          tagsValue += "," + "";
        }
        tagsValue += checkbox.name + "";
        index++;
      }
    });
    return tagsValue;
  }

  function getQuickResults(inputValue) {
    const searchResultList = document.querySelector(".tuile-results-container");
    tagsValue = getTagQuery();
    let query = "";
    query = inputValue === "" ? "" : `q=${inputValue}&`;
    query = query + (tagsValue === "" ? "" : `tags=${tagsValue}`);
    $.get(form.dataset.quickSearchResults, query, function (data) {
      if (searchResultList) {
        searchResultList.innerHTML = "";
        $.each(data.results, function (index, result) {
          const html = $(`<a href="${result.url}"> ${result.title}</a>`);
          searchResultList.append(html.get(0));
        });
      }
    });
  }

  function getTagValues(inputValue) {
    checkboxes.forEach((checkbox) => {
      const inputQuery = inputValue !== "" ? `&q=${inputValue}` : "";
      const query = "tags=" + checkbox.name + "&limit=no-limit" + inputQuery;
      $.get(form.dataset.quickSearchResults, query, function (data) {
        checkbox.parentElement.parentElement.querySelector(
          ".checkbox-amount"
        ).innerHTML = data.resultTotal;
      });
    });
  }
  function clickAccordion(e) {
    const itemPanel = document.querySelector("#accordion-item-panel");
    if (itemPanel.style.display === "none") {
      itemPanel.style.display = "block";
    } else {
      itemPanel.style.display = "none";
    }
  }

  if (document.querySelector("#rechercheMetier")) {
    getTagValues("");
  }

  window.addEventListener("resize", () => {
    if (
      suggestions.style.display == "block" ||
      valueChips.style.display == "block"
    ) {
      form.style.bottom = "0rem";
      inputContainer.style.marginBottom = "0rem";

      if (window.screen.width < 1200) {
        form.style.bottom = "-18rem";
        inputContainer.style.marginBottom = "8rem";
      }
    }

    if (
      suggestions.style.display == "none" &&
      valueChips.style.display == "none"
    ) {
      form.style.bottom = "0rem";
      inputContainer.style.marginBottom = "0rem";

      if (window.screen.width < 1200) {
        form.style.bottom = "-10rem";
      }
    }
  });
} catch (error) {
  console.log(error);
}
