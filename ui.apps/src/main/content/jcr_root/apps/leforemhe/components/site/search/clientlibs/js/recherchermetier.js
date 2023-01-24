const SHOW = 'block';
const HIDE = 'none';
const COMMA = ',';
const PX = 'px';
const AJOUTER_LABEL = 'Ajouter';
const TRUE_LABEL = 'true';
const EMPTY_STRING = '';

$(document).ready(function () {
        let input = document.getElementById("searchInput");
        let suggestions = document.getElementById("searchSuggestions");
        const form = document.getElementsByClassName("searchContainer")[0];
        const checkboxes = document.querySelectorAll('.checkbox-container input');
        let valueChips = document.getElementById("valueChips");
        const showResults = form.dataset.showSearchResults;
        const fallbackImage = form.dataset.fallbackImage;
        const dynamicSearch = form.dataset.dynamicSearch;
        const orCheckbox = document.querySelector('.searchCheckbox-container')
        const orCheckboxInput = document.querySelector('.searchCheckbox-container input');
        const resultCounter = document.querySelector('.result-counter');
        const resultCounterHeader = document.querySelector('.result-counter-header');
        const params = new Proxy(new URLSearchParams(window.location.search), {
            get: (searchParams, prop) => searchParams.get(prop),
        });
        let innerValueChips = [];
        let tagsValue = EMPTY_STRING;

// get value form the input field
// and add it to the suggestion drop down container
        window.getValue = function getValue(e) {
            if (e.value == EMPTY_STRING || e.value.split(EMPTY_STRING)[0] == " ") {
                suggestions.style.display = HIDE;
                return;
            }

            const ajouterButton = $(`<button type="button" onclick="addSuggestion(this.innerText)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">${AJOUTER_LABEL} ${e.value}</button>`)
            const suggestionsList = document.querySelector('#searchSuggestions');
            suggestionsList.innerHTML = EMPTY_STRING;
            suggestionsList.append(ajouterButton.get(0))
            if (form.dataset.quickSuggestionsEnabled === TRUE_LABEL) {
                $.get(form.dataset.quickSuggestions, `q=${e.value}`, function (data) {
                    $.each(data.suggestions, function (index, suggestion) {
                        if (!innerValueChips.includes(suggestion.trim())) {
                            const html = $(`<button type="button" onclick="addSuggestion(this.innerText)" id="suggeBtn" class="chip-button" style="background-color: #d9eff0">${suggestion.trim()}</button>`)
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
        window.addSuggestion = function suggestionClick(suggestionValue) {
            input.value = EMPTY_STRING;
            suggestions.style.display = HIDE;
            valueChips.style.display = SHOW;
            var chip = document.createElement("span");
            chip.className = "chips chips-secondary search-suggestion-term";
            if (suggestionValue.indexOf(AJOUTER_LABEL) !== -1) {
                let suggestionValueSplitted = suggestionValue.split(`${AJOUTER_LABEL} `)[1];
                chip.innerText = suggestionValueSplitted
                innerValueChips.push(suggestionValueSplitted);
            } else {
                chip.innerText = suggestionValue;
                innerValueChips.push(suggestionValue);
            }

            chip.addEventListener("click", function () {
                deleteValueChip(this);
            });

            valueChips.appendChild(chip);
            let query = EMPTY_STRING;
            innerValueChips.forEach(value => {
                query = query + value + COMMA;
            })
            orCheckbox.style.bottom = "-" + valueChips.offsetHeight + PX;
            if (dynamicSearch === TRUE_LABEL) {
                getQuickResults(query)
            }
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
            let query = EMPTY_STRING;
            innerValueChips.forEach(value => {
                query = query + value + ',';
            })
            getQuickResults(query);
            getTagValues(query)
        }

        window.checkboxClick = function checkboxClick(e) {
            if (e.checked === false) {
                checkboxes.forEach(checkbox => {
                    if (e.name === checkbox.name && checkbox.checked === true) {
                        checkbox.checked = false;
                    }
                })
            }
            let query = EMPTY_STRING;
            innerValueChips.forEach(value => {
                query = query + value + ',';
            })
            getQuickResults(query);
            getTagValues(query)
            checkboxes.forEach(checkbox => {
                if (checkbox.id === e.id) {
                    checkbox.checked = e.checked
                }
            })
        }

        function getTagQuery() {
            let tagsValue = EMPTY_STRING;
            let index = 0;
            checkboxes.forEach((checkbox) => {
                if (checkbox.checked && !tagsValue.includes(checkbox.name)) {
                    tagsValue += index === 0 ? checkbox.name : ',' + checkbox.name;
                    index++
                }
            })
            return tagsValue;
        }

        function getQuickResults(inputValue) {
            if (showResults == TRUE_LABEL) {
                const searchResultList = document.querySelector('.tuile-results-container')
                tagsValue = getTagQuery();
                let query = EMPTY_STRING;
                query = inputValue === EMPTY_STRING ? EMPTY_STRING : `q=${inputValue}&`;
                query = query + (orCheckboxInput.checked === false ? EMPTY_STRING : `or=true&`);
                query = query + (tagsValue === EMPTY_STRING ? EMPTY_STRING : `tags=${tagsValue}`);
                $.get(form.dataset.quickSearchResults, query, function (data) {
                        let noResultText = searchResultList.dataset.noresult;
                        searchResultList.innerHTML = EMPTY_STRING;
                        setResultCounterLabel(data.resultTotal, resultCounter.getElementsByTagName('h5')[0]);
                        setResultCounterLabel(data.resultTotal, resultCounterHeader);
                        if (data.resultTotal > 0) {
                            $.each(data.results, function (index, result) {
                                var $jobTagHtml = $("<div>");
                                $.each(result.jobTags, function (newIndex, jobTag) {
                                    $jobTagHtml.append('<button class="chip-button" style="background-color: ' + jobTag.backgroundColor + '">' + jobTag.title + '</button>');
                                })
                                $jobTagHtml.append('</div>');
                                const html = $(`<div class="teaser tuileContainer" tabindex="0">
<a href="${result.vanityPath}">
    <div class="cmp-teaser">
        <div class="cmp-teaser__image">
                    <img src="${getFeaturedImageOrFallback(result.featuredImage)}" loading="lazy" class="cmp-image__image">
        </div>
        <div class="cmp-teaser__content">
            <div class="cmp-teaser__header">
                <h2 class="cmp-teaser__title">${result.title}</h2>
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
                        } else {
                            const html = $(`<div class="zero-results-container">${noResultText}</div>`)
                            searchResultList.append(html.get(0));
                        }
                    }
                )
                ;
            }
        }

        function getTagValues(inputValue) {
            checkboxes.forEach(checkbox => {
                const inputQuery = inputValue !== EMPTY_STRING ? `&q=${inputValue}` : EMPTY_STRING;
                const query = "tags=" + checkbox.name + (params.tags !== null ? ',' + params.tags : EMPTY_STRING) + (orCheckboxInput.checked === false ? EMPTY_STRING : `&or=true`) + inputQuery
                $.get(form.dataset.quickSearchResults, query, function (data) {
                    checkbox.parentElement.parentElement.querySelector(".checkbox-amount").innerHTML = data.resultTotal;
                });

            })
        }

        window.clickAccordion = function clickAccordion(itemPanel) {
            itemPanel = itemPanel.parentElement.parentElement.querySelector('#accordion-item-panel');
            if (itemPanel.style.display === HIDE) {
                itemPanel.style.display = SHOW;
            } else {
                itemPanel.style.display = HIDE;
            }
        }

        function setResultCounterLabel(resultTotal, resultElement) {
            if (resultElement !== undefined) {
                let resultCounterData = resultElement.dataset;
                resultElement.innerText = resultTotal === '1' ?
                    `${resultTotal} ${resultCounterData.resultcounterlabelone}` :
                    `${resultTotal} ${resultCounterData.resultcounterlabel}`;
            }
        }

        function initSearchPage() {
            let value = params.q; // "some_value"
            let tags = params.tags;
            let orCheckboxInitValue = params.or; // "some_value"

            if (orCheckboxInitValue === TRUE_LABEL) {
                document.getElementById("searchCheckbox").checked = TRUE_LABEL;
            }
            initSuggestionValues(value, tags);

            if (value != null) {
                getQuickResults(value);
                getTagValues(value)
            } else {
                getQuickResults(EMPTY_STRING);
                getTagValues(EMPTY_STRING)
            }
        }

        function initSuggestionValues(qParams, tags) {
            if (qParams != null && qParams.length != 0) {
                let splittedQParams = qParams.split(",");

                splittedQParams.forEach(param => {
                    addSuggestion(param)
                })
            }
            if (tags != null && tags.length != 0) {
                let splittedTags = tags.split(COMMA)
                splittedTags.forEach(tag => {
                    checkboxes.forEach(checkbox => {
                        if (checkbox.name === tag) {
                            checkbox.checked = true;
                        }
                    })
                })
            }
        }

        function getFeaturedImageOrFallback(featuredImage) {
            if (featuredImage === EMPTY_STRING) {
                return fallbackImage;
            } else {
                return featuredImage;
            }
        }

        initSearchPage();
    }
)


