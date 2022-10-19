$( document ).ready(function() {
    let input = document.getElementById("searchInput");
    let suggestions = document.getElementById("searchSuggestions");
    const form = document.getElementsByClassName("searchContainer")[0];
    const checkboxes = document.querySelectorAll('.checkbox-container input');
    let valueChips = document.getElementById("valueChips");
    let innerValueChips = [];
    let tagsValue = '';

// get value form the input field
// and add it to the suggestion drop down container
    window.getValue = function getValue(e) {
        if (e.value == "" || e.value.split("")[0] == " ") {
            suggestions.style.display = "none";
            return;
        }

        const ajouterButton = $(`<button onclick="suggestionClick(this)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">Ajouter ${e.value}</button>`)
        const suggestionsList = document.querySelector('#searchSuggestions');
        suggestionsList.innerHTML = '';
        suggestionsList.append(ajouterButton.get(0))
        if (form.dataset.quickSuggestionsEnabled === "true") {
            $.get(form.dataset.quickSuggestions, `q=${e.value}`, function (data) {
                $.each(data.suggestions, function (index, suggestion) {
                    const html = $(`<button onclick="suggestionClick(this)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">${suggestion}</button>`)
                    suggestionsList.append(html.get(0))
                });
            });
        }

        if (window.screen.width <= 1425) {
            suggestions.style.display = "none";
            valueChips.style.display = "none";
            return;
        }

        suggestions.style.display = "block";
    }

// on click of the suggestion
// add a chip to the second value drop down menu
    window.suggestionClick = function suggestionClick(e) {
        input.value = "";
        suggestions.style.display = "none";

        valueChips.style.display = "block";

        var chip = document.createElement("span");

        chip.className = "chips chips-secondary";

        if(e.innerText.indexOf('Ajouter') !== -1) {
            chip.innerText = e.innerText.split("Ajouter ")[1]
            innerValueChips.push(e.innerText.split("Ajouter ")[1]);
        } else {
            chip.innerText = e.innerText;
            innerValueChips.push(e.innerText);
        }

        chip.addEventListener("click", function () {
            deleteValueChip(this);
        });

        valueChips.appendChild(chip);
        let query = '';
        innerValueChips.forEach(value => {
            query = query + value + ',';
        })
        getQuickResults(query)
        getTagValues(query)
    }

// on click on the chip
// delete the chip
    function deleteValueChip(e) {
        e.remove();

        if (valueChips.childNodes.length < 1) {
            valueChips.style.display = "none";
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
        const searchResultList = document.querySelector('.tuile-results-container')
        tagsValue = getTagQuery();
        let query = '';
        query = inputValue === "" ? '' : `q=${inputValue}&`;
        query = query + (tagsValue === "" ? '' : `tags=${tagsValue}`);
        $.get(form.dataset.quickSearchResults, query, function (data) {
            searchResultList.innerHTML = '';
            $.each(data.results, function (index, result) {
                var $jobTagHtml = $("<div>");
                $.each(result.jobTags, function (newIndex, jobTag) {
                    $jobTagHtml.append('<button class="chip-button" style="background-color: ' + jobTag.backgroundColor +'">'+ jobTag.title + '</button>');
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
        if (itemPanel.style.display === 'none') {
            itemPanel.style.display = 'block';
        } else {
            itemPanel.style.display = 'none';
        }
    }

    if (document.querySelector('#rechercheMetier')) {
        getTagValues('');
    }

    function initSearchPage() {
        getQuickResults("")
    }

    initSearchPage();

})
