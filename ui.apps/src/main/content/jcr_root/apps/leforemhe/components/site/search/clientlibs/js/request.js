$(document).ready(function () {
    const form = document.getElementsByClassName("searchContainer")[0];
    const orCheckbox = document.getElementById("orCheckbox");
    const urlSearch = form.dataset.searchResultPage;
    const showResults = form.dataset.showSearchResults;
    let searchBtn = document.getElementsByClassName("searchBtn")[0];

    function doSearch() {
        if (typeof urlSearch === 'string') {
            let suggestionTerms = document.querySelectorAll(".search-suggestion-term");
            var url = '';
            suggestionTerms.forEach((item, index) => {
                if (index === 0) {
                    url += `?q=${item.innerText}`;
                } else {
                    url += `,${item.innerText}`;
                }
            });
            if (orCheckbox.checked) {
                url += '&or=true'
            }
            window.location = urlSearch + url;
        }
    }

    function initSearch() {
        if (showResults == "false") {
            searchBtn.addEventListener("click", doSearch);
        }
    }

    initSearch();

})
