const SHOW = 'block';
const HIDE = 'none';

$(document).ready(function () {
    let input = document.getElementById("searchInput");
    let suggestions = document.getElementById("searchSuggestions");
    const form = document.getElementsByClassName("searchContainer")[0];
    const checkboxes = document.querySelectorAll('.checkbox-container input');
    let valueChips = document.getElementById("valueChips");
    const showResults = form.dataset.showSearchResults;
    const orCheckbox = document.querySelector('.searchCheckbox input');
    let innerValueChips = [];
    let tagsValue = '';
    orCheckbox.parentElement.style.marginTop = '1rem';

// get value form the input field
// and add it to the suggestion drop down container
    window.getValue = function getValue(e) {
        if (e.value == "" || e.value.split("")[0] == " ") {
            suggestions.style.display = HIDE;
            return;
        }

        const ajouterButton = $(`<button type="button" onclick="suggestionClick(this)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">Ajouter ${e.value}</button>`)
        const suggestionsList = document.querySelector('#searchSuggestions');
        suggestionsList.innerHTML = '';
        suggestionsList.append(ajouterButton.get(0))
        if (form.dataset.quickSuggestionsEnabled === "true") {
            $.get(form.dataset.quickSuggestions, `q=${e.value}`, function (data) {
                $.each(data.suggestions, function (index, suggestion) {
                    if (!innerValueChips.includes(suggestion.trim())) {
                        const html = $(`<button type="button" onclick="suggestionClick(this)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">${suggestion.trim()}</button>`)
                        suggestionsList.append(html.get(0))
                    }
                });
            });
        }

        if (window.screen.width <= 1425) {
            suggestions.style.display = HIDE;
            valueChips.style.display = HIDE;
            return;
        }
        suggestions.style.display = SHOW;
    }

// on click of the suggestion
// add a chip to the second value drop down menu
    window.suggestionClick = function suggestionClick(e) {
        let suggestionValue = e.innerText
        input.value = "";
        suggestions.style.display = HIDE;
        valueChips.style.display = SHOW;
        var chip = document.createElement("span");
        chip.className = "chips chips-secondary search-suggestion-term";
        if (suggestionValue.indexOf('Ajouter') !== -1) {
            chip.innerText = suggestionValue.split("Ajouter ")[1]
            innerValueChips.push(suggestionValue.split("Ajouter ")[1]);
        } else {
            chip.innerText = suggestionValue;
            innerValueChips.push(suggestionValue);
        }

            chip.addEventListener("click", function () {
                deleteValueChip(this);
            });

        valueChips.appendChild(chip);
        let query = '';
        innerValueChips.forEach(value => {
            query = query + value + ',';
        })
        orCheckbox.parentElement.style.marginTop = valueChips.offsetHeight + 'px';
        getQuickResults(query)
        getTagValues(query)
    }

// on click on the chip
// delete the chip
    function deleteValueChip(e) {
        e.remove();

        if (valueChips.childNodes.length < 1) {
            valueChips.style.display = HIDE;
        }
        let index = innerValueChips.indexOf(e.innerText);
        innerValueChips.splice(index, 1);
        let query = '';
        innerValueChips.forEach(value => {
            query = query + value + ',';
        })
        getQuickResults(query);
        getTagValues(query)
    }

    window.checkboxClick = function checkboxClick(e) {
        let query = '';
        innerValueChips.forEach(value => {
            query = query + value + ',';
        })
        getQuickResults(query);
        getTagValues(query)
    }

    function getTagQuery() {
        let tagsValue = '';
        let index = 0;
        checkboxes.forEach((checkbox) => {
            if (checkbox.checked) {
                if (index > 0) {
                    tagsValue += ',' + '';
                }
                tagsValue += checkbox.name + '';
                index++
            }
        })
        return tagsValue;
    }

    function getQuickResults(inputValue) {
        if (showResults == "true") {
            const searchResultList = document.querySelector('.tuile-results-container')
            tagsValue = getTagQuery();
            let query = '';
            query = inputValue === "" ? '' : `q=${inputValue}&`;
            query = query + (orCheckbox.checked === false ? '' : `or=true&`);
            query = query + (tagsValue === "" ? '' : `tags=${tagsValue}`);
            $.get(form.dataset.quickSearchResults, query, function (data) {
                searchResultList.innerHTML = '';
                $.each(data.results, function (index, result) {
                    var $jobTagHtml = $("<div>");
                    $.each(result.jobTags, function (newIndex, jobTag) {
                        $jobTagHtml.append('<button class="chip-button" style="background-color: ' + jobTag.backgroundColor + '">' + jobTag.title + '</button>');
                    })
                    $jobTagHtml.append('</div>');
                    const html = $(`<div class="teaser tuileContainer">
<a href="${result.vanityPath}">
    <div class="cmp-teaser">
        <div class="cmp-teaser__image">
                    <img src="${result.featuredImage}" loading="lazy" class="cmp-image__image">
        </div>
        <div class="cmp-teaser__content">
            <div class="cmp-teaser__header">
                <h2 class="cmp-teaser__title">${result.title}</h2>
                <div class="cmp-teaser__image">
                    <div class="cmp-image">
                        <img src="/etc.clientlibs/leforemhe/clientlibs/clientlib-site/resources/images/heart.svg" loading="lazy" class="cmp-image__image">
                    </div>
                </div>
            </div>
            <div class="cmp-teaser__description">
                <p>${result.description}</p>
            </div>
        </div>
            <div class="aem cmp-teaser__tags">
        ${$jobTagHtml.html()}
        </div>
        </div>
    </a>
</div>`);
                    searchResultList.append(html.get(0))
                });

            });
        }
    }

    function getTagValues(inputValue) {
        checkboxes.forEach(checkbox => {
            const inputQuery = inputValue !== '' ? `&q=${inputValue}` : '';
            const query = "tags=" + checkbox.name + "&limit=no-limit" + inputQuery
            $.get(form.dataset.quickSearchResults, query, function (data) {
                checkbox.parentElement.parentElement.querySelector(".checkbox-amount").innerHTML = data.resultTotal;
            });

        })
    }

    window.clickAccordion = function clickAccordion(e) {
        const itemPanel = document.querySelector('#accordion-item-panel');
        if (itemPanel.style.display === HIDE) {
            itemPanel.style.display = SHOW;
        } else {
            itemPanel.style.display = HIDE;
        }
    }

    if (document.querySelector('#rechercheMetier')) {
        getTagValues('');
    }

    function initSearchPage() {
        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
        });

        let value = params.q; // "some_value"

        if (value != null) {
            getQuickResults(value);
        } else {
            getQuickResults("");
        }
    }

    initSearchPage();

})
