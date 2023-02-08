try {
  let input = document.getElementById("searchInput");
  let suggestions = document.getElementById("searchSuggestions");
  const form = document.getElementById("rechercheMetier");
  const shortRecherMetier = form.parentElement;
  const checkboxes = document.querySelectorAll(".checkbox-container input");
  let valueChips = document.getElementById("valueChips");
  let searchCheckbox = document.querySelector(".searchCheckbox-container");
  let innerValueChips = [];
  let tagsValue = "";
  let inputContainer = document.querySelectorAll(".searchbox div")[0];
  let searchBtn = document.querySelector(".aem.searchBtn button");
  let searchbox = document.querySelector(".searchbox");
  let searchContainer = document.querySelector(".searchContainer");

  suggestions.style.display = valueChips.style.display = "none";

  // get value form the input field
  // and add it to the suggestion drop down container
  function getValue(e) {
    if (e.value == "" || e.value.split("")[0] == " ") {
      if (valueChips.style.display == "block") {
      } else {
        suggestions.style.display = "none";
        searchbox.style.marginBottom = "0rem";
        inputContainer.style.marginBottom = "0rem";

        if (window.screen.width > 1200) {
          searchContainer.style.bottom = "0rem";
        } else {
          searchContainer.style.bottom = "-10rem";
        }
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

    if (window.screen.width < 1200 && suggestions.style.display == "block") {
      searchContainer.style.bottom = "-18rem";
      inputContainer.style.marginBottom = "8rem";
    }

    //dynamicTabIndex();
    dynamicSpacing();
    chipOnEnter();
  }

  // input on enter
  input.addEventListener("keypress", (event) => {
    if (event.key === "Enter") {
      setTimeout(() => {
        event.preventDefault();
        if (suggestions.style.display == "block") {
          suggestionClick(suggestions.querySelector("button"));
        }
      }, 250);
    }
  });

  // checkbox on enter
  searchCheckbox.addEventListener("keypress", (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      searchCheckbox.querySelector("input").click();
    }
  });

  // searchBtn on enter
  searchBtn.addEventListener("keypress", (event) => {
    if (event.key === "Enter") {
      event.preventDefault();
      searchBtn.click();
    }
  });

  function chipOnEnter() {
    if (suggestions.style.display == "block") {
      suggestions.querySelectorAll("button").forEach((button) => {
        button.addEventListener("keyup", (event) => {
          if (event.key == "Enter") {
            event.preventDefault();
            suggestionClick(button);
          }
        });
      });
    }

    if (valueChips.style.display == " block") {
      valueChips.querySelectorAll("span").forEach((span) => {
        span.addEventListener("keyup", (event) => {
          if (event.key == "Enter") {
            event.preventDefault();
            deleteValueChip(span);
          }
        });
      });
    }
  }

  // on click of the suggestion
  // add a chip to the second value drop down menu
  function suggestionClick(e) {
    input.value = "";
    suggestions.style.display = "none";

    valueChips.style.display = "block";

    var chip = document.createElement("span");
    chip.tabIndex = 0;

    chip.className = "chips chips-secondary";

    if (e.innerText.trim().split("").includes(":")) {
      chip.innerText = e.innerText.trim().split(":")[1];
      innerValueChips.push(e.innerText.trim().split(":")[1]);
    } else {
      chip.innerText = e.innerText.trim();
      innerValueChips.push(e.innerText.trim());
    }

    chip.addEventListener("click", function () {
      deleteValueChip(this);
    });

    chip.addEventListener("keypress", function () {
      deleteValueChip(this);
    });

    valueChips.appendChild(chip);
    let query = "";
    innerValueChips.forEach((value) => {
      query = query + value + ",";
    });
    getQuickResults(query);
    getTagValues(query);

    //dynamicTabIndex();
    dynamicSpacing();
    chipOnEnter();
  }

  // on click on the chip
  // delete the chip
  function deleteValueChip(e) {
    e.remove();

    if (valueChips.childNodes.length < 1) {
      valueChips.style.display = "none";
      searchCheckbox.style.bottom = "-4.8rem";

      searchContainer.style.bottom = "0rem";
      inputContainer.style.marginBottom = "0rem";

      if (window.screen.width < 1200) {
        searchContainer.style.bottom = "-10rem";
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

    //dynamicTabIndex();
    dynamicSpacing();
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

  // dynamically modify tab index
  function dynamicTabIndex() {
    let index = 4;

    if (
      suggestions.style.display == "block" &&
      valueChips.style.display == "block"
    ) {
      suggestions.querySelector("button").tabIndex = index++;
      valueChips.querySelectorAll("span").forEach((chip) => {
        chip.tabIndex = index;

        index++;
      });
    }

    if (
      suggestions.style.display == "none" &&
      valueChips.style.display == "block"
    ) {
      valueChips.querySelectorAll("span").forEach((chip) => {
        chip.tabIndex = index;

        index++;
      });
    }

    if (
      suggestions.style.display == "block" &&
      valueChips.style.display == "none"
    ) {
      index = 2;

      suggestions.querySelectorAll("button").forEach((button) => {
        button.tabIndex = index++;
      });
    }

    searchCheckbox.tabIndex = index++;
    searchBtn.tabIndex = index++;

    if (
      window.screen.width > 1200 &&
      suggestions.style.display == "block" &&
      valueChips.style.display == "none"
    )
      searchCheckbox.tabIndex = -1;
  }

  window.addEventListener(
    "focus",
    (e) => {
      focusedElement = e.target;
    },
    true
  );

  // css mods on wondow resize
  window.addEventListener("resize", () => {
    dynamicSpacing();
    //dynamicTabIndex();
  });

  function dynamicSpacing() {
    if (shortRecherMetier.className == "short-searchContainer") return;
    if (window.screen.width > 1200) {
      searchContainer.style.bottom = "0rem";
      searchbox.style.marginBottom = "0rem";
      inputContainer.style.marginBottom = "0rem";
    } else {
      searchContainer.style.bottom = "-10rem";
      searchbox.style.marginBottom = "0rem";
    }

    if (window.screen.width > 1200 && suggestions.style.display == "block") {
      if (suggestions.offsetHeight > 88) {
        let marginBottom = ((suggestions.offsetHeight - 88) / 46) * 5;
        //searchbox.style.marginBottom = marginBottom + 5 + "rem";
      } else {
        //searchbox.style.marginBottom = "5rem";
      }
    }

    if (window.screen.width > 1200 && valueChips.style.display == "block") {
      if (valueChips.offsetHeight > 88) {
        let marginBottom = ((valueChips.offsetHeight - 88) / 46) * 5;
        searchbox.style.marginBottom = marginBottom + 5 + "rem";
      } else {
        searchbox.style.marginBottom = "5rem";
      }
    }

    if (window.screen.width < 1200 && suggestions.style.display == "block") {
      if (suggestions.offsetHeight > 88) {
        let marginBottom = ((suggestions.offsetHeight - 88) / 46) * 5 + 8;

        inputContainer.style.marginBottom = marginBottom + "rem";

        let bottom = ((suggestions.offsetHeight - 88) / 46) * 5 + 18;
        searchContainer.style.bottom = -bottom + "rem";
      } else {
        inputContainer.style.marginBottom = "8rem";
        searchContainer.style.bottom = "-18rem";
      }
    }

    if (window.screen.width < 1200 && valueChips.style.display == "block") {
      if (valueChips.offsetHeight > 88) {
        let marginBottom = ((valueChips.offsetHeight - 88) / 46) * 5 + 8;

        inputContainer.style.marginBottom = marginBottom + "rem";

        let bottom = ((valueChips.offsetHeight - 88) / 46) * 5 + 18;
        searchContainer.style.bottom = -bottom + "rem";
      } else {
        inputContainer.style.marginBottom = "8rem";
        searchContainer.style.bottom = "-18rem";
      }
    }

    if (
      suggestions.style.display == "none" &&
      valueChips.style.display == "none"
    ) {
      if (window.screen.width > 1200) {
        searchContainer.style.bottom = "0rem";
      } else {
        searchContainer.style.bottom = "-10rem";
      }
    }
  }
} catch (error) {
  console.log(error);
}
